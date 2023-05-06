package ttaomae.foodtracker.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import ttaomae.foodtracker.data.RestaurantWithFoodItems

object JsonUtil {
    private object RestaurantJson {
        private val moshi: Moshi = Moshi.Builder().build()
        @OptIn(ExperimentalStdlibApi::class)
        val adapter: JsonAdapter<List<RestaurantWithFoodItems>> = moshi.adapter<List<RestaurantWithFoodItems>>()
    }
    fun serialize(restaurants: List<RestaurantWithFoodItems>): String {
        return RestaurantJson.adapter.toJson(restaurants)
    }

    fun deserialize(json: String): List<RestaurantWithFoodItems>? {
        return RestaurantJson.adapter.fromJson(json)
    }
}
