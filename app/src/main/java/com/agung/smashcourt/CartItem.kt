package com.agung.smashcourt

data class CartItem(
    val imageResId: Int,
    val orderName: String? = null,
    val itemName: String? = null,
    val description: String? = null,
    val date: String? = null,
    val time: String? = null,
    val quantity: String? = null,
    val price: String,
    val isPay: Boolean = false,
    val type: String? = null
)


