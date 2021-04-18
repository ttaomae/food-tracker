package ttaomae.foodtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ttaomae.foodtracker.data.FoodItemWithRestaurant
import ttaomae.foodtracker.databinding.FoodItemSummaryBinding

abstract class FoodItemViewHolder<B : ViewDataBinding>(val binding: B) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener { onClick(it) }
    }

    abstract fun bind(foodItem: FoodItemWithRestaurant)
    abstract fun onClick(view: View)
}

class FoodItemAdapter<B : ViewDataBinding>(
    val createViewHolder: (LayoutInflater, ViewGroup) -> FoodItemViewHolder<B>,
) :
    ListAdapter<FoodItemWithRestaurant, FoodItemViewHolder<B>>(FoodItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder<B> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return createViewHolder(layoutInflater, parent)
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder<B>, position: Int) {
        val foodItem = getItem(position)
        holder.bind(foodItem)
    }
}

object FoodItemCallback : DiffUtil.ItemCallback<FoodItemWithRestaurant>() {
    override fun areItemsTheSame(
        oldItem: FoodItemWithRestaurant,
        newItem: FoodItemWithRestaurant
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: FoodItemWithRestaurant,
        newItem: FoodItemWithRestaurant
    ): Boolean {
        return oldItem.foodItem.name == newItem.foodItem.name
                && oldItem.foodItem.description == newItem.foodItem.description
                && oldItem.foodItem.rating == newItem.foodItem.rating
                && oldItem.restaurant.name == newItem.restaurant.name
    }
}
