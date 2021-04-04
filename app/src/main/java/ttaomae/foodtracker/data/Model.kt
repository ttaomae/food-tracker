package ttaomae.foodtracker.data

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val rating: Float
) : Parcelable {
    constructor(id: Long?, restaurantId: Long?, name: String, description: String, rating: Float) :
            this(id ?: 0L, restaurantId ?: 0L, name, description, rating)
}

@Parcelize
@Entity(tableName = "restaurant")
data class Restaurant(@PrimaryKey(autoGenerate = true) val id: Long, val name: String) :
    Parcelable {
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

@Parcelize
data class FoodItemWithRestaurant(
    val foodItem: FoodItem,
    val restaurant: Restaurant,
) : Parcelable {
    @IgnoredOnParcel val id = foodItem.id
    @IgnoredOnParcel val name = foodItem.name
    @IgnoredOnParcel val description = foodItem.description
    @IgnoredOnParcel val rating = foodItem.rating
}
