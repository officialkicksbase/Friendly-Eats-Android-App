package com.example.data.repository

import com.example.data.dao.FriendlyEatsDao
import com.example.data.database.DatabaseSeeder
import com.example.data.model.DinnerEvent
import com.example.data.model.Reservation
import com.example.data.model.Restaurant
import com.example.data.model.Review
import com.example.data.model.PrivateBookingRequest
import com.example.data.model.ChefProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class FriendlyEatsRepository(private val dao: FriendlyEatsDao) {

    val restaurants: Flow<List<Restaurant>> = dao.getAllRestaurants()
    val dinnerEvents: Flow<List<DinnerEvent>> = dao.getAllDinnerEvents()
    val reservations: Flow<List<Reservation>> = dao.getAllReservations()
    val reviews: Flow<List<Review>> = dao.getAllReviews()
    val privateBookings: Flow<List<PrivateBookingRequest>> = dao.getAllPrivateBookings()
    val chefProfiles: Flow<List<ChefProfile>> = dao.getAllChefProfiles()

    fun getDinnerEventsForRestaurant(restaurantId: Int): Flow<List<DinnerEvent>> {
        return dao.getDinnerEventsForRestaurant(restaurantId)
    }

    suspend fun getRestaurantById(id: Int): Restaurant? = withContext(Dispatchers.IO) {
        dao.getRestaurantById(id)
    }

    suspend fun getDinnerEventById(id: Int): DinnerEvent? = withContext(Dispatchers.IO) {
        dao.getDinnerEventById(id)
    }

    suspend fun checkAndSeedDatabase() = withContext(Dispatchers.IO) {
        val currentRestaurants = dao.getRestaurantsList()
        if (currentRestaurants.isEmpty()) {
            dao.insertRestaurants(DatabaseSeeder.defaultRestaurants)
            dao.insertDinnerEvents(DatabaseSeeder.defaultDinnerEvents)
            dao.insertChefProfiles(DatabaseSeeder.defaultChefProfiles)
            DatabaseSeeder.defaultReviews.forEach { dao.insertReview(it) }
        } else {
            // Check for missing default restaurants and insert them
            val existingRestaurantIds = currentRestaurants.map { it.id }.toSet()
            val missingRestaurants = DatabaseSeeder.defaultRestaurants.filter { it.id !in existingRestaurantIds }
            if (missingRestaurants.isNotEmpty()) {
                dao.insertRestaurants(missingRestaurants)
            }

            // Check for missing default chef profiles and insert them
            val currentChefs = dao.getChefProfilesList()
            val existingChefNames = currentChefs.map { it.chefName.lowercase() }.toSet()
            val missingChefs = DatabaseSeeder.defaultChefProfiles.filter { it.chefName.lowercase() !in existingChefNames }
            if (missingChefs.isNotEmpty()) {
                dao.insertChefProfiles(missingChefs)
            }

            // Check for missing default dinner events and insert them
            val currentEvents = dao.getDinnerEventsList()
            val existingEventIds = currentEvents.map { it.id }.toSet()
            val missingEvents = DatabaseSeeder.defaultDinnerEvents.filter { it.id !in existingEventIds }
            if (missingEvents.isNotEmpty()) {
                dao.insertDinnerEvents(missingEvents)
            }
        }
    }

    suspend fun createDinnerEvent(event: DinnerEvent): Long = withContext(Dispatchers.IO) {
        dao.insertDinnerEvent(event)
    }

    suspend fun createReservation(
        dinnerEventId: Int,
        userName: String,
        userEmail: String,
        seatsToBook: Int
    ): Boolean = withContext(Dispatchers.IO) {
        val event = dao.getDinnerEventById(dinnerEventId) ?: return@withContext false
        val restaurant = dao.getRestaurantById(event.restaurantId) ?: return@withContext false

        if (event.bookedSeats + seatsToBook <= event.maxSeats) {
            // Update dinner event seats
            val updatedEvent = event.copy(bookedSeats = event.bookedSeats + seatsToBook)
            dao.updateDinnerEvent(updatedEvent)

            // Insert reservation record
            val reservation = Reservation(
                dinnerEventId = dinnerEventId,
                restaurantName = restaurant.name,
                eventTitle = event.title,
                chefName = event.chefName,
                dateString = event.dateString,
                timeString = event.timeString,
                userName = userName,
                userEmail = userEmail,
                bookedSeats = seatsToBook
            )
            dao.insertReservation(reservation)
            return@withContext true
        } else {
            return@withContext false
        }
    }

    suspend fun cancelReservation(reservationId: Int, dinnerEventId: Int, seatsRefunded: Int) = withContext(Dispatchers.IO) {
        dao.cancelReservation(reservationId)
        val event = dao.getDinnerEventById(dinnerEventId)
        if (event != null) {
            val newSeats = (event.bookedSeats - seatsRefunded).coerceAtLeast(0)
            dao.updateDinnerEvent(event.copy(bookedSeats = newSeats))
        }
    }

    // --- Reviews ---
    suspend fun createReview(review: Review): Long = withContext(Dispatchers.IO) {
        dao.insertReview(review)
    }

    // --- Private Dinner Bookings ---
    suspend fun submitPrivateBooking(booking: PrivateBookingRequest): Long = withContext(Dispatchers.IO) {
        dao.insertPrivateBooking(booking)
    }

    suspend fun updatePrivateBooking(booking: PrivateBookingRequest) = withContext(Dispatchers.IO) {
        dao.updatePrivateBooking(booking)
    }

    // --- Chef Profiles ---
    suspend fun getChefProfileByName(chefName: String): ChefProfile? = withContext(Dispatchers.IO) {
        dao.getChefProfileByName(chefName)
    }

    suspend fun saveChefProfile(profile: ChefProfile): Long = withContext(Dispatchers.IO) {
        dao.insertChefProfile(profile)
    }
}
