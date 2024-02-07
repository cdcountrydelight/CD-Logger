package com.countrydelight.cdlogger.data.local.event

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
internal interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvent(eventEntity: EventEntity): Long

    @Delete
    fun deleteEvent(event: EventEntity): Int


    @Query("SELECT * FROM EventEntity")
    fun getAllEvents(): List<EventEntity>
}