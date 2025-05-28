/**
 * Model.kt
 * 
 * API yanıtları ve yerel veritabanı depolaması için ülke veri modelini tanımlar.
 * 
 * Amacı:
 * - Ülke verilerinin yapısını tanımlar
 * - Room veritabanı için entity olarak kullanılır
 * - JSON API yanıtlarını Kotlin nesnelerine dönüştürür
 * - Veri tutarlılığını sağlar
 * 
 * Kullanılan Teknolojiler:
 * - Room Database: Android için SQLite soyutlama katmanı
 * - Entity: Veritabanı tablosu olarak işaretler
 * - ColumnInfo: Veritabanı tablosundaki sütun isimlerini belirtir
 * - PrimaryKey: Veritabanı tablosu için birincil anahtarı tanımlar
 * - Gson: JSON alan isimlerini sınıf özelliklerine eşler
 * - Data class: equals(), hashCode(), toString() ve copy() metodlarını otomatik oluşturur
 */
package com.kilavuzhilmi.kotlincountries.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Ülke veri sınıfı
 * API'den gelen verileri ve veritabanı işlemlerini yönetir
 * 
 * @property countryName API'den gelen ülke adı (JSON'da "name" olarak eşleşir)
 * @property countryRegion API'den gelen ülke bölgesi (JSON'da "region" olarak eşleşir)
 * @property countryCapital API'den gelen ülke başkenti (JSON'da "capital" olarak eşleşir)
 * @property countryCurrency API'den gelen ülke para birimi (JSON'da "currency" olarak eşleşir)
 * @property countryLanguage API'den gelen ülke dili (JSON'da "language" olarak eşleşir)
 * @property imageUrl API'den gelen ülke bayrağı resmi URL'i (JSON'da "flag" olarak eşleşir)
 * @property uuid Veritabanı için otomatik oluşturulan benzersiz tanımlayıcı (API'den gelmez)
 */
@Entity(tableName = "country")
data class Country (

        @ColumnInfo(name = "name")
        @SerializedName("name")
        val countryName: String?,

        @ColumnInfo(name= "region")
        @SerializedName("region")
        val countryRegion: String?,

        @ColumnInfo(name = "capital")
        @SerializedName("capital")
        val countryCapital: String?,

        @ColumnInfo(name="currency")
        @SerializedName("currency")
        val countryCurrency: String?,

        @ColumnInfo(name="language")
        @SerializedName("language")
        val countryLanguage: String?,

        @ColumnInfo(name="flag")
        @SerializedName("flag")
        val imageUrl: String?)
{
        /**
         * Veritabanı tablosu için birincil anahtar
         * Veritabanına eklendiğinde otomatik oluşturulur (0'dan başlar)
         * Veritabanı işlemleri ve ekranlar arası geçiş için kullanılır
         */
        @PrimaryKey(autoGenerate = true)
        var uuid: Int = 0
}
