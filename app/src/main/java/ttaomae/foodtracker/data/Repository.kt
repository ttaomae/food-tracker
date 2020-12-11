package ttaomae.foodtracker.data

import javax.inject.Inject
import javax.inject.Singleton

interface FoodItemRepository {
    suspend fun get(id: Long): FoodItem?
    suspend fun getAll(): List<FoodItem>
    suspend fun save(item: FoodItem)
    suspend fun delete(item: FoodItem)
}

@Singleton
class FoodItemDaoRepository @Inject constructor(private val foodItemDao: FoodItemDao) :
    FoodItemRepository {
    override suspend fun get(id: Long): FoodItem? = foodItemDao.findById(id)
    override suspend fun getAll(): List<FoodItem> = foodItemDao.findAll()
    override suspend fun save(item: FoodItem) = foodItemDao.insert(item)
    override suspend fun delete(item: FoodItem) = foodItemDao.delete(item)
}

interface RestaurantRepository {
    suspend fun get(id: Long): Restaurant?
    suspend fun getAll(): List<Restaurant>
    suspend fun save(restuarant: Restaurant)
    suspend fun delete(restuarant: Restaurant)
}

@Singleton
class RestaurantDaoRepository @Inject constructor(private val restaurantDao: RestaurantDao) :
    RestaurantRepository {
    override suspend fun get(id: Long): Restaurant? = restaurantDao.findById(id)
    override suspend fun getAll(): List<Restaurant> = restaurantDao.findAll()
    override suspend fun save(restuarant: Restaurant) = restaurantDao.insert(restuarant)
    override suspend fun delete(restuarant: Restaurant) = restaurantDao.delete(restuarant)
}
