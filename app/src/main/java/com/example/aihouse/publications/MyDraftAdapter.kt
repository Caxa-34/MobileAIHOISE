package com.example.aihouse.publications

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aihouse.Helper
import com.example.aihouse.R
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.LikePublicationRequest
import com.example.aihouse.databinding.PublicationCardBinding
import com.example.aihouse.databinding.UserPublicationCardBinding
import com.example.aihouse.models.Draft
import com.example.aihouse.models.Publication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MyDraftAdapter(private val context: Context,
                     private val drafts: List<Draft>,
                     private val navController: NavController) :
    RecyclerView.Adapter<MyDraftAdapter.PublicationViewHolder>() {
    lateinit var binding : UserPublicationCardBinding

    class PublicationViewHolder(val binding: UserPublicationCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationViewHolder {
        //Log.d("Adapter", "onCreateViewHolder called")
        binding = UserPublicationCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PublicationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PublicationViewHolder, position: Int) {
        //Log.d("Adapter", "onBindViewHolder called for position: $position")
        val draft = drafts[position]
        //Log.e("PUBLICATIONS", publications.toString())
        with(holder.binding) {
            // Закомментируем пока установку данных для диагностики
            txtTitleMypublicationcard.text = draft.title
            txtTextMypublicationcard.text = draft.text
            txtDateTimeCreateMypublicarioncard.text = ApiHelper.formatDateTime(ApiHelper.parseDateTime(draft.dateUpdate!!))
            llLikesMypublicationcard.visibility = View.GONE
            root.setOnClickListener {
                navToPublic(draft)
            }
        }
    }

    private fun navToPublic(draft: Draft) {
        Helper.clickedDraft = draft
        navController.navigate(R.id.createPublicationFragment)
    }

    override fun getItemCount(): Int {
        //Log.d("Adapter", "getItemCount called with size: ${publications.size}")
        return drafts.size
    }
}
