/**
 * CountryAdapter.kt
 * 
 * Ülke listesini RecyclerView'da göstermek için kullanılan adapter sınıfı.
 * 
 * Amacı:
 * - RecyclerView ile ülke verileri arasında köprü görevi görür
 * - Ülke verilerini liste öğelerine dönüştürür
 * - Liste öğelerine tıkllandığında detay sayfasına yönlendirme yapar
 * - Verimli bellek kullanımı için ViewHolder desenini uygular
 * 
 * Kullanılan Teknolojiler:
 * - RecyclerView: Büyük veri setlerini verimli şekilde göstermek için kullanılan liste bileşeni
 * - ViewHolder Deseni: Liste öğelerinin görünümlerini önbelleğe alarak performansı artırır
 * - Navigation Component: Fragment'lar arası geçişleri yönetir
 * - Data Binding: UI bileşenlerini verilerle bağlar
 * - Glide: Resim yükleme ve önbellekleme işlemlerini yönetir
 */
package com.kilavuzhilmi.kotlincountries.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.kilavuzhilmi.kotlincountries.R
import com.kilavuzhilmi.kotlincountries.adapter.CountryAdapter.CountryViewHolder
import com.kilavuzhilmi.kotlincountries.model.Country
import com.kilavuzhilmi.kotlincountries.util.downloadFromUrl
import com.kilavuzhilmi.kotlincountries.util.placeholderProcessBar
import com.kilavuzhilmi.kotlincountries.view.FeedFragmentDirections

/**
 * RecyclerView için ülke öğelerini görüntüleyen adapter sınıfı
 * @param countryList Görüntülenecek ülke nesnelerinin ArrayList'i
 */
class CountryAdapter(val countryList: ArrayList<Country>) :
    RecyclerView.Adapter<CountryViewHolder>() {
    
    /**
     * Yeni ViewHolder'lar oluşturur
     * item_country layout'unu şişirir ve yeni bir CountryViewHolder döndürür
     * 
     * @param p0 ViewHolder'ın ekleneceği üst ViewGroup
     * @param p1 Yeni View'ın tipi (bu uygulamada kullanılmıyor)
     * @return item_country tipinde bir View tutan yeni bir CountryViewHolder
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CountryViewHolder {
        val inflate = LayoutInflater.from(p0.context)
        val view = inflate.inflate(R.layout.item_country, p0, false)
        return CountryViewHolder(view)
    }

    /**
     * Verileri ViewHolder'a bağlar
     * Ülke detaylarını ayarlar ve tıklama olayını yönetir
     * 
     * @param p0 Veri bağlanacak ViewHolder
     * @param p1 Veri setindeki öğenin konumu
     */
    override fun onBindViewHolder(p0: CountryViewHolder, p1: Int) {
        p0.bind(countryList[p1])
        p0.view.setOnClickListener {
            try {
                // UUID'nin geçerli olduğundan emin ol
                val countryUuid = countryList[p1].uuid
                if (countryUuid > 0) {
                    // Ülke UUID'sini argüman olarak kullanarak navigasyon aksiyonu oluştur
                    val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryUuid)
                    // Detay sayfasına git
                    Navigation.findNavController(it).navigate(action)
                } else {
                    Log.e("CountryAdapter", "Geçersiz UUID: $countryUuid")
                    Toast.makeText(it.context, "Bu ülke detayları gösterilemiyor", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("CountryAdapter", "Navigasyon hatası: ${e.message}")
                Toast.makeText(it.context, "Detaylar yüklenirken bir hata oluştu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Veri setindeki toplam öğe sayısını döndürür
     * 
     * @return Ülke listesinin boyutu
     */
    override fun getItemCount(): Int {
        return countryList.size
    }

    /**
     * Liste öğelerinin görünümlerini önbelleğe alan ViewHolder sınıfı
     * Her liste öğesi için UI bileşenlerine referanslar tutar
     * 
     * @param view Tek bir liste öğesinin UI bileşenlerini içeren View
     */
    class CountryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.name)
        private val regionTextView: TextView = view.findViewById(R.id.region)
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        
        /**
         * Ülke verilerini UI bileşenlerine bağlar
         * 
         * @param country Görüntülenecek verileri içeren Country nesnesi
         */
        fun bind(country: Country) {
            try {
                nameTextView.text = country.countryName ?: "Bilgi yok"
                regionTextView.text = country.countryRegion ?: "Bilgi yok"
                // Bayrak resmini yüklemek için extension fonksiyonunu kullan
                imageView.downloadFromUrl(country.imageUrl, placeholderProcessBar(view.context))
            } catch (e: Exception) {
                Log.e("CountryViewHolder", "Veri bağlama hatası: ${e.message}")
            }
        }
    }

    /**
     * Ülke listesini yeni verilerle günceller ve UI'ı yeniler
     * 
     * @param newCountryList Görüntülenecek yeni ülke listesi
     */
    fun updateCountryList(newCountryList: List<Country>) {
        try {
            countryList.clear()
            countryList.addAll(newCountryList)
            notifyDataSetChanged() // Adapter'a tüm öğeleri yenilemesini bildir
        } catch (e: Exception) {
            Log.e("CountryAdapter", "Güncelleme hatası: ${e.message}")
        }
    }
}

