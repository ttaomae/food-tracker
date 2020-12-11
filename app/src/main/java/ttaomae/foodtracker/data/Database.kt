package ttaomae.foodtracker.data

import android.content.Context
import androidx.room.*

@Database(entities = [FoodItem::class, Restaurant::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, AppDatabase::class.java, "food").build()
                    .also { instance = it }
            }
        }
    }
}

@Dao
interface FoodItemDao {
    @Query("SELECT * FROM food_item WHERE id = :id")
    suspend fun findById(id: Long): FoodItem?

    @Query("SELECT * FROM food_item")
    suspend fun findAll(): List<FoodItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodItem: FoodItem)
}

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurant WHERE id = :id")
    suspend fun findById(id: Long): Restaurant?

    @Query("SELECT * FROM restaurant")
    suspend fun findAll(): List<Restaurant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurant: Restaurant)
}
