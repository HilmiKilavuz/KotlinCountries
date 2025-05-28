/**
 * CountryAPI.kt
 * 
 * Ülke verilerini çekmek için RESTful API isteklerini tanımlayan Retrofit arayüzü.
 * 
 * Amacı:
 * - RESTful API için endpoint'leri tanımlar
 * - HTTP metodlarını ve istek parametrelerini belirtir
 * - API yanıtlarını Kotlin nesnelerine dönüştürür
 * - Ağ isteklerinin yapısını belirler
 * 
 * Kullanılan Teknolojiler:
 * - Retrofit: Android ve Java için tip güvenli HTTP istemcisi
 * - RxJava: Asenkron programlama için reaktif uzantılar
 * - GET: HTTP GET metodu için anotasyon
 * - Interface: Ağ istekleri için sözleşme tanımlar
 * - Single: Tek bir değer döndüren ve tamamlanan observable
 */
package com.kilavuzhilmi.kotlincountries.service

import com.kilavuzhilmi.kotlincountries.model.Country
import io.reactivex.Single
import retrofit2.http.GET

/**
 * API endpoint'lerini tanımlayan CountryAPI arayüzü
 * Retrofit, bu arayüzün bir uygulamasını çalışma zamanında oluşturur
 */
interface CountryAPI {
    //https://raw.githubusercontent.com/atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json
    //BASE URL-> https://raw.githubusercontent.com/
    //EXT-> atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json

    /**
     * API'den ülke listesini çeker
     * 
     * @return Single<List<Country>> - Ülke nesnelerinin listesini yayınlayan bir observable
     * - Single: Tam olarak bir değer veya hata yayınlar
     * - Endpoint, Retrofit'in List<Country>'ye dönüştürdüğü bir JSON dizisi döndürür
     */
    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
    fun getCountries(): Single<List<Country>>

}