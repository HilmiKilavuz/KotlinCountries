/**
 * Util.kt
 * 
 * Resim yükleme ve görüntüleme işlemleri için yardımcı fonksiyonlar.
 * 
 * Amacı:
 * - Yaygın resim yükleme görevleri için extension fonksiyonları sağlar
 * - Resim yükleme kütüphanelerinin karmaşıklığını gizler
 * - Tutarlı yükleme göstergeleri oluşturur
 * - Resim işleme işlemlerini standartlaştırır
 * 
 * Kullanılan Teknolojiler:
 * - Glide: Resim yükleme ve önbellekleme kütüphanesi
 * - Extension fonksiyonlar: Mevcut sınıfları genişletmek için Kotlin özelliği
 * - CircularProgressDrawable: Android'in yerleşik yükleme göstergesi
 */
package com.kilavuzhilmi.kotlincountries.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kilavuzhilmi.kotlincountries.R

/**
 * ImageView için URL'den resim yüklemek için extension fonksiyon
 * 
 * @param Url Yüklenecek resmin URL'i
 * @param progressDrawable Resim yüklenirken gösterilecek yükleme göstergesi
 * 
 * Bu extension fonksiyon:
 * 1. Yer tutucu ve hata resmi ile Glide istek seçeneklerini ayarlar
 * 2. Glide'ı kullanarak URL'den resmi yükler
 * 3. Resim yüklenirken bir yükleme göstergesi gösterir
 * 4. Yükleme başarısız olursa yedek bir resim gösterir
 */
fun ImageView.downloadFromUrl(Url: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)

    Glide.with(this)
        .setDefaultRequestOptions(options)
        .load(Url)
        .into(this)
}

/**
 * Yükleme göstergesi olarak kullanılmak üzere dairesel ilerleme çizimi oluşturur
 * 
 * @param context Çizimi oluşturmak için kullanılan bağlam
 * @return Yapılandırılmış ve başlatılmış bir CircularProgressDrawable
 * 
 * Bu fonksiyon:
 * 1. Verilen bağlamla yeni bir CircularProgressDrawable oluşturur
 * 2. Görünümünü yapılandırır (çizgi kalınlığı ve yarıçap)
 * 3. Animasyonu başlatır
 * 4. Kullanıma hazır yükleme göstergesini döndürür
 */
fun placeholderProcessBar(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}