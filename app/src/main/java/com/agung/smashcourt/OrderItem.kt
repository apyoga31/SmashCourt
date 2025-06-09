package com.agung.smashcourt

data class OrderItem(
    val imageResId: Int,
    val courtName: String,
    val description: String,
    val date: String,
    val time: String,
    val price: String
)

val order = listOf(
    OrderItem(
        R.drawable.court2,
        "Lapangan 1",
        "Pagi",
        "Senin, 09 Juni 2025",
        "09.00-10.00",
        "RP. 20.000"
    )
)