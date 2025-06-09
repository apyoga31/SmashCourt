package com.agung.smashcourt

data class CartItem(
    val imageResId: Int,
    val courtName: String,
    val description: String,
    val date: String,
    val time: String,
    val price: String
)

val item = listOf(
    CartItem(
        R.drawable.court2,
        "Lapangan 1",
        "Pagi",
        "Senin, 09 Juni 2025",
        "09.00-10.00",
        "RP. 20.000"
    ),
    CartItem(
        R.drawable.court2,
        "Lapangan 2",
        "Siang",
        "Senin, 09 Juni 2025",
        "10.00-11.00",
        "RP. 22.500"
    )
)


