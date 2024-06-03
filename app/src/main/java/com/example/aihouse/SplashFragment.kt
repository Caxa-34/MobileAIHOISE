package com.example.aihouse

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.LoginRequest
import com.example.aihouse.databinding.FragmentSplashBinding
import com.example.aihouse.initializationPerson.Type
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    private val delay: Long = 2000 /// задержка отображения сплеша


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = findNavController()


        Handler(Looper.getMainLooper()).postDelayed({
            authorization()
        }, delay)
    }

    private fun authorization() {
        val sharedPreferences = requireContext().applicationContext.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "") ?: ""
        val username = sharedPreferences.getString("username", "") ?: ""
        val password = sharedPreferences.getString("password", "") ?: ""

        var req = LoginRequest(
            name = username,
            email = email,
            password = password
        )

        Log.e("DATA USER", username + " " + email + " " + password);

        var controller = findNavController()

        lifecycleScope.launch {
            val resultName = ApiHelper.loginNameUser(req)
            resultName.onSuccess { response ->

                if (response.message == "NameExists") {
                    controller.navigate(R.id.autorizationFragment)
                    return@launch
                }
                if (response.message == "InvalidPass") {
                    controller.navigate(R.id.autorizationFragment)
                    return@launch
                }

                if (response.message == "UserFound") {
                    Helper.currentUser = response.userData!!
                    Helper.saveUserData(requireContext(), Helper.currentUser.email, Helper.currentUser.name, req.password)
                    controller.navigate(R.id.mainFragment)
                    return@launch
                }
                controller.navigate(R.id.autorizationFragment)
                return@launch
            }.onFailure { error ->
            }
            val resultEmail = ApiHelper.loginNameUser(req)
            resultEmail.onSuccess { response ->

                if (response.message == "EmailExists") {
                    controller.navigate(R.id.autorizationFragment)
                    return@launch
                }
                if (response.message == "InvalidPass") {
                    controller.navigate(R.id.autorizationFragment)
                    return@launch
                }

                if (response.message == "UserFound") {
                    Helper.currentUser = response.userData!!
                    Helper.saveUserData(requireContext(), Helper.currentUser.email, Helper.currentUser.name, req.password)
                    controller.navigate(R.id.mainFragment)
                    return@launch
                }

                controller.navigate(R.id.autorizationFragment)
                return@launch
            }.onFailure { error ->
            }
        }
    }
}