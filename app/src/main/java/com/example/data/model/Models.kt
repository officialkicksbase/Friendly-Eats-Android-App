package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class Restaurant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val city: String,
    val state: String,
    val address: String,
    val cuisineType: String,
    val rating: Double,
    val imageUrl: String,
    val description: String,
    val capacityLimit: Int = 50
)

@Entity(tableName = "dinner_events")
data class DinnerEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val restaurantId: Int,
    val title: String,
    val chefName: String,
    val chefBio: String,
    val chefImageUrl: String,
    val dateString: String,
    val timeString: String,
    val description: String,
    val appetizer: String,
    val mainCourse: String,
    val dessert: String,
    val pairings: String,
    val price: Double,
    val maxSeats: Int,
    val bookedSeats: Int = 0,
    val isAiGenerated: Boolean = false
)

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dinnerEventId: Int,
    val restaurantName: String,
    val eventTitle: String,
    val chefName: String,
    val dateString: String,
    val timeString: String,
    val userName: String,
    val userEmail: String,
    val bookedSeats: Int,
    val bookedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val targetId: Int, // ID of Restaurant, or ID of Chef/Guest (depending on type)
    val targetType: String, // "RESTAURANT", "CHEF", "GUEST"
    val targetName: String, // Name of Restaurant, Chef, or Guest
    val rating: Float,
    val comment: String,
    val reviewerName: String,
    val reviewerEmail: String,
    val dateString: String = "2026-05-20"
)

@Entity(tableName = "private_bookings")
data class PrivateBookingRequest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val restaurantId: Int,
    val restaurantName: String,
    val dateString: String,
    val timeString: String,
    val guestCount: Int,
    val specialRequirements: String,
    val userName: String,
    val userEmail: String,
    val status: String = "Pending", // "Pending", "Approved", "Declined"
    val bookedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chef_profiles")
data class ChefProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chefName: String,
    val bio: String,
    val specialties: String, // e.g. "Italian, Pastry, Vegan"
    val dishPhotos: String, // Semicolon-separated URLs or description
    val websiteUrl: String,
    val socialMediaUrl: String,
    val imageUrl: String
)

