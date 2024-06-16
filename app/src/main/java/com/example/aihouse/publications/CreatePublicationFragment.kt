package com.example.aihouse.publications

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
import com.example.aihouse.api.CreatePublicationRequest
import com.example.aihouse.api.DraftRequest
import com.example.aihouse.cards.PublicationCard
import com.example.aihouse.cards.UserCard
import com.example.aihouse.databinding.FragmentCreatePublicationBinding
import com.example.aihouse.models.Draft
import kotlinx.coroutines.launch

class CreatePublicationFragment : Fragment() {
    private lateinit var binding: FragmentCreatePublicationBinding
    private var draft: Draft? = null
    private var type = "Publication"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreatePublicationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPublicCreatepublication.setOnClickListener {
            public()
        }
        binding.btnSaveDraftCreatepublication.setOnClickListener {
            saveDraft()
        }
        binding.btnBackCreatepublication.setOnClickListener {
            var bundle = Bundle()
            var controller = findNavController()
            bundle.putString("act", "openRightPanel")
            controller.navigate(R.id.mainFragment, bundle)
        }
        binding.etTextPublicationCreatepublication.setText("")
        binding.etTitlePublicationCreatepublication.setText("")
        if (Helper.clickedDraft != null) {
            draft = Helper.clickedDraft!!
            type = "Draft"
            binding.etTitlePublicationCreatepublication.setText(draft?.title!!)
            binding.etTextPublicationCreatepublication.setText(draft?.text!!)
        }
        Helper.clickedDraft = null
    }

    fun saveDraft() {
        var title = binding.etTitlePublicationCreatepublication.text.toString()
        var text = binding.etTextPublicationCreatepublication.text.toString()

        if (title == "") {
            Toast.makeText(context, "Введите заголовок", Toast.LENGTH_SHORT).show()
            return
        }
        if (text == "") {
            Toast.makeText(context, "Введите текст", Toast.LENGTH_SHORT).show()
            return
        }

        if (type == "Draft") {
            var req = DraftRequest(
                title = title,
                text = text,
                idUser = null,
                id = draft?.id
            )
            lifecycleScope.launch {
                val resultName = ApiHelper.updateDraft(req)
                resultName.onSuccess { response ->
                    Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show()
                }.onFailure { error ->

                }

                var bundle = Bundle()
                var controller = findNavController()
                bundle.putString("act", "openRightPanel")
                controller.navigate(R.id.mainFragment, bundle)
            }
        }
        if (type == "Publication") {
            var req = DraftRequest(
                title = title,
                text = text,
                idUser = Helper.currentUser.id,
                id = null
            )
            lifecycleScope.launch {
                val resultName = ApiHelper.addDraft(req)
                resultName.onSuccess { response ->
                    Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show()
                }.onFailure { error ->

                }

                var bundle = Bundle()
                var controller = findNavController()
                bundle.putString("act", "openRightPanel")
                controller.navigate(R.id.mainFragment, bundle)
            }
        }
    }

    fun public() {
        var title = binding.etTitlePublicationCreatepublication.text.toString()
        var text = binding.etTextPublicationCreatepublication.text.toString()

        if (title == "") {
            Toast.makeText(context, "Введите заголовок", Toast.LENGTH_SHORT).show()
            return
        }
        if (text == "") {
            Toast.makeText(context, "Введите текст", Toast.LENGTH_SHORT).show()
            return
        }

        var req = CreatePublicationRequest(
            title = binding.etTitlePublicationCreatepublication.text.toString(),
            text = binding.etTextPublicationCreatepublication.text.toString(),
            idUser = Helper.currentUser.id,
            idDraft = draft?.id
        )
        lifecycleScope.launch {
            val resultName = ApiHelper.createPublications(req)
            resultName.onSuccess { response ->
                Toast.makeText(context, "Опубликовано!", Toast.LENGTH_SHORT).show()
            }.onFailure { error ->

            }

            var bundle = Bundle()
            var controller = findNavController()
            bundle.putString("act", "openRightPanel")
            controller.navigate(R.id.mainFragment, bundle)
        }
    }
}