package com.akz.cinema.util

fun Int.formatCurrency(): String {
    val k = this / 1000
    val m = k / 1000
    val b = m / 1000

    return if (b > 0) {
        "$${b}${if(m%b>0) ".${(m%b).toString().trimEnd('0')}" else ""}B"
    } else if (m > 0) {
        "$${m}${if(k%m>0) ".${(k%m).toString().trimEnd('0')}" else ""}M"
    } else if (k > 0) {
        "$${k}K"
    } else {
        "less than $1K"
    }
}