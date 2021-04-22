package ttaomae.foodtracker.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "food_item",
    foreignKeys = [
        ForeignKey(
            entity = Restaurant::class,
            childColumns = ["restaurantId"],
            parentColumns = ["id"]
        )
    ]
)
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val restaurantId: Long,
    val name: String,
    val description: String,
    val rating: Float,
    val comments: String
) {
    constructor(id: Long?, restaurantId: Long?, name: String, description: String, rating: Float, comments: String) :
            this(id ?: 0L, restaurantId ?: 0L, name, description, rating, comments)
}

@Entity(tableName = "restaurant")
data class Restaurant(@PrimaryKey(autoGenerate = true) val id: Long, val name: String) {
    constructor(id: Long?, name: String) : this(id ?: 0L, name)
}

data class RestaurantWithFoodItems(
    @Embedded val restaurant: Restaurant,
    @Relation(
        parentColumn = "id",
        entityColumn = "restaurantId"
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
        parentColumn = "restaurantId",
        entityColumn = "id"
    )
    val restaurant: Restaurant,
)
