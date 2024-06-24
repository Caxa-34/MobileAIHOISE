package com.example.aihouse.publications

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aihouse.Helper
import com.example.aihouse.R
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.LikeCommentRequest
import com.example.aihouse.api.LikePublicationRequest
import com.example.aihouse.api.SubscribeReqest
import com.example.aihouse.databinding.CommentPublicationCardBinding
import com.example.aihouse.databinding.PublicationCardBinding
import com.example.aihouse.models.Publication
import com.example.aihouse.models.PublicationComment
import com.example.aihouse.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.Comment

class PublicationCommentAdapter(private val context: Context, private val comments: List<PublicationComment>, private var coroutineScope: CoroutineScope) :
    RecyclerView.Adapter<PublicationCommentAdapter.CommentViewHolder>() {
    lateinit var binding : CommentPublicationCardBinding

    class CommentViewHolder(val binding: CommentPublicationCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        //Log.d("Adapter", "onCreateViewHolder called")
        binding = CommentPublicationCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    var userClick = true

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        with(holder.binding) {
            // Закомментируем пока установку данных для диагностики
            txtNameComment.text = comment.author?.name
            if (comment.author?.id == Helper.currentUser.id) {
                txtNameComment.text = "Вы"
                val typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)
                txtNameComment.typeface = typeface
            }
            txtTextComment.text = comment.text
            btnLikeComment.isChecked = comment.isSetLike!!
            txtLikesMypublicationcard.text = comment.countLikes.toString()
            txtTimeComment.text = ApiHelper.formatDateTime(ApiHelper.parseDateTime(comment.dateCreate!!))
            ApiHelper.loadImage(context, comment.author?.imagePath!!, imgAvatarComment)
            btnLikeComment.setOnCheckedChangeListener { buttonView, isChecked ->
                if (userClick)
                    like(comment, isChecked, txtLikesMypublicationcard, btnLikeComment)
                userClick = true
            }
            txtNameComment.setOnClickListener {
                navToUserPage(it, comment.author)
            }
            imgAvatarComment.setOnClickListener {
                navToUserPage(it, comment.author)
            }
        }
    }

    fun navToUserPage(view: View, user: User) {
        Helper.clickedUser = user
        view.findNavController().navigate(R.id.userPageFragment)
    }

    private fun like(comment: PublicationComment, like : Boolean, txtLikes : TextView, btnLike : CheckBox) {
        var type = "remove"
        if (like) type = "set"
        var req = LikeCommentRequest(
            idComment = comment.id!!,
            idUser = Helper.currentUser.id,
            type = type
        )
        btnLike.isEnabled = false
        coroutineScope.launch {
            val res = ApiHelper.likeComment(req)
            res.onSuccess { response ->
                //Toast.makeText(context, "Like " + type, Toast.LENGTH_SHORT).show()
                txtLikes.setText(response.countLikes.toString())
                comment.countLikes = response.countLikes
                if (response.result == "LikeRemoved") {
                    userClick = true
                    comment.isSetLike = false
                }
                if (response.result == "LikeSetted") {
                    userClick = true
                    comment.isSetLike = true
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
            btnLike.isEnabled = true
        }
    }

    override fun getItemCount(): Int {
        //Log.d("Adapter", "getItemCount called with size: ${publications.size}")
        return comments.size
    }
}
