package com.agung.smashcourt

data class OrderModel(
    val date: com.google.firebase.Timestamp? = null,
    val orderName: String = "",
    val price: Long = 0,
    val userId: String = "",
    val isPay: Boolean = false,
    val type: String = "",
    val providerId: String = ""
)
