package com.example.bnibagus.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao

interface DataTransaksiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaksi(transaksiQrs: DataTransaksiQr)
    @Delete
    suspend fun deleteTransaksi(transaksiQrs: DataTransaksiQr)
    @Query("SELECT * FROM datatransaksiqr ORDER BY id ASC")
    fun getTransaksiOrderdById(): kotlinx.coroutines.flow.Flow<List<DataTransaksiQr>>

}