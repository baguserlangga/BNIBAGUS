package com.example.bnibagus

sealed class Destinations(val route:String) {
    object FirstScreen : Destinations("First Screen")
    object SecondScreen : Destinations("Second Screen")
    object ScanQR : Destinations("Scan QR")
    object TransaksiScreen : Destinations("Transaksi Screen")
    object PortoFolio : Destinations("Portofolio Screen")
    object LineChart : Destinations("LineChart Screen")

}