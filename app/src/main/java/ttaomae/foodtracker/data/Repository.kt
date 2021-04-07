package ttaomae.foodtracker.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface FoodItemRepository {
    suspend fun get(id: Long): FoodItem?
    suspend fun getAll(): List<FoodItem>
    suspend fun save(item: FoodItem): Long
    suspend fun delete(item: FoodItem)
}

@Singleton
class FoodItemDaoRepository @Inject constructor(private val foodItemDao: FoodItemDao) :
    FoodItemRepository {
    override suspend fun get(id: Long): FoodItem? = foodItemDao.findById(id)
    override suspend fun getAll(): List<FoodItem> = foodItemDao.findAll()
    override suspend fun save(item: FoodItem): Long = foodItemDao.insert(item)
    override suspend fun delete(item: FoodItem) = foodItemDao.delete(item)
}

interface RestaurantRepository {
    suspend fun get(id: Long): Restaurant?
    fun getAll(): Flow<List<Restaurant>>
    suspend fun getWithFoodItems(id: Long): RestaurantWithFoodItems
    fun getAllWithFoodItems(): Flow<List<RestaurantWithFoodItems>>
    suspend fun save(restuarant: Restaurant): Long
    suspend fun delete(restuarant: Restaurant)
}

@Singleton
class RestaurantDaoRepository @Inject constructor(private val restaurantDao: RestaurantDao) :
    RestaurantRepository {
    override suspend fun get(id: Long): Restaurant? = restaurantDao.findById(id)
    override fun getAll() = restaurantDao.findAll()
    override suspend fun getWithFoodItems(id: Long): RestaurantWithFoodItems =
        restaurantDao.findWithFoodItems(id)
    override fun getAllWithFoodItems(): Flow<List<RestaurantWithFoodItems>> =
        restaurantDao.findAllWithFoodItems()
    override suspend fun save(restuarant: Restaurant): Long = restaurantDao.insert(restuarant)
    override suspend fun delete(restuarant: Restaurant) = restaurantDao.delete(restuarant)
}
