package com.example.aihouse.initializationPerson

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.aihouse.Helper
import com.example.aihouse.R
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.ApiService
import com.example.aihouse.api.RegisterRequest
import com.example.aihouse.databinding.FragmentRegistrationBinding
import com.example.aihouse.databinding.VerificationDialogBinding
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private var code = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var controller = findNavController()
        binding.btnToAutorizationRegistration.setOnClickListener {
            controller.navigate(R.id.autorizationFragment)
        }
        binding.btnRegistrationRegistration.setOnClickListener {
            showRules()
        }
        binding.etNameRegistration.etTextEtcustom.addTextChangedListener {
            binding.etNameRegistration.etErrorEtcustom.visibility = View.INVISIBLE
            binding.btnRegistrationRegistration.isEnabled = checkData()
        }
        binding.etEmailRegistration.etTextEtcustom.addTextChangedListener {
            binding.etEmailRegistration.etErrorEtcustom.visibility = View.INVISIBLE
            binding.btnRegistrationRegistration.isEnabled = checkData()
        }
        binding.etPasswordRegistration.etTextEtcustom.addTextChangedListener {
            binding.etPasswordRegistration.etErrorEtcustom.visibility = View.INVISIBLE
            binding.btnRegistrationRegistration.isEnabled = checkData()
        }

        binding.etNameRegistration.etTitleEtcustom.setText("Имя пользователя")
        binding.etEmailRegistration.etTitleEtcustom.setText("Email")
        binding.etPasswordRegistration.etTitleEtcustom.setText("Пароль")

        binding.etNameRegistration.etTextEtcustom.setHint("Ваше имя")
        binding.etEmailRegistration.etTextEtcustom.setHint("Email")
        binding.etPasswordRegistration.etTextEtcustom.setHint("Придумайте пароль")

        binding.etPasswordRegistration.chbShowPassEtcustom.visibility = View.VISIBLE
        binding.etPasswordRegistration.chbShowPassEtcustom.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                binding.etPasswordRegistration.etTextEtcustom.transformationMethod = HideReturnsTransformationMethod.getInstance()
            else
                binding.etPasswordRegistration.etTextEtcustom.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        binding.btnRegistrationRegistration.isEnabled = false

        binding.etNameRegistration.etTextEtcustom.inputType = InputType.TYPE_CLASS_TEXT
        binding.etEmailRegistration.etTextEtcustom.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        binding.etPasswordRegistration.etTextEtcustom.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.etPasswordRegistration.etTextEtcustom.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    fun checkData() : Boolean {
        var name = binding.etNameRegistration.etTextEtcustom.text.toString()
        var email = binding.etEmailRegistration.etTextEtcustom.text.toString()
        var pass = binding.etPasswordRegistration.etTextEtcustom.text.toString()
        if (name.isNullOrEmpty() || name == "") return false
        if (name.length > 16) {
            binding.etNameRegistration.etErrorEtcustom.setText("Длина больше 16")
            binding.etNameRegistration.etErrorEtcustom.visibility = View.VISIBLE
            return false
        }
        if (email.isNullOrEmpty() || email == "" || !email.contains("@") || !email.contains(".")) return false
        if (pass.isNullOrEmpty() || pass == "") return false


        return true
    }

    fun showRules() {
        var rulesDialogBinding = layoutInflater.inflate(R.layout.rules_dialog, null)
        var rulesDialog = Dialog(binding.btnRegistrationRegistration.context)

        rulesDialogBinding.findViewById<Button>(R.id.btnPositiveResult_rules).isEnabled = false

        rulesDialogBinding.findViewById<CheckBox>(R.id.chbAgree_rules).setOnCheckedChangeListener { buttonView, isChecked ->
            rulesDialogBinding.findViewById<Button>(R.id.btnPositiveResult_rules).isEnabled = isChecked

        }
        rulesDialogBinding.findViewById<Button>(R.id.btnPositiveResult_rules).setOnClickListener {
            rulesDialog.dismiss()
            verification()
        }

        val txtRules = Helper.rules.joinToString(separator = "\n\n") { it.text }
        rulesDialogBinding.findViewById<TextView>(R.id.txtRules_ruleagree).setText(txtRules)

        rulesDialog.setContentView(rulesDialogBinding)
        rulesDialog.setCancelable(false)
        rulesDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        rulesDialog.show()
    }

    fun verification() {
        var req = RegisterRequest(
            name = binding.etNameRegistration.etTextEtcustom.text.toString(),
            email = binding.etEmailRegistration.etTextEtcustom.text.toString(),
            password = binding.etPasswordRegistration.etTextEtcustom.text.toString()
        )
        lifecycleScope.launch {
            val result = ApiHelper.getCode(req)
            result.onSuccess { response ->
                //Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()

                if (response.message == "EmailUsed") {
                    binding.etEmailRegistration.etErrorEtcustom.setText("Email уже занят")
                    binding.etEmailRegistration.etErrorEtcustom.visibility = View.VISIBLE
                    return@launch
                }
                if (response.message == "NameUsed") {
                    binding.etNameRegistration.etErrorEtcustom.setText("Имя уже занято")
                    binding.etNameRegistration.etErrorEtcustom.visibility = View.VISIBLE
                    return@launch
                }

                if (response.message == "CodeGenerated") {
                    code = response.verificationCode!!
                    showCodeVerification()
                }
            }.onFailure { error ->
                //Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun checkCode(strCode: String) : Boolean {
        if (strCode.isNullOrEmpty()) return false
        if (strCode.length < 4) return false
        return true
    }

    fun showCodeVerification() {
        var verificationDialogBinding = VerificationDialogBinding.inflate(layoutInflater)
        var verificationDialog = Dialog(requireContext())
        var etCodeBinding = verificationDialogBinding.etCodeVerification

        verificationDialogBinding.btnPositiveResultVerification.isEnabled = false
        etCodeBinding.etErrorEtcustom.setText("")

        verificationDialogBinding.btnPositiveResultVerification.setOnClickListener {
            var inputCode = etCodeBinding.etTextEtcustom.text.toString().toInt()
            if (code == inputCode) {
                verificationDialog.dismiss()
                registration()
            }
            else {
                etCodeBinding.etTextEtcustom.setText("")
                etCodeBinding.etErrorEtcustom.setText("Неверный код")
            }
        }

        etCodeBinding.etTextEtcustom.addTextChangedListener {
            etCodeBinding.etErrorEtcustom.setText("")
            if (!checkCode(etCodeBinding.etTextEtcustom.text.toString())) {
                verificationDialogBinding.btnPositiveResultVerification.isEnabled = false
            }
            else {
                verificationDialogBinding.btnPositiveResultVerification.isEnabled = true
            }
        }

        verificationDialog.setContentView(verificationDialogBinding.root)
        verificationDialog.setCancelable(false)
        verificationDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        verificationDialog.show()
        //Toast.makeText(requireContext(), "SHOWED", Toast.LENGTH_SHORT).show()
    }

    fun registration() {
        var req = RegisterRequest(
            name = binding.etNameRegistration.etTextEtcustom.text.toString(),
            email = binding.etEmailRegistration.etTextEtcustom.text.toString(),
            password = binding.etPasswordRegistration.etTextEtcustom.text.toString()
        )

        lifecycleScope.launch {
            val result = ApiHelper.registerUser(req)
            result.onSuccess { response ->
                //Log.e("sign in", "title: " + response.title + "\nmessage: " + response.message)
                //Toast.makeText(context, "${response.title}: ${response.message}", Toast.LENGTH_SHORT).show()

                if (response.message == "EmailUsed") {
                    binding.etEmailRegistration.etErrorEtcustom.setText("Email уже занят")
                    binding.etEmailRegistration.etErrorEtcustom.visibility = View.VISIBLE
                    return@launch
                }
                if (response.message == "NameUsed") {
                    binding.etNameRegistration.etErrorEtcustom.setText("Имя уже занято")
                    binding.etNameRegistration.etErrorEtcustom.visibility = View.VISIBLE
                    return@launch
                }

                if (response.message == "UserCreated") {
                    Helper.currentUser = response.userData!!
                    Helper.saveUserData(requireContext(), req.email, req.name, req.password)
                    var controller = findNavController()
                    controller.navigate(R.id.mainFragment)
                    Log.e("Зарегался!", Helper.currentUser.toString())
                    Toast.makeText(requireContext(), "Зарегался!", Toast.LENGTH_SHORT).show()
                }
            }.onFailure { error ->
                //Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}