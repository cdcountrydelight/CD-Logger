package com.countrydelight.cdlogger.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.countrydelight.cdlogger.data.local.event.EventDao
import com.countrydelight.cdlogger.data.local.event.EventEntity


/**
 * The Room database for the CDLogger app.
 */
@Database(entities = [EventEntity::class], version = 2)
@TypeConverters(CDLoggerTypeConverters::class)
internal abstract class CDLoggerDatabase : RoomDatabase() {


    /**
     * Returns the EventDao for accessing the event table.
     */
    abstract fun getEventDao(): EventDao


    companion object {
        @Volatile
        private var INSTANCE: CDLoggerDatabase? = null

        fun get(context: Context): CDLoggerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CDLoggerDatabase::class.java,
                    "com.countrydelight.cdlogger.database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}