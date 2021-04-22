package ttaomae.foodtracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ttaomae.foodtracker.data.FoodItem
import ttaomae.foodtracker.data.FoodItemRepository
import ttaomae.foodtracker.data.FoodItemWithRestaurant
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.data.RestaurantRepository
import javax.inject.Inject

@HiltViewModel
class FoodItemEditViewModel @Inject internal constructor(
    private val foodItemRepository: FoodItemRepository,
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {
    val restaurants: MutableLiveData<List<Restaurant>> by lazy {
        MutableLiveData<List<Restaurant>>()
    }

    val foodItem: MutableLiveData<FoodItemWithRestaurant> by lazy {
        MutableLiveData<FoodItemWithRestaurant>()
    }

    val restaurant: MutableLiveData<Restaurant> by lazy {
        MutableLiveData<Restaurant>()
    }

    private val selectedRestaurantId: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }

    val savedItemId: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }

    fun setFoodItem(id: Long) {
        viewModelScope.launch {
            val f = foodItemRepository.getWithRestaurant(id)
            if (f != null) {
                foodItem.value = f
                selectedRestaurantId.value = f.restaurant.id
            }
        }
    }

    fun setRestaurant(id: Long) {
        viewModelScope.launch {
            restaurantRepository.get(id)?.let {
                restaurant.value = it
                selectRestaurant(id)
            } ?: run {
                restaurants.value = restaurantRepository.getAll()
            }
        }
    }

    fun selectRestaurant(id: Long) {
        selectedRestaurantId.value = id
    }

    fun saveItem(name: String, description: String, rating: Float, comments: String) {
        val id = foodItem.value?.foodItem?.id ?: 0L
        selectedRestaurantId.value?.let {
            val item = FoodItem(id, it, name, description, rating, comments)
            viewModelScope.launch {
                val newId = foodItemRepository.save(item)
                savedItemId.value = newId
            }
        }
    }
}
