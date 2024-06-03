package com.example.aihouse.cards

import android.content.Context
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.aihouse.databinding.NotificationCardBinding

class NotificationCard(context: Context) : ConstraintLayout(context) {
    private var binding: NotificationCardBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = NotificationCardBinding.inflate(inflater, this, true)
    }
}