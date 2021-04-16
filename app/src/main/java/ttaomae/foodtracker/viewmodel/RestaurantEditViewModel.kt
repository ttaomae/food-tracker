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
class RestaurantEditViewModel @Inject internal constructor(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {
    val restaurant: MutableLiveData<Restaurant> by lazy {
        MutableLiveData<Restaurant>()
    }

    val savedRestaurantId: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }

    fun setRestaurant(id: Long) {
        viewModelScope.launch {
            val r = restaurantRepository.get(id)
            if (r != null) {
                restaurant.value = r
            }
        }
    }

    fun saveRestaurant(name: String) {
        val id = restaurant.value?.id ?: 0L
        val restaurant = Restaurant(id, name)
        viewModelScope.launch {
            val newId = restaurantRepository.save(restaurant)
            savedRestaurantId.value = newId
        }
    }
}
