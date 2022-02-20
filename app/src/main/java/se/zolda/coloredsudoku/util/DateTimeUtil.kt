package se.zolda.coloredsudoku.util

fun Long.formatSecondsHHmmss(): String{
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    return when{
        hours >= 1L -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else -> String.format("%02d:%02d", minutes, seconds)
    }
}

fun Long.formatMillisHHmmss(): String{
    val sec = this / 1000
    return sec.formatSecondsHHmmss()
}