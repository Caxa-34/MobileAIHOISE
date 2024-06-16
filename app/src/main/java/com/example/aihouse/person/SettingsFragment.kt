package com.example.aihouse.person

import android.app.DatePickerDialog
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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.aihouse.Helper
import com.example.aihouse.R
import androidx.lifecycle.lifecycleScope
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.SetUserInfoRequest
import com.example.aihouse.api.UserRequest
import com.example.aihouse.databinding.ChangeAvatarDialogBinding
import com.example.aihouse.databinding.ChangeNicknameDialogBinding
import com.example.aihouse.databinding.ChangePasswordDialogBinding
import com.example.aihouse.databinding.DeleteUserDialogBinding
import com.example.aihouse.databinding.FragmentSettingsBinding
import com.example.aihouse.models.UserGender
import com.example.aihouse.models.UserSetting
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    private lateinit var genders: MutableList<UserGender>
    private lateinit var mySettings: UserSetting
    private var selectedDate: LocalDateTime? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var controller = findNavController()

        // отображение Общие по умолчанию
        setDisactiveButtons()
        binding.btnGeneralSettings.setBackgroundResource(R.drawable.background_topleft_active_setting)
        binding.btnGeneralSettings.setTextColor(ContextCompat.getColorStateList(binding.btnPrivateSettings.context,
            R.color.additional_color
        ))
        binding.generalSettings.visibility = View.VISIBLE

        // переключение между настройками
        binding.btnAccountSettings.setOnClickListener {
            showSettings(it as Button)
        }
        binding.btnGeneralSettings.setOnClickListener {
            showSettings(it as Button)
        }
        binding.btnPrivateSettings.setOnClickListener {
            showSettings(it as Button)
        }
        binding.btnNotificationSettings.setOnClickListener {
            showSettings(it as Button)
        }

        // кнопка назад
        binding.btnBackSettings.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("act", "openRightPanel")
            controller.navigate(R.id.mainFragment, bundle)
        }

        // настройки аккаунта
        binding.btnDeleteAccAccsetting.setOnClickListener {
            showDeleteAccDialog()
        }
        binding.btnChangeAvatarAccsetting.setOnClickListener {
            showChangeAvatarDialog()
        }
        binding.btnChangeNicknameAccsetting.setOnClickListener {
            showChangeNicknameDialog()
        }
        binding.btnChangePassAccsetting.setOnClickListener {
            showChangePassDialog()
        }
        binding.dateBirthdaySettings.setOnClickListener {
            showDatePicker()
        }
        binding.btnSaveSettings.setOnClickListener {
            saveSettings()
        }
        showSettingsLoad()
    }

    private fun saveSettings() {
        var aboutMe = binding.etAboutMeSettings.text.toString()
        var idGender = (binding.spinGenderSettings.selectedItem as UserGender).id
        var req = SetUserInfoRequest (
            idUser = Helper.currentUser.id,
            aboutMe = if (aboutMe.isNullOrEmpty()) null else aboutMe,
            idGender = if (idGender == 0) null else idGender,
            birthday = if (selectedDate == null) null else selectedDate.toString(),
            notifResponse = binding.notifResponseSettings.isChecked,
            notifReference = binding.notifReferenceSettings.isChecked,
            notifSubscribe = binding.notifSubscribeSettings.isChecked,
            notifLike = binding.notifLikeSettings.isChecked,
            notifComment = binding.notifCommentSettings.isChecked,
            privateShowSubscriber = binding.privateShowSubscriberSettings.isChecked,
            mobileGetPush = binding.mobileGetPushSettings.isChecked,
            notifTechnical = binding.notifTechnicalSettings.isChecked
        )
        lifecycleScope.launch {
            val res = ApiHelper.setUserInfo(req)
            res.onSuccess { response ->
                if (response.title == "Success")
                    Toast.makeText(requireContext(), "Сохранено!", Toast.LENGTH_SHORT).show()
                Log.e("SAVING SETTINGS", response.message.toString())
            }.onFailure { error ->
                Log.e("SAVING SETTINGS", error.message.toString())
            }
        }
    }

    private fun showSettingsLoad() {
        var req = UserRequest(
            idUser = Helper.currentUser.id
        )
        lifecycleScope.launch {
            val res = ApiHelper.getUserInfo(req)
            res.onSuccess { response ->
                genders = mutableListOf(
                    UserGender(0, "Не указывать"),
                )
                mySettings = response.userSettings!!
                genders.addAll( response.genders!! )
            }.onFailure { error ->
                Log.e("ERROR SETTINGS", error.message.toString());
            }

            binding.notifResponseSettings.isChecked = mySettings.notifResponse!!
            binding.notifReferenceSettings.isChecked = mySettings.notifReference!!
            binding.notifSubscribeSettings.isChecked = mySettings.notifSubscribe!!
            binding.notifLikeSettings.isChecked = mySettings.notifLike!!
            binding.notifCommentSettings.isChecked = mySettings.notifComment!!
            binding.privateShowSubscriberSettings.isChecked = mySettings.privateShowSubscriber!!
            binding.mobileGetPushSettings.isChecked = mySettings.mobileGetPush!!
            binding.notifTechnicalSettings.isChecked = mySettings.notifTechnical!!

            binding.spinGenderSettings.adapter = GenderSpinnerAdapter(requireContext(), genders)
            binding.spinGenderSettings.setSelection(0)

            if (mySettings.birthday != null) {
                selectedDate = ApiHelper.parseDateTime(mySettings.birthday!!)
                val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                binding.dateBirthdaySettings.text = selectedDate?.format(dateFormat)

            }

            binding.etAboutMeSettings.setText(mySettings.aboutMe)

            if (mySettings.idGender != null) {
                val selectedGenderPosition = genders.indexOfFirst { it.id == mySettings.idGender }
                if (selectedGenderPosition >= 0) {
                    binding.spinGenderSettings.setSelection(selectedGenderPosition)
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.customDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDayOfMonth)
                }

                // Преобразование в LocalDateTime
                selectedDate = selectedCalendar.time.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()

                // Форматирование и отображение выбранной даты
                val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                binding.dateBirthdaySettings.text = selectedDate?.format(dateFormat)
            },
            year, month, dayOfMonth
        )



        datePickerDialog.show()
    }

    fun showSettings(clickedButton: Button) {
        setDisactiveButtons()
        var settingStr = clickedButton.text
        when (settingStr) {
            "Общие" -> {
                binding.btnGeneralSettings.setBackgroundResource(R.drawable.background_topleft_active_setting)
                binding.btnGeneralSettings.setTextColor(ContextCompat.getColorStateList(binding.btnPrivateSettings.context,
                    R.color.additional_color
                ))
                binding.generalSettings.visibility = View.VISIBLE
            }
            "Уведомления" -> {
                binding.btnNotificationSettings.setBackgroundResource(R.drawable.background_topright_active_setting)
                binding.btnNotificationSettings.setTextColor(ContextCompat.getColorStateList(binding.btnPrivateSettings.context,
                    R.color.additional_color
                ))
                binding.notificationsSettings.visibility = View.VISIBLE
            }
            "Приватность" -> {
                binding.btnPrivateSettings.setBackgroundResource(R.drawable.background_bottomleft_active_setting)
                binding.btnPrivateSettings.setTextColor(ContextCompat.getColorStateList(binding.btnPrivateSettings.context,
                    R.color.additional_color
                ))
                binding.privateSettings.visibility = View.VISIBLE
            }
            "Аккаунт" -> {
                binding.btnAccountSettings.setBackgroundResource(R.drawable.background_bottomright_active_setting)
                binding.btnAccountSettings.setTextColor(ContextCompat.getColorStateList(binding.btnPrivateSettings.context,
                    R.color.additional_color
                ))
                binding.accountSettings.visibility = View.VISIBLE
            }
        }
    }

    fun setDisactiveButtons() {
        binding.btnPrivateSettings.setBackgroundResource(R.drawable.background_bottomleft_disactive_setting)
        binding.btnPrivateSettings.setTextColor(ContextCompat.getColorStateList(binding.btnPrivateSettings.context,
            R.color.main_color
        ))
        binding.btnGeneralSettings.setBackgroundResource(R.drawable.background_topleft_disactive_setting)
        binding.btnGeneralSettings.setTextColor(ContextCompat.getColorStateList(binding.btnPrivateSettings.context,
            R.color.main_color
        ))
        binding.btnNotificationSettings.setBackgroundResource(R.drawable.background_topright_disactive_setting)
        binding.btnNotificationSettings.setTextColor(ContextCompat.getColorStateList(binding.btnPrivateSettings.context,
            R.color.main_color
        ))
        binding.btnAccountSettings.setBackgroundResource(R.drawable.background_bottomright_disactive_setting)
        binding.btnAccountSettings.setTextColor(ContextCompat.getColorStateList(binding.btnPrivateSettings.context,
            R.color.main_color
        ))

        binding.accountSettings.visibility = View.GONE
        binding.privateSettings.visibility = View.GONE
        binding.notificationsSettings.visibility = View.GONE
        binding.generalSettings.visibility = View.GONE

    }

    fun showDeleteAccDialog() {
        var deleteDialogBinding = DeleteUserDialogBinding.inflate(layoutInflater)
        var deleteDialog = Dialog(binding.btnAccountSettings.context)
        var etPassConfitm = deleteDialogBinding.etConfitmPassDelacc
        etPassConfitm.etTitleEtcustom.setText("Пароль")
        etPassConfitm.etTextEtcustom.hint = "Ваш пароль"
        etPassConfitm.etTextEtcustom.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        etPassConfitm.chbShowPassEtcustom.visibility = View.VISIBLE
        etPassConfitm.chbShowPassEtcustom.setOnCheckedChangeListener { buttonView, isChecked ->
            val etPass = etPassConfitm.chbShowPassEtcustom
            if (isChecked)
                etPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
            else
                etPass.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        deleteDialogBinding.btnCancelDelacc.setOnClickListener {
            deleteDialog.dismiss()
        }
        deleteDialogBinding.btnDeleteDelacc.setOnClickListener {
            deleteDialog.dismiss()
            deleteAccount()
        }
        deleteDialog.setContentView(deleteDialogBinding.root)
        deleteDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteDialog.setCancelable(false)
        deleteDialog.show()
    }
    fun deleteAccount() {
        var controller = findNavController()
        controller.navigate(R.id.autorizationFragment)
    }

    fun showChangeAvatarDialog() {
        var changeAvatarDialogBinding = ChangeAvatarDialogBinding.inflate(layoutInflater)
        var changeAvatarDialog = Dialog(binding.btnAccountSettings.context)

        changeAvatarDialogBinding.btnApplyChangeavatar.setOnClickListener {
            changeAvatarDialog.dismiss()
            changeAvatar()
        }
        changeAvatarDialogBinding.btnCancelChangeavatar.setOnClickListener {
            changeAvatarDialog.dismiss()
        }
        changeAvatarDialog.setContentView(changeAvatarDialogBinding.root)
        changeAvatarDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        changeAvatarDialog.setCancelable(true)
        changeAvatarDialog.show()
    }
    fun changeAvatar() {

    }

    fun showChangeNicknameDialog() {
        var changeNicknameDialogBinding = ChangeNicknameDialogBinding.inflate(layoutInflater)
        var changeNicknameDialog = Dialog(binding.btnAccountSettings.context)

        var etPassConfitm = changeNicknameDialogBinding.etNewNicknameChangenickname
        etPassConfitm.etTitleEtcustom.setText("Имя пользователя")
        etPassConfitm.etTextEtcustom.hint = "Новое имя"
        etPassConfitm.etTextEtcustom.inputType = InputType.TYPE_CLASS_TEXT

        changeNicknameDialogBinding.btnApplyChangenickname.setOnClickListener {
            changeNicknameDialog.dismiss()
            changeNickname()
        }
        changeNicknameDialogBinding.btnCancelChangenickname.setOnClickListener {
            changeNicknameDialog.dismiss()
        }
        changeNicknameDialog.setContentView(changeNicknameDialogBinding.root)
        changeNicknameDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        changeNicknameDialog.setCancelable(true)
        changeNicknameDialog.show()
    }
    fun changeNickname() {

    }

    fun showChangePassDialog() {
        var changePassDialogBinding = ChangePasswordDialogBinding.inflate(layoutInflater)
        var changePassDialog = Dialog(binding.btnAccountSettings.context)

        var etPassOld = changePassDialogBinding.etOldPassChangepass
        etPassOld.etTitleEtcustom.setText("Подствердите пароль")
        etPassOld.etTextEtcustom.hint = "Старый пароль"
        etPassOld.etTextEtcustom.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        etPassOld.chbShowPassEtcustom.visibility = View.VISIBLE
        etPassOld.chbShowPassEtcustom.setOnCheckedChangeListener { buttonView, isChecked ->
            val etPass = etPassOld.chbShowPassEtcustom
            if (isChecked)
                etPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
            else
                etPass.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        var etPassNew = changePassDialogBinding.etNewPassChangepass
        etPassNew.etTitleEtcustom.setText("Придумайте пароль")
        etPassNew.etTextEtcustom.hint = "Новый пароль"
        etPassNew.etTextEtcustom.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        etPassNew.chbShowPassEtcustom.visibility = View.VISIBLE
        etPassNew.chbShowPassEtcustom.setOnCheckedChangeListener { buttonView, isChecked ->
            val etPass = etPassNew.chbShowPassEtcustom
            if (isChecked)
                etPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
            else
                etPass.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        changePassDialogBinding.btnApplyChangepass.setOnClickListener {
            changePassDialog.dismiss()
            changePass()
        }
        changePassDialogBinding.btnCancelChangepass.setOnClickListener {
            changePassDialog.dismiss()
        }
        changePassDialog.setContentView(changePassDialogBinding.root)
        changePassDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        changePassDialog.setCancelable(true)
        changePassDialog.show()
    }
    fun changePass() {

    }
}