package com.agung.smashcourt

data class Court(
    val name: String,
    val image: Int,
    val location: String? = null,
    val providerId: String? = null,
)

val courts = listOf(
    Court("GOR Purnama", R.drawable.court2, "Gor Purnama", "CgWDdhbh9GWUmaJwLvloBwhwl6i1"),
    Court("GOR Windujanten", R.drawable.court2, "Gor Windujanten", "CXlcQbE6wVgTqLENfzQGAzcYHP82"),
    Court("GOR Sadamantra", R.drawable.court2, "Gor Sadamantra", "byHXayTzz5XaEfSpU5wSm5CkDJC3"),
    Court("GOR Winduhaji", R.drawable.court2, "Gor Winduhaji", "bdcAcBZedCR9pPHlWDL6F2GVTQ73"),
    Court("GOR Andrimusthi", R.drawable.court2, "Gor Andrimusthi", "862BK87vIJORwnvP1SB7fnyhzlH3"),
    Court("Sewa Raket dan Beli Shuttlecock", R.drawable.court),
)