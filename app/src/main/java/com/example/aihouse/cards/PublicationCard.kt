package com.example.aihouse.cards

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.example.aihouse.databinding.PublicationCardBinding
import com.example.aihouse.databinding.UserPublicationCardBinding

enum class PublicationCardType {
    FEED_CARD,
    USER_CARD
}

class PublicationCard(context: Context, typeCard: PublicationCardType) : LinearLayout(context) {
    private lateinit var bindingFeedcard: PublicationCardBinding
    private lateinit var bindingUsercard: UserPublicationCardBinding

    init {
        val inflater = LayoutInflater.from(context)
        when (typeCard) {
            PublicationCardType.FEED_CARD -> {
                bindingFeedcard = PublicationCardBinding.inflate(inflater, this, true)
                initFeedCard()
            }
            PublicationCardType.USER_CARD -> {
                bindingUsercard = UserPublicationCardBinding.inflate(inflater, this, true)
                initUserCard()
            }
        }
    }

    private fun initFeedCard() {
        val binding = bindingFeedcard

    }

    private fun initUserCard() {
        val binding = bindingUsercard

    }


}