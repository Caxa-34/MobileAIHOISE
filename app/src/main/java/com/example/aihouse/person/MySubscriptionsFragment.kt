package com.example.aihouse.person

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.aihouse.databinding.FragmentMySubscriptionsBinding

class MySubscriptionsFragment : Fragment() {
    private lateinit var binding: FragmentMySubscriptionsBinding

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

        binding.searchMysubscriptions.addTextChangedListener {
            var currentSearchNotEmpty = binding.searchMysubscriptions.text.isNotEmpty()

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