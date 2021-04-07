package ttaomae.foodtracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ttaomae.foodtracker.data.FoodItemDao
import ttaomae.foodtracker.data.FoodItemDaoRepository
import ttaomae.foodtracker.data.FoodItemRepository
import ttaomae.foodtracker.data.RestaurantDao
import ttaomae.foodtracker.data.RestaurantDaoRepository
import ttaomae.foodtracker.data.RestaurantRepository

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideFoodRepository(foodItemDao: FoodItemDao): FoodItemRepository {
        return FoodItemDaoRepository(foodItemDao)
    }

    @Provides
    fun provideRestaurantRepository(restaurantDao: RestaurantDao): RestaurantRepository {
        return RestaurantDaoRepository(restaurantDao)
    }
}
