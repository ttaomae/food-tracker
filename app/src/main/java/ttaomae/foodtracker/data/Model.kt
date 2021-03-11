package ttaomae.foodtracker.data

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

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
    @PrimaryKey val id: Long?,
    val restaurantId: Long,
    val name: String,
    val description: String,
    val rating: Float
) : Parcelable

@Parcelize
@Entity(tableName = "restaurant")
data class Restaurant(@PrimaryKey val id: Long?, val name: String)

data class RestaurantWithFoodItems(
    @Embedded val restaurant: Restaurant,
    @Relation(
        parentColumn = "id",
        entityColumn = "restaurantId"
    )
    val foodItems: List<FoodItem>
)
