package com.countrydelight.cdlogger.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 1. Add new columns
        db.execSQL("ALTER TABLE EventEntity ADD COLUMN appVersionName TEXT")
        db.execSQL("ALTER TABLE EventEntity ADD COLUMN appVersionCode INTEGER")
        // 2. Change eventName to nullable
        db.execSQL("CREATE TABLE EventEntity_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, eventName TEXT, eventData TEXT NOT NULL, createdAt INTEGER NOT NULL, appVersionName TEXT, appVersionCode INTEGER)")
        db.execSQL("INSERT INTO EventEntity_new (id, eventName, eventData, createdAt) SELECT id, eventName, eventData, createdAt FROM EventEntity")
        db.execSQL("DROP TABLE EventEntity")
        db.execSQL("ALTER TABLE EventEntity_new RENAME TO EventEntity")
    }
}