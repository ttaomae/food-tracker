package ttaomae.foodtracker

import android.app.Application
import ttaomae.foodtracker.model.FoodItem

class FoodTrackerApplication : Application() {
    val foodItems: MutableList<FoodItem> = mutableListOf(
        // Add some sample data.
        FoodItem("PB&J Sandwich", " A sandwich filled with peanut butter and jelly.", 3.0f),
        FoodItem("Bacon and Eggs", "Crispy fried bacon and eggs over easy.", 4.5f)
    )
}
