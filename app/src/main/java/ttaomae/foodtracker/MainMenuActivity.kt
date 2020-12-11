package ttaomae.foodtracker

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }

    fun viewRestaurants(view: View) {
        val intent = Intent(this, ListRestuarantsActivity::class.java)
        startActivity(intent)
    }

    fun viewFoodItems(view: View) {
        val intent = Intent(this, ListFoodItemsActivity::class.java)
        startActivity(intent)
    }
}
