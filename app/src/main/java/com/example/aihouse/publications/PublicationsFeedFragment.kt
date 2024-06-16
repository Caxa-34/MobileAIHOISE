package com.example.aihouse.publications

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aihouse.Helper
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.SearchRequest
import com.example.aihouse.api.UserRequest
import com.example.aihouse.cards.PublicationCard
import com.example.aihouse.cards.UserCard
import com.example.aihouse.databinding.FragmentPublicationsFeedBinding
import com.example.aihouse.models.Publication
import com.example.aihouse.models.User
import kotlinx.coroutines.launch

class PublicationsFeedFragment : Fragment() {
    private lateinit var binding: FragmentPublicationsFeedBinding
    private var textWatcher: TextWatcher? = null

    lateinit var publicationsFeed: List<Publication>
    lateinit var publicationsSearched: List<Publication>
    lateinit var usersSearched: List<User>

    lateinit var adapterFeed: PublicationFeedAdapter
    lateinit var adapterUserFeed: UserFeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPublicationsFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    var title = "Публикации"
    var lastSearchNotEmpty = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filtersPublicationsfeed.visibility = View.GONE

        if (textWatcher == null) {
            textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val currentSearchNotEmpty = s?.isNotEmpty() ?: false
                    if (!currentSearchNotEmpty) {
                        binding.filtersPublicationsfeed.visibility = View.GONE
                    } else if (currentSearchNotEmpty && !lastSearchNotEmpty) {
                        binding.filtersPublicationsfeed.visibility = View.VISIBLE
                        binding.filterPublicationsPublicationsfeed.isChecked = true
                    }

                    //Log.e("SHOW PUBLICATIONS", "SHOW")

                    if (currentSearchNotEmpty) {
                        search()
                    } else {
                        showfeed()
                    }

                    lastSearchNotEmpty = currentSearchNotEmpty
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            binding.searchPublicationsfeed.addTextChangedListener(textWatcher)
        }
        binding.filtersPublicationsfeed.setOnCheckedChangeListener { group, checkedId ->
            search()
        }
        showfeed()
    }

    private fun search() {
        if (binding.filterPublicationsPublicationsfeed.isChecked) searchPublications()
        if (binding.filterUserPublicationsfeed.isChecked) searchAuthors()
    }

    private fun searchPublications() {
        //Toast.makeText(requireNotNull(context), "Feed", Toast.LENGTH_SHORT).show()

        var req = SearchRequest(
            idUser = Helper.currentUser.id,
            fragment = binding.searchPublicationsfeed.text.toString()
        )
        lifecycleScope.launch {
            val res = ApiHelper.searchPublications(req)
            res.onSuccess { response ->
                publicationsSearched = response.publications!!
                publicationsSearched = publicationsSearched.sortedByDescending { it.countLetters!! }
                adapterFeed = PublicationFeedAdapter(requireContext(), publicationsSearched, lifecycleScope)
                binding.feedPublicationfeed.layoutManager = LinearLayoutManager(context)
                binding.feedPublicationfeed.adapter = adapterFeed
            }.onFailure { error ->

            }
        }
    }
    private fun searchAuthors() {
        var req = SearchRequest(
            idUser = Helper.currentUser.id,
            fragment = binding.searchPublicationsfeed.text.toString()
        )
        lifecycleScope.launch {
            val res = ApiHelper.searchAuthors(req)
            res.onSuccess { response ->
                usersSearched = response.users!!
                Log.e("USERSSERARC", usersSearched.toString())
                usersSearched = usersSearched.sortedByDescending { it.countLetters!! }
                adapterUserFeed = UserFeedAdapter(requireContext(), usersSearched, lifecycleScope)
                binding.feedPublicationfeed.layoutManager = LinearLayoutManager(context)
                binding.feedPublicationfeed.adapter = adapterUserFeed
            }.onFailure { error ->
                Log.e("ERROR: USERSSERARC", usersSearched.size.toString())
            }
        }
    }

    private fun showfeed() {
        var req = UserRequest(
            idUser = Helper.currentUser.id
        )
        lifecycleScope.launch {
            val res = ApiHelper.getFeedPublications(req)
            res.onSuccess { response ->
                //Toast.makeText(requireNotNull(context), "Feed", Toast.LENGTH_SHORT).show()
                publicationsFeed = response.publications!!
                publicationsFeed = publicationsFeed.sortedByDescending { ApiHelper.parseDateTime(it.dateCreate!!) }
                //Log.e("FEED JSON", publicationsFeed.toString())
                adapterFeed = PublicationFeedAdapter(requireContext(), publicationsFeed, lifecycleScope)
                binding.feedPublicationfeed.layoutManager = LinearLayoutManager(context)
                binding.feedPublicationfeed.adapter = adapterFeed

            }.onFailure { error ->

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.searchPublicationsfeed.removeTextChangedListener(textWatcher)
        textWatcher = null
    }
}