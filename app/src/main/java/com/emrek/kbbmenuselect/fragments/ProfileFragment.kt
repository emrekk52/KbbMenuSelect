package com.emrek.kbbmenuselect.fragments

import android.app.Activity
import android.content.Intent
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

    private val REQUEST_CODE_STORAGE_PERMISSION = 1

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
        binding.myCard.setOnClickListener {
            if (!collapse) {
                binding.next.rotation = 90f
                binding.altBar.visibility = View.VISIBLE
                collapse = true
            } else {
                binding.next.rotation = 0f
                binding.altBar.visibility = View.GONE
                collapse = false
            }


        }

        binding.updateButton.setOnClickListener {

            if (binding.nameTextInput.length() > 0)
                my_profile.nameAndSurname = binding.nameTextInput.text.toString()


            if (binding.phoneTextInput.length() > 4)
                my_profile.phone = binding.phoneTextInput.text.toString()


            GetFoods().updateProfile(my_profile)
        }

        binding.userPicture.setOnClickListener {

            ImagePicker.with(requireActivity()).compress(1024).crop().createIntent { intent ->
                startForProfileImageResult.launch(intent)
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

        viewModel.getProfile().observe(requireActivity(), Observer {
            my_profile = it
            binding.nameTextInputLayout.hint = it.nameAndSurname.toString()
            binding.phoneTextInputLayout.hint = "+90 " + it.phone.toString()
            binding.userName.text = it.nameAndSurname.toString()
            binding.userEmail.text = it.email.toString()
            Picasso.get().load(it.profilePhotoUrl.toString()).placeholder(R.drawable.plus_b)
                .into(binding.userPicture)

        })

        viewModel.getLikeCount().observe(requireActivity(), Observer {
            binding.counterLike.text = it.toString()
        })


    }

}