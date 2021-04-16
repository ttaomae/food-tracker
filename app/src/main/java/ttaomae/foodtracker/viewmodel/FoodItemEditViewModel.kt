package ttaomae.foodtracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
    val restaurants: LiveData<List<Restaurant>> = restaurantRepository.getAll().asLiveData()

    val foodItem: MutableLiveData<FoodItemWithRestaurant> by lazy {
        MutableLiveData<FoodItemWithRestaurant>()
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

    fun selectRestaurant(id: Long) {
        selectedRestaurantId.value = id
    }

    fun saveItem(name: String, description: String, rating: Float) {
        val id = foodItem.value?.foodItem?.id ?: 0L
        selectedRestaurantId.value?.let {
            val item = FoodItem(id, it, name, description, rating)
            viewModelScope.launch {
                val newId = foodItemRepository.save(item)
                savedItemId.value = newId
            }
        }
    }
}
