package ttaomae.foodtracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ttaomae.foodtracker.data.FoodDaoRepository
import ttaomae.foodtracker.data.FoodItemDao
import ttaomae.foodtracker.data.FoodRepository

@Module
@InstallIn(ActivityComponent::class)
class RepositoryModule {
    @Provides
    fun providesFoodRepository(foodItemDao: FoodItemDao): FoodRepository {
        return FoodDaoRepository(foodItemDao)
    }
}
