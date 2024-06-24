package com.example.aihouse.person

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aihouse.Helper
import com.example.aihouse.R
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.PublicationRequest
import com.example.aihouse.api.SubscribeReqest
import com.example.aihouse.api.UserFullRequest
import com.example.aihouse.databinding.FragmentUserPageBinding
import com.example.aihouse.models.Publication
import com.example.aihouse.models.User
import com.example.aihouse.publications.MyPublicationAdapter
import com.example.aihouse.publications.PublicationCommentAdapter
import kotlinx.coroutines.launch

class UserPageFragment : Fragment() {
    private lateinit var binding: FragmentUserPageBinding
    private lateinit var user: User
    private lateinit var publications: List<Publication>
    var isOpen = false
    var userClick = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var navController = findNavController()
        user = Helper.clickedUser!!
        Helper.clickedUser == null
        if (user == null) {
            navController.popBackStack()
        }
        binding.btnBackUserpage.setOnClickListener {
            navController.popBackStack()
        }
        binding.btnSubscribeUserpage.setOnCheckedChangeListener { buttonView, isChecked ->
            if (userClick)
                subscribe(isChecked, binding.btnSubscribeUserpage)
            userClick = true
        }
        binding.btnCopyIDUserpage.setOnClickListener {
            val myID = user.id.toString()
            Helper.saveToBuffer(requireContext(), myID)
            Toast.makeText(context, "ID скопирован!", Toast.LENGTH_SHORT).show();
        }
        binding.btnUserInfoUserpage.setOnClickListener {
            if (isOpen) {
                binding.imgArrowUserpage.rotation = -90f
                val layoutParams = binding.llCenterDataUserpage.layoutParams
                layoutParams.height = 128.dpToPx()
                binding.llCenterDataUserpage.layoutParams = layoutParams
            }
            else {
                binding.imgArrowUserpage.rotation = 90f
                val layoutParams = binding.llCenterDataUserpage.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.llCenterDataUserpage.layoutParams = layoutParams
            }
            isOpen = !isOpen
        }
        var req = UserFullRequest(
            idAuthor = user.id,
            idUser = Helper.currentUser.id
        )
        lifecycleScope.launch {
            val res = ApiHelper.getFullUser(req)
            res.onSuccess { response ->
                publications = response.publications!!
                user = response.userData!!
            }.onFailure { error -> }
            setInfo()
        }
    }

    fun setInfo() {
        with(binding) {
            txtNameUserpage.setText(user.name)
            txtIDUserpage.setText("ID: " + user.id.toString())
            ApiHelper.loadImage(requireContext(), user.imagePath!!, imgAvatarUserpage)
            txtGenderUserpage.setText(user.gender)
            txtAgeUserpage.setText(if (user.age == null) "Не указан" else user.age.toString())
            txtAboutUserpage.setText(user.aboutMe)
            userClick = false
            btnSubscribeUserpage.isChecked = user.isSetSubscribe!!
            userClick = true
            Log.e("USER", user.toString())


            var isMy = user.id == Helper.currentUser.id
            publications = publications.sortedByDescending { ApiHelper.parseDateTime(it.dateCreate!!) }
            var adapterPublications = MyPublicationAdapter(requireContext(), publications, lifecycleScope, isMy)
            binding.listUserPublicationsUserpage.layoutManager = LinearLayoutManager(context)
            binding.listUserPublicationsUserpage.adapter = adapterPublications
        }
    }


    private fun subscribe(subscribe : Boolean, btnSub : CheckBox) {
        var type = "unsubscribe"
        if (subscribe) type = "subscribe"
        var req = SubscribeReqest(
            idAuthor = user.id,
            idUser = Helper.currentUser.id,
            type = type
        )
        btnSub.isEnabled = false
        lifecycleScope.launch {
            val res = ApiHelper.subscribe(req)
            res.onSuccess { response ->
                //Toast.makeText(context, "Like " + type, Toast.LENGTH_SHORT).show()
                if (response.result == "Unsubscribed") {
                    userClick = true
                    user.isSetSubscribe = false
                }
                if (response.result == "Subscribed") {
                    userClick = true
                    user.isSetSubscribe = true
                }
                if (response.result == "SubscribeWasnt" || response.result == "SubscribeWas") {
                    userClick = false
                    btnSub.isChecked = !subscribe
                }
            }.onFailure { error ->
                //Toast.makeText(context  , "Error: Like " + type, Toast.LENGTH_SHORT).show()
                userClick = false
                btnSub.isChecked = !subscribe
            }
            btnSub.isEnabled = true
        }
    }

}