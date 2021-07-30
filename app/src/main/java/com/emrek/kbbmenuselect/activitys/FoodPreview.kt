package com.emrek.kbbmenuselect.activitys

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.databinding.ActivityFoodPreviewBinding
import com.emrek.kbbmenuselect.models.FoodBag
import com.emrek.kbbmenuselect.models.FoodModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*


class FoodPreview : BottomSheetDialogFragment() {

    private lateinit var binding: ActivityFoodPreviewBinding
    var count = 1

    var bags = GetFoods().getBags()
    var likeFoods = GetFoods().getLikeFoods()


    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ActivityFoodPreviewBinding.inflate(layoutInflater)


        var data = arguments?.getParcelable<FoodModel>("data")

        getLikeFoodId(data?.foodID!!)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarBackground(data?.foodColor.toString())
        }

        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(data?.foodPicture))
        imageList.add(SlideModel(data?.foodPicture))
        imageList.add(SlideModel(data?.foodPicture))


        setBackground(binding.imageSlider, data?.foodColor.toString())
        setBackground(binding.relativeLayout, data?.foodColor.toString())


        if (data?.drinkLiter.toString() == "null")
            binding.foodName.text = data?.foodName
        else
            binding.foodName.text = "${data?.foodName} ${data?.drinkLiter}"

        binding.foodDescription.text = data?.foodDescription
        binding.foodPrice.text = "₺" + data?.foodPrice?.replace('.', ',')


        buttonClick(data!!)
        getBagsLength()


        binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_INSIDE)


        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    private fun buttonClick(data: FoodModel) {


        binding.addButton.setOnClickListener {
            ++count
            if (count < 10)
                binding.foodPiece.text = "0" + count
            else
                binding.foodPiece.text = "" + count

            priceUpdate(count, data?.foodPrice?.toFloatOrNull())

        }
        binding.removeButton.setOnClickListener {
            if (count > 1)
                --count

            if (count < 10)
                binding.foodPiece.text = "0" + count
            else
                binding.foodPiece.text = "" + count


            priceUpdate(count, data?.foodPrice?.toFloatOrNull())

        }
        binding.backButton.setOnClickListener {
            this.dismiss()
        }

        binding.heartButton.setOnClickListener {
            if (GetFoods().isAuth())
                if (it.tag != "reverse") {
                    it.background =
                        activity?.getDrawable(R.drawable.bottom_button_heart_reverse_background)
                    it.setTag("reverse")
                    binding.heartButton.setImageResource(R.drawable.heart_reverse)
                    addLikeFoods(data)
                } else {
                    it.background =
                        activity?.getDrawable(R.drawable.bottom_button_heart_background)
                    it.setTag("normal")
                    binding.heartButton.setImageResource(R.drawable.heart)
                    removeLikeFoods(data)
                }
            else {
                val intent = Intent(activity, LoginActivity::class.java)
                intent.putExtra("info", "Öncelikle giriş yapmalısınız *")
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }

        binding.addBag.setOnClickListener {
            if (GetFoods().isAuth())
                if (it.tag != "reverse") {
                    it.setTag("reverse")
                    binding.addBag.text = "Sepete eklendi"
                    addBag(data)

                } else {
                    it.setTag("normal")
                    binding.addBag.text = "Sepete ekle"

                    removeBag(data)
                }
            else {
                val intent = Intent(activity, LoginActivity::class.java)
                intent.putExtra("info", "Öncelikle giriş yapmalısınız *")
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

        }


        binding.shopButton.setOnClickListener {

            val intent: Intent?
            if (GetFoods().isAuth()) {
                intent = Intent(activity, OrderActivity::class.java)
            } else {
                intent = Intent(activity, LoginActivity::class.java)
                intent.putExtra("info", "Öncelikle giriş yapmalısınız *")
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

    }

    fun addLikeFoods(data: FoodModel) {
        likeFoods.child(data.foodID.toString()).setValue(data)
    }

    fun removeLikeFoods(data: FoodModel) {
        likeFoods.child(data.foodID.toString()).removeValue()
    }

    fun getLikeFoodId(id: String) {

        var listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                binding.heartButton.background =
                    activity?.getDrawable(R.drawable.bottom_button_heart_background)
                binding.heartButton.setImageResource(R.drawable.heart)
                binding.heartButton.setTag("normal")
                dataSnapshot.children.forEach {
                    if (id.equals(it.child("foodID").value.toString())) {
                        binding.heartButton.background =
                            activity?.getDrawable(R.drawable.bottom_button_heart_reverse_background)
                        binding.heartButton.setImageResource(R.drawable.heart_reverse)
                        binding.heartButton.setTag("reverse")
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        likeFoods.addValueEventListener(listener)

    }

    fun addBag(data: FoodModel) {

        val foodBag = FoodBag(
            data.foodName,
            data.foodPicture,
            data.foodPrice,
            data.foodColor,
            data.drinkLiter,
            data.foodID
        )
        foodBag.bagFoodPiece = binding.foodPiece.text.toString()
        foodBag.bagTotalPrice =
            (foodBag.bagFoodPiece.toString().toInt() * data.foodPrice?.toFloat()!!).toString()


        GetFoods().addToBag(foodBag)

    }

    fun removeBag(data: FoodModel) {
        bags.child(data.foodID.toString()).removeValue()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.window?.statusBarColor = Color.TRANSPARENT
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        activity?.window?.statusBarColor = Color.TRANSPARENT
    }

    fun priceUpdate(count: Int?, price: Float?) {


        if (count != null && price != null) {
            var sumPrice = (count?.times(price!!) ?: Float)
            sumPrice = "₺" + "%.2f".format(sumPrice)
            binding.foodPrice.text = sumPrice.replace('.', ',')
        }


    }

    fun setBackground(view: View, color: String) {

        when (color) {

            "lightGreen" -> {
                view.setBackgroundColor(Color.parseColor("#89E18D"))
            }
            "darkGreen" -> {
                view.setBackgroundColor(Color.parseColor("#4EE455"))
            }

            "lightBlue" -> {
                view.setBackgroundColor(Color.parseColor("#9FAEFF"))
            }
            "darkBlue" -> {
                view.setBackgroundColor(Color.parseColor("#4F6AFD"))
            }

            "lightRed" -> {
                view.setBackgroundColor(Color.parseColor("#FF8484"))
            }
            "darkRed" -> {
                view.setBackgroundColor(Color.parseColor("#FF4747"))
            }

            "lightYellow" -> {
                view.setBackgroundColor(Color.parseColor("#FAEC77"))
            }
            "darkYellow" -> {
                view.setBackgroundColor(Color.parseColor("#DCCB41"))
            }

            "lightOrange" -> {
                view.setBackgroundColor(Color.parseColor("#FFD08A"))
            }
            "darkOrange" -> {
                view.setBackgroundColor(Color.parseColor("#FFBF5E"))
            }

            "lightBrown" -> {
                view.setBackgroundColor(Color.parseColor("#937954"))
            }
            "darkBrown" -> {
                view.setBackgroundColor(Color.parseColor("#916A31"))
            }

            "lightGray" -> {
                view.setBackgroundColor(Color.parseColor("#D6D6D6"))
            }
            "darkGray" -> {
                view.setBackgroundColor(Color.parseColor("#B1B1B1"))
            }

            "lightPurple" -> {
                view.setBackgroundColor(Color.parseColor("#EEBCF6"))
            }
            "darkPurple" -> {
                view.setBackgroundColor(Color.parseColor("#C385CD"))
            }


            else -> {
                view.setBackgroundColor(Color.parseColor("#D6D6D6"))
            }
        }

    }

    fun setStatusBarBackground(color: String) {

        when (color) {

            "lightGreen" -> {
                activity?.window?.statusBarColor = Color.parseColor("#89E18D")

            }
            "darkGreen" -> {
                activity?.window?.statusBarColor = Color.parseColor("#4EE455")
            }

            "lightBlue" -> {
                activity?.window?.statusBarColor = Color.parseColor("#9FAEFF")
            }
            "darkBlue" -> {
                activity?.window?.statusBarColor = Color.parseColor("#4F6AFD")
            }

            "lightRed" -> {
                activity?.window?.statusBarColor = Color.parseColor("#FF8484")
            }
            "darkRed" -> {
                activity?.window?.statusBarColor = Color.parseColor("#FF4747")
            }

            "lightYellow" -> {
                activity?.window?.statusBarColor = Color.parseColor("#FAEC77")
            }
            "darkYellow" -> {
                activity?.window?.statusBarColor = Color.parseColor("#DCCB41")
            }

            "lightOrange" -> {
                activity?.window?.statusBarColor = Color.parseColor("#FFD08A")
            }
            "darkOrange" -> {
                activity?.window?.statusBarColor = Color.parseColor("#FFBF5E")
            }

            "lightBrown" -> {
                activity?.window?.statusBarColor = Color.parseColor("#937954")
            }
            "darkBrown" -> {
                activity?.window?.statusBarColor = Color.parseColor("#916A31")
            }

            "lightGray" -> {
                activity?.window?.statusBarColor = Color.parseColor("#D6D6D6")
            }
            "darkGray" -> {
                activity?.window?.statusBarColor = Color.parseColor("#B1B1B1")
            }

            "lightPurple" -> {
                activity?.window?.statusBarColor = Color.parseColor("#EEBCF6")
            }
            "darkPurple" -> {
                activity?.window?.statusBarColor = Color.parseColor("#C385CD")
            }


            else -> {
                activity?.window?.statusBarColor = Color.parseColor("#D6D6D6")
            }
        }

    }


    fun getBagsLength() {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var listSize = 0
                dataSnapshot.children.forEach {
                    listSize++
                }
                binding.bagCount.text = listSize.toString()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        bags.addValueEventListener(listener)
    }

}




