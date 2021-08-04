package com.emrek.kbbmenuselect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.adapters.MyOrderAdapter
import com.emrek.kbbmenuselect.databinding.FragmentMyOrderBinding
import com.emrek.kbbmenuselect.viewmodels.OrderViewModel


class MyOrderFragment : Fragment() {

    private lateinit var binding: FragmentMyOrderBinding
    private lateinit var viewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyOrderBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(requireActivity()).get(OrderViewModel::class.java)

        getMyOrderList()

        return binding.root
    }

    fun getMyOrderList() {

        GetFoods().getOrders(viewModel)

        viewModel.getOrders().observe(requireActivity(), Observer {

            val adapter = MyOrderAdapter(it)
            binding.orderRecycler.adapter = adapter




            if (it.isNullOrEmpty())
                binding.orderAnimation.visibility = View.VISIBLE
            else
                binding.orderAnimation.visibility = View.GONE

        })


    }


}