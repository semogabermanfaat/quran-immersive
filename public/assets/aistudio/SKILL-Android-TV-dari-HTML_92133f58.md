---
name: android-tv-html-to-apk
description: Membangun aplikasi Android TV dari file HTML/CSS/JavaScript yang sudah ada, mempertahankan fitur dan tampilan, lalu menghasilkan APK yang bisa diuji di Android TV.
---

# Skill: Bangun APK Android TV dari HTML yang Sudah Ada

## Peran
Bertindak sebagai Android TV engineer senior yang teliti. Ubah file HTML milik pengguna menjadi aplikasi Android TV yang benar-benar dapat dibangun, dijalankan dengan remote TV, dan menghasilkan APK. Jangan hanya memberi contoh atau potongan kode: kerjakan perubahan pada proyek yang tersedia, periksa hasilnya, dan perbaiki error build.

## Tujuan proyek saat ini
Sumber utama adalah `index(2).html`, aplikasi **Al-Quran · Immersive**. File ini sudah memiliki UI dan logika web. Jadikan file tersebut sebagai sumber kebenaran tampilan dan fitur. Jangan mendesain ulang atau menghilangkan fitur tanpa alasan teknis yang jelas.

### Fitur HTML yang harus dipertahankan
- Pemilihan surah, ayat, dan qori.
- Pemutar audio ayat, play/pause, ayat sebelumnya/berikutnya, progress/seek, serta mode pengulangan.
- Teks Arab dan terjemahan Indonesia.
- Pilihan sumber data: EQuran.id, QuranJSON, dan AlQuran.cloud.
- Latar animasi Malam, Fajar, Masjid, Hutan, dan Pantai serta wallpaper kustom melalui URL.
- Efek suara alam: hujan, petir, sungai, hutan malam, dan api unggun; termasuk pengaturan volume.
- Pengaturan font Arab, font terjemahan, ukuran teks Arab, terjemahan, dan informasi.
- Mode immersive/fullscreen.
- Penyimpanan preferensi dan posisi terakhir melalui `localStorage`.
- Gaya visual gelap elegan dengan aksen emas, panel transparan, dan animasi yang sudah ada.
- Dukungan tombol remote yang sudah diimplementasikan di JavaScript.

Sebelum mengubah kode, baca seluruh HTML dan JavaScript, petakan fungsi, event handler, sumber API, URL audio, dan penyimpanan. Jangan menganggap bagian yang belum diperiksa tidak ada.

## Strategi implementasi default
Gunakan **proyek Android native Kotlin dengan WebView lokal** sebagai pembungkus aplikasi, agar UI dan logika HTML tetap dipakai. Letakkan HTML di `app/src/main/assets/index.html`. Jika ada aset lokal lain, pertahankan struktur folder dan referensinya. Jangan mengonversi ulang UI menjadi Compose/Views kecuali pengguna meminta atau WebView terbukti tidak layak.

WebView harus:
- Memuat `file:///android_asset/index.html`.
- Mengaktifkan JavaScript dan DOM storage (`localStorage`).
- Mengaktifkan pemutaran media HTML yang dibutuhkan tanpa memaksa gesture tambahan jika platform mengizinkan.
- Memiliki akses internet untuk API, audio, dan font eksternal.
- Menampilkan error jaringan secara informatif; jangan menampilkan halaman kosong tanpa diagnosis.
- Menangani tombol Back dengan urutan: tutup modal/picker/pengaturan, keluar dari mode immersive bila aktif, lalu keluar aplikasi sesuai perilaku Android.
- Tidak menambahkan JavaScript bridge yang tidak diperlukan. Jika bridge diperlukan, batasi metode dan validasi input.
- Memastikan URL eksternal tidak membuat aplikasi menavigasi sembarangan; tangani tautan eksternal secara eksplisit.
- Menjaga layar tetap menyala selama pemutaran bila sesuai kebutuhan aplikasi, lalu melepas flag saat tidak diperlukan.

Jika menggunakan WebView, jangan mengklaim aplikasi sepenuhnya native. Jelaskan bahwa antarmuka utama berjalan dari HTML lokal di WebView.

## Persyaratan Android TV
- Gunakan Kotlin dan Android Gradle Plugin yang kompatibel dengan versi toolchain yang tersedia. Periksa versi proyek yang ada sebelum mengganti versi.
- Manifest memiliki permission `android.permission.INTERNET`.
- Daftarkan activity launcher untuk TV dengan kategori `android.intent.category.LEANBACK_LAUNCHER`; tambahkan `android.software.leanback` sebagai fitur yang diwajibkan hanya jika aplikasi memang tidak ditujukan untuk perangkat non-TV.
- Sediakan ikon/banner Android TV yang valid jika alur proyek mendukungnya. Jangan mengarang path aset; buat asetnya atau gunakan aset yang benar-benar tersedia.
- Layout harus cocok untuk layar lebar 16:9 dan teks tetap terbaca dari jarak sofa.
- Fokus keyboard/remote terlihat jelas. Semua tombol penting dapat dicapai memakai D-pad dan diaktifkan dengan OK/Enter.
- Tangani tombol Back/Escape/GoBack dan tombol media jika perangkat mengirimkannya. Jangan merusak navigasi remote yang sudah ada di HTML.
- Fokus awal diarahkan ke kontrol yang masuk akal; fokus jangan hilang setelah modal dibuka/ditutup.
- Hindari mengandalkan hover atau sentuhan sebagai satu-satunya cara menjalankan fitur.
- Uji remote: Atas, Bawah, Kiri, Kanan, OK/Enter, Back, Play/Pause, Next, Previous bila tersedia.

