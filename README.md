# 📖 Al-Quran Immersive — Android TV Edition

Aplikasi **Al-Quran · Immersive** untuk Android TV dengan visual elegan, audio ayat per-qori, terjemahan bahasa Indonesia, efek suara alam ambien, latar animasi dinamis, serta navigasi 100% kompatibel dengan remote TV (D-pad & media keys).

---

## 🌟 Fitur Utama

- **Mushaf Lengkap 114 Surah**:
  - Teks Arab berharakat indah dengan font pilihan (Scheherazade New, Amiri, Noto Naskh, Lateef).
  - Terjemahan bahasa Indonesia lengkap dengan font modern (Plus Jakarta Sans, Inter, Lora, Cormorant).
  - Skala ukuran teks Arab, terjemahan, dan info surah yang dapat disesuaikan untuk kenyamanan baca di layar TV dari jarak jauh.
- **Pilihan 3 Sumber API Database Mushaf**:
  1. **EQuran.id** (Standar Kemenag RI)
  2. **QuranJSON** (Data GitHub)
  3. **AlQuran.cloud** (Data Internasional Utsmani)
- **Audio Murattal Berkualitas (7 Qori Ternama)**:
  - Mishary Rashid Alafasy
  - Abdul Basit (Murattal)
  - Abdul Basit (Mujawwad)
  - Mahmoud Khalil Al-Husary
  - Muhammad Siddiq Al-Minshawi
  - Abdurrahman As-Sudais
  - Yasser Al-Dosari
  - *Fitur pemutar*: Play/Pause, Ayat Sebelumnya, Ayat Berikutnya, Seekbar/Progress, dan Mode Ulang (Semua, 1 Ayat, atau 1 Surah).
- **Efek Suara Alam Ambien**:
  - Hujan (Rain)
  - Petir Lembut (Thunder)
  - Gemercik Air Sungai (River)
  - Suasana Hutan Malam (Night Forest)
  - Api Unggun (Campfire)
  - Kontrol volume suara alam yang terpisah dan bertahap.
- **Latar Belakang Animasi Bergerak**:
  - Malam berbintang dengan bulan & bintang jatuh.
  - Fajar dengan sinar matahari terbit & siluet burung.
  - Masjid megah bercahaya keemasan di malam hari.
  - Hutan pinus berkabut dengan kunang-kunang bersinar.
  - Pantai senja dengan deburan ombak berirama & pohon kelapa.
  - Dukungan kustom wallpaper via URL gambar atau tautan Pinterest.
- **Mode Immersive Layar Penuh**:
  - Menyembunyikan menu atas dan dock pemutar untuk pengalaman membaca dan mendengarkan yang khusyuk dan hening.
  - Otomatis memulai lantunan saat masuk mode immersive.
- **Penyimpanan Pengaturan Otomatis**:
  - Posisi surah & ayat terakhir, qori yang dipilih, font, latar, suara alam, dan volume tersimpan aman di penyimpanan lokal (`localStorage` key: `quran-immersive-v17`).

---

## 🎮 Kontrol Remote TV (D-Pad & Media Keys)

Aplikasi dirancang khusus untuk kenyamanan navigasi sofa menggunakan Remote TV Android:

| Tombol Remote | Fungsi |
| :--- | :--- |
| **Arah Atas / Bawah** | Berpindah antar tombol menu atas, ayat, dock pemutar, atau opsi setelan |
| **Arah Kiri / Kanan** | Memilih opsi horizontal (Surah, Ayat, Qori, Play, Tombol Navigasi) |
| **OK / Enter / Center** | Membuka daftar pilihan, memilih surah/ayat/qori, atau memutar audio |
| **Kembali (Back / Return)** | 1. Menutup popup pemilihan surah/ayat/qori/setelan<br>2. Keluar dari mode immersive<br>3. Keluar dari aplikasi |
| **Play / Pause (Media)** | Memulai atau menjeda audio murattal ayat |
| **Next / Prev Track** | Berpindah ke ayat berikutnya atau ayat sebelumnya |

---

## 🛠️ Arsitektur & Teknologi

- **Platform Target**: Android TV (Leanback Launcher compatible) & Android Tablet/Mobile
- **Minimum SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 15 / 16 (API 36)
- **Wrapper**: Kotlin Native `ComponentActivity` + Android `WebView` dengan hardware acceleration
- **UI Engine**: HTML5, CSS3 Glassmorphism, Vanilla JavaScript
- **Manajemen Layar**: `FLAG_KEEP_SCREEN_ON` (layar TV tetap menyala selama audio berputar) & `WindowInsetsControllerCompat` (fullscreen borderless)

---

## 📦 File APK Debug & Lokasi Output

Hasil build APK debug tersedia di:
```text
app/build/outputs/apk/debug/app-debug.apk
```
Ukuran: **~23 MB**

---

## 📲 Cara Memasang APK di Android TV

### Metode 1: Menggunakan Flashdisk / USB Drive (Paling Mudah)
1. Unduh atau salin file `app-debug.apk` ke flashdisk.
2. Tancapkan flashdisk ke port USB di Android TV / Android TV Box.
3. Buka aplikasi File Manager di TV (seperti *File Commander*, *FX File Explorer*, atau *AnExplorer*).
4. Pilih file `app-debug.apk`, lalu pilih **Install**.
   *(Jika diminta izin instalasi dari sumber tidak dikenal, aktifkan izin untuk aplikasi File Manager tersebut).*

### Metode 2: Menggunakan Aplikasi "Send Files to TV" (Tanpa Kabel)
1. Pasang aplikasi **Send Files to TV** di HP/Laptop dan di Android TV.
2. Pastikan kedua perangkat berada di jaringan Wi-Fi yang sama.
3. Kirim file `app-debug.apk` dari HP/Laptop ke Android TV.
4. Buka file yang diterima di Android TV lalu jalankan instalasi.

### Metode 3: Menggunakan ADB Wireless (Developer)
1. Aktifkan **Developer Options** di TV: `Pengaturan > Preferensi Perangkat > Tentang > Tekan Build/Nomor Versi sebanyak 7 kali`.
2. Aktifkan **USB Debugging / Network Debugging**.
3. Hubungkan komputer Anda:
   ```bash
   adb connect <ALAMAT_IP_TV>:5555
   ```
4. Pasang APK langsung ke Android TV:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 🏗️ Cara Membangun Proyek (Build from Source)

Pastikan lingkungan telah memiliki JDK 17+ dan Android SDK.

- **Menjalankan Unit Test**:
  ```bash
  gradle testDebugUnitTest
  ```
- **Membangun APK Debug**:
  ```bash
  gradle assembleDebug
  ```

---

## 📄 Lisensi & Kredit

- **Teks Mushaf & Terjemahan**: EQuran.id (Kemenag RI), QuranJSON, AlQuran.cloud.
- **Audio Reciter**: EveryAyah.com.
- **Efek Suara Ambien**: Noctune Sound Assets (CC0 / Open Source).
