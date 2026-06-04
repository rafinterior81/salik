package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.SulukDao
import com.example.data.entity.DailyJournal
import com.example.data.entity.ZikirCounter
import com.example.data.entity.KhalwatRetreat
import com.example.data.entity.CustomWorshipItem

@Database(
    entities = [
        DailyJournal::class,
        ZikirCounter::class,
        KhalwatRetreat::class,
        CustomWorshipItem::class
    ],
    version = 3,
    exportSchema = false
)
abstract class SulukDatabase : RoomDatabase() {
    abstract fun sulukDao(): SulukDao

    companion object {
        @Volatile
        private var INSTANCE: SulukDatabase? = null

        fun getDatabase(context: Context): SulukDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SulukDatabase::class.java,
                    "suluk_spiritual_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
