package com.example.aihouse.person

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
import com.example.aihouse.api.UserRequest
import com.example.aihouse.databinding.FragmentMySubscriptionsBinding
import com.example.aihouse.models.User
import com.example.aihouse.publications.UserFeedAdapter
import kotlinx.coroutines.launch

class MySubscriptionsFragment : Fragment() {
    private lateinit var binding: FragmentMySubscriptionsBinding
    lateinit var adapterUsers: UserFeedAdapter
    lateinit var users: List<User>
    private var textWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMySubscriptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    var title = "Мои подписки"
    var lastSearchNotEmpty = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (textWatcher == null) {
            textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val currentSearchNotEmpty = s?.isNotEmpty() ?: false
                    //Log.e("SHOW PUBLICATIONS", "SHOW")

                    if (currentSearchNotEmpty) {
                        search()
                    } else {
                        showMy()
                    }

                    lastSearchNotEmpty = currentSearchNotEmpty
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            binding.searchMysubs.addTextChangedListener(textWatcher)
        }

        showMy()
    }

    private fun search() {
        var text = binding.searchMysubs.text.toString()
        var usersSearch = users.filter { Helper.isUserMatch(it, text) }
        adapterUsers = UserFeedAdapter(requireContext(), usersSearch, lifecycleScope)
        binding.subscriptionsMysubs.layoutManager = LinearLayoutManager(context)
        binding.subscriptionsMysubs.adapter = adapterUsers
    }

    private fun showMy() {
        var req = UserRequest(
            idUser = Helper.currentUser.id,
        )
        lifecycleScope.launch {
            val res = ApiHelper.getMySubsctiptions(req)
            res.onSuccess { response ->
                users = response.users!!
                adapterUsers = UserFeedAdapter(requireContext(), users, lifecycleScope)
                binding.subscriptionsMysubs.layoutManager = LinearLayoutManager(context)
                binding.subscriptionsMysubs.adapter = adapterUsers
            }.onFailure { error ->
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.searchMysubs.removeTextChangedListener(textWatcher)
        textWatcher = null
    }
}