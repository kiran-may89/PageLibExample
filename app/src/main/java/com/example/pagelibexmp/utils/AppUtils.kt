package com.example.pagelibexmp.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AppUtils {
    companion object{
        public fun getDate(dateString:String): String {
            return try {
                val format1 = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
                val date: Date = format1.parse(dateString)
                val sdf: DateFormat = SimpleDateFormat("MMM d, yyyy")
                sdf.format(date)
            } catch (ex: Exception) {
                ex.printStackTrace()
                "xx"
            }
        }

        fun getTime(dateString:String): String{
            return try {
                val format1 =
                    SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
                val date = format1.parse(dateString)
                val sdf: DateFormat = SimpleDateFormat("h:mm a")
                sdf.format(date)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                "xx"
            }
        }

        fun getRandomNumber():Long{
            return (Math.random() * (100000 - 0 + 1) + 0).toLong()
        }
    }
}