package com.emrek.kbbmenuselect.activitys

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.databinding.ActivityMyCreditCardBinding
import com.emrek.kbbmenuselect.models.DebitCardModel
import com.emrek.kbbmenuselect.viewmodels.DebitCardViewModel

class MyCreditCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyCreditCardBinding
    private lateinit var viewModel: DebitCardViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCreditCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DebitCardViewModel::class.java)

        val message = intent.getStringExtra("message")

        if (!message.isNullOrEmpty()) {
            binding.infoBar.visibility = View.VISIBLE
            binding.infoBar.text = message
        }

        getCard()

        buttonSetup()

    }


    fun getCard() {
        GetFoods().getCard(viewModel)
        val sharedPr = getSharedPreferences(packageName, Context.MODE_PRIVATE)

        viewModel.getDebitCard().observe(this, Observer {
            if (!it.isNullOrEmpty() && it.size > 0) {
                binding.cardBack.cardCvv.text = it[0].cardCVV
                binding.cardFront.bankName.text = it[0].bankName
                binding.cardFront.cardNumber.text = it[0].cardNumber
                binding.cardFront.cardSkt.text = it[0].cardSKT
                binding.cardFront.cardName.text = sharedPr.getString("user_name", "")
            }
        })
    }

    fun buttonSetup() {
        binding.addCard.setOnClickListener {

            if (!binding.input.bankName.text.toString().isNullOrEmpty() &&
                !binding.input.cardCvv.text.toString().isNullOrEmpty() &&
                !binding.input.cardNumber.text.toString().isNullOrEmpty() &&
                !binding.input.cardSkt.text.toString().isNullOrEmpty()
            ) {
                GetFoods().setCard(
                    DebitCardModel(
                        binding.input.cardNumber.text.toString(),
                        binding.input.cardCvv.text.toString(),
                        binding.input.cardSkt.text.toString(),
                        binding.input.bankName.text.toString(),
                        ""
                    ),
                    it
                )

                binding.input.cardCvv.text.clear()
                binding.input.cardNumber.text?.clear()
                binding.input.cardSkt.text?.clear()
                binding.input.bankName.text.clear()

            }

        }

        binding.back.setOnClickListener { finish() }

    }

}