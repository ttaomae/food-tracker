package ttaomae.foodtracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.data.RestaurantRepository
import ttaomae.foodtracker.data.RestaurantWithFoodItems
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel @Inject internal constructor(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {
    val restaurant: MutableLiveData<RestaurantWithFoodItems> by lazy {
        MutableLiveData<RestaurantWithFoodItems>()
    }

    fun setRestaurant(id: Long) {
        viewModelScope.launch {
            restaurant.value = restaurantRepository.getWithFoodItems(id)
        }
    }

    fun deleteRestaurant() {
        viewModelScope.launch {
            restaurantRepository.delete(restaurant.value!!.restaurant)
        }
    }
}
