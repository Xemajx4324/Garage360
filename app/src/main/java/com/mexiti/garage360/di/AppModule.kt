package com.mexiti.garage360.di

import android.content.Context
import androidx.room.Room
import com.mexiti.garage360.room.ClientDao
import com.mexiti.garage360.room.GarageDatabase
import com.mexiti.garage360.room.WorkOrderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesGarageDatabase(@ApplicationContext context: Context): GarageDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = GarageDatabase::class.java,
            name = "garage_db" // Nombre de la BD
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesClientDao(garageDatabase: GarageDatabase): ClientDao {
        return garageDatabase.clientDao()
    }

    @Singleton
    @Provides
    fun providesWorkOrderDao(garageDatabase: GarageDatabase): WorkOrderDao {
        return garageDatabase.workOrderDao()
    }

}
