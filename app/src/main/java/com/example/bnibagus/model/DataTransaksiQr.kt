package com.example.bnibagus.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "datatransaksiqr")

data class DataTransaksiQr(
    @PrimaryKey(autoGenerate = true)
    val id :Int = 0,
    val bank : String,
    val id_transaksi :String,
    val merchant  : String,
    val nominal : Int,
)