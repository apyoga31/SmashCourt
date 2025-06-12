package com.agung.smashcourt

data class Court(
    val name: String,
    val image: Int
)

val courts = listOf(
    Court("Booking Lapangan 1", R.drawable.court2),
    Court("Booking Lapangan 2", R.drawable.court2),
    Court("Booking Lapangan 3", R.drawable.court2),
    Court("Booking Lapangan 4", R.drawable.court2),
    Court("Booking Lapangan 5", R.drawable.court2),
    Court("Sewa Raket dan Beli Shuttlecock", R.drawable.court),
)