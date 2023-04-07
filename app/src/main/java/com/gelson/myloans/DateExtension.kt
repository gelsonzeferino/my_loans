package com.gelson.myloans

import java.text.SimpleDateFormat
import java.util.*

private val locale = Locale("pt", "br")

fun Date.format(): String{
    return SimpleDateFormat("dd/MM/yyyy", locale).format(this)
}