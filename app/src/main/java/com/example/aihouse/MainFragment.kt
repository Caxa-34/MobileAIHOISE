package com.example.aihouse

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.aihouse.api.ApiHelper
import com.example.aihouse.api.RegisterRequest
import com.example.aihouse.api.UserRequest
import com.example.aihouse.databinding.ActionsRightPanelBinding
import com.example.aihouse.databinding.FragmentMainBinding
import com.example.aihouse.databinding.PersonLeftPanelBinding
import com.example.aihouse.discussions.DiscussionsFeedFragment
import com.example.aihouse.discussions.MyDiscussionsFragment
import com.example.aihouse.person.MySubscriptionsFragment
import com.example.aihouse.person.SettingsFragment
import com.example.aihouse.publications.MyPublicationsFragment
import com.example.aihouse.publications.PublicationsFeedFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var bindingPerson: PersonLeftPanelBinding
    private lateinit var bindingActions: ActionsRightPanelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        bindingPerson = binding.personLeftPanel
        bindingActions = binding.actionsRightPanel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var controller = findNavController()

        // правая панель
        bindingActions.btnCreatePublicationRightpanel.setOnClickListener {
            controller.navigate(R.id.createPublicationFragment)
        }
        bindingActions.btnCreateDiscussionRightpanel.setOnClickListener {
                controller.navigate(R.id.createDiscussionFragment)
        }
        bindingActions.btnSettingsRightpanel.setOnClickListener {
            controller.navigate(R.id.settingsFragment)
        }
        bindingActions.txtSupportEmailRightpanel.setOnClickListener {
            Helper.saveToBuffer(requireContext(), "ai.house@inbox.ru")
            Toast.makeText(context, "Почта скопирована!", Toast.LENGTH_SHORT).show();
        }

        // левая панель
        bindingPerson.btnCopyIdPersonLeftpanel.setOnClickListener {
            val myID = Helper.currentUser.id.toString()
            Helper.saveToBuffer(requireContext(), myID)
            Toast.makeText(context, "ID скопирован!", Toast.LENGTH_SHORT).show();
        }
        bindingPerson.btnNotificationLeftpanel.setOnClickListener {
            controller.navigate(R.id.notificationFragment)
        }
        bindingPerson.btnRulesForumLeftpanel.setOnClickListener {
            showRules()
        }
        bindingPerson.btnExitAccountLeftpanel.setOnClickListener {
            showExitDialog()
        }

        // открытие панелей
        binding.imgAvatarMain.setOnClickListener {
            binding.drawerMain.openDrawer(GravityCompat.START)
        }
        binding.btnMenuMain.setOnClickListener {
            binding.drawerMain.openDrawer(GravityCompat.END)
        }

        // переходы между нижней навигацией
        val publicationsFeed = PublicationsFeedFragment()
        val discussionsFeed = DiscussionsFeedFragment()
        val myPublications = MyPublicationsFragment()
        val myDiscussons = MyDiscussionsFragment()
        val mySubscriptions = MySubscriptionsFragment()
        binding.navBottomMenuMain.setOnItemSelectedListener { item ->
            //Toast.makeText(context, "ChangingFeed", Toast.LENGTH_SHORT).show()
            when (item.itemId) {
                R.id.navigation_discussions -> {
                    setCurrentFeed(discussionsFeed)
                    binding.txtTitleMain.setText(discussionsFeed.title)
                    //Log.e("ChangeFeed", "ToDiscussions")
                    true
                }
                R.id.navigation_publications -> {
                    setCurrentFeed(publicationsFeed)
                    binding.txtTitleMain.setText(publicationsFeed.title)
                    //Log.e("ChangeFeed", "ToPublication")
                    true
                }
                else -> false
            }
        }

        bindingPerson.btnMyPublicationsLeftpanel.setOnClickListener {
            binding.drawerMain.closeDrawer(GravityCompat.START)
            setCurrentFeed(myPublications)
            binding.txtTitleMain.setText(myPublications.title)
            binding.navBottomMenuMain.uncheckAllItems()
        }
        bindingPerson.btnMyDiscussionsLeftpanel.setOnClickListener {
            binding.drawerMain.closeDrawer(GravityCompat.START)
            setCurrentFeed(myDiscussons)
            binding.txtTitleMain.setText(myDiscussons.title)
            binding.navBottomMenuMain.uncheckAllItems()
        }
        bindingPerson.btnMySubscriptionsLeftpanel.setOnClickListener {
            binding.drawerMain.closeDrawer(GravityCompat.START)
            setCurrentFeed(mySubscriptions)
            binding.txtTitleMain.setText(mySubscriptions.title)
            binding.navBottomMenuMain.uncheckAllItems()
        }

        var act = arguments?.getString("act")
        binding.drawerMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        setCurrentFeed(publicationsFeed)

        if (act == "openRightPanel")
            binding.drawerMain.openDrawer(GravityCompat.END)
        if (act == "openLeftPanel")
            binding.drawerMain.openDrawer(GravityCompat.START)

        binding.imgNotifMain.visibility = View.GONE

        showUserData()
    }

    private fun showUserData() {
        bindingPerson.txtNicknameLeftpanel.setText(Helper.currentUser.name)
        bindingPerson.txtIdPersonLeftpanel.setText("ID: " + Helper.currentUser.id)
        Log.e("USER", Helper.currentUser.toString())
        ApiHelper.loadImage(requireContext(), Helper.currentUser.imagePath!!, binding.imgAvatarMain)
        ApiHelper.loadImage(requireContext(), Helper.currentUser.imagePath!!, bindingPerson.imgAvatarLeftpanel)
        var req = UserRequest(
            idUser = Helper.currentUser.id
        )
        lifecycleScope.launch {
            val result = ApiHelper.getMyNotifs(req)
            result.onSuccess { response ->
                Helper.notifications = response.notifications!!

                val readntCount = Helper.notifications.count { !it.wasRead!! }

                binding.imgNotifMain.visibility = View.VISIBLE
                bindingPerson.txtNotifLeftpanel.setText(readntCount.toString())
                if (readntCount == 0) {
                    bindingPerson.imgNotifLeftpanel.setImageResource(R.drawable.ic_no_notification)
                    bindingPerson.txtNotifLeftpanel.setText("")
                    binding.imgNotifMain.visibility = View.GONE
                }

            }.onFailure { error ->
                Log.e("ERROR NOTIF", "Error: ${error.message}")
            }
        }
    }

    private fun setCurrentFeed(fragment: Fragment) =
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.navContainer_main, fragment)
            commit()
        }

    fun BottomNavigationView.uncheckAllItems() {
        menu.setGroupCheckable(0, true, false)
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
        menu.setGroupCheckable(0, true, true)
    }

    fun showRules() {
        var rulesDialogBinding = layoutInflater.inflate(R.layout.rules_dialog, null)
        var rulesDialog = Dialog(bindingPerson.btnRulesForumLeftpanel.context)

        rulesDialogBinding.findViewById<CheckBox>(R.id.chbAgree_rules).visibility = View.GONE
        rulesDialogBinding.findViewById<Button>(R.id.btnPositiveResult_rules).setText("Ок")
        rulesDialogBinding.findViewById<Button>(R.id.btnPositiveResult_rules).setOnClickListener {
            rulesDialog.dismiss()
        }
        val txtRules = Helper.rules.joinToString(separator = "\n\n") { it.text }
        rulesDialogBinding.findViewById<TextView>(R.id.txtRules_ruleagree).setText(txtRules)

        rulesDialog.setContentView(rulesDialogBinding)
        rulesDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        rulesDialog.show()
    }

    fun showExitDialog() {
        var exitDialogBinding = layoutInflater.inflate(R.layout.exit_account_dialog, null)
        var exitDialog = Dialog(bindingPerson.btnRulesForumLeftpanel.context)
        exitDialogBinding.findViewById<Button>(R.id.btnCancel_exitacc).setOnClickListener {
            exitDialog.dismiss()
        }
        exitDialogBinding.findViewById<Button>(R.id.btnExit_exitacc).setOnClickListener {
            exitDialog.dismiss()
            exitAccount()
        }
        exitDialog.setContentView(exitDialogBinding)
        exitDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        exitDialog.show()
    }

    fun exitAccount() {
        var controller = findNavController()
        controller.navigate(R.id.autorizationFragment)
    }

}