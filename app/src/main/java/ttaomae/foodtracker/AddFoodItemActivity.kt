package ttaomae.foodtracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ttaomae.foodtracker.data.FoodItem
import ttaomae.foodtracker.data.FoodItemRepository
import javax.inject.Inject

@AndroidEntryPoint
class AddFoodItemActivity : AppCompatActivity() {
    private var item: FoodItem? = null
    @Inject lateinit var foodItemRepository: FoodItemRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_item)

        runBlocking {
            val job = launch {
                item = intent.extras?.let {
                    val id = it.getLong("itemId")
                    foodItemRepository.get(id)
                }
            }
            job.join()
        }

        findViewById<EditText>(R.id.edit_text_item_name).apply { setText(item?.name) }
        findViewById<EditText>(R.id.edit_text_item_description).apply { setText(item?.description) }
        findViewById<RatingBar>(R.id.rating_bar_item_input).apply { rating = item?.rating ?: 0f }
    }

    fun addFoodItem(view: View) {
        val nameView = findViewById<EditText>(R.id.edit_text_item_name)
        val descriptionView = findViewById<EditText>(R.id.edit_text_item_description)
        val ratingBar = findViewById<RatingBar>(R.id.rating_bar_item_input)

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
        runBlocking {
            launch {
                foodItemRepository.save(foodItem)
            }
        }


        val intent = Intent(this, ListFoodItemsActivity::class.java)
        startActivity(intent)
    }
}
