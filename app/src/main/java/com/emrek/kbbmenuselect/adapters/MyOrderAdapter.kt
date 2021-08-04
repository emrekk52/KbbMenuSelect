package com.emrek.kbbmenuselect.adapters

import android.graphics.Color
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.models.FoodBag
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.models.OrderModel
import com.squareup.picasso.Picasso

class MyOrderAdapter(private var list: List<OrderModel>) :
    RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {

    private var clickListener: MyOrderAdapter.ClickListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val picture1: ImageView? = view.findViewById(R.id.picture1)
        val picture2: ImageView? = view.findViewById(R.id.picture2)
        val picture3: ImageView? = view.findViewById(R.id.picture3)
        val collapse_button: ImageView? = view.findViewById(R.id.collapse_button)

        val detail_recycler: RecyclerView? = view.findViewById(R.id.detail_recycler)
        val card: CardView? = view.findViewById(R.id.card)

        val tax_layout: RelativeLayout? = view.findViewById(R.id.tax_layout)


        val order_time: TextView? = view.findViewById(R.id.order_time)
        val order_status: TextView? = view.findViewById(R.id.order_status)
        val payment_method: TextView? = view.findViewById(R.id.payment_method)
        val cash: TextView? = view.findViewById(R.id.cash)

        val tax1: TextView? = view.findViewById(R.id.tax1)
        val tax2: TextView? = view.findViewById(R.id.tax2)


        fun setUp(data: OrderModel) {
            when (data.foodBag.size) {
                1 -> Picasso.get().load(data.foodBag[0].foodPicture).into(picture1)
                2 -> {
                    Picasso.get().load(data.foodBag[0].foodPicture).into(picture1)
                    Picasso.get().load(data.foodBag[1].foodPicture).into(picture2)
                }
                else -> {
                    Picasso.get().load(data.foodBag[0].foodPicture).into(picture1)
                    Picasso.get().load(data.foodBag[1].foodPicture).into(picture2)
                    Picasso.get().load(data.foodBag[2].foodPicture).into(picture3)
                }
            }

            detailRecylerSetup(data.foodBag)

            order_time?.text = data.orderTime
            order_status?.text = data.orderStatus
            payment_method?.text = "${data.paymentMethod} ile ödendi"
            cash?.text = "₺${data.sumTotal}"

            tax1?.text = "KDV: ₺" + data.tax1
            tax2?.text = "Kulllanım bedeli: ₺" + data.tax2


            if (data.orderStatus.equals("Sipariş tamamlandı"))
                order_status?.setTextColor(Color.parseColor("#04C30D"))
            else if(data.orderStatus.equals("Sipariş reddedildi"))
                order_status?.setTextColor(Color.parseColor("#ff0000"))

            card?.setOnClickListener {
                if (collapse_button?.rotation == 0f) {
                    tax_layout?.visibility = View.VISIBLE
                    collapse_button?.rotation = 90f
                } else {
                    tax_layout?.visibility = View.GONE
                    collapse_button?.rotation = 0f
                }

            }
        }

        init {
            if (clickListener != null) {
                itemView.setOnClickListener(this)
            }
        }

        override fun onClick(v: View?) {
            if (v != null) {
                clickListener?.onItemClick(v, adapterPosition)
            }
        }


        fun detailRecylerSetup(list: ArrayList<FoodBag>) {
            val adapter = MyOrderDetailAdapter(list)
            detail_recycler?.adapter = adapter
        }


    }

    fun setOnItemClickListener(clickListener: MyOrderAdapter.ClickListener) {
        this.clickListener = clickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.my_order_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setUp(list[position])
    }

    override fun getItemCount() = list.size


    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }

}