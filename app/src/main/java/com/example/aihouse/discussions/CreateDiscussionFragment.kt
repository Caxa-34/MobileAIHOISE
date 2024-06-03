package com.example.aihouse.discussions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.aihouse.R
import com.example.aihouse.databinding.FragmentCreateDiscussionBinding

class CreateDiscussionFragment : Fragment() {
    private lateinit var binding: FragmentCreateDiscussionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    }
}