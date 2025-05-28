/**
 * CountryAPIServices.kt
 * 
 * Ülke verilerini çekmek için API çağrılarını gerçekleştiren servis sınıfı.
 * 
 * Amacı:
 * - Retrofit örneğini yapılandırır ve oluşturur
 * - API endpoint'lerine erişim metodları sağlar
 * - Ağ isteği uygulama detaylarını kapsüller
 * - API ile iletişimi yönetir
 * 
 * Kullanılan Teknolojiler:
 * - Retrofit: Android ve Java için tip güvenli HTTP istemcisi
 * - RxJava: Asenkron programlama için reaktif uzantılar
 * - Gson Converter: JSON yanıtlarını Kotlin nesnelerine dönüştürür
 * - RxJava2CallAdapterFactory: Retrofit'ten RxJava 2 tiplerini döndürmek için adaptör
 */
package com.kilavuzhilmi.kotlincountries.service

import com.kilavuzhilmi.kotlincountries.model.Country
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Ülke verileri için API isteklerini yöneten servis sınıfı
 * Uygun dönüştürücüler ve adaptörlerle Retrofit örneğini yapılandırır
 */
class CountryAPIServices {
    //BASE URL-> https://raw.githubusercontent.com/
    private val BASE_URL = "https://raw.githubusercontent.com/"
    
    /**
     * Retrofit örneği yapılandırması:
     * 1. Tüm API istekleri için temel URL'i ayarlar
     * 2. JSON yanıtlarını Kotlin nesnelerine ayrıştırmak için GsonConverterFactory ekler
     * 3. RxJava dönüş tiplerini desteklemek için RxJava2CallAdapterFactory ekler
     * 4. Retrofit örneğini oluşturur
     * 5. CountryAPI arayüzünün bir uygulamasını oluşturur
     */
     val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CountryAPI::class.java)

    /**
     * API'den ülke verilerini alır
     * 
     * @return Single<List<Country>> - Ülke nesnelerinin listesini veya hatayı yayınlayan bir observable
     * Bu metod, gerçek API çağrısını Retrofit tarafından oluşturulan uygulamaya devreder
     */
    fun getData(): Single<List<Country>>{
        return api.getCountries()
    }
}