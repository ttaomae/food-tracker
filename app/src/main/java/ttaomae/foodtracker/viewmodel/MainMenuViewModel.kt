package ttaomae.foodtracker.viewmodel

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ttaomae.foodtracker.data.FoodItemRepository
import ttaomae.foodtracker.data.RestaurantRepository
import ttaomae.foodtracker.data.RestaurantWithFoodItems
import ttaomae.foodtracker.json.JsonUtil
import java.io.FileWriter
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject internal constructor(
    private val restaurantRepository: RestaurantRepository,
    private val foodItemRepository: FoodItemRepository
) : ViewModel() {
    private val restaurantsWithFoodItems: LiveData<List<RestaurantWithFoodItems>> =
        restaurantRepository.getAllWithFoodItems().asLiveData()

    fun import(json: String) {
        val restaurants = JsonUtil.deserialize(json)
        viewModelScope.launch {
            restaurants?.forEach { restaurant ->
                restaurantRepository.save(restaurant.restaurant)
                restaurant.foodItems.forEach { foodItem ->
                    foodItemRepository.save(foodItem)
                }
            }
        }
    }

    fun export(contentResolver: ContentResolver, uri: Uri, lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            restaurantsWithFoodItems.observe(lifecycleOwner) { data ->
                val json = JsonUtil.serialize(data)
                contentResolver.openFileDescriptor(uri, "w")?.use {
                    FileWriter(it.fileDescriptor).use { writer ->
                        writer.write(json)
                    }
                }
            }
        }
    }
}
