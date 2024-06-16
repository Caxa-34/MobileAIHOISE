package com.example.aihouse.publications

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aihouse.Helper
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.DraftRequest
import com.example.aihouse.api.UserRequest
import com.example.aihouse.databinding.FragmentMyPublicationsBinding
import com.example.aihouse.models.Draft
import com.example.aihouse.models.Publication
import com.example.aihouse.models.User
import kotlinx.coroutines.launch

class MyPublicationsFragment : Fragment() {
    private lateinit var binding: FragmentMyPublicationsBinding
    private var textWatcher: TextWatcher? = null

    lateinit var publications: List<Publication>
    lateinit var adapterPublications: MyPublicationAdapter
    lateinit var drafts: List<Draft>
    lateinit var adapterDrafts: MyDraftAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPublicationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    var title = "Мои публикации"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (textWatcher == null) {
            textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (binding.filterPublishedMypublications.isChecked) showPublications()
                    if (binding.filterDraftsMypublications.isChecked) showDrafts()
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            binding.searchMypublications.addTextChangedListener(textWatcher)
        }
        binding.filtersMypublications.setOnCheckedChangeListener { group, checkedId ->
            if (binding.filterPublishedMypublications.isChecked) showPublications()
            if (binding.filterDraftsMypublications.isChecked) showDrafts()
        }
        binding.filterPublishedMypublications.isChecked = true
    }

    private fun showDrafts() {
        var req = DraftRequest(
            idUser = Helper.currentUser.id,
            id = null,
            text = null,
            title = null
        )
        lifecycleScope.launch {
            val res = ApiHelper.getMyDrafts(req)
            res.onSuccess { response ->
                //Toast.makeText(requireNotNull(context), "Feed", Toast.LENGTH_SHORT).show()
                drafts = response.drafts!!
                drafts = drafts.sortedByDescending { ApiHelper.parseDateTime(it.dateUpdate!!) }
                val searchText = binding.searchMypublications.text.toString()
                if (!searchText.isNullOrEmpty())
                    drafts = drafts.filter { it.title!!.contains(searchText) }
                adapterDrafts = MyDraftAdapter(requireContext(), drafts, findNavController())
                binding.listMypublications.layoutManager = LinearLayoutManager(context)
                binding.listMypublications.adapter = adapterDrafts
            }.onFailure { error ->

            }
        }
    }

    private fun showPublications() {
        var req = UserRequest(
            idUser = Helper.currentUser.id
        )
        lifecycleScope.launch {
            val res = ApiHelper.getMyPublications(req)
            res.onSuccess { response ->
                //Toast.makeText(requireNotNull(context), "Feed", Toast.LENGTH_SHORT).show()
                publications = response.publications!!
                publications = publications.sortedByDescending { ApiHelper.parseDateTime(it.dateCreate!!) }
                val searchText = binding.searchMypublications.text.toString()
                if (!searchText.isNullOrEmpty())
                    publications = publications.filter { it.title!!.contains(searchText) }

                adapterPublications = MyPublicationAdapter(requireContext(), publications, lifecycleScope)
                binding.listMypublications.layoutManager = LinearLayoutManager(context)
                binding.listMypublications.adapter = adapterPublications
            }.onFailure { error ->

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.searchMypublications.removeTextChangedListener(textWatcher)
        textWatcher = null
    }
}