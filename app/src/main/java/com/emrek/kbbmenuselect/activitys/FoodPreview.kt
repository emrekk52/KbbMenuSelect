package com.emrek.kbbmenuselect.activitys

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.databinding.ActivityFoodPreviewBinding
import com.emrek.kbbmenuselect.models.CommentModel
import com.emrek.kbbmenuselect.models.FoodBag
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.viewmodels.FoodPreviewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList


class FoodPreview : BottomSheetDialogFragment() {

    private lateinit var binding: ActivityFoodPreviewBinding
    var count = 1
    lateinit var data: FoodModel
    lateinit var viewModel: FoodPreviewModel
    private lateinit var list: ArrayList<CommentModel>

    var bags = GetFoods().getBags()
    var likeFoods = GetFoods().getLikeFoods()


    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ActivityFoodPreviewBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(requireActivity()).get(FoodPreviewModel::class.java)

        data = arguments?.getParcelable<FoodModel>("data")!!

        if (data?.isOffer == true)
            binding.offerLayout.visibility = View.VISIBLE

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

        binding.deliveryTime.text = "Yaklaşık ${data?.deliveryTime} dakika içerisinde hazır!"
        binding.foodCalory.text = "1 adet ${data?.foodCalory}joule(${data?.foodGram})"

        buttonClick(data!!)
        getBagsLength()
        viewModelCheck()


        binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_INSIDE)


        return binding.root
    }


    fun viewModelCheck() {

        if (GetFoods().isAuth()) {
            binding.commentTextLayout.visibility = View.VISIBLE
        } else {
            binding.commentTextLayout.visibility = View.GONE
            binding.ratingBar.visibility = View.GONE
            binding.commentButton.visibility = View.GONE
            binding.commentText.text = "Yorum yapmak için giriş yap"
            binding.commentText.setTextColor(Color.parseColor("#5A5656"))
        }

        GetFoods().getComments(viewModel, data.foodID.toString())

        viewModel.getComments().observe(requireActivity(), androidx.lifecycle.Observer {
            list = it
        })

        viewModel.getCommentCount().observe(requireActivity(), androidx.lifecycle.Observer {

            if (it > 0) {
                binding.commentCountLayout.visibility = View.VISIBLE
                binding.commentCount.text = it.toString() + " adet"
                binding.commentGo.text = "yorum"
                binding.commentEnd.text = "bulundu"
            } else
                binding.commentCountLayout.visibility = View.GONE


        })

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
            if (GetFoods().isLogin(requireActivity()))
                if (it.tag != "reverse") {
                    it.background =
                        activity?.getDrawable(R.drawable.bottom_button_heart_reverse_background)
                    it.setTag("reverse")
                    binding.heartButton.setImageResource(R.drawable.heart_reverse)
                    addLikeFoods(data)
                    if (data.food_likes.toString() == "null")
                        data.food_likes = 1
                    else
                        data.food_likes = data.food_likes!!.toInt() + 1
                    GetFoods().updateFood(data)
                } else {
                    it.background =
                        activity?.getDrawable(R.drawable.bottom_button_heart_background)
                    it.setTag("normal")
                    binding.heartButton.setImageResource(R.drawable.heart)
                    removeLikeFoods(data)
                    if (data.food_likes.toString() != "null")
                        data.food_likes = data.food_likes!!.toInt() - 1

                    GetFoods().updateFood(data)
                }

        }

        binding.addBag.setOnClickListener {
            if (GetFoods().isLogin(requireActivity()))
                if (it.tag != "reverse") {
                    it.setTag("reverse")
                    binding.addBag.text = "Sepete eklendi"
                    addBag(data)

                } else {
                    it.setTag("normal")
                    binding.addBag.text = "Sepete ekle"

                    removeBag(data)
                }


        }


        binding.shopButton.setOnClickListener {

            if (GetFoods().isLogin(requireActivity())) {
                val intent = Intent(activity, OrderActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

        }

        binding.commentEdittext.doOnTextChanged { text, start, before, count ->
            if (text?.trim()?.length!! > 0 && binding.ratingBar.rating > 0) {
                binding.commentButton.isEnabled = true
                binding.commentButton.setTextColor(Color.parseColor("#019C09"))
            } else {
                binding.commentButton.isEnabled = false
                binding.commentButton.setTextColor(Color.parseColor("#80AC82"))
            }
        }


        binding.ratingBar.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            if (binding.commentEdittext.text?.trim()?.length!! > 0 && rating > 0) {
                binding.commentButton.isEnabled = true
                binding.commentButton.setTextColor(Color.parseColor("#019C09"))
            } else {
                binding.commentButton.isEnabled = false
                binding.commentButton.setTextColor(Color.parseColor("#80AC82"))
            }

        }

        binding.commentButton.setOnClickListener {

            GetFoods().setComments(
                CommentModel(
                    binding.commentEdittext.text.toString(),
                    "",
                    "",
                    "",
                    data.foodID.toString(),
                    binding.ratingBar.rating
                ), it
            )

            binding.commentEdittext.text?.clear()
            binding.ratingBar.rating = 0f

        }

        binding.commentGo.setOnClickListener {

            if (list.size > 0) {
                val intent = Intent(requireActivity(), CommentActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.putParcelableArrayListExtra("comment", list)
                intent.putExtra("food_name", data.foodName.toString())
                startActivity(intent)
            }

        }



        binding.foodDescription.setOnClickListener {

            if (binding.foodDescription.ellipsize != null) {
                binding.foodDescription.ellipsize = null
                binding.foodDescription.maxLines = Integer.MAX_VALUE
            } else {
                binding.foodDescription.ellipsize = TextUtils.TruncateAt.END
                binding.foodDescription.maxLines = 4
            }

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


        GetFoods().addToBag(requireActivity(), foodBag)

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






