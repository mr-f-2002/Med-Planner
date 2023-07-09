package com.example.navbar

import android.net.Uri

data class MyModel(
    var medImg: String?,
    var medName: String?,
    var medStock: Int?,
    var morningImg: Int?,
    var noonImg: Int?,
    var nightImg: Int?
){
    fun getUri(): Uri? {
        return Uri.parse(medImg)
    }
}