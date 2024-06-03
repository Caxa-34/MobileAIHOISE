package com.example.aihouse.cards

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.example.aihouse.databinding.PublicationCardBinding
import com.example.aihouse.databinding.UserCardBinding

class UserCard(context: Context) : LinearLayout(context) {
    private var binding: UserCardBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = UserCardBinding.inflate(inflater, this, true)
    }

    // Другие методы для установки данных
}
