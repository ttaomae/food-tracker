package ttaomae.foodtracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.data.RestaurantRepository
import ttaomae.foodtracker.data.RestaurantWithFoodItems
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject internal constructor(
    restaurantRepository: RestaurantRepository
) : ViewModel() {
    val restaurantsWithFoodItems: LiveData<List<RestaurantWithFoodItems>> =
        restaurantRepository.getAllWithFoodItems().asLiveData()
}
