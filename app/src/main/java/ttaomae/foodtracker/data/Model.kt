package ttaomae.foodtracker.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "food_item",
    foreignKeys = [
        ForeignKey(
            entity = Restaurant::class,
            childColumns = ["restaurant_id"],
            parentColumns = ["id"]
        )
    ]
)
@JsonClass(generateAdapter = true)
data class FoodItem(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "restaurant_id", index = true) val restaurantId: Long,
    val name: String,
    val description: String?,
    val rating: Float,
    val comments: String?
) {
    constructor(id: Long?, restaurantId: Long?, name: String, description: String?, rating: Float, comments: String?) :
            this(id ?: 0L, restaurantId ?: 0L, name, description, rating, comments)
}

@Entity(tableName = "restaurant")
@JsonClass(generateAdapter = true)
data class Restaurant(@PrimaryKey(autoGenerate = true) val id: Long, val name: String) {
    constructor(id: Long?, name: String) : this(id ?: 0L, name)
}

@JsonClass(generateAdapter = true)
data class RestaurantWithFoodItems(
    @Embedded val restaurant: Restaurant,
    @Relation(
        parentColumn = "id",
        entityColumn = "restaurant_id"
    )
    val foodItems: List<FoodItem>
) {
    fun asFoodItemList(): List<FoodItemWithRestaurant> {
        return foodItems.map { FoodItemWithRestaurant(it, restaurant) }
    }
}

data class FoodItemWithRestaurant(
    @Embedded val foodItem: FoodItem,
    @Relation(
        parentColumn = "restaurant_id",
        entityColumn = "id"
    )
    val restaurant: Restaurant,
)
