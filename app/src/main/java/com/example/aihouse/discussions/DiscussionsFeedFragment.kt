package com.example.aihouse.discussions

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aihouse.Helper
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.databinding.FragmentDiscussionsFeedBinding
import com.example.aihouse.models.Discussion
import com.example.aihouse.models.Publication
import com.example.aihouse.publications.PublicationFeedAdapter
import kotlinx.coroutines.launch

class DiscussionsFeedFragment : Fragment() {
    private lateinit var binding: FragmentDiscussionsFeedBinding
    private var textWatcher: TextWatcher? = null
    lateinit var discussionsFeed: List<Discussion>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDiscussionsFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    var title = "Обсуждения"
    var lastSearchNotEmpty = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filtersDiscussionsfeed.visibility = View.GONE

        if (textWatcher == null) {
            textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val currentSearchNotEmpty = s?.isNotEmpty() ?: false
                    if (!currentSearchNotEmpty) {
                        binding.filtersDiscussionsfeed.visibility = View.GONE
                    } else if (currentSearchNotEmpty && !lastSearchNotEmpty) {
                        binding.filtersDiscussionsfeed.visibility = View.VISIBLE
                        binding.filterAllDiscussionsfeed.isChecked = true
                    }

                    Log.e("SHOW PUBLICATIONS", "SHOW")

                    if (currentSearchNotEmpty) {
                        search()
                    } else {
                        showfeed()
                    }

                    lastSearchNotEmpty = currentSearchNotEmpty
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            binding.searchDiscussionsfeed.addTextChangedListener(textWatcher)
        }
        showfeed()
    }

    private fun search() {
        var fragment = binding.searchDiscussionsfeed.text.toString()
        var publicationsSearched = discussionsFeed.filter { Helper.hasSearchPublicationMatch(it.title, it.textQuestion, fragment) }
        publicationsSearched = publicationsSearched.sortedByDescending { Helper.searchScorePublication(it.title, it.textQuestion, fragment) }

        var adapterFeed = DiscussionFeedAdapter(requireContext(), publicationsSearched, lifecycleScope)
        binding.discussionsfeed.layoutManager = LinearLayoutManager(context)
        binding.discussionsfeed.adapter = adapterFeed
    }

    private fun showfeed() {
        lifecycleScope.launch {
            val res = ApiHelper.getDiscussions()
            res.onSuccess { response ->
                discussionsFeed = response.discussions!!
                var adapterFeed = DiscussionFeedAdapter(requireContext(), discussionsFeed, lifecycleScope)
                binding.discussionsfeed.layoutManager = LinearLayoutManager(context)
                binding.discussionsfeed.adapter = adapterFeed

            }.onFailure { error ->
                Log.e("Get discussions", error.message.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.searchDiscussionsfeed.removeTextChangedListener(textWatcher)
    }
}