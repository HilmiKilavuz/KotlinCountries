/**
 * CustomSharedPreferences.kt
 * 
 * Singleton deseni ile paylaşılan tercihleri yönetmek için yardımcı sınıf.
 * 
 * Amacı:
 * - Paylaşılan tercihlere erişim ve yönetim için merkezi bir yol sağlar
 * - Thread-safe singleton desenini uygular
 * - Zaman damgaları gibi uygulama tercihlerini saklar ve alır
 * - Veri önbellekleme stratejisini yönetir
 * 
 * Kullanılan Teknolojiler:
 * - SharedPreferences: Basit veriler için Android'in anahtar-değer deposu
 * - Singleton deseni: Uygulama genelinde tek bir örnek sağlar
 * - Volatile: Değişikliklerin tüm thread'ler arasında görünür olmasını sağlar
 * - Double-checked locking: Thread-safe singleton uygulaması
 */
package com.kilavuzhilmi.kotlincountries.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * Singleton uygulaması ile SharedPreferences için özel sarmalayıcı sınıf
 * Özellikle önbellek zaman damgaları için uygulama tercihlerini yönetir
 */
class CustomSharedPreferences {
    /**
     * Statik üyeleri ve singleton uygulamasını içeren companion object
     */
    companion object {
        /**
         * SharedPreferences'da zaman tercihini saklamak için anahtar
         */
        private val PREFERENCES_TIME = "preferences_time"
        
        /**
         * SharedPreferences örneği referansı
         */
        private var sharedPreferences: SharedPreferences? = null

        /**
         * Volatile instance, değişikliklerin tüm thread'lere görünür olmasını sağlar
         */
        @Volatile
        private var instance: CustomSharedPreferences? = null
        
        /**
         * Örnek oluşturma sırasında senkronizasyon için kilit nesnesi
         */
        private var lock = Any()

        /**
         * invoke operatör fonksiyonu kullanarak singleton uygulaması
         * Thread güvenliği için double-checked locking desenini uygular
         * 
         * @param context SharedPreferences'a erişmek için kullanılan uygulama bağlamı
         * @return CustomSharedPreferences'in singleton örneği
         */
        operator fun invoke(context: Context): CustomSharedPreferences =
            instance ?: synchronized(lock) {
                instance ?: makeCustomSharedPreferences(context).also {
                    instance = it
                }
            }

        /**
         * CustomSharedPreferences'in yeni bir örneğini oluşturur
         * PreferenceManager kullanarak SharedPreferences'ı başlatır
         * 
         * @param context Varsayılan SharedPreferences'ı almak için kullanılan uygulama bağlamı
         * @return CustomSharedPreferences'in yeni bir örneği
         */
        private fun makeCustomSharedPreferences(context: Context): CustomSharedPreferences {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferences()
        }
    }

    /**
     * SharedPreferences'a bir zaman damgası kaydeder
     * API'den veri ne zaman çekildiğini takip etmek için kullanılır
     * 
     * @param time Kaydedilecek zaman damgası (nanosaniye cinsinden)
     */
    fun saveTime(time: Long) {
        sharedPreferences?.edit()?.putLong(PREFERENCES_TIME, time)?.apply()
    }

    /**
     * SharedPreferences'tan kaydedilmiş zaman damgasını alır
     * 
     * @return Kaydedilmiş zaman damgası veya ayarlanmamışsa 0
     */
    fun getTime() = sharedPreferences?.getLong(PREFERENCES_TIME, 0)

    /**
     * SharedPreferences'taki tüm tercihleri temizler
     * Uygulama verilerini sıfırlarken kullanılır
     */
    fun resetPreferences() {
        sharedPreferences?.edit()?.clear()?.apply()
    }
}