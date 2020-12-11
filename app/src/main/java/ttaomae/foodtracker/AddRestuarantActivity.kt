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
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.data.RestaurantRepository
import javax.inject.Inject

@AndroidEntryPoint
class AddRestuarantActivity : AppCompatActivity() {
    private var restaurant: Restaurant? = null
    @Inject lateinit var restaurantRepository: RestaurantRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_restaurant)

        runBlocking {
            val job = launch {
                restaurant = intent.extras?.let {
                    val id = it.getLong("itemId")
                    restaurantRepository.get(id)
                }
            }
            job.join()
        }

        findViewById<EditText>(R.id.editTextRestaurantName).apply { setText(restaurant?.name) }
    }

    fun addRestaurant(view: View) {
        val nameView = findViewById<EditText>(R.id.editTextRestaurantName)

        if (nameView.text.isBlank()) {
            return
        }

        val restaurant = Restaurant(restaurant?.id, nameView.text.toString())
        runBlocking {
            launch {
                restaurantRepository.save(restaurant)
            }
        }


        val intent = Intent(this, ListRestuarantsActivity::class.java)
        startActivity(intent)
    }
}
