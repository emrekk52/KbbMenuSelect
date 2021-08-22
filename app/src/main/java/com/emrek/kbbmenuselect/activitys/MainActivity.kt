package com.emrek.kbbmenuselect.activitys


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.*
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.adapters.StoryAdapter
import com.emrek.kbbmenuselect.databinding.ActivityMainBinding
import com.emrek.kbbmenuselect.models.StoryModel
import com.emrek.kbbmenuselect.viewmodels.MainViewModel
import com.emrek.kbbmenuselect.viewmodels.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI
import kotlinx.android.synthetic.main.activity_order.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val bags = GetFoods().getBags()
    val storys = GetFoods().getStory()

    lateinit var storyAdapter: StoryAdapter
    private var storyList = mutableListOf<StoryModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.getRoot()
        setContentView(view)

        storyAdapter = StoryAdapter(storyList,applicationContext)
        binding.horizontalRecycler.adapter = storyAdapter

        bottomBarInit()


        val isLoginMessage = intent.getStringExtra("isLogin")
        if (!isLoginMessage.isNullOrEmpty())
            Snackbar.make(view, isLoginMessage, Snackbar.LENGTH_SHORT).show()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(Color.TRANSPARENT)
        }


        setUpButton()
        getBagsLength()
        getStorys()

    }


    fun bottomBarInit() {

        val bottomNavigationView = findViewById<ExpandableBottomBar>(R.id.bottomNavigation)
        val navController = Navigation.findNavController(this, R.id.frameLayout)

        ExpandableBottomBarNavigationUI.setupWithNavController(
            bottomNavigationView,
            navController
        )


        val viewModel = ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)
        GetFoods().getMyLikeFoodCount(viewModel)

        val sharedPr = getSharedPreferences(packageName, Context.MODE_PRIVATE)

        val liste = arrayOf(
            "Merhaba ${sharedPr.getString("user_name", "")} \uD83C\uDF88",
            "Mutlu günler \uD83D\uDE0A",
            "Canın bir şeyler çektiyse, durma hadi bir şeyler söyle!",
            "Afiyet olsun! \uD83C\uDF54"
        )

        countHelloText(liste)



        viewModel.getLikeCount().observe(this, Observer {
            val menu = binding.bottomNavigation.menu
            val like = menu.findItemById(R.id.likeFoodFragment).notification()

            if (it.toInt() == 0)
                like.clear()
            else
                like.show(it.toString())
        })


    }

    fun countHelloText(liste: Array<String>) {

        var count = 0
        binding.textSwitch.setFactory(
            object : ViewSwitcher.ViewFactory {
                @SuppressLint("WrongConstant")
                override fun makeView(): View {

                    val txtview = TextView(baseContext)
                    txtview.setTextColor(Color.BLACK)
                    txtview.textSize = 20f
                    txtview.gravity = Gravity.CENTER_VERTICAL
                    txtview.typeface = ResourcesCompat.getFont(baseContext, R.font.roboto_medium)
                    txtview.isSingleLine = true
                    txtview.ellipsize = TextUtils.TruncateAt.MARQUEE
                    txtview.isSelected = true
                    return txtview
                }
            }
        )


        object : CountDownTimer(20000, 5000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.textSwitch.setText(
                    liste[count]
                )
                if (count == liste.size - 1)
                    count = 0
                else
                    count++
            }

            override fun onFinish() {
            }
        }.start()


    }


    fun setUpButton() {
        binding.shoppingBag.setOnClickListener {

            if (GetFoods().isLogin(this@MainActivity)) {
                val intent = Intent(this@MainActivity, OrderActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }


        storyAdapter.setOnItemClickListener(object : StoryAdapter.ClickListener {
            override fun onItemClick(v: View, position: Int) {
                val intent = Intent(this@MainActivity, StoryActivity::class.java)
                intent.putExtra("storyObject", storyList[position])
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

        })



        binding.searchButton.setOnClickListener {

            val intent = Intent(this, SearchActivity::class.java)

            val options =
                ViewCompat.getTransitionName(binding.searchButton)?.let { it1 ->
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this, binding.searchButton,
                        it1
                    )
                }

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent, options?.toBundle())

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
                TODO("Not yet implemented")
            }
        }

        bags.addValueEventListener(listener)
    }


    fun getStorys() {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                storyList.clear()
                dataSnapshot.children.forEach {
                    storyList.add(
                        StoryModel(
                            it.child("brandName").value.toString(),
                            it.child("brandPicture").value.toString(),
                            it.child("brandContent").value.toString(),
                            it.child("contentTime").value.toString(),
                            it.child("isTick").value.toString().toBoolean()
                        )
                    )
                }
                storyList.shuffle()
                storyAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        storys.addValueEventListener(listener)

    }


}