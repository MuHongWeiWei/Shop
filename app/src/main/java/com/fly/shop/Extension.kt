package com.fly.shop

import android.app.Activity
import android.content.Context

fun Activity.setNickname(nickname: String) {
    getSharedPreferences("shop", Context.MODE_PRIVATE)
        .edit()
        .putString("nickname", nickname)
        .commit()
}

fun Activity.getNickname(): String{
    return  getSharedPreferences("shop", Context.MODE_PRIVATE).getString("nickname", "")
}