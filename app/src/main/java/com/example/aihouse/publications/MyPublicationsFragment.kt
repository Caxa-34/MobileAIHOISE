package com.example.aihouse.publications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.aihouse.databinding.FragmentMyPublicationsBinding

class MyPublicationsFragment : Fragment() {
    private lateinit var binding: FragmentMyPublicationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPublicationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    var title = "Мои публикации"
    var lastSearchNotEmpty = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filtersMypublications.visibility = View.GONE

        binding.searchMypublications.addTextChangedListener {
            var currentSearchNotEmpty = binding.searchMypublications.text.isNotEmpty()
            if (!currentSearchNotEmpty) binding.filtersMypublications.visibility = View.GONE
            if (currentSearchNotEmpty && !lastSearchNotEmpty) {
                binding.filtersMypublications.visibility = View.VISIBLE
                binding.filterPublishedMypublications.isChecked = true
            }

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