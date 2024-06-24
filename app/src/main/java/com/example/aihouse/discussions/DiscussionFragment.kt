package com.example.aihouse.discussions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aihouse.Helper
import com.example.aihouse.R
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.BanRequest
import com.example.aihouse.api.ComplaintRequest
import com.example.aihouse.api.GetDiscussionRequest
import com.example.aihouse.api.SendCommentRequest
import com.example.aihouse.api.SendMessageRequest
import com.example.aihouse.api.SetAnsweredRequest
import com.example.aihouse.api.SubscribeReqest
import com.example.aihouse.databinding.ComplaintsDialogBinding
import com.example.aihouse.databinding.FragmentDiscussionBinding
import com.example.aihouse.databinding.ViolationsDialogBinding
import com.example.aihouse.databinding.WriteMessageEdittextBinding
import com.example.aihouse.messages.DiscussionMessageAdapter
import com.example.aihouse.models.Complaint
import com.example.aihouse.models.Discussion
import com.example.aihouse.models.DiscussionMessage
import com.example.aihouse.models.User
import com.example.aihouse.models.Violation
import com.example.aihouse.publications.ComplaintAdapter
import com.example.aihouse.publications.ViolationAdapter
import kotlinx.coroutines.launch

class DiscussionFragment : Fragment() {

    private lateinit var binding: FragmentDiscussionBinding
    private lateinit var etBinding: WriteMessageEdittextBinding
    private lateinit var discussion: Discussion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiscussionBinding.inflate(inflater, container, false)
        etBinding = binding.etInputDiscussion
        return binding.root
    }

    var showedQuestion = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var navController = findNavController()
        discussion = Helper.clickedDiscussion!!
        Helper.clickedDiscussion == null
        if (discussion == null) {
            navController.popBackStack()
        }
        binding.infoDiscussion.setOnClickListener {
            showedQuestion = !showedQuestion
            binding.llQuestionDiscussion.visibility = if (showedQuestion) View.VISIBLE else View.GONE
        }
        binding.scrollviewDiscussion.post {
            binding.scrollviewDiscussion.fullScroll(ScrollView.FOCUS_DOWN)
        }
        binding.btnBackDiscussion.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }
        etBinding.btnSendWrmes.setOnClickListener {
            sendMessage()
        }
        update()
    }

    fun update() {
        var req = GetDiscussionRequest(
            idDiscussion = discussion.id!!
        )
        lifecycleScope.launch {
            val res = ApiHelper.getDiscussion(req)
            res.onSuccess { response ->
                discussion = response.discussion!!
            }.onFailure { error ->
            }
            var isCanSetAnswer = (discussion.idAuthor == Helper.currentUser.id) && !discussion.isAnswered
            if (discussion.messages == null) discussion.messages = listOf()
            var adapterUserFeed = DiscussionMessageAdapter(requireContext(), discussion.messages!!, isCanSetAnswer)
            { message ->
                if (!discussion.isAnswered) setAnswer(message)
            }
            binding.messagesDiscussion.layoutManager = LinearLayoutManager(context)
            binding.messagesDiscussion.adapter = adapterUserFeed
            with(binding) {
                txtTitleDiscussion.setText(discussion.title)
                txtQuestionDiscussion.setText(discussion.textQuestion)
                ApiHelper.loadImage(requireContext(), discussion.icon, imgIconDiscussion)
                llIsAnsweredDiscussion.visibility = if (discussion.isAnswered) View.VISIBLE else View.GONE
            }
        }
        etBinding.etWrmes.text = null
    }

    fun setAnswer(message: DiscussionMessage) {
        var req = SetAnsweredRequest(
            idDiscussion = discussion.id,
            idMessage = message.id
        )
        lifecycleScope.launch {
            val res = ApiHelper.setAnswer(req)
            res.onSuccess { response ->
                Log.e("Set answer", response.toString())
                update()
            }.onFailure { error ->
                Log.e("Set answer", error.toString())

            }
        }
    }

    fun sendMessage() {
        var text = etBinding.etWrmes.text.toString()
        if (text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Пустое сообщение", Toast.LENGTH_SHORT).show()
            return
        }
        var req = SendMessageRequest(
            idDiscussion = discussion.id,
            idUser = Helper.currentUser.id,
            text = text
        )
        lifecycleScope.launch {
            val res = ApiHelper.sendMessage(req)
            update()
        }
    }

}