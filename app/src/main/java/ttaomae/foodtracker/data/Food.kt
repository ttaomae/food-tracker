package ttaomae.foodtracker.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FoodItem(val id: String?, val name: String, val description: String, val rating: Float) :
    Parcelable {
    constructor(id: String, item: FoodItem) : this(id, item.name, item.description, item.rating)
}
