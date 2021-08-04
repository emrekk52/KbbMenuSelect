package com.emrek.kbbmenuselect.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.activitys.LoginActivity
import com.emrek.kbbmenuselect.activitys.MyCreditCardActivity
import com.emrek.kbbmenuselect.databinding.FragmentProfileBinding
import com.emrek.kbbmenuselect.models.ProfileModel
import com.emrek.kbbmenuselect.viewmodels.ProfileViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import java.util.jar.Manifest


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var collapse = false
    lateinit var my_profile: ProfileModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater)

        buttonSetup()
        viewModelLiveDataSetup()




        return binding.root
    }

    fun buttonSetup() {
        binding.profileInclude.myCard.setOnClickListener {
            if (!collapse) {
                binding.profileInclude.next.rotation = 90f
                binding.profileInclude.altBar.visibility = View.VISIBLE
                collapse = true
            } else {
                binding.profileInclude.next.rotation = 0f
                binding.profileInclude.altBar.visibility = View.GONE
                collapse = false
            }


        }

        binding.profileInclude.updateButton.setOnClickListener {

            if (binding.profileInclude.nameTextInput.length() > 0)
                my_profile.nameAndSurname = binding.profileInclude.nameTextInput.text.toString()


            if (binding.profileInclude.phoneTextInput.length() > 4)
                my_profile.phone = binding.profileInclude.phoneTextInput.text.toString()


            GetFoods().updateProfile(my_profile)
        }

        binding.profileInclude.userPicture.setOnClickListener {

            ImagePicker.with(requireActivity()).compress(1024).crop().createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
        }


        binding.loginInclude.loginButton.setOnClickListener {

            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.putExtra("info", "Devam etmek için giriş yapınız!")
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

        }


        binding.signoutButton.setOnClickListener {
            GetFoods().signOut(requireActivity())
        }


        binding.cartButton.setOnClickListener {

            if (GetFoods().isLogin(requireActivity())) {
                val intent = Intent(requireActivity(), MyCreditCardActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

        }


    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                GetFoods().uploadProfileImage(fileUri, requireView(), my_profile)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "İşlem iptal edildi!", Toast.LENGTH_SHORT).show()
            }
        }


    fun viewModelLiveDataSetup() {


        val viewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        GetFoods().getProfileInfo(viewModel)
        GetFoods().getMyLikeFoodCount(viewModel)
        GetFoods().getOrderFoodCount(viewModel)
        GetFoods().getIsAuthProfile(viewModel)

        var sharedPr =
            requireActivity().getSharedPreferences(
                requireActivity().packageName,
                Context.MODE_PRIVATE
            )

        viewModel.getProfile().observe(requireActivity(), Observer {
            my_profile = it
            binding.profileInclude.nameTextInputLayout.hint = it.nameAndSurname.toString()
            binding.profileInclude.phoneTextInputLayout.hint = "+90 " + it.phone.toString()
            binding.profileInclude.userName.text = it.nameAndSurname.toString()
            binding.profileInclude.userEmail.text = it.email.toString()
            Picasso.get().load(it.profilePhotoUrl.toString()).placeholder(R.drawable.plus_b)
                .into(binding.profileInclude.userPicture)
            sharedPr.edit().putString("user_name", it.nameAndSurname.toString()).apply()
        })

        viewModel.getLikeCount().observe(requireActivity(), Observer {
            binding.profileInclude.counterLike.text = it.toString()
        })

        viewModel.getOrderCount().observe(requireActivity(), Observer {
            binding.profileInclude.orderCounter.text = it.toString()
        })

        viewModel.getAuth().observe(requireActivity(), Observer {

            if (it == true) {
                binding.loginInclude.loginLayout.visibility = View.GONE
                binding.profileInclude.profile.visibility = View.VISIBLE
                binding.optionsLayout.visibility = View.VISIBLE

            } else {
                binding.profileInclude.profile.visibility = View.GONE
                binding.optionsLayout.visibility = View.GONE
                binding.loginInclude.loginLayout.visibility = View.VISIBLE

            }

        })

    }

}