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
import com.example.aihouse.databinding.PublicationCardBinding
import com.example.aihouse.databinding.UserPublicationCardBinding
import com.example.aihouse.models.Publication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MyPublicationAdapter(private val context: Context, private val publications: List<Publication>, private var coroutineScope: CoroutineScope) :
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
        //Log.e("PUBLICATIONS", publications.toString())
        with(holder.binding) {
            // Закомментируем пока установку данных для диагностики
            txtTitleMypublicationcard.text = publication.title
            txtTextMypublicationcard.text = publication.text
            txtDateTimeCreateMypublicarioncard.text = ApiHelper.formatDateTime(ApiHelper.parseDateTime(publication.dateCreate!!))
            Log.e("PUB TIME", "ID: " + publication.id + " || DATE: " + publication.dateCreate.toString())
            txtLikesMypublicationcard.text = publication.countLikes.toString()
        }
    }

    override fun getItemCount(): Int {
        //Log.d("Adapter", "getItemCount called with size: ${publications.size}")
        return publications.size
    }
}
