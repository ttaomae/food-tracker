package ttaomae.foodtracker.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "food_item")
data class FoodItem(
    @PrimaryKey val id: Long?,
    val name: String,
    val description: String,
    val rating: Float
) : Parcelable
