package ttaomae.foodtracker.data

import javax.inject.Inject
import javax.inject.Singleton

interface FoodRepository {
    suspend fun get(id: Long): FoodItem?
    suspend fun getAll(): List<FoodItem>
    suspend fun save(item: FoodItem)
}

@Singleton
class FoodDaoRepository @Inject constructor(private val foodItemDao: FoodItemDao) : FoodRepository {
    override suspend fun get(id: Long): FoodItem? =  foodItemDao.findById(id)
    override suspend fun getAll(): List<FoodItem> = foodItemDao.findAll()
    override suspend fun save(item: FoodItem) = foodItemDao.insertFoodItem(item)
}
