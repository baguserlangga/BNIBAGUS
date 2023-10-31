package com.example.bnibagus.model

import com.example.bnibagus.SortType

data class TransaksiState (

    val Transaksi: List<DataTransaksiQr> = emptyList(),
    val bank: String = "",
    val id_transaksi: String = "",
    val merchant: String = "",
    val nominal:Int = 0,
    val isAddingTransaksi:Boolean = false,
    val sortType: SortType = SortType.IDS,


    )