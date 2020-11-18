package ttaomae.foodtracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import ttaomae.foodtracker.data.FoodRepository
import ttaomae.foodtracker.data.FoodItem

class AddFoodItemActivity : AppCompatActivity() {
    private var item: FoodItem? = null
    private lateinit var foodRepository: FoodRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_item)

        foodRepository = (application as FoodTrackerApplication).foodRepository
        item = intent.extras?.let {
            val id = it.getString("itemId") ?: ""
            foodRepository.get(id)
        }

        findViewById<EditText>(R.id.editTextName).apply { setText(item?.name) }
        findViewById<EditText>(R.id.editTextDescription).apply { setText(item?.description) }
        findViewById<RatingBar>(R.id.ratingBar).apply { rating = item?.rating ?: 0f }
    }

    fun addFoodItem(view: View) {
        val nameView = findViewById<EditText>(R.id.editTextName)
        val descriptionView = findViewById<EditText>(R.id.editTextDescription)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)

        if (nameView.text.isBlank()) {
            return
        }
        if (descriptionView.text.isBlank()) {
            return
        }

        val foodItem = FoodItem(
            item?.id,
            nameView.text.toString(),
            descriptionView.text.toString(),
            ratingBar.rating
        )
        foodRepository.save(foodItem)

        val intent = Intent(this, ListFoodItemsActivity::class.java)
        startActivity(intent)
    }
}
