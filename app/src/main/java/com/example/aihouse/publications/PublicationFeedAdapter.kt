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
import com.example.aihouse.models.Publication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PublicationFeedAdapter(private val context: Context, private val publications: List<Publication>, private var coroutineScope: CoroutineScope) :
    RecyclerView.Adapter<PublicationFeedAdapter.PublicationViewHolder>() {
    lateinit var binding : PublicationCardBinding

    class PublicationViewHolder(val binding: PublicationCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationViewHolder {
        //Log.d("Adapter", "onCreateViewHolder called")
        binding = PublicationCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PublicationViewHolder(binding)
    }

    var userClick = true

    override fun onBindViewHolder(holder: PublicationViewHolder, position: Int) {
        //Log.d("Adapter", "onBindViewHolder called for position: $position")
        val publication = publications[position]
        //Log.e("PUBLICATIONS", publications.toString())
        with(holder.binding) {
            // Закомментируем пока установку данных для диагностики
            txtTitlePublicationcard.text = publication.title
            txtTextPublicationcard.text = publication.text
            txtNameAuthorPublicationcard.text = publication.author?.name
            txtDateCreationPublicationcard.text = publication.dateCreate
            txtCountLikesPublicationcard.text = publication.countLikes.toString()
            btnLikePublicationcard.setOnCheckedChangeListener { buttonView, isChecked ->
                if (userClick)
                    like(publication, isChecked, txtCountLikesPublicationcard, btnLikePublicationcard)
                userClick = true
            }
            userClick = false
            btnLikePublicationcard.isChecked = publication.isSetLike!!
            userClick = true
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
        return publications.size
    }
}
