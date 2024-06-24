package com.example.aihouse.discussions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.aihouse.Helper
import com.example.aihouse.R
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.CreateDiscussionRequest
import com.example.aihouse.api.CreatePublicationRequest
import com.example.aihouse.databinding.FragmentCreateDiscussionBinding
import kotlinx.coroutines.launch

class CreateDiscussionFragment : Fragment() {
    private lateinit var binding: FragmentCreateDiscussionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreateDiscussionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var controller = findNavController()
        var bundle = Bundle()

        binding.btnCreateCreatediscussion.setOnClickListener {
            bundle.putString("act", "openRightPanel")
            createDiscussion()
            controller.navigate(R.id.mainFragment, bundle)
        }
        binding.btnBackCreatediscussion.setOnClickListener {
            bundle.putString("act", "openRightPanel")
            controller.navigate(R.id.mainFragment, bundle)
        }
        binding.btnUploadImageCreatediscussion.setOnClickListener {

        }
    }
    fun createDiscussion() {
        var title = binding.etTitleCreatediscussion.text.toString()
        var text = binding.etQestionCreatediscussion.text.toString()

        if (title == "") {
            Toast.makeText(context, "Введите заголовок", Toast.LENGTH_SHORT).show()
            return
        }
        if (text == "") {
            Toast.makeText(context, "Введите свой вопрос", Toast.LENGTH_SHORT).show()
            return
        }

        var req = CreateDiscussionRequest(
            title = binding.etTitleCreatediscussion.text.toString(),
            question = binding.etQestionCreatediscussion.text.toString(),
            idUser = Helper.currentUser.id
        )
        lifecycleScope.launch {
            val result = ApiHelper.createDiscussion(req)
            result.onSuccess { response ->
                Toast.makeText(context, "Cоздано!", Toast.LENGTH_SHORT).show()
            }.onFailure { error ->

            }

            var bundle = Bundle()
            var controller = findNavController()
            bundle.putString("act", "openRightPanel")
            controller.navigate(R.id.mainFragment, bundle)
        }
    }
}