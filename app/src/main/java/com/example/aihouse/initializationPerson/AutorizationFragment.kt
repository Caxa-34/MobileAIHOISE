package com.example.aihouse.initializationPerson

import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.aihouse.Helper
import com.example.aihouse.R
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.ApiService
import com.example.aihouse.api.LoginRequest
import com.example.aihouse.databinding.FragmentAutorizationBinding
import kotlinx.coroutines.launch

enum class Type {
    EMAIL,
    NAME
}

class AutorizationFragment : Fragment() {
    private lateinit var binding: FragmentAutorizationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAutorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    var inputType = Type.NAME

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = findNavController()

        binding.btnAutorizationAutorization.setOnClickListener {
            authorization()
        }
        binding.btnToRegistrationAutorization.setOnClickListener {
            controller.navigate(R.id.registrationFragment)
        }
        binding.btnRecoveryAutorization.setOnClickListener {
            controller.navigate(R.id.recoveryEmailFragment)
        }

        binding.etPassAutorization.etTitleEtcustom.setText("Пароль")
        binding.etInputAutorizsation.etTitleEtcustom.setText("Имя пользователя")
        binding.etPassAutorization.etTextEtcustom.hint = "Ваш пароль"
        binding.etInputAutorizsation.etTextEtcustom.hint = "Ваше имя"

        binding.etPassAutorization.etTextEtcustom.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.etPassAutorization.chbShowPassEtcustom.visibility = View.VISIBLE
        binding.etInputAutorizsation.etTextEtcustom.inputType = InputType.TYPE_CLASS_TEXT

        binding.etPassAutorization.chbShowPassEtcustom.setOnCheckedChangeListener { buttonView, isChecked ->
            val etPass = binding.etPassAutorization.etTextEtcustom
            if (isChecked)
                etPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
            else
                etPass.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        binding.etInputAutorizsation.etTextEtcustom.addTextChangedListener {
            binding.btnAutorizationAutorization.isEnabled = checkData()
            binding.etInputAutorizsation.etErrorEtcustom.visibility = View.INVISIBLE
        }
        binding.etPassAutorization.etTextEtcustom.addTextChangedListener {
            binding.btnAutorizationAutorization.isEnabled = checkData()
            binding.etPassAutorization.etErrorEtcustom.visibility = View.INVISIBLE
        }

        binding.btnInputEmailAutorization.setOnClickListener {
            inputType = Type.EMAIL

            binding.etInputAutorizsation.etTitleEtcustom.setText("Email")
            binding.etInputAutorizsation.etTextEtcustom.hint = "Ваша почта"
            binding.etInputAutorizsation.etTextEtcustom.setText("")
            binding.etInputAutorizsation.etTextEtcustom.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            binding.btnInputEmailAutorization.isEnabled = false
            binding.btnInputLoginAutorization.isEnabled = true
        }
        binding.btnInputLoginAutorization.setOnClickListener {
            inputType = Type.NAME

            binding.etInputAutorizsation.etTitleEtcustom.setText("Имя пользователя")
            binding.etInputAutorizsation.etTextEtcustom.hint = "Ваше имя"
            binding.etInputAutorizsation.etTextEtcustom.setText("")
            binding.etInputAutorizsation.etTextEtcustom.inputType = InputType.TYPE_CLASS_TEXT

            binding.btnInputEmailAutorization.isEnabled = true
            binding.btnInputLoginAutorization.isEnabled = false
        }

        binding.btnAutorizationAutorization.isEnabled = false
        binding.etPassAutorization.etTextEtcustom.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    fun checkData() : Boolean {
        var input = binding.etInputAutorizsation.etTextEtcustom.text.toString()
        var pass = binding.etPassAutorization.etTextEtcustom.text.toString()

        if (inputType == Type.NAME)
            if (input.isNullOrEmpty() || input == "") return false
        if (inputType == Type.EMAIL)
            if (input.isNullOrEmpty() || input == "" || !input.contains("@") || !input.contains(".")) return false
        if (pass.isNullOrEmpty() || pass == "") return false

        return true
    }

    fun authorization() {
        var req = LoginRequest(
            name = binding.etInputAutorizsation.etTextEtcustom.text.toString(),
            email = binding.etInputAutorizsation.etTextEtcustom.text.toString(),
            password = binding.etPassAutorization.etTextEtcustom.text.toString()
        )

        if (inputType == Type.NAME) lifecycleScope.launch {
            val result = ApiHelper.loginNameUser(req)
            result.onSuccess { response ->

                if (response.message == "NameExists") {
                    binding.etInputAutorizsation.etErrorEtcustom.setText("Аккаунт не существует")
                    binding.etInputAutorizsation.etErrorEtcustom.visibility = View.VISIBLE
                    return@launch
                }
                if (response.message == "InvalidPass") {
                    binding.etPassAutorization.etErrorEtcustom.setText("Неверный пароль")
                    binding.etPassAutorization.etErrorEtcustom.visibility = View.VISIBLE
                    return@launch
                }
                if (response.message == "UserBanned") {
                    binding.etPassAutorization.etErrorEtcustom.setText("Пользователь забанен")
                    binding.etPassAutorization.etErrorEtcustom.visibility = View.VISIBLE
                    return@launch
                }
                if (response.message == "UserDeleted") {
                    binding.etPassAutorization.etErrorEtcustom.setText("Пользователь удалён")
                    binding.etPassAutorization.etErrorEtcustom.visibility = View.VISIBLE
                    return@launch
                }

                Helper.currentUser = response.userData!!
                Helper.saveUserData(requireContext(), Helper.currentUser.email, Helper.currentUser.name, req.password)

                var controller = findNavController()
                controller.navigate(R.id.mainFragment)
            }.onFailure { error ->
                //Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        if (inputType == Type.EMAIL) lifecycleScope.launch {
            val result = ApiHelper.loginEmailUser(req)
            result.onSuccess { response ->

                if (response.message == "EmailExists") {
                    binding.etInputAutorizsation.etErrorEtcustom.setText("Аккаунт не существует")
                    binding.etInputAutorizsation.etErrorEtcustom.visibility = View.VISIBLE
                    return@launch
                }
                if (response.message == "InvalidPass") {
                    binding.etPassAutorization.etErrorEtcustom.setText("Неверный пароль")
                    binding.etPassAutorization.etErrorEtcustom.visibility = View.VISIBLE
                    return@launch
                }
                if (response.message == "UserBanned") {
                    binding.etPassAutorization.etErrorEtcustom.setText("Пользователь забанен")
                    binding.etPassAutorization.etErrorEtcustom.visibility = View.VISIBLE
                    return@launch
                }
                if (response.message == "UserDeleted") {
                    binding.etPassAutorization.etErrorEtcustom.setText("Пользователь удалён")
                    binding.etPassAutorization.etErrorEtcustom.visibility = View.VISIBLE
                    return@launch
                }

                Helper.currentUser = response.userData!!
                Helper.saveUserData(requireContext(), Helper.currentUser.email, Helper.currentUser.name, req.password)

                var controller = findNavController()
                controller.navigate(R.id.mainFragment)
            }.onFailure { error ->
                //Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}