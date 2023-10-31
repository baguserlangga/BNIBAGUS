package com.example.bnibagus.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DataTransaksiQr::class],
    version = 1
)
abstract class TransaksiQrDB: RoomDatabase() {
    abstract val dao : DataTransaksiDao
}