package com.sleepee.bondoman.data.model

import com.google.gson.annotations.SerializedName

data class ScanResponse (
    @SerializedName("items") val itemsList: ItemWrapper
)

data class Item (
    val name: String,
    val qty: Int,
    val price: Double
)

data class ItemWrapper(
    val items: List<Item>
)