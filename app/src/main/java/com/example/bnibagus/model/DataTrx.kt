package com.example.bnibagus.model

import com.google.gson.annotations.SerializedName

class DataTrx (
    @SerializedName("trx_date" ) var trxDate : String? = null,
    @SerializedName("nominal"  ) var nominal : Int?    = null
        )
