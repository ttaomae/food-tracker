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
import ttaomae.foodtracker.data.FoodRepository
import javax.inject.Inject

@AndroidEntryPoint
class AddFoodItemActivity : AppCompatActivity() {
    private var item: FoodItem? = null
    @Inject lateinit var foodRepository: FoodRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_item)

        runBlocking {
            val job = launch {
                item = intent.extras?.let {
                    val id = it.getLong("itemId")
                    foodRepository.get(id)
                }
            }
            job.join()
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
        runBlocking {
            launch {
                foodRepository.save(foodItem)
            }
        }


        val intent = Intent(this, ListFoodItemsActivity::class.java)
        startActivity(intent)
    }
}
