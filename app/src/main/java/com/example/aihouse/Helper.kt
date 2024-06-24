package com.example.aihouse

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import com.example.aihouse.models.Discussion
import com.example.aihouse.models.Draft
import com.example.aihouse.models.Notification
import com.example.aihouse.models.Publication
import com.example.aihouse.models.Rule
import com.example.aihouse.models.User

object Helper {
    lateinit var currentUser: User

    var clickedDraft : Draft? = null
    var clickedPublication : Publication? = null
    var clickedDiscussion : Discussion? = null
    var clickedUser : User? = null

    lateinit var rules: List<Rule>
    lateinit var notifications: List<Notification>

    fun saveToBuffer(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("buffer", text)
        clipboard.setPrimaryClip(clip)
    }
    fun saveUserData(context: Context, email: String, username: String, password: String) {
        val sharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }
    fun searchScorePublication(pubTitle: String, pubText: String, searchText: String): Int {
        val searchWords = searchText.split("\\s+".toRegex()).filter { it.isNotEmpty() }.map { it.lowercase() }
        val title = pubTitle.lowercase()
        val text = pubText.lowercase()
        var score = 0
        for (word in searchWords) {
            if (title.contains(word)) {
                score += 10
            }
            if (text.contains(word)) {
                score += 1
            }
        }
        return score
    }
    fun hasSearchPublicationMatch(pubTitle: String, pubText: String, searchText: String): Boolean {
        val searchWords = searchText.split("\\s+".toRegex()).filter { it.isNotEmpty() }.map { it.lowercase() }
        val title = pubTitle.lowercase()
        val text = pubText.lowercase()
        for (word in searchWords) {
            if (title.contains(word) || text.contains(word)) {
                return true
            }
        }
        return false
    }
    fun isUserMatch(user: User, searchText: String): Boolean {
        val searchLower = searchText.lowercase()
        if (searchText.startsWith("@")) {
            val searchId = searchText.removePrefix("@")
            if (user.id.toString().contains(searchId)) {
                return true
            }
        }
        if (user.name.lowercase().contains(searchLower)) {
            return true
        }
        return false
    }
}