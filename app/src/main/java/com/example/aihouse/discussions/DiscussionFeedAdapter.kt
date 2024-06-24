package com.example.aihouse.discussions

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
import com.example.aihouse.api.SubscribeReqest
import com.example.aihouse.databinding.DiscussionCardBinding
import com.example.aihouse.models.Discussion
import com.example.aihouse.models.Publication
import com.example.aihouse.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DiscussionFeedAdapter(private val context: Context, private var discussions: List<Discussion>, private var coroutineScope: CoroutineScope) :
    RecyclerView.Adapter<DiscussionFeedAdapter.PublicationViewHolder>() {
    lateinit var binding : DiscussionCardBinding

    class PublicationViewHolder(val binding: DiscussionCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationViewHolder {
        binding = DiscussionCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PublicationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PublicationViewHolder, position: Int) {
        discussions = discussions.sortedByDescending { it.dateUpdate }
        discussions = discussions.sortedByDescending { it.countParticipants }

        val discussion = discussions[position]
        with(holder.binding) {
            txtTitleDiscussioncard.text = discussion.title
            txtCountParticipansDiscussioncard.text = discussion.countParticipants.toString()
            txtDateUpdateDiscussioncard.text = ApiHelper.formatDateTime(ApiHelper.parseDateTime(discussion.dateUpdate))
            ApiHelper.loadImage(context, discussion.icon, imgIconDiscussioncard)
            if (!discussion.isAnswered) imgIsAnsweredDiscussioncard.visibility = View.GONE

            root.setOnClickListener {
                navToDiscussion(it, discussion)
            }
        }

    }

    fun navToDiscussion(view: View, dis: Discussion) {
        Helper.clickedDiscussion = dis
        view.findNavController().navigate(R.id.discussionFragment)
    }

    override fun getItemCount(): Int {
        //Log.d("Adapter", "getItemCount called with size: ${publications.size}")
        return discussions.size
    }
}
