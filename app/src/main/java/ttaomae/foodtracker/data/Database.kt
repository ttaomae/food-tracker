package ttaomae.foodtracker.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

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

    @Transaction
    @Query("SELECT * FROM food_item WHERE id = :id")
    suspend fun findWithRestaurant(id: Long): FoodItemWithRestaurant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodItem: FoodItem): Long

    @Delete
    suspend fun delete(foodItem: FoodItem)
}

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurant WHERE id = :id")
    suspend fun findById(id: Long): Restaurant?

    @Query("SELECT * FROM restaurant")
    fun findAll(): Flow<List<Restaurant>>

    @Transaction
    @Query("SELECT * FROM restaurant WHERE id = :id")
    fun findWithFoodItems(id: Long): RestaurantWithFoodItems

    @Transaction
    @Query("SELECT * FROM restaurant")
    fun findAllWithFoodItems(): Flow<List<RestaurantWithFoodItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurant: Restaurant): Long

    @Delete
    suspend fun delete(restaurant: Restaurant)
}
