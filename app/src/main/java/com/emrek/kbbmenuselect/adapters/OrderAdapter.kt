package com.emrek.kbbmenuselect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.activitys.OrderActivity
import com.emrek.kbbmenuselect.models.FoodBag
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_order.*

class OrderAdapter(private val dataset: List<FoodBag>, private val activity: OrderActivity) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.food_bag_design, parent, false)


        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: OrderAdapter.ViewHolder, position: Int) {
        return holder.setUp(dataset[position])
    }

    override fun getItemCount() = dataset.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var bagTotal = 0f

        val foodPicture: ImageView? = view.findViewById(R.id.food_picture)
        val foodName: TextView? = view.findViewById(R.id.food_name)
        val foodPrice: TextView? = view.findViewById(R.id.food_price)
        val totalPrice: TextView? = view.findViewById(R.id.total_price)
        val foodPiece: TextView? = view.findViewById(R.id.food_piece)

        val increase: ImageView? = view.findViewById(R.id.increase)
        var decrease: ImageView? = view.findViewById(R.id.decrease)
        var remove_food: ImageView? = view.findViewById(R.id.remove_food)

        fun setUp(dataset: FoodBag) {
            Picasso.get().load(dataset.foodPicture).placeholder(R.drawable.biggerplaceholder)
                .into(foodPicture)

            foodName?.text = dataset.foodName
            foodPrice?.text = dataset.foodPrice
            totalPrice?.text = dataset.bagTotalPrice
            if (dataset.bagFoodPiece?.toInt()!! < 10)
                foodPiece?.text = "0" + dataset.bagFoodPiece?.toInt()
            else
                foodPiece?.text = dataset.bagFoodPiece

            bagTotal =
                activity.binding.bagTotal.text.toString().toFloat() + totalPrice?.text.toString()
                    .toFloat()

            activity.binding.bagTotal.text = String.format("%.2f", bagTotal)

            sumTotal()
            buttonSetup(dataset)

        }

        fun buttonSetup(data: FoodBag) {
            var piece = foodPiece?.text.toString().toInt()
            increase?.setOnClickListener {
                piece++
                if (piece < 10) {
                    foodPiece?.text = "0" + piece
                } else {
                    foodPiece?.text = piece.toString()
                }
                val total = piece * foodPrice?.text.toString().toFloat()
                totalPrice?.text = String.format("%.2f", total)

                activity.binding.bagTotal.text =
                    (activity.binding.bagTotal.text.toString()
                        .toFloat() + foodPrice?.text.toString().toFloat()).toString()
                activity.binding.bagTotal.text =
                    String.format("%.2f", activity.binding.bagTotal.text.toString().toFloat())

                sumTotal()

                updateDatabase(data, piece.toString(), totalPrice?.text.toString())


            }

            decrease?.setOnClickListener {
                if (piece > 1) {
                    piece--
                    activity.binding.bagTotal.text =
                        (activity.binding.bagTotal.text.toString()
                            .toFloat() - foodPrice?.text.toString().toFloat()).toString()
                    activity.binding.bagTotal.text =
                        String.format("%.2f", activity.binding.bagTotal.text.toString().toFloat())

                    sumTotal()

                }

                if (piece < 10) {
                    foodPiece?.text = "0" + piece
                } else {
                    foodPiece?.text = piece.toString()
                }
                val total = piece * foodPrice?.text.toString().toFloat()
                totalPrice?.text = String.format("%.2f", total)

                updateDatabase(
                    data,
                    piece.toString(),
                    totalPrice?.text.toString()
                )

            }

            remove_food?.setOnClickListener {
                activity.database.child(data.foodID.toString()).removeValue()
                activity.binding.bagTotal.text = String.format("%.2f", bagTotal)


            }


        }

        fun sumTotal() {

            val sumT = activity.binding.bagTotal.text.toString()
                .toFloat() + activity.tax1
                .toFloat() + activity.tax2

            activity.binding.total.text = String.format("%.2f", sumT)

        }


        fun updateDatabase(data: FoodBag, piece: String, bagTotalPrice: String) {
            data.bagFoodPiece = piece
            data.bagTotalPrice = bagTotalPrice
            GetFoods().getBags().child(data.foodID.toString()).setValue(data)

        }

    }


}