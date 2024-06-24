package com.example.aihouse.messages

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.aihouse.Helper
import com.example.aihouse.R
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.databinding.MessageDiscussionCardBinding
import com.example.aihouse.models.DiscussionMessage
import com.example.aihouse.models.User
import kotlinx.coroutines.CoroutineScope

class DiscussionMessageAdapter(
    private val context: Context,
    private var messages: List<DiscussionMessage>,
    private var isCanSetAnswer: Boolean,
    private val onSetAnswerClick: (DiscussionMessage) -> Unit
) : RecyclerView.Adapter<DiscussionMessageAdapter.PublicationViewHolder>() {
    lateinit var binding: MessageDiscussionCardBinding

    class PublicationViewHolder(val binding: MessageDiscussionCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationViewHolder {
        binding = MessageDiscussionCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PublicationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PublicationViewHolder, position: Int) {
        messages = messages.sortedBy { ApiHelper.parseDateTime(it.dateCreate) }
        val message = messages[position]
        with(holder.binding) {
            txtNameMessage.text = message.author.name
            txtTimeMessage.text = ApiHelper.formatDateTime(ApiHelper.parseDateTime(message.dateCreate))
            ApiHelper.loadImage(context, message.author.imagePath!!, imgAvatarMessage)
            txtTextMessage.text = message.text
            if (!message.isAnswer) llIsAnsweredMessage.visibility = View.GONE
            if (message.author.id == Helper.currentUser.id) {
                txtNameMessage.text = "Вы"
                val typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)
                txtNameMessage.typeface = typeface
            }
            txtNameMessage.setOnClickListener {
                navToDiscussion(it, message.author)
            }
            imgAvatarMessage.setOnClickListener {
                navToDiscussion(it, message.author)
            }
            if (isCanSetAnswer)
                root.setOnLongClickListener {
                    showPopupMenu(it, message)
                    true
                }
        }
    }

    private fun showPopupMenu(view: View, message: DiscussionMessage) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.message_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_set_answer -> {
                    onSetAnswerClick(message)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    fun navToDiscussion(view: View, user: User) {
        Helper.clickedUser = user
        view.findNavController().navigate(R.id.userPageFragment)
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}
