/**
 * CountryDatabase.kt
 * 
 * Ülke verilerinin yerel depolanması için Room veritabanı uygulaması.
 * 
 * Amacı:
 * - Uygulama için veritabanı örneği sağlar
 * - Room veritabanını entity'ler ve versiyon ile yapılandırır
 * - Singleton desenini uygulayarak tek bir veritabanı örneğinin var olmasını sağlar
 * - Veritabanı işlemlerini yönetir
 * 
 * Kullanılan Teknolojiler:
 * - Room Database: Android için SQLite soyutlama katmanı
 * - Database: Room veritabanını yapılandırır
 * - Singleton deseni: Veritabanının tek bir örneğinin olmasını sağlar
 * - Volatile: Değişikliklerin tüm thread'ler arasında görünür olmasını sağlar
 * - Double-checked locking: Thread-safe singleton uygulaması
 */
package com.kilavuzhilmi.kotlincountries.service

import android.content.Context
import android.graphics.ColorSpace
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kilavuzhilmi.kotlincountries.model.Country

/**
 * Veri erişim nesnelerine (DAO'lar) erişim sağlayan Room veritabanı sınıfı
 * 
 * @Database anotasyonu şunları belirtir:
 * - entities: Veritabanı tablolarını tanımlayan entity sınıflarının listesi (Country)
 * - version: Migrasyonlar için veritabanı şema versiyonu
 */
@Database(entities = arrayOf(Country::class), version = 1)
abstract class CountryDatabase : RoomDatabase() {

    /**
     * CountryDAO'yu almak için soyut metod
     * Room, bu metodun bir uygulamasını çalışma zamanında oluşturur
     * 
     * @return Veritabanı işlemleri için CountryDAO arayüz uygulaması
     */
    abstract fun countryDao(): CountryDAO

    /**
     * Veritabanının singleton uygulaması için companion object
     * Veritabanının yalnızca bir örneğinin oluşturulmasını sağlar
     */
    companion object {
        /**
         * Volatile, instance'daki değişikliklerin tüm thread'lere hemen görünür olmasını sağlar
         * Çok thread'li ortamlarda önbellek sorunlarını önler
         */
        @Volatile
        private var instance: CountryDatabase? = null

        /**
         * Örnek oluşturma sırasında senkronizasyon için kilit nesnesi
         */
        private val lock = Any()
        
        /**
         * invoke operatör fonksiyonu kullanarak singleton uygulaması
         * Thread güvenliği için double-checked locking desenini uygular
         * 
         * @param context Veritabanını oluşturmak için kullanılan uygulama bağlamı
         * @return CountryDatabase'in singleton örneği
         */
        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context = context).also {
                instance = it
            }
        }

        /**
         * Room veritabanının yeni bir örneğini oluşturur
         * 
         * @param context Veritabanını oluşturmak için kullanılan uygulama bağlamı
         * @return CountryDatabase'in yeni bir örneği
         * 
         * Veritabanı oluşturucu:
         * - Veritabanı adını "countrydatabase" olarak ayarlar
         * - Şema versiyonu değiştiğinde yıkıcı migrasyon yapılandırır
         */
        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, CountryDatabase::class.java, "countrydatabase"
        )
        .fallbackToDestructiveMigration()
        .build()
    }
}