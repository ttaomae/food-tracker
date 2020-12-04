package ttaomae.foodtracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ttaomae.foodtracker.data.FoodItemDao
import ttaomae.foodtracker.data.FoodItemDaoRepository
import ttaomae.foodtracker.data.FoodItemRepository

@Module
@InstallIn(ActivityComponent::class)
class RepositoryModule {
    @Provides
    fun providesFoodRepository(foodItemDao: FoodItemDao): FoodItemRepository {
        return FoodItemDaoRepository(foodItemDao)
    }
}
