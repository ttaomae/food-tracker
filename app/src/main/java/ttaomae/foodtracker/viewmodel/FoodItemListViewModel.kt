package ttaomae.foodtracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import ttaomae.foodtracker.data.FoodItemWithRestaurant
import ttaomae.foodtracker.data.RestaurantRepository
import javax.inject.Inject

@HiltViewModel
class FoodItemListViewModel @Inject internal constructor(
    restaurantRepository: RestaurantRepository
): ViewModel() {
    val foodItems: LiveData<List<FoodItemWithRestaurant>> = restaurantRepository
        .getAllWithFoodItems()
        .map { list ->
            list.flatMap { restaurant ->
                restaurant.asFoodItemList()
            }
        }
        .asLiveData()
}
