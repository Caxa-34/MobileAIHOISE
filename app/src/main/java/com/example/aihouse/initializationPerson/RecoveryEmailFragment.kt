package com.example.aihouse.initializationPerson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.aihouse.R
import com.example.aihouse.databinding.FragmentRecoveryEmailBinding

class RecoveryEmailFragment : Fragment() {
    private lateinit var binding: FragmentRecoveryEmailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecoveryEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var contoller = findNavController()

        binding.btnSendCodeRecoveryEmail.setOnClickListener {
            contoller.navigate(R.id.recoveryCodeFragment)
        }
        binding.btnToAutorizationRecoveryEmail.setOnClickListener {
            contoller.navigate(R.id.autorizationFragment)
        }
    }
}