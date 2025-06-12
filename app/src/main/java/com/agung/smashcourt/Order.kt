package com.agung.smashcourt

data class Order(
    val date: com.google.firebase.Timestamp? = null,
    val isPay: Boolean = false,
    val orderName: String? = null,
    val itemName: String? = null,
    val price: Long = 0,
    val quantity: Int? = null,
    val type: String = ""
)
