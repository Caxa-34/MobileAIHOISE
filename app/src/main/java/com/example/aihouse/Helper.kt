package com.example.aihouse

import android.content.Context
import android.util.Log
import com.example.aihouse.models.User

object Helper {
    lateinit var currentUser: User

    //lateinit var count

    fun saveUserData(context: Context, email: String, username: String, password: String) {
        val sharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
        //Log.e("DATA USER", username + " " + email + " " + password);
    }
}