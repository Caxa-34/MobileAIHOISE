package com.example.aihouse.person

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aihouse.Helper
import com.example.aihouse.R
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.UserRequest
import com.example.aihouse.databinding.FragmentNotificationsBinding
import com.example.aihouse.publications.PublicationFeedAdapter
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {
    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var controller = findNavController()
        var bundle = Bundle()

        binding.btnBackNotification.setOnClickListener {
            bundle.putString("act", "openLeftPanel")
            controller.navigate(R.id.mainFragment, bundle)
        }
        getNotification()
    }

    fun getNotification() {
        var req = UserRequest(
            idUser = Helper.currentUser.id
        )
        lifecycleScope.launch {
            val result = ApiHelper.getMyNotifs(req)
            result.onSuccess { response ->
                Helper.notifications = response.notifications!!
                Helper.notifications = Helper.notifications.sortedByDescending { ApiHelper.parseDateTime(it.dateCreate!!) }
                var adapter = NotificatonsAdapter(requireContext(), Helper.notifications, lifecycleScope)
                binding.listNotifsNotfication.layoutManager = LinearLayoutManager(context)
                binding.listNotifsNotfication.adapter = adapter
            }.onFailure { error ->
                //Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        }
    }


}