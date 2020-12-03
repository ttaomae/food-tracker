package ttaomae.foodtracker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ttaomae.foodtracker.data.FoodDatabase
import ttaomae.foodtracker.data.FoodItemDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideFoodDatabase(@ApplicationContext context: Context): FoodDatabase {
        return FoodDatabase.getInstance(context)
    }

    @Provides
    fun provideFoodItemDao(foodDatabase: FoodDatabase): FoodItemDao {
        return foodDatabase.foodItemDao()
    }
}
