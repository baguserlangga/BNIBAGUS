package com.example.bnibagus.model

import com.google.gson.annotations.SerializedName

class DonutChart (
    @SerializedName("label"      ) var label      : String?         = null,
    @SerializedName("percentage" ) var percentage : String?         = null,
    @SerializedName("data"       ) var data       : ArrayList<DataTrx> = arrayListOf()
        )