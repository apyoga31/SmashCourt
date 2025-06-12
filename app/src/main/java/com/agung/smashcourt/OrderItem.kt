package com.agung.smashcourt

data class OrderItem(
    val imageResId: Int,
    val orderName: String,
    val description: String,
    val itemName: String,
    val quantity: String,
    val date: String,
    val time: String,
    val price: String
)
