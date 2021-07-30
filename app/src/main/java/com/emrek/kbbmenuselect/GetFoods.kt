package com.emrek.kbbmenuselect

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.emrek.kbbmenuselect.activitys.MainActivity
import com.emrek.kbbmenuselect.fragments.ProfileFragment
import com.emrek.kbbmenuselect.models.FoodBag
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.models.ProfileModel
import com.emrek.kbbmenuselect.models.StoryModel
import com.emrek.kbbmenuselect.viewmodels.MainViewModel
import com.emrek.kbbmenuselect.viewmodels.ProfileViewModel
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import org.mindrot.jbcrypt.BCrypt
import java.util.*
import kotlin.random.Random

class GetFoods() {


    private var auth: FirebaseAuth = Firebase.auth


    fun getStorage(): StorageReference {
        return FirebaseStorage.getInstance("gs://menuapp-28717.appspot.com").getReference()
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

    fun signOut() {
        if (isAuth())
            getAuth().signOut()
    }

    fun addToBag(foodBag: FoodBag) {

        database.child("bags").child(auth.uid.toString()).child(foodBag.foodID.toString())
            .setValue(foodBag)

    }


    fun setFoods(foodCategory: String, model: FoodModel): DatabaseReference {

        model.foodID = createFoodId(model.foodName.toString())
        database.child(foodCategory).child(model.foodName.toString())
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

    fun getProfileInfo(viewModel: MainViewModel) {

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
}



