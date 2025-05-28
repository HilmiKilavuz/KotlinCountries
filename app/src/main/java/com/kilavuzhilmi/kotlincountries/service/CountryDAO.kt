/**
 * CountryDAO.kt
 * 
 * Ülkelerle ilgili veritabanı işlemleri için Veri Erişim Nesnesi (DAO) arayüzü.
 * 
 * Amacı:
 * - Veritabanındaki verilere erişim ve manipülasyon metodlarını tanımlar
 * - SQL sorguları üzerinde bir soyutlama katmanı sağlar
 * - Veritabanı CRUD işlemlerini (Oluştur, Oku, Güncelle, Sil) yönetir
 * - Veritabanı işlemlerini asenkron olarak gerçekleştirir
 * 
 * Kullanılan Teknolojiler:
 * - Room Database: Android için SQLite soyutlama katmanı
 * - DAO: Bu arayüzü bir Room DAO'su olarak işaretler
 * - Query: Veritabanı işlemleri için SQL sorgularını tanımlar
 * - Insert: Veritabanına veri ekleyen metodları işaretler
 * - Coroutines: Asenkron veritabanı işlemleri için kullanılır
 */
package com.kilavuzhilmi.kotlincountries.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kilavuzhilmi.kotlincountries.model.Country

/**
 * Ülke veritabanı işlemleri için Veri Erişim Nesnesi arayüzü
 * Room, bu arayüzün bir uygulamasını çalışma zamanında oluşturur
 */
@Dao
interface CountryDAO {
    //Data Access Object

    /**
     * Veritabanına birden fazla ülke nesnesi ekler
     * 
     * @param country Eklenecek ülke nesneleri (değişken sayıda)
     * @return List<Long> Eklenen öğelerin satır ID'lerinin (birincil anahtarlar) listesi
     * 
     * 'vararg' anahtar kelimesi, birden fazla Country nesnesini ayrı argümanlar olarak geçmeye izin verir
     * Dönüş değeri, her eklenen satır için otomatik oluşturulan ID'lerin bir listesidir
     */
    @Insert
    fun insertAll(vararg country: Country): List<Long>

    //suspend-> coroutine scope
    //vararg-> multiple country objects
    //List<Long>-> primary keys

    /**
     * Veritabanından tüm ülkeleri alır
     * 
     * @return List<Country> Veritabanındaki tüm ülkelerin listesi
     * 
     * Bu metod, ülke tablosundan tüm kayıtları almak için basit bir SQL SELECT sorgusu kullanır
     */
    @Query("SELECT * FROM country")
    fun getAllCountries(): List<Country>

    /**
     * Benzersiz tanımlayıcısına göre belirli bir ülkeyi alır
     * 
     * @param uuid Alınacak ülkenin benzersiz tanımlayıcısı
     * @return Country Belirtilen UUID'ye sahip ülke
     * 
     * Bu metod, eşleşen UUID'ye sahip bir ülkeyi bulmak için parametreli bir SQL sorgusu kullanır
     */
    @Query("SELECT * FROM country WHERE uuid = :uuid")
    fun getCountryById(uuid: Int): Country

    /**
     * Veritabanından tüm ülkeleri siler
     * 
     * Bu metod, ülke tablosundan tüm kayıtları kaldırmak için bir SQL DELETE sorgusu kullanır
     * API'den veri yenilenirken yinelenen girişleri önlemek için kullanılır
     */
    @Query("DELETE FROM country")
    fun deleteAllCountries()
}