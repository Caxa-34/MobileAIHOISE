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
import com.example.aihouse.api.GetFeedPublicaionsRequest
import com.example.aihouse.api.LikePublicationRequest
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
        val publication = users[position]
        //Log.e("PUBLICATIONS", publications.toString())
        with(holder.binding) {

        }
    }

    private fun like(publication : Publication, like : Boolean, txtLikes : TextView, btnLike : CheckBox) {
        var type = "remove"
        if (like) type = "set"
        var req = LikePublicationRequest(
            idPublication = publication.id!!,
            idUser = Helper.currentUser.id,
            type = type
        )
        coroutineScope.launch {
            val res = ApiHelper.likePublication(req)
            res.onSuccess { response ->
                //Toast.makeText(context, "Like " + type, Toast.LENGTH_SHORT).show()
                txtLikes.setText(response.countLikes.toString())
                if (response.result == "LikeRemoved" || response.result == "LikeSetted") {
                    userClick = true
                }
                if (response.result == "LikeWasnt" || response.result == "LikeWas") {
                    userClick = false
                    btnLike.isChecked = !like
                }
            }.onFailure { error ->
                //Toast.makeText(context  , "Error: Like " + type, Toast.LENGTH_SHORT).show()
                userClick = false
                btnLike.isChecked = !like
            }
        }
    }

    override fun getItemCount(): Int {
        //Log.d("Adapter", "getItemCount called with size: ${publications.size}")
        return users.size
    }
}
