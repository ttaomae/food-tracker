package ttaomae.foodtracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.data.RestaurantRepository
import javax.inject.Inject

@AndroidEntryPoint
class EditRestuarantActivity : AppCompatActivity() {
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

        findViewById<EditText>(R.id.edit_text_restaurant_name).apply { setText(restaurant?.name) }

        // Disable button if we are adding a new item.
        findViewById<Button>(R.id.button_delete_restaurant).isEnabled = restaurant != null
    }

    fun addRestaurant(view: View) {
        val nameView = findViewById<EditText>(R.id.edit_text_restaurant_name)

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

    fun deleteRestaurant(view: View) {
        restaurant?.let {
            runBlocking {
                launch {
                    restaurantRepository.delete(it)
                }
            }
        }

        val intent = Intent(this, ListRestuarantsActivity::class.java)
        startActivity(intent)
    }
}
