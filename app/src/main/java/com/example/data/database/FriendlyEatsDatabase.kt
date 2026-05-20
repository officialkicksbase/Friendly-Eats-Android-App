package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.FriendlyEatsDao
import com.example.data.model.DinnerEvent
import com.example.data.model.Reservation
import com.example.data.model.Restaurant
import com.example.data.model.Review
import com.example.data.model.PrivateBookingRequest
import com.example.data.model.ChefProfile

@Database(entities = [Restaurant::class, DinnerEvent::class, Reservation::class, Review::class, PrivateBookingRequest::class, ChefProfile::class], version = 3, exportSchema = false)
abstract class FriendlyEatsDatabase : RoomDatabase() {

    abstract fun friendlyEatsDao(): FriendlyEatsDao

    companion object {
        @Volatile
        private var INSTANCE: FriendlyEatsDatabase? = null

        fun getDatabase(context: Context): FriendlyEatsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FriendlyEatsDatabase::class.java,
                    "friendly_eats_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
