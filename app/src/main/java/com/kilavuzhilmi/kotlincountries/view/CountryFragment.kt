/**
 * CountryFragment.kt
 * 
 * Seçilen bir ülkenin detaylı bilgilerini gösteren fragment.
 * 
 * Amacı:
 * - Belirli bir ülke hakkında detaylı bilgileri gösterir
 * - ViewModel kullanarak veritabanından ülke verilerini alır
 * - Ülke verilerini UI bileşenlerine bağlar
 * - Kullanıcıya zengin bir detay görünümü sunar
 * 
 * Kullanılan Teknolojiler:
 * - Fragment: Android'de modüler UI bileşeni
 * - ViewModel: Yaşam döngüsüne duyarlı bir şekilde UI ile ilgili verileri yönetir
 * - LiveData: Android yaşam döngüsüne saygı gösteren gözlemlenebilir veri tutucusu
 * - Navigation Component: Fragment navigasyonunu ve argüman geçişini yönetir
 * - SafeArgs: Tip güvenli navigasyon ve argüman geçişi
 * - Glide: Resim yükleme ve önbellekleme
 */
package com.kilavuzhilmi.kotlincountries.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.kilavuzhilmi.kotlincountries.R
import com.kilavuzhilmi.kotlincountries.util.downloadFromUrl
import com.kilavuzhilmi.kotlincountries.util.placeholderProcessBar
import com.kilavuzhilmi.kotlincountries.viewmodel.CountryViewModel

/**
 * Seçilen bir ülkenin detaylı bilgilerini gösteren fragment
 * Navigasyon argümanı olarak ülke UUID'sini alır ve veritabanından veri çeker
 */
class CountryFragment : Fragment() {
    /**
     * Bu fragment için ViewModel örneği
     * UI verilerini ve veritabanı işlemlerini yönetir
     */
    private lateinit var viewModel: CountryViewModel
    
    /**
     * Gösterilecek ülkenin UUID'si
     * FeedFragment'tan navigasyon argümanı olarak alınır
     */
    private var countryUuid = 0
    
    /**
     * Ülke bilgilerini göstermek için UI bileşenleri
     */
    private lateinit var countryName: TextView
    private lateinit var countryCapital: TextView
    private lateinit var countryRegion: TextView
    private lateinit var countryCurrency: TextView
    private lateinit var countryLanguage: TextView
    private lateinit var countryImage: ImageView

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
        return inflater.inflate(R.layout.fragment_country, container, false)
    }

    /**
     * View oluşturulduktan sonra çağrılır
     * UI bileşenlerini ayarlar, argümanları alır, ViewModel'i başlatır ve LiveData'yı gözlemler
     * 
     * @param view Fragment'ın kök view'ı
     * @param savedInstanceState Fragment yeniden oluşturuluyorsa önceki durum
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // UI bileşeni referanslarını başlat
        viewConnection(view)
        
        try {
            // Navigasyon paketinden countryUuid argümanını çıkar
            arguments?.let {
                countryUuid = CountryFragmentArgs.fromBundle(it).countryUuid
            }
            
            // ViewModel'i başlat ve veritabanından ülke verilerini al
            viewModel = ViewModelProvider(this).get(CountryViewModel::class.java)
            viewModel.getDataFromRoom(uuid = countryUuid)

            // LiveData gözlemcilerini ayarla
            observeLiveData()
        } catch (e: Exception) {
            Log.e("CountryFragment", "onViewCreated'da hata: ${e.message}")
            Toast.makeText(context, "Ülke detayları yüklenirken bir hata oluştu", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * ViewModel'den LiveData nesneleri için gözlemciler ayarlar
     * Ülke verileri değiştiğinde UI'ı günceller
     */
    private fun observeLiveData() {
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->
            country?.let {
                try {
                    // Ülke verilerini UI bileşenlerine bağla
                    countryName.text = country.countryName ?: "Bilgi yok"
                    countryCapital.text = country.countryCapital ?: "Bilgi yok"
                    countryCurrency.text = country.countryCurrency ?: "Bilgi yok"
                    countryLanguage.text = country.countryLanguage ?: "Bilgi yok"
                    countryRegion.text = country.countryRegion ?: "Bilgi yok"
                    
                    // Extension fonksiyonu kullanarak ülke bayrağı resmini yükle
                    context?.let { ctx ->
                        countryImage.downloadFromUrl(country.imageUrl, placeholderProcessBar(ctx))
                    }
                } catch (e: Exception) {
                    Log.e("CountryFragment", "Veri bağlama hatası: ${e.message}")
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
        try {
            countryName = view.findViewById<TextView>(R.id.countryName)
            countryCapital = view.findViewById<TextView>(R.id.countryCapital)
            countryRegion = view.findViewById<TextView>(R.id.countryRegion)
            countryCurrency = view.findViewById<TextView>(R.id.countryCurrency)
            countryLanguage = view.findViewById<TextView>(R.id.countryLanguage)
            countryImage = view.findViewById<ImageView>(R.id.countryImage)
        } catch (e: Exception) {
            Log.e("CountryFragment", "viewConnection'da hata: ${e.message}")
        }
    }
}


