package ttaomae.foodtracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import ttaomae.foodtracker.model.FoodItem

class AddFoodItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_item)
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

        val foodItem = FoodItem(nameView.text.toString(), descriptionView.text.toString(), ratingBar.rating)
        (application as FoodTrackerApplication).foodItems.add(foodItem)

        val intent = Intent(this, ListFoodItemsActivity::class.java)
        startActivity(intent)
    }
}
