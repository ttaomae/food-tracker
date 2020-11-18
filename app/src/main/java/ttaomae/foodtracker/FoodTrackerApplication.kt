package ttaomae.foodtracker

import android.app.Application
import ttaomae.foodtracker.data.FoodRepository
import ttaomae.foodtracker.data.InMemoryFoodRepository
import ttaomae.foodtracker.data.FoodItem

class FoodTrackerApplication : Application() {
    val foodRepository: FoodRepository = InMemoryFoodRepository()

    init {
        // Add some sample data.
        foodRepository.save(
            FoodItem(
                "0",
                "PB&J Sandwich",
                " A sandwich filled with peanut butter and jelly.",
                3.0f
            )
        )
        foodRepository.save(
            FoodItem(
                "1",
                "Bacon and Eggs",
                "Crispy fried bacon and eggs over easy.",
                4.5f
            )
        )
    }
}
