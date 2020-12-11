package ttaomae.foodtracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ttaomae.foodtracker.data.FoodItem
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.data.RestaurantRepository
import javax.inject.Inject

@AndroidEntryPoint
class ListRestuarantsActivity : AppCompatActivity() {
    @Inject lateinit var restaurantRepository: RestaurantRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_restaurants)

        val linearLayoutManager = LinearLayoutManager(this)
        val restaurantAdapter = RestaurantAdapter { startAddRestaurantActivity(it) }
        runBlocking {
            launch {
                val restaurants = restaurantRepository.getAll()
                restaurantAdapter.submitList(restaurants)
            }
        }
        findViewById<RecyclerView>(R.id.restaurantsList).apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = restaurantAdapter
        }
    }

    fun addRestaurant(view: View) {
        startAddRestaurantActivity(null)
    }

    private fun startAddRestaurantActivity(item: Restaurant?) {
        val intent = Intent(this, AddRestuarantActivity::class.java)
        item?.apply { intent.putExtra("itemId", item.id) }
        startActivity(intent)
    }
}

class RestaurantAdapter(private val onClick: (Restaurant) -> Unit) :
    ListAdapter<Restaurant, RestaurantAdapter.ViewHolder>(RestaurantCallback) {

    class ViewHolder(view: View, private val onClick: (Restaurant) -> Unit) :
        RecyclerView.ViewHolder(view) {

        val nameView: TextView = view.findViewById(R.id.restaurantNameTextView)
        var item: Restaurant? = null

        init {
            view.setOnClickListener {
                item?.let {
                    onClick(it)
                }
            }
        }

        fun bind(item: Restaurant) {
            nameView.text = item.name
            this.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

object RestaurantCallback : DiffUtil.ItemCallback<Restaurant>() {
    override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem.name == newItem.name
    }
}
