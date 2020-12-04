package ttaomae.foodtracker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ttaomae.foodtracker.data.AppDatabase
import ttaomae.foodtracker.data.FoodItemDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideFoodItemDao(appDatabase: AppDatabase): FoodItemDao {
        return appDatabase.foodItemDao()
    }
}
