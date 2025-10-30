package com.mexiti.garage360.room

import androidx.room.*
import com.mexiti.garage360.model.Client
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Query("SELECT * FROM clients ORDER BY name ASC")
    fun getAllClients(): Flow<List<Client>>

    @Query("SELECT * FROM clients WHERE id = :id")
    fun getClientById(id: Long): Flow<Client?> // Puede ser nulo si no lo encuentra

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(client: Client)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(client: Client)

    @Delete
    suspend fun delete(client: Client)
}