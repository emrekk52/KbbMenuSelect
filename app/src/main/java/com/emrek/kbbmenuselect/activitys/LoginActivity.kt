package com.emrek.kbbmenuselect.activitys

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.databinding.FragmentLoginBinding


class LoginActivity : AppCompatActivity() {


    private lateinit var binding: FragmentLoginBinding
    private var toggle = 1
    private var isEmailTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = window

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, 1)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, 1)
            window.statusBarColor = Color.TRANSPARENT
        }

        val infox = intent.getStringExtra("info")

        if (!infox.isNullOrEmpty()) {
            binding.info.visibility = View.VISIBLE
            binding.info.text = infox
        }

        inputSetup()


    }

    fun inputSetup() {
        binding.textEmailEditText.doOnTextChanged { text, start, before, count ->
            if (text!!.contains('@') && text!!.contains('.') && text!!.length > 7 || text.length == 0) {
                isEmailTrue = true
                binding.textEmailLayout.error = null
            } else {
                isEmailTrue = false
                binding.textEmailLayout.error = "* e-posta istenilen formatta değil!"
            }

        }

        binding.txtSignup.setOnClickListener {
            toggle = 2
            binding.txtSignup.setTextColor(Color.BLACK)
            binding.txtLogin.setTextColor(Color.parseColor("#B1B1B1"))
            binding.textNameLayout.visibility = View.VISIBLE
            binding.textPhoneLayout.visibility = View.VISIBLE
            binding.selectorLogin.visibility = View.GONE
            binding.selectorSignup.visibility = View.VISIBLE
            binding.loginButton.text = "Kayıt ol"
        }

        binding.txtLogin.setOnClickListener {
            toggle = 1
            binding.txtLogin.setTextColor(Color.BLACK)
            binding.txtSignup.setTextColor(Color.parseColor("#B1B1B1"))
            binding.textNameLayout.visibility = View.GONE
            binding.textPhoneLayout.visibility = View.GONE
            binding.selectorLogin.visibility = View.VISIBLE
            binding.selectorSignup.visibility = View.GONE
            binding.loginButton.text = "Giriş yap"
        }

        binding.exit.setOnClickListener {
            finish()
        }

        binding.loginButton.setOnClickListener {

            val email = binding.textEmailEditText.text.toString()
            val password = binding.textPasswordEditText.text.toString()
            val name = binding.textNameEditText.text.toString()
            val phone = binding.textPhoneEditText.text.toString()


            if (toggle == 1) {
                if (!email.isNullOrEmpty() && !password.isNullOrEmpty() && isEmailTrue)
                    GetFoods().signInWithEmailAndPassword(
                        email,
                        password,
                        this@LoginActivity,
                        it
                    )


            } else if (toggle == 2) {
                if (!email.isNullOrEmpty() && !password.isNullOrEmpty() && !name.isNullOrEmpty() && !phone.isNullOrEmpty() && isEmailTrue)
                    GetFoods().createUserWithEmailAndPassword(
                        name, email, phone,
                        password,
                        this@LoginActivity,
                        it
                    )


            }

        }


        binding.resetButton.setOnClickListener {

            GetFoods().resetPassword(binding.textEmailEditText.text.toString(), it)
        }

    }

}




