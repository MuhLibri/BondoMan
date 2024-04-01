# Sleepee Inc. - Tugas Besar 1 - BondoMan


## Deskripsi Aplikasi

Aplikasi yang bernama Bondoman ini merupakan aplikasi manajemen aplikasi yang membolehkan penggunanya melakukan manajemen transaksi, yakni penambahan, pengubahan, dan penghapusan data transaksi. Transaksi memiliki atribut berupa judul, harga, tanggal, kategori ("Pemasukan" atau "Pengeluaran") dan lokasi pengguna. Lokasi di-_update_ secara otomatis saat aplikasi dibuka, dan pengguna dapat mengubah lokasi tersebut saat menambah/mengedit transaksi di daftar transaksi. Pengguna dapat mengakses Google Maps berdasarkan lokasi tersebut.

Pengguna juga mampu melakukan scan nota melalui kamera aplikasi atau galeri. Hasil scan akan menghasilkan dummy data transaksi yang ditambahkan ke daftar transaksi. Terdapat graf yang menunjukkan porsi transaksi berdasarkan kategorinya. Pengguna dapat menyimpan daftar transaksi dalam bentuk file XLS/XLSX, mengirim daftar transaksi dalam Gmail dengan attachment file XLS/XLSX, dan membuat transaksi acak.

Pengguna perlu login ke dalam akun yang telah ditentukan untuk menggunakan aplikasi. Waktu penggunaan aplikasi setelah login adalah maksimum 5 menit sebelum pengguna perlu melakukan login lagi. Pengguna akan diberikan JWT yang akan kadaluwarsa setelah 5 menit. Aplikasi dapat mengecek apakah kondisi hp saat ini tersambung dengan internet. Walaupun tanpa internet, aplikasi dapat dibuka, akan tetapi aplikasi tidak bisa melakukan permintaan dan penarikan data dari server.

## Library yang Digunakan

- Google ML Kit (Scan)
- Retrofit (API)
- KSP (Room)
- FusedLocationProviderClient (Get Current Location)
- Geocoder
- JSoup (Santizing Input)
- OtaliaStudios (Twibbon)
- Google Play Services Maps (Google Map Intent)
- Apache POI (XLS/XLSX)
- https://github.com/PhilJay/MPAndroidChart (Graf)

## Screenshot Aplikasi

## Pembagian Kerja Kelompok

| Nama                        | NIM      | Pembagian Kerja                                                                   |
|-----------------------------|----------|-----------------------------------------------------------------------------------|
| Adam Dharma Sakti           | 10023359 | Header                                                                            |
| Muhammad Equillibrie Fajria | 13521047 | Graf transaksi, simpan transaksi dalam XLS/XLSX, intent Gmail, broadcast receiver |
| M Farrel Danendra Rachim    | 13521048 | Navbar, CRUD Transaksi, daftar transaksi, scan nota, Twibbon, OWASP               |
| Addin Munawar Yusuf         | 13521085 | Login, logout, background service, network sensing                                |

## Jumlah Jam Persiapan

| Nama                        | NIM      | Jumlah Jam Persiapan/Pengerjaan |
|-----------------------------|----------|---------------------------------|
| Adam Dharma Sakti           | 10023359 | 1 jam                           |
| Muhammad Equillibrie Fajria | 13521047 | 25 jam                          |
| M Farrel Danendra Rachim    | 13521048 | 30 jam                          |
| Addin Munawar Yusuf         | 13521085 | 25 jam                          |