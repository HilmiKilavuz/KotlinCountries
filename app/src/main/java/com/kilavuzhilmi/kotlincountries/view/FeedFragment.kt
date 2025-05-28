/**
 * FeedFragment.kt
 * 
 * Ülke listesini gösteren ana fragment.
 * 
 * Amacı:
 * - RecyclerView'da ülke listesini gösterir
 * - Veri yükleme, hata durumları ve yükleme göstergelerini yönetir
 * - Verileri güncellemek için aşağı çekerek yenileme işlevselliği sağlar
 * - Hata durumunda verileri sıfırlama imkanı sunar
 * 
 * Kullanılan Teknolojiler:
 * - Fragment: Android'de modüler UI bileşeni
 * - ViewModel: Yaşam döngüsüne duyarlı bir şekilde UI ile ilgili verileri yönetir
 * - LiveData: Android yaşam döngüsüne saygı gösteren gözlemlenebilir veri tutucusu
 * - RecyclerView: Verimli kaydırma listesi görünümü
 * - SwipeRefreshLayout: Aşağı çekerek yenileme işlevselliği
 * - Observer deseni: ViewModel'den gelen veri değişikliklerine tepki verir
 */
package com.kilavuzhilmi.kotlincountries.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kilavuzhilmi.kotlincountries.R
import com.kilavuzhilmi.kotlincountries.adapter.CountryAdapter
import com.kilavuzhilmi.kotlincountries.viewmodel.FeedViewModel

/**
 * Ülke listesini gösteren fragment
 * Uygulamanın ana ekranı olarak kaydırılabilir bir listede ülkeleri gösterir
 */
class FeedFragment : Fragment() {
    /**
     * Bu fragment için ViewModel örneği
     * Veri işlemlerini ve UI durumunu yönetir
     */
    private lateinit var viewModel: FeedViewModel
    
    /**
     * RecyclerView için adapter
     * Daha sonra doldurulacak boş bir ArrayList ile başlatılır
     */
    private val countryAdapter = CountryAdapter(arrayListOf())
    
    /**
     * UI bileşenleri
     */
    private lateinit var countryList: RecyclerView
    private lateinit var countryError: TextView
    private lateinit var countryLoading: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var resetDataButton: Button
    
    /**
     * Fragment oluşturulduğunda çağrılır
     * UI oluşturulmadan önce çağrılan yaşam döngüsü metodu
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Fragment layout'unu şişirir
     * Bu fragment için View hiyerarşisini oluşturur ve döndürür
     * 
     * @param inflater Layout'u şişirmek için kullanılır
     * @param container Fragment UI'ının ekleneceği üst view
     * @param savedInstanceState Fragment yeniden oluşturuluyorsa önceki durum
     * @return Fragment'ın layout'unun kök View'ı
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment için layout'u şişir
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    /**
     * View oluşturulduktan sonra çağrılır
     * UI bileşenlerini ayarlar, ViewModel'i başlatır ve olay dinleyicilerini ayarlar
     * 
     * @param view Fragment'ın kök view'ı
     * @param savedInstanceState Fragment yeniden oluşturuluyorsa önceki durum
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // UI bileşeni referanslarını başlat
        viewConnection(view)
        
        // ViewModel'i başlat ve veri yüklemeyi başlat
        viewModel = ViewModelProvider(this)[FeedViewModel::class.java]
        viewModel.refreshData()

        // RecyclerView'ı layout manager ve adapter ile ayarla
        countryList.layoutManager = LinearLayoutManager(context)
        countryList.adapter = countryAdapter
        
        // Aşağı çekerek yenileme işlevselliğini ayarla
        swipeRefreshLayout.setOnRefreshListener {
            countryList.visibility = View.GONE
            countryError.visibility= View.GONE
            countryLoading.visibility= View.VISIBLE
            viewModel.getDataFromApi()
            swipeRefreshLayout.isRefreshing = false
        }
        
        // Hata kurtarma için sıfırlama düğmesini ayarla
        resetDataButton.setOnClickListener {
            countryLoading.visibility = View.VISIBLE
            countryError.visibility = View.GONE
            countryList.visibility = View.GONE
            resetDataButton.visibility = View.GONE
            viewModel.resetData()
        }

        // LiveData gözlemcilerini ayarla
        observeLiveData()
    }

   /**
    * ViewModel'den LiveData nesneleri için gözlemciler ayarlar
    * Veri değişikliklerine, yükleme durumlarına ve hatalara göre UI'ı günceller
    */
   private fun observeLiveData() {
        // Ülkeler verisi için gözlemci
        viewModel.countries.observe(viewLifecycleOwner, Observer { countries ->
            countries?.let {
                // Ülkeler mevcut olduğunda, listeyi göster ve sıfırlama düğmesini gizle
                countryList.visibility = View.VISIBLE
                resetDataButton.visibility = View.GONE
                countryAdapter.updateCountryList(countries)
            }
        })

        // Hata durumu için gözlemci
        viewModel.countryError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (error) {
                    // Hata oluştuğunda, hata mesajını ve sıfırlama düğmesini göster
                    countryError.visibility = View.VISIBLE
                    resetDataButton.visibility = View.VISIBLE
                    countryLoading.visibility = View.GONE
                    countryList.visibility = View.GONE
                } else {
                    // Hata çözüldüğünde, hata mesajını ve sıfırlama düğmesini gizle
                    countryError.visibility = View.GONE
                    resetDataButton.visibility = View.GONE
                }
            }
        })

        // Yükleme durumu için gözlemci
        viewModel.countryLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    // Yüklenirken, ilerleme göstergesini göster ve diğer bileşenleri gizle
                    countryLoading.visibility = View.VISIBLE
                    countryList.visibility = View.GONE
                    countryError.visibility = View.GONE
                    resetDataButton.visibility = View.GONE
                } else {
                    // Yükleme tamamlandığında, ilerleme göstergesini gizle
                    countryLoading.visibility = View.GONE
                }
            }
        })
    }

    /**
     * UI bileşenlerine referansları başlatır
     * 
     * @param view UI bileşenlerini içeren kök view
     */
    private fun viewConnection(view: View) {
        countryList = view.findViewById(R.id.countryList)
        countryError = view.findViewById(R.id.countryerror)
        countryLoading = view.findViewById(R.id.countryloading)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        resetDataButton = view.findViewById(R.id.resetDataButton)
    }
}