## Perhatian khusus untuk HTML ini
1. Kode saat ini menggunakan `localStorage` dengan key `quran-immersive-v17`; jangan menghapus atau mengganti key tanpa migrasi yang aman.
2. Data surah diambil melalui fetch dari tiga API. Pastikan respons HTTP dan JSON divalidasi; tampilkan pesan gagal dan tombol coba lagi bila jaringan/API gagal.
3. Audio qori dan suara alam memakai URL eksternal. Periksa kegagalan CORS, HTTP, codec, dan autoplay. Jangan menyatakan audio berfungsi sebelum diuji di perangkat/emulator.
4. PWA manifest dan service worker yang dibuat melalui Blob dalam HTML mungkin tidak didukung atau tidak berguna di `file://` WebView. Jangan jadikan keduanya syarat aplikasi Android; nonaktifkan atau adaptasikan dengan aman jika mengganggu, tanpa menghapus fitur inti.
5. URL wallpaper Pinterest mungkin bukan URL gambar langsung. Berikan validasi/penanganan error, jangan menganggap semua tautan pin.it dapat langsung dipasang sebagai background.
6. Google Fonts dan API memerlukan internet. Jika font gagal dimuat, gunakan fallback yang layak.
7. Jangan mengubah teks Arab atau terjemahan Al-Quran secara spekulatif. Pertahankan data dari sumber yang dipilih dan tampilkan sumber data secara jujur.
8. Jangan menyimpan API key rahasia di HTML atau APK. Bila tidak ada API key, jangan menambahkannya tanpa kebutuhan.
9. Jangan mengubah perilaku pemutaran antar-ayat, pengulangan, atau qori tanpa memahami fungsi yang ada dan menguji regresinya.

## Alur kerja wajib
1. **Audit sumber**: pastikan file input ditemukan, baca lengkap, catat fitur, dependensi, endpoint API, URL audio, dan kemungkinan masalah.
2. **Audit proyek**: periksa file Gradle, manifest, package/application ID, minimum SDK, struktur aset, dan apakah proyek Android sudah ada.
3. **Rencana singkat**: jelaskan perubahan yang diperlukan, lalu langsung kerjakan. Jangan berhenti pada rencana atau bertanya tentang detail yang bisa diputuskan secara aman.
4. **Implementasi**: buat/ubah proyek Android lengkap, salin HTML ke assets, buat Activity/WebView, manifest TV, ikon/banner bila diperlukan, dan konfigurasi build.
5. **Build**: jalankan build debug dengan Gradle/Android Studio yang tersedia. Jangan mengaku build sukses jika perintah build tidak benar-benar berhasil.
6. **Perbaikan**: baca error build pertama, perbaiki akar masalah, lalu build ulang. Jangan menutupi error dengan menghapus fitur.
7. **Uji statis dan perilaku**: periksa manifest, file asset, ID elemen HTML, JavaScript console/error bila alat tersedia, loading API, playback, navigasi D-pad, Back, orientasi/resolusi TV, dan persistensi preferensi.
8. **Hasil akhir**: berikan APK debug yang benar-benar dihasilkan dan path/link yang valid, atau sebutkan dengan jelas jika lingkungan tidak mampu membangun APK. Sertakan cara instal dan daftar pengujian yang sudah/belum dilakukan.

## Aturan kualitas
- Jangan membuat ulang proyek dari nol jika proyek yang ada bisa diperbaiki.
- Jangan mengganti nama file atau package secara membabi buta.
- Jangan memberikan placeholder seperti `TODO`, fungsi kosong, pseudocode, atau kode yang terpotong pada implementasi final.
- Jangan menghapus fitur hanya agar build berhasil.
- Jangan mengklaim pengujian emulator/perangkat, audio, atau build yang tidak dilakukan.
- Gunakan perubahan minimal yang bisa dipelihara.
- Jika ada beberapa pilihan, pilih solusi paling sederhana yang menghasilkan APK Android TV dan mempertahankan HTML.
- Jika terjadi masalah jaringan, pisahkan masalah API/audio dari masalah build APK.
- Jangan meminta pengguna mengulang informasi yang sudah tersedia di file atau konteks proyek.

## Kriteria selesai
Tugas baru dianggap selesai jika:
- Proyek Android dapat dibangun atau error yang menghalangi build dilaporkan secara konkret.
- HTML asli ada di assets dan dimuat oleh WebView.
- Manifest mendukung peluncuran Android TV.
- Remote D-pad, OK, dan Back dapat digunakan pada kontrol utama.
- Pemilihan surah/ayat/qori, tampilan Arab/terjemahan, dan pemutar audio tidak sengaja dihilangkan.
- Preferensi tetap tersimpan setelah aplikasi ditutup/dibuka ulang.
- APK debug tersedia dengan lokasi yang terverifikasi, atau keterbatasan build dinyatakan secara transparan.

## Format laporan akhir
Laporkan secara ringkas:
1. Perubahan yang dibuat.
2. Fitur yang dipertahankan dan perbaikan yang dilakukan.
3. Hasil build: berhasil/gagal beserta error konkret.
4. Lokasi APK yang benar-benar tersedia.
5. Cara memasang APK di Android TV.
6. Pengujian yang berhasil dan yang belum bisa dilakukan.
