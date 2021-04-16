package ttaomae.foodtracker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ttaomae.foodtracker.data.FoodItem
import ttaomae.foodtracker.data.FoodItemRepository
import ttaomae.foodtracker.data.FoodItemWithRestaurant
import javax.inject.Inject

@HiltViewModel
class FoodItemDetailViewModel @Inject internal constructor(
    private val foodItemRepository: FoodItemRepository
) : ViewModel() {
    val foodItem: MutableLiveData<FoodItemWithRestaurant> by lazy {
        MutableLiveData<FoodItemWithRestaurant>()
    }

    fun setItem(id: Long) {
        viewModelScope.launch {
            foodItem.value = foodItemRepository.getWithRestaurant(id)
        }
    }

    fun deleteItem() {
        viewModelScope.launch {
            foodItemRepository.delete(foodItem.value!!.foodItem)
        }
    }
}
