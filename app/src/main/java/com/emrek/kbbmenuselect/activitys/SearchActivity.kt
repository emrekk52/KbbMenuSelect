package com.emrek.kbbmenuselect.activitys

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.adapters.SearchAdapter
import com.emrek.kbbmenuselect.databinding.ActivitySearchBinding
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.viewmodels.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        binding.searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s?.trim()?.length!! <= 1 && s?.toString()?.contains(" ")!!)
                    binding.searchBar.text = ""


                if (s?.trim()?.length!! > 0)
                    binding.searchText.visibility = View.GONE
                else
                    binding.searchText.visibility = View.VISIBLE

            }

            override fun afterTextChanged(s: Editable?) {

                GetFoods().getQuery(viewModel, s.toString().trim())

            }


        })



        viewModel.getQuery().observe(this, Observer {

            if (it.size == 0 && binding.searchBar.text?.toString()?.trim()?.length!! > 0) {
                binding.notFoundText.visibility = View.VISIBLE
                binding.notFoundText.text =
                    "'${binding.searchBar.text.toString().trim()}' için sonuç bulunamadı!"

            } else {
                binding.notFoundText.visibility = View.GONE
            }


            val adapter = SearchAdapter(it)
            binding.recyclerView.adapter = adapter

            adapter.setOnItemClickListener(object : SearchAdapter.ClickListener {
                override fun onItemClick(v: View, position: Int) {
                    if (it[position].foodID?.isEmpty() == false && it[position].foodPicture?.isEmpty() == false) {
                        bottomSheetDialog(it[position])
                    }
                }

            })

        })

    }


    fun bottomSheetDialog(data: FoodModel) {

        val bundle = Bundle()
        bundle.putParcelable("data", data)

        val fd = FoodPreview()
        fd.arguments = bundle

        fd.show(supportFragmentManager!!, "bottomSheetDialog")


    }


}