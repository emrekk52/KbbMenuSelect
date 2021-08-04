package com.emrek.kbbmenuselect

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import com.denzcoskun.imageslider.models.SlideModel
import com.emrek.kbbmenuselect.activitys.LoginActivity
import com.emrek.kbbmenuselect.activitys.MainActivity
import com.emrek.kbbmenuselect.models.*
import com.emrek.kbbmenuselect.viewmodels.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.mindrot.jbcrypt.BCrypt
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class GetFoods() {


    private var auth: FirebaseAuth = Firebase.auth


    fun getStorage(): StorageReference {
        return FirebaseStorage.getInstance("gs://menuapp-28717.appspot.com").getReference()
    }


    fun getFood(referencePath: String): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference(referencePath)
    }


    private var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference()

    fun getDatabase(): DatabaseReference {
        return database
    }


    fun getFruits(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("fruits")
    }

    fun getCommentsReference(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("comments")
    }


    fun getProfile(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("profiles")
    }


    fun getVegetables(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("vegetables")
    }

    fun getHotDrink(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("hotDrink")
    }


    fun getColdDrink(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("coldDrink")
    }


    fun getAuth(): FirebaseAuth {
        return auth
    }


    fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        phone: String,
        password: String,
        activity: Activity,
        view: View
    ) {
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty())
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        setProfile(ProfileModel(name, email, phone, auth.currentUser?.uid))
                        signInWithEmailAndPassword(email, password, activity, view)
                    } else
                        Snackbar.make(
                            view,
                            "Giriş yapılamadı: " + task.exception.toString(),
                            Snackbar.LENGTH_SHORT
                        ).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()


                }

    }


    fun isAuth(): Boolean {

        if (getAuth().currentUser != null)
            return true
        else
            return false

    }


    fun isLogin(activity: Activity): Boolean {

        if (getAuth().currentUser != null)
            return true
        else {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("info", "Öncelikle giriş yapmalısınız *")
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(intent)
            return false
        }


    }


    fun getIsAuthProfile(viewModel: ProfileViewModel) {
        if (getAuth().currentUser != null)
            viewModel.setAuth(true)
        else
            viewModel.setAuth(false)
    }

    fun signOut(activity: Activity) {
        if (isAuth()) {
            getAuth().signOut()
            val sharedPr = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
            sharedPr.edit().putString("user_name", "").apply()
            val intent = Intent(activity, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    fun addToBag(activity: Activity, foodBag: FoodBag) {
        if (isLogin(activity))
            database.child("bags").child(auth.uid.toString()).child(foodBag.foodID.toString())
                .setValue(foodBag)

    }


    fun setFoods(model: FoodModel): DatabaseReference {

        model.foodID = generateID()
        model.isOffer = true
        model.deliveryTime = "4"
        model.foodCalory = "700"
        model.food_likes = 19
        model.foodGram = "1,2kg-1,5kg"
        database.child(model.foodCategory.toString()).child(model.foodName.toString())
            .setValue(
                model
            ) { databaseError: DatabaseError?, databaseReference: DatabaseReference ->
                Log.d("databaseError", databaseError.toString())
            }

        return database
    }


    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        activity: Activity,
        view: View
    ) {
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty())
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra(
                            "isLogin",
                            getAuth().currentUser?.email + " oturumuna giriş yapıldı"
                        )
                        activity.startActivity(intent)
                        activity.finish()
                    } else
                        Snackbar.make(
                            view,
                            "Giriş yapılamadı: " + task.exception.toString(),
                            Snackbar.LENGTH_SHORT
                        ).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()


                }

    }

    fun setProfile(profileModel: ProfileModel) {
        getProfile().child(profileModel.userID.toString())
            .setValue(profileModel)
    }

    fun updateProfile(profileModel: ProfileModel) {
        getProfile().child(getAuth().uid.toString())
            .setValue(profileModel)
    }

    fun createFoodId(foodName: String): String {

        return BCrypt.hashpw(foodName, BCrypt.gensalt())
    }


    fun getBags(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("bags").child(getAuth().uid.toString())
    }


    fun getLikeFoods(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("myLikesFood").child(getAuth().uid.toString())
    }


    fun setBrandStory(story: StoryModel) {
        getStory().child(generateID()).setValue(story)
    }


    fun getStory(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("storys")
    }

    fun generateID(): String {

        var id = ""
        var wordList = arrayOf(
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",
            "g",
            "h",
            "i",
            "j",
            "k",
            "l",
            "m",
            "n",
            "o",
            "p",
            "r",
            "s",
            "t",
            "u",
            "v",
            "w",
            "y",
            "x",
            "z"
        )


        for (i in 0..16)
            if (i % 2 == 0)
                id += wordList[Random.nextInt(0, wordList.size - 1)]
            else if (i % 4 == 0)
                id += wordList[Random.nextInt(0, wordList.size - 1)].uppercase()
            else
                id += Random.nextInt(0, 9)

        return id
    }

    fun getProfileInfo(viewModel: ProfileViewModel) {

        if (isAuth()) {
            val _profile = getProfile().child(auth.uid.toString())
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    var name = ""
                    var email = ""
                    var phone = ""
                    var userid = ""
                    var photo_url = ""
                    var photo_id = ""

                    dataSnapshot.children.forEach {
                        when (it.key.toString()) {
                            "email" -> email = it.value.toString()
                            "nameAndSurname" -> name = it.value.toString()
                            "phone" -> phone = it.value.toString()
                            "profilePhotoID" -> photo_id = it.value.toString()
                            "profilePhotoUrl" -> photo_url = it.value.toString()
                            "userID" -> userid = it.value.toString()
                        }

                    }

                    val prf = ProfileModel(
                        name, email, phone, userid
                    )

                    if (photo_url != "") {
                        prf.profilePhotoUrl = photo_url
                        prf.profilePhotoID = photo_id
                    }

                    viewModel.setProfile(
                        prf
                    )

                }

                override fun onCancelled(p0: DatabaseError) {

                }

            }

            _profile.addValueEventListener(listener)
        }
    }


    fun getMyLikeFoodCount(viewModel: ProfileViewModel) {

        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                viewModel.setLikeCount(dataSnapshot.childrenCount)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }
        if (isAuth())
            getLikeFoods().addValueEventListener(listener)

    }

    fun getMyLikeFoodCount(viewModel: MainViewModel) {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                viewModel.setLikeCount(dataSnapshot.childrenCount)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        getLikeFoods().addValueEventListener(listener)

    }

    fun getOrderFoodCount(viewModel: ProfileViewModel) {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                viewModel.setOrderCount(dataSnapshot.childrenCount)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        getOrderReference().addValueEventListener(listener)

    }


    fun uploadProfileImage(imageUri: Uri, view: View, my_profile: ProfileModel) {
        var id = generateID()
        getStorage().child(id).putFile(imageUri).addOnFailureListener { exception ->
            Snackbar.make(view, "Yükleme başarısız: " + exception, Snackbar.LENGTH_SHORT)
                .setAnimationMode(Snackbar.ANIMATION_MODE_FADE).show()
        }.addOnSuccessListener { task ->
            getStorage().child(id).downloadUrl.addOnSuccessListener { uri ->

                if (!my_profile.profilePhotoID.isNullOrEmpty() && !my_profile.profilePhotoUrl.isNullOrEmpty()) {
                    getStorage().child(my_profile.profilePhotoID.toString()).delete()
                }


                my_profile.profilePhotoUrl = uri.toString()
                my_profile.profilePhotoID = id
                updateProfile(my_profile)

            }

        }

    }


    fun getBestSellerReference(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("fruits")
    }


    fun getAdsReference(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("ads")
    }

    fun setAds(pht: String) {

        getAdsReference().child(generateID()).setValue(pht)

    }

    fun getBestSeller(viewModel: HomeViewModel) {
        val list = mutableListOf<FoodModel>()
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                dataSnapshot.children.forEach {
                    var food = FoodModel(
                        it.child("foodName").value.toString(),
                        it.child("foodPicture").value.toString(),
                        it.child("foodDescription").value.toString(),
                        it.child("foodPrice").value.toString(),
                        it.child("foodColor").value.toString(),
                        it.child("drinkLiter").value.toString(),
                        it.child("foodCategory").value.toString()
                    )

                    food.foodID = it.child("foodID").value.toString()
                    food.isOffer = it.child("isOffer").value.toString().toBoolean()
                    if (it.child("food_likes").value.toString() != "null")
                        food.food_likes = it.child("food_likes").value.toString().toInt()

                    food.foodGram = it.child("foodGram").value.toString()

                    food.deliveryTime = it.child("deliveryTime").value.toString()
                    food.foodCalory = it.child("foodCalory").value.toString()

                    list.add(food)

                }


                list.shuffle()
                viewModel.setBestSeller(list)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        getBestSellerReference().addValueEventListener(listener)

    }

    fun getAds(viewModel: HomeViewModel) {
        val list = ArrayList<SlideModel>()
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                dataSnapshot.children.forEach {
                    list.add(SlideModel(it.value.toString()))
                }

                list.shuffle()
                viewModel.setAds(list)
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        getAdsReference().addValueEventListener(listener)

    }

    fun getShuffleFoodList(viewModel: HomeViewModel) {
        val list = mutableListOf<FoodModel>()
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    var food = FoodModel(
                        it.child("foodName").value.toString(),
                        it.child("foodPicture").value.toString(),
                        it.child("foodDescription").value.toString(),
                        it.child("foodPrice").value.toString(),
                        it.child("foodColor").value.toString(),
                        it.child("drinkLiter").value.toString(),
                        it.child("foodCategory").value.toString(),
                    )

                    food.foodID = it.child("foodID").value.toString()
                    food.isOffer = it.child("isOffer").value.toString().toBoolean()
                    if (it.child("food_likes").value.toString() != "null")
                        food.food_likes = it.child("food_likes").value.toString().toInt()

                    food.foodGram = it.child("foodGram").value.toString()

                    food.deliveryTime = it.child("deliveryTime").value.toString()
                    food.foodCalory = it.child("foodCalory").value.toString()

                    list.add(food)
                }

                list.shuffle()
                viewModel.setShuffleList(list)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        getFruits().addListenerForSingleValueEvent(listener)
        getVegetables().addListenerForSingleValueEvent(listener)
        getColdDrink().addListenerForSingleValueEvent(listener)

        getFood("hotDrink").addListenerForSingleValueEvent(listener)
        getFood("soups").addListenerForSingleValueEvent(listener)
        getFood("appetizer").addListenerForSingleValueEvent(listener)
        getFood("cakes").addListenerForSingleValueEvent(listener)
        getFood("fish").addListenerForSingleValueEvent(listener)
        getFood("meats").addListenerForSingleValueEvent(listener)

    }


    fun updateFood(data: FoodModel) {
        lateinit var reference: DatabaseReference
        when (data.foodCategory.toString()) {
            "fruits" -> reference = getFood(data.foodCategory.toString())
            "vegetables" -> reference = getFood(data.foodCategory.toString())
            "coldDrink" -> reference = getFood(data.foodCategory.toString())

            "soups" -> reference = getFood(data.foodCategory.toString())
            "appetizer" -> reference = getFood(data.foodCategory.toString())
            "cakes" -> reference = getFood(data.foodCategory.toString())
            "fish" -> reference = getFood(data.foodCategory.toString())
            "meats" -> reference = getFood(data.foodCategory.toString())
            "hotDrink" -> reference = getFood(data.foodCategory.toString())
        }
        reference.child(data.foodName.toString()).setValue(data)
    }


    fun getOrderReference(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("orders").child(auth.uid.toString())
    }


    fun getCardReference(): DatabaseReference {
        return FirebaseDatabase.getInstance("https://menuapp-28717-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("debitcards").child(auth.uid.toString())
    }

    fun setCard(data: DebitCardModel, view: View) {
        val id = generateID()
        data.cardID = id
        getCardReference().child(id).setValue(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    Snackbar.make(view, "Kart eklendi!", Snackbar.LENGTH_SHORT).show()
                else
                    Snackbar.make(
                        view,
                        "Kart eklenemedi: " + task.exception.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
            }
    }


    fun resetPassword(email: String, view: View) {
        getAuth().sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful)
                Snackbar.make(
                    view,
                    "Şifre sıfırlama bağlantısı ${email} adresine gönderildi!",
                    Snackbar.LENGTH_LONG
                ).show()
            else
                Snackbar.make(
                    view,
                    task.exception.toString(),
                    Snackbar.LENGTH_LONG
                ).show()

        }
    }


    fun getCard(viewModel: DebitCardViewModel) {
        val list = mutableListOf<DebitCardModel>()
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                dataSnapshot.children.forEach {
                    list.add(
                        DebitCardModel(
                            it.child("cardNumber").value.toString(),
                            it.child("cardCVV").value.toString(),
                            it.child("cardSKT").value.toString(),
                            it.child("bankName").value.toString(),
                            it.child("cardID").value.toString()
                        )
                    )
                }

                viewModel.setDebitCard(list)
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        getCardReference().addValueEventListener(listener)

    }


    fun getCardCount(viewModel: DebitCardViewModel) {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                viewModel.setDebitCardCount(dataSnapshot.childrenCount)
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        getCardReference().addValueEventListener(listener)

    }


    fun setOrder(data: OrderModel) {
        data.orderTime = getCurrentTime()
        getOrderReference().child(generateID()).setValue(data)
    }


    fun getCurrentTime(): String {

        val currentDate =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"))
                    .toString()
            } else {
                SimpleDateFormat("HH:mm dd.MM.yyyy").format(Date()).toString()
            }

        return currentDate

    }


    fun clearBags() {
        getBags().removeValue()
    }

    fun getOrders(viewModel: OrderViewModel) {

        val list = mutableListOf<OrderModel>()
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                dataSnapshot.children.forEach {
                    list.add(
                        OrderModel(
                            getOrderDetailList(it.child("foodBag")),
                            it.child("sumTotal").value.toString(),
                            it.child("paymentMethod").value.toString(),
                            it.child("orderStatus").value.toString(),
                            it.child("orderTime").value.toString(),
                            it.child("tax1").value.toString(),
                            it.child("tax2").value.toString()
                        )

                    )

                }



                list.sortWith(compareBy({ OrderModel::orderTime.toString() }))
                viewModel.setOrders(list)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        getOrderReference().addValueEventListener(listener)

    }

    fun getOrderDetailList(it: DataSnapshot): ArrayList<FoodBag> {

        val list = ArrayList<FoodBag>()


        it.children.forEach {
            val fg = FoodBag(
                it.child("foodName").value.toString(),
                it.child("foodPicture").value.toString(),
                it.child("foodPrice").value.toString(),
                it.child("foodColor").value.toString(),
                it.child("drinkLiter").value.toString(),
                it.child("foodID").value.toString()
            )
            fg.bagFoodPiece = it.child("bagFoodPiece").value.toString()
            fg.bagTotalPrice = it.child("bagTotalPrice").value.toString()
            list.add(
                fg
            )
        }

        list.shuffle()

        return list
    }


    fun getComments(viewModel: FoodPreviewModel, path: String) {

        val list = ArrayList<CommentModel>()
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                viewModel.setCommentCount(dataSnapshot.childrenCount)
                dataSnapshot.children.forEach {
                    val cd = CommentModel(
                        it.child("comment").value.toString(),
                        it.child("userEmail").value.toString(),
                        it.child("commentTime").value.toString(),
                        it.child("userUid").value.toString(),
                        it.child("food_id").value.toString(),
                        it.child("ratingCount").value.toString().toFloat()
                    )

                    cd.user_photo = it.child("user_photo").value.toString()

                    list.add(
                        cd
                    )

                }

                list.sortedByDescending { it.ratingCount }
                viewModel.setComment(list)
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        getCommentsReference().child(path).addValueEventListener(listener)


    }


    fun setComments(data: CommentModel, view: View) {

        data.commentTime = getCurrentTime()
        data.userUid = getAuth().uid.toString()
        data.userEmail = getAuth().currentUser?.email.toString()

        getCommentsReference().child(data.food_id.toString()).child(generateID()).setValue(data)
            .addOnCompleteListener { task ->

                if (task.isSuccessful)
                    Snackbar.make(view, "Yorum gönderildi!", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(Snackbar.ANIMATION_MODE_FADE).show()
                else
                    Snackbar.make(
                        view,
                        "Yorum gönderilemedi: " + task.exception,
                        Snackbar.LENGTH_SHORT
                    )
                        .setAnimationMode(Snackbar.ANIMATION_MODE_FADE).show()

            }

    }


}

private fun DataSnapshot.children(value: () -> Unit) {

}



