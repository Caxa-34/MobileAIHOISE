package com.example.aihouse.publications

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aihouse.Helper
import com.example.aihouse.R
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.LikePublicationRequest
import com.example.aihouse.databinding.PublicationCardBinding
import com.example.aihouse.databinding.UserPublicationCardBinding
import com.example.aihouse.models.Publication
import com.example.aihouse.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MyPublicationAdapter(private val context: Context, private val publications: List<Publication>, private var coroutineScope: CoroutineScope, private var isMy: Boolean) :
    RecyclerView.Adapter<MyPublicationAdapter.PublicationViewHolder>() {
    lateinit var binding : UserPublicationCardBinding

    class PublicationViewHolder(val binding: UserPublicationCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationViewHolder {
        //Log.d("Adapter", "onCreateViewHolder called")
        binding = UserPublicationCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PublicationViewHolder(binding)
    }

    var userClick = true

    override fun onBindViewHolder(holder: PublicationViewHolder, position: Int) {
        //Log.d("Adapter", "onBindViewHolder called for position: $position")
        val publication = publications[position]
        Log.e("PUBLICATION", publication.toString())
        with(holder.binding) {
            // Закомментируем пока установку данных для диагностики
            txtTitleMypublicationcard.text = publication.title
            txtTextMypublicationcard.text = publication.text
            txtDateTimeCreateMypublicarioncard.text = ApiHelper.formatDateTime(ApiHelper.parseDateTime(publication.dateCreate!!))
            Log.e("PUB TIME", "ID: " + publication.id + " || DATE: " + publication.dateCreate.toString())
            txtLikesMypublicationcard.text = publication.countLikes.toString()

            txtTextMypublicationcard.setOnClickListener {
                navToPublication(it, publication)
            }
            txtTitleMypublicationcard.setOnClickListener {
                navToPublication(it, publication)
            }
            if (publication.isSetLike != null) {
                userClick = false
                btnLikeUserpublication.isChecked = publication.isSetLike!!
                userClick = true
            }
            btnLikeUserpublication.isEnabled = !isMy
            btnLikeUserpublication.setOnCheckedChangeListener { buttonView, isChecked ->
                if (userClick)
                    like(publication, isChecked, txtLikesMypublicationcard, btnLikeUserpublication)
                userClick = true
            }
        }

    }

    fun navToPublication(view: View, pub: Publication) {
        Helper.clickedPublication = pub
        view.findNavController().navigate(R.id.publicationFragment)
    }

    override fun getItemCount(): Int {
        //Log.d("Adapter", "getItemCount called with size: ${publications.size}")
        return publications.size
    }

    private fun like(publication : Publication, like : Boolean, txtLikes : TextView, btnLike : CheckBox) {
        var type = "remove"
        if (like) type = "set"
        var req = LikePublicationRequest(
            idPublication = publication.id!!,
            idUser = Helper.currentUser.id,
            type = type
        )
        btnLike.isEnabled = false
        coroutineScope.launch {
            val res = ApiHelper.likePublication(req)
            res.onSuccess { response ->
                //Toast.makeText(context, "Like " + type, Toast.LENGTH_SHORT).show()
                txtLikes.setText(response.countLikes.toString())
                publication.countLikes = response.countLikes
                if (response.result == "LikeRemoved") {
                    userClick = true
                    publication.isSetLike = false
                }
                if (response.result == "LikeSetted") {
                    userClick = true
                    publication.isSetLike = true
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
}
