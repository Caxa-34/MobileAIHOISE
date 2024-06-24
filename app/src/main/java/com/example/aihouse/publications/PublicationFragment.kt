package com.example.aihouse.publications

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
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aihouse.Helper
import com.example.aihouse.R
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.BanRequest
import com.example.aihouse.api.ComplaintRequest
import com.example.aihouse.api.LikePublicationRequest
import com.example.aihouse.api.PublicationRequest
import com.example.aihouse.api.SendCommentRequest
import com.example.aihouse.api.SubscribeReqest
import com.example.aihouse.databinding.ComplaintsDialogBinding
import com.example.aihouse.databinding.FragmentPublicationBinding
import com.example.aihouse.databinding.FullPublicationCardBinding
import com.example.aihouse.databinding.ViolationsDialogBinding
import com.example.aihouse.databinding.WriteMessageEdittextBinding
import com.example.aihouse.models.Complaint
import com.example.aihouse.models.Publication
import com.example.aihouse.models.User
import com.example.aihouse.models.Violation
import kotlinx.coroutines.launch

class PublicationFragment : Fragment() {
    private lateinit var binding: FragmentPublicationBinding
    private lateinit var etBinding: WriteMessageEdittextBinding
    private lateinit var bindingPublication: FullPublicationCardBinding
    private lateinit var publication: Publication
    private lateinit var violations: List<Violation>
    private lateinit var complaints: List<Complaint>
    private var selectedComplaintIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPublicationBinding.inflate(inflater, container, false)
        bindingPublication = binding.fullPublicationCardPublication
        etBinding = binding.etSendPublication
        return binding.root
    }

    var userClick = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var navController = findNavController()
        publication = Helper.clickedPublication!!
        Helper.clickedPublication == null
        if (publication == null) {
            navController.popBackStack()
        }
        update()
    }

    fun update() {
        var req = PublicationRequest(
            id = publication.id!!,
            idUser = Helper.currentUser.id
        )
        lifecycleScope.launch {
            val res = ApiHelper.getPublication(req)
            res.onSuccess { response ->
                publication = response.publication!!
            }.onFailure { error ->
            }
            var comments = publication.comments?.sortedByDescending { ApiHelper.parseDateTime(it.dateCreate!!) }
            comments = comments!!.sortedByDescending { it.countLikes!! }

            if (comments == null) comments = listOf()
            var adapterUserFeed = PublicationCommentAdapter(requireContext(), comments, lifecycleScope)
            binding.listComment.layoutManager = LinearLayoutManager(context)
            binding.listComment.adapter = adapterUserFeed

            with(bindingPublication) {
                txtTitleFullpub.setText(publication.title)
                txtTextFullpub.setText(publication.text)
                txtTimeFullpub.setText(ApiHelper.formatDateTime(ApiHelper.parseDateTime(publication.dateCreate!!)))
                txtNameFullpub.setText(publication.author?.name)

                userClick = false
                btnLikeFullpub.isChecked = publication.isSetLike!!
                userClick = false
                btnComplaintFullpub.isChecked = publication.isSetComplaint!!
                userClick = false
                btnSubscribeFullpub.isChecked = publication.isSetSubscribe!!
                userClick = true

                txtCountLikesFullpub.setText(publication.countLikes.toString())
                txtCountCommentsFullpub.setText(publication.countComments.toString())
                txtCountComplaintsFullpubs.setText(publication.countComplaints.toString())

                if (publication.isSetComplaint!!) {
                    btnComplaintFullpub.isEnabled = false
                    btnComplaintFullpub.isChecked = true
                }
                if (publication.countComplaints!! == 0 && Helper.currentUser.idRole == 1)
                    btnComplaintFullpub.isEnabled = false

                ApiHelper.loadImage(requireContext(), publication.author?.imagePath!!, imgAvatarFullcard)
                if (publication.author?.name == Helper.currentUser.name) {
                    txtNameFullpub.text = "Вы"
                    val typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_bold)
                    txtNameFullpub.typeface = typeface
                    btnSubscribeFullpub.visibility = View.GONE
                    btnComplaintFullpub.visibility = View.GONE
                    btnLikeFullpub.isEnabled = false
                }
                btnLikeFullpub.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (userClick)
                        like(publication, isChecked, txtCountLikesFullpub, btnLikeFullpub)
                    userClick = true
                }
                btnSubscribeFullpub.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (userClick)
                        subscribe(publication, isChecked, btnSubscribeFullpub)
                    userClick = true
                }
                btnComplaintFullpub.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (userClick) {
                        if (Helper.currentUser.idRole == 1)
                            showComplaintsDialog()
                        else
                            showAddComplaintDialog()
                    }
                    userClick = true
                }
                txtNameFullpub.setOnClickListener {
                    navToUserPage(it, publication.author!!)
                }
                imgAvatarFullcard.setOnClickListener {
                    navToUserPage(it, publication.author!!)
                }
            }
        }
        binding.btnBackNotification.setOnClickListener {
            findNavController().popBackStack()
        }
        etBinding.btnSendWrmes.setOnClickListener {
            sendComment()
        }
        etBinding.etWrmes.text = null
    }

    fun navToUserPage(view: View, user: User) {
        Helper.clickedUser = user
        findNavController().navigate(R.id.userPageFragment)
    }

    fun sendComment() {
        var text = etBinding.etWrmes.text.toString()
        if (text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Пустое сообщение", Toast.LENGTH_SHORT).show()
            return
        }
        var req = SendCommentRequest(
            idPublication = publication.id!!,
            idUser = Helper.currentUser.id,
            text = text
        )
        lifecycleScope.launch {
            val res = ApiHelper.sendComment(req)
            update()
        }
    }

    fun showAddComplaintDialog() {
        userClick = false
        bindingPublication.btnComplaintFullpub.isChecked = false

        lifecycleScope.launch {
            val res = ApiHelper.getViolations()
            res.onSuccess { response ->
                violations = response.violations!!
            }

            val dialogBinding = ViolationsDialogBinding.inflate(layoutInflater)
            val complaintDialog = Dialog(requireContext())
            dialogBinding.btnPositiveResultViolations.isEnabled = false

            val complaintAdapter = ViolationAdapter(violations)
            { selectedIndex ->
                selectedComplaintIndex = selectedIndex
                dialogBinding.btnPositiveResultViolations.isEnabled = true
            }
            dialogBinding.lvComplaintsViolations.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = complaintAdapter
            }

            dialogBinding.btnPositiveResultViolations.setOnClickListener {
                if (selectedComplaintIndex != -1) {
                    addComplaint(violations[selectedComplaintIndex])
                    complaintDialog.dismiss()
                }
            }

            complaintDialog.setContentView(dialogBinding.root)
            complaintDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            complaintDialog.show()
        }
    }
    fun showComplaintsDialog() {
        userClick = false
        bindingPublication.btnComplaintFullpub.isChecked = false

        val req = ComplaintRequest(
            idPublication = publication.id!!,
            idViolation = null,
            idUser = null
        )
        lifecycleScope.launch {
            val res = ApiHelper.getComplaints(req)
            res.onSuccess { response ->
                complaints = response.complaints!!.sortedByDescending { it.count }
            }

            val dialogBinding = ComplaintsDialogBinding.inflate(layoutInflater)
            val complaintDialog = Dialog(requireContext())
            dialogBinding.btnPositiveResultComplaintdialog.isEnabled = false
            dialogBinding.etBanDaysComplaintdialog.etTitleEtcustom.setText("Длительность в днях")
            dialogBinding.etBanDaysComplaintdialog.etTextEtcustom.hint = "1234"
            dialogBinding.etBanDaysComplaintdialog.etTextEtcustom.inputType = InputType.TYPE_CLASS_NUMBER
            dialogBinding.etBanDaysComplaintdialog.etTextEtcustom.filters = arrayOf(InputFilter.LengthFilter(5)) // Максимальная длина 5 символов

            var isSelected = false
            var isSetDays = true

            val complaintAdapter = ComplaintAdapter(complaints) { selectedIndex ->
                selectedComplaintIndex = selectedIndex
                isSelected = true
                dialogBinding.btnPositiveResultComplaintdialog.isEnabled = isSelected && isSetDays
                // Log.e("BOOLEAN", isSelected.toString() + " " + isSetDays.toString())
            }

            dialogBinding.lvComplaintsComplaintdialog.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = complaintAdapter
            }

            dialogBinding.cbBanAuthorComplaintdialog.setOnCheckedChangeListener { _, isChecked ->
                dialogBinding.etBanDaysComplaintdialog.root.visibility = if (isChecked) View.VISIBLE else View.GONE
                isSetDays = if (isChecked && dialogBinding.etBanDaysComplaintdialog.etTextEtcustom.text.isNullOrEmpty())
                    false else true
                dialogBinding.btnPositiveResultComplaintdialog.isEnabled = isSelected && isSetDays
                dialogBinding.btnPositiveResultComplaintdialog.text = if (isChecked) "Удалить и забанить" else "Удалить"

            }

            dialogBinding.btnPositiveResultComplaintdialog.setOnClickListener {
                if (selectedComplaintIndex != -1) {
                    val banAuthor = dialogBinding.cbBanAuthorComplaintdialog.isChecked
                    val banDays = if (banAuthor) dialogBinding.etBanDaysComplaintdialog.etTextEtcustom.text.toString() else null
                    delPublication(complaints[selectedComplaintIndex], banDays, dialogBinding.cbBanAuthorComplaintdialog.isChecked)
                    complaintDialog.dismiss()
                }
            }
            dialogBinding.etBanDaysComplaintdialog.etTextEtcustom.addTextChangedListener {
                isSetDays = false
                if (!dialogBinding.etBanDaysComplaintdialog.etTextEtcustom.text.isNullOrEmpty())
                    if (dialogBinding.etBanDaysComplaintdialog.etTextEtcustom.text.toString().toInt() > 0)
                        isSetDays = true
                dialogBinding.btnPositiveResultComplaintdialog.isEnabled = isSelected && isSetDays

            }

            complaintDialog.setContentView(dialogBinding.root)
            complaintDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            complaintDialog.show()
        }
    }

    fun addComplaint(violation: Violation) {
        var req = ComplaintRequest(
            idPublication = publication.id!!,
            idUser = Helper.currentUser.id,
            idViolation = violation.id
        )
        lifecycleScope.launch {
            val res = ApiHelper.addComplaint(req)
            res.onSuccess { response ->
                // Log.e("ADD COMPLAINT", response.toString())
                if (response.message == "ViolationCreated") {
                    update()
                }
            }.onFailure { error ->
                // Log.e("ADD COMPLAINT ERROR", error.toString())
            }
        }
    }

    fun delPublication(complaint: Complaint, days: String?, isBan: Boolean) {
        var req = BanRequest(
            idPublication = publication.id!!,
            idUser = null,
            idViolation = null,
            idAdmin = null,
            days = null
        )
        if (isBan) {
            req.idUser = publication.author!!.id
            req.idViolation = complaint.id
            req.idAdmin = Helper.currentUser.id
            req.days = days!!.toInt()
        }

        lifecycleScope.launch {
            ApiHelper.banUser(req)
            findNavController().popBackStack()
        }
    }

    private fun like(publication : Publication, like : Boolean, txtLikes : TextView, btnLike : CheckBox) {
        var type = "remove"
        if (like) type = "set"
        var req = LikePublicationRequest(
            idPublication = publication.id!!,
            idUser = Helper.currentUser.id,
            type = type
        )
        btnLike.isEnabled = false
        lifecycleScope.launch {
            val res = ApiHelper.likePublication(req)
            res.onSuccess { response ->
                //Toast.makeText(context, "Like " + type, Toast.LENGTH_SHORT).show()
                txtLikes.setText(response.countLikes.toString())
                if (response.result == "LikeRemoved" || response.result == "LikeSetted") {
                    userClick = true
                }
                if (response.result == "LikeWasnt" || response.result == "LikeWas") {
                    userClick = false
                    btnLike.isChecked = !like
                }
            }.onFailure { error ->
                //Toast.makeText(context  , "Error: Like " + type, Toast.LENGTH_SHORT).show()
                userClick = false
                btnLike.isChecked = !like
            }
            btnLike.isEnabled = true
        }
    }

    private fun subscribe(publication : Publication, subscribe : Boolean, btnSub : CheckBox) {
        var type = "unsubscribe"
        if (subscribe) type = "subscribe"
        var req = SubscribeReqest(
            idAuthor = publication.idAuthor!!,
            idUser = Helper.currentUser.id,
            type = type
        )
        btnSub.isEnabled = false
        lifecycleScope.launch {
            val res = ApiHelper.subscribe(req)
            res.onSuccess { response ->
                //Toast.makeText(context, "Like " + type, Toast.LENGTH_SHORT).show()
                if (response.result == "Unsubscribed" || response.result == "Subscribed") {
                    userClick = true
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