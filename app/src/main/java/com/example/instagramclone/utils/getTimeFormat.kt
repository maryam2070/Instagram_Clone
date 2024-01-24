package com.example.instagramclone.utils

import java.text.SimpleDateFormat
import java.util.Date

fun getTimeFormat(date: Date) = SimpleDateFormat("h:mm a").format(date)