package com.example.data.dao

import androidx.room.*
import com.example.data.model.DinnerEvent
import com.example.data.model.Reservation
import com.example.data.model.Restaurant
import com.example.data.model.Review
import com.example.data.model.PrivateBookingRequest
import com.example.data.model.ChefProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendlyEatsDao {

    // --- Restaurants ---
    @Query("SELECT * FROM restaurants ORDER BY rating DESC")
    fun getAllRestaurants(): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurants")
    suspend fun getRestaurantsList(): List<Restaurant>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getRestaurantById(id: Int): Restaurant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: Restaurant): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurants(restaurants: List<Restaurant>)

    // --- Dinner Events ---
    @Query("SELECT * FROM dinner_events ORDER BY id DESC")
    fun getAllDinnerEvents(): Flow<List<DinnerEvent>>

    @Query("SELECT * FROM dinner_events")
    suspend fun getDinnerEventsList(): List<DinnerEvent>

    @Query("SELECT * FROM dinner_events WHERE restaurantId = :restaurantId ORDER BY id DESC")
    fun getDinnerEventsForRestaurant(restaurantId: Int): Flow<List<DinnerEvent>>

    @Query("SELECT * FROM dinner_events WHERE id = :id")
    suspend fun getDinnerEventById(id: Int): DinnerEvent?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDinnerEvent(dinnerEvent: DinnerEvent): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDinnerEvents(dinnerEvents: List<DinnerEvent>)

    @Update
    suspend fun updateDinnerEvent(dinnerEvent: DinnerEvent)

    // --- Reservations ---
    @Query("SELECT * FROM reservations ORDER BY bookedAt DESC")
    fun getAllReservations(): Flow<List<Reservation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: Reservation): Long

    @Query("DELETE FROM reservations WHERE id = :id")
    suspend fun cancelReservation(id: Int)

    // --- Reviews ---
    @Query("SELECT * FROM reviews ORDER BY id DESC")
    fun getAllReviews(): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review): Long

    // --- Private Bookings ---
    @Query("SELECT * FROM private_bookings ORDER BY bookedAt DESC")
    fun getAllPrivateBookings(): Flow<List<PrivateBookingRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrivateBooking(booking: PrivateBookingRequest): Long

    @Update
    suspend fun updatePrivateBooking(booking: PrivateBookingRequest)

    // --- Chef Profiles ---
    @Query("SELECT * FROM chef_profiles")
    fun getAllChefProfiles(): Flow<List<ChefProfile>>

    @Query("SELECT * FROM chef_profiles")
    suspend fun getChefProfilesList(): List<ChefProfile>

    @Query("SELECT * FROM chef_profiles WHERE chefName = :chefName LIMIT 1")
    suspend fun getChefProfileByName(chefName: String): ChefProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChefProfile(profile: ChefProfile): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChefProfiles(profiles: List<ChefProfile>)
}
