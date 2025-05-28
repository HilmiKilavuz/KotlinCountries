# Kotlin Countries

Bu proje, ülkelerin temel bilgilerini gösteren ve detaylı bilgilerine erişim sağlayan bir Android uygulamasıdır. MVVM (Model-View-ViewModel) mimarisi kullanılarak geliştirilmiştir.

## 🚀 Özellikler

- Ülke listesi görüntüleme
- Ülke detaylarını görüntüleme
- Aşağı çekerek yenileme
- Çevrimdışı veri desteği
- Hata yönetimi ve kullanıcı geri bildirimi
- Yükleme göstergeleri
- Ülke bayrakları görüntüleme

## 🛠 Kullanılan Teknolojiler

- **Kotlin**: Programlama dili
- **Android Jetpack**: Modern Android geliştirme araçları
  - ViewModel: UI verilerini yönetme
  - LiveData: Yaşam döngüsüne duyarlı veri tutucu
  - Room: Yerel veritabanı
  - Navigation Component: Fragment yönetimi
- **Retrofit**: API istekleri için
- **RxJava**: Asenkron programlama
- **Glide**: Resim yükleme ve önbellekleme
- **Coroutines**: Asenkron işlemler
- **SharedPreferences**: Yerel veri depolama

## 📱 Ekran Görüntüleri

[Ekran görüntüleri buraya eklenecek]

## 🏗 Proje Yapısı

```
app/
├── java/
│   └── com.kilavuzhilmi.kotlincountries/
│       ├── adapter/         # RecyclerView adaptörleri
│       ├── model/           # Veri modelleri
│       ├── service/         # API ve veritabanı servisleri
│       ├── util/            # Yardımcı sınıflar
│       ├── view/            # Fragment'lar
│       └── viewmodel/       # ViewModel sınıfları
└── res/                     # Kaynaklar (layout, drawable, values)
```

## 🚀 Başlangıç

### Gereksinimler

- Android Studio Arctic Fox veya üzeri
- Android SDK 21 veya üzeri
- Kotlin 1.5.0 veya üzeri

### Kurulum

1. Projeyi klonlayın:
```bash
git clone https://github.com/kullaniciadi/KotlinCountries.git
```

2. Android Studio'da projeyi açın

3. Gradle senkronizasyonunu tamamlayın

4. Uygulamayı çalıştırın

## 📦 Veri Kaynağı

Uygulama, ülke verilerini şu API'den alır:
```
https://raw.githubusercontent.com/atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json
```

## 🛠 Mimari

Proje, MVVM (Model-View-ViewModel) mimarisi kullanılarak geliştirilmiştir:

- **Model**: Veri modelleri ve veri kaynakları
- **View**: Fragment'lar ve UI bileşenleri
- **ViewModel**: UI verilerini ve iş mantığını yönetir

## 🔄 Veri Akışı

1. API'den veri çekme
2. Room veritabanına kaydetme
3. ViewModel üzerinden UI'a aktarma
4. Fragment'larda görüntüleme

## 🧪 Test

Projeyi test etmek için:

1. Android Studio'da projeyi açın
2. Bir Android cihaz veya emülatör bağlayın
3. Run butonuna tıklayın

## 📝 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakın.

## 👥 Katkıda Bulunma

1. Bu depoyu fork edin
2. Yeni bir branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add some amazing feature'`)
4. Branch'inizi push edin (`git push origin feature/amazing-feature`)
5. Bir Pull Request oluşturun

## 📞 İletişim

Proje Sahibi - [@kilavuzhilmi](https://github.com/kilavuzhilmi)

Proje Linki: [https://github.com/kilavuzhilmi/KotlinCountries](https://github.com/kilavuzhilmi/KotlinCountries) 