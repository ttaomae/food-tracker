package ttaomae.foodtracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ttaomae.foodtracker.model.FoodItem


class ListFoodItemsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_food_items)

        val items = (application as FoodTrackerApplication).foodItems
        println(items)

        val linearLayoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.itemsList).apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = FoodItemAdapter(items)
        }

    }

    fun addFoodItem(view: View) {
        val intent = Intent(this, AddFoodItemActivity::class.java)

        startActivity(intent)
    }
}

class FoodItemAdapter(private val items: List<FoodItem>) :
        RecyclerView.Adapter<FoodItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.nameTextView)
        val descriptionView: TextView = view.findViewById(R.id.descriptionTextView)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBarDisplay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.nameView.text = item.name
        holder.descriptionView.text = item.description
        holder.ratingBar.rating = item.rating
    }

    override fun getItemCount() = items.size
}
