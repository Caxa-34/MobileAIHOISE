package com.example.aihouse.discussions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.aihouse.databinding.FragmentMyDiscussionsBinding

class MyDiscussionsFragment : Fragment() {
    private lateinit var binding: FragmentMyDiscussionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyDiscussionsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    var title = "Мои обсуждения"
    var lastSearchNotEmpty = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchMydiscussions.addTextChangedListener {
            var currentSearchNotEmpty = binding.searchMydiscussions.text.isNotEmpty()

            if (currentSearchNotEmpty) search()
            else showMy()

            lastSearchNotEmpty = currentSearchNotEmpty
        }
    }

    private fun search() {
        Toast.makeText(requireNotNull(context), "Search", Toast.LENGTH_SHORT).show()
    }

    private fun showMy() {
        Toast.makeText(requireNotNull(context), "My", Toast.LENGTH_SHORT).show()
    }
}