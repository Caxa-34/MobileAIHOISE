package com.example.aihouse.publications

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aihouse.Helper
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.LikePublicationRequest
import com.example.aihouse.api.SubscribeReqest
import com.example.aihouse.databinding.PublicationCardBinding
import com.example.aihouse.databinding.UserCardBinding
import com.example.aihouse.models.Publication
import com.example.aihouse.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserFeedAdapter(private val context: Context, private val users: List<User>, private var coroutineScope: CoroutineScope) :
    RecyclerView.Adapter<UserFeedAdapter.UserViewHolder>() {
    lateinit var binding : UserCardBinding

    class UserViewHolder(val binding: UserCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        //Log.d("Adapter", "onCreateViewHolder called")
        binding = UserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    var userClick = true

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        //Log.d("Adapter", "onBindViewHolder called for position: $position")
        val user = users[position]
        //Log.e("PUBLICATIONS", publications.toString())
        with(holder.binding) {
            txtIDUsercard.text = "ID: " + user.id.toString()
            txtNameUsercard.text = user.name
            btnSubcribeUsercard.setOnCheckedChangeListener { buttonView, isChecked ->
                if (userClick)
                    subscribe(user, isChecked, btnSubcribeUsercard)
                userClick = true
            }
            userClick = false
            btnSubcribeUsercard.isChecked = user.isSetSubscribe!!
            userClick = true
        }
    }

    private fun subscribe(user : User, subscribe : Boolean, btnSub : CheckBox) {
        var type = "unsubscribe"
        if (subscribe) type = "subscribe"
        var req = SubscribeReqest(
            idAuthor = user.id,
            idUser = Helper.currentUser.id,
            type = type
        )
        btnSub.isEnabled = false
        coroutineScope.launch {
            val res = ApiHelper.subscribe(req)
            res.onSuccess { response ->
                //Toast.makeText(context, "Like " + type, Toast.LENGTH_SHORT).show()

                if (response.result == "SubscribeWasnt" || response.result == "SubscribeWas") {
                    userClick = false
                    btnSub.isChecked = !subscribe
                }
            }.onFailure { error ->
                //Toast.makeText(context  , "Error: Like " + type, Toast.LENGTH_SHORT).show()
                userClick = false
                btnSub.isChecked = !subscribe
            }
            btnSub.isEnabled = true
        }
    }

    override fun getItemCount(): Int {
        //Log.d("Adapter", "getItemCount called with size: ${publications.size}")
        return users.size
    }
}
