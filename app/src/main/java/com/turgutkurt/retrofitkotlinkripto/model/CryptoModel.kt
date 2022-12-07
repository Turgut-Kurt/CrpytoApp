package com.turgutkurt.retrofitkotlinkripto.model

import com.google.gson.annotations.SerializedName

data class CryptoModel(
    @SerializedName("currency")
    //isimler aynıysa serializedName yazmama gerek yok
    val currency:String,
    @SerializedName("price")
    val price :String
    )