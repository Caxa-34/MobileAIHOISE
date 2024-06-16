package com.example.aihouse.person

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aihouse.Helper
import com.example.aihouse.R
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.LikePublicationRequest
import com.example.aihouse.api.NotificationRequest
import com.example.aihouse.api.SubscribeReqest
import com.example.aihouse.databinding.NotificationCardBinding
import com.example.aihouse.databinding.PublicationCardBinding
import com.example.aihouse.models.Notification
import com.example.aihouse.models.Publication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NotificatonsAdapter(private val context: Context, private val notifications: List<Notification>, private var coroutineScope: CoroutineScope) :
    RecyclerView.Adapter<NotificatonsAdapter.NotificationViewHolder>() {
    lateinit var binding : NotificationCardBinding

    class NotificationViewHolder(val binding: NotificationCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        //Log.d("Adapter", "onCreateViewHolder called")
        binding = NotificationCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        with(holder.binding) {
            // отобразить все данные
            txtTitleNotification.text = notification.title
            txtTextNotification.text = notification.text
            txtDateNotification.text = ApiHelper.formatDateTime(ApiHelper.parseDateTime(notification.dateCreate!!))

            // Скрыть или показать значок непрочитанного уведомления

            if (notification.wasRead == true) {
                imgUnviewedNotification.visibility = View.GONE
                imgBackgroundNotification.setBackgroundResource(R.drawable.background_gray_border)
            }

            root.setOnClickListener {
                read(notification, this)
            }
        }
    }

    private fun read(notification: Notification, clickedcard: NotificationCardBinding) {
        var req = NotificationRequest(
            idNotification = notification.id!!
        )
        coroutineScope.launch {
            val res = ApiHelper.readNotif(req)
            res.onSuccess { response ->
                if (response.message == "NotifReaded") {
                    clickedcard.imgUnviewedNotification.visibility = View.GONE
                    clickedcard.imgBackgroundNotification.setBackgroundResource(R.drawable.background_gray_border)
                }
            }.onFailure { error ->

            }
        }
    }

    override fun getItemCount(): Int {
        //Log.d("Adapter", "getItemCount called with size: ${publications.size}")
        return notifications.size
    }
}
