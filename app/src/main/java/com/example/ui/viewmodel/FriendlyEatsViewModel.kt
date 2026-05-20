package com.example.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.FriendlyEatsDatabase
import com.example.data.model.DinnerEvent
import com.example.data.model.Reservation
import com.example.data.model.Restaurant
import com.example.data.model.Review
import com.example.data.model.PrivateBookingRequest
import com.example.data.model.ChefProfile
import com.example.data.repository.FriendlyEatsRepository
import com.example.data.api.GeminiClient
import com.example.data.api.GeneratedMenuResult
import com.example.data.settings.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FriendlyEatsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FriendlyEatsDatabase.getDatabase(application)
    private val repository = FriendlyEatsRepository(database.friendlyEatsDao())
    val userSettings = UserSettings(application)

    // --- Core Database Flows ---
    val restaurants: StateFlow<List<Restaurant>> = repository.restaurants
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val dinnerEvents: StateFlow<List<DinnerEvent>> = repository.dinnerEvents
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val reservations: StateFlow<List<Reservation>> = repository.reservations
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val reviews: StateFlow<List<Review>> = repository.reviews
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val privateBookings: StateFlow<List<PrivateBookingRequest>> = repository.privateBookings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val chefProfiles: StateFlow<List<ChefProfile>> = repository.chefProfiles
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- UI Selection States ---
    private val _selectedRestaurant = MutableStateFlow<Restaurant?>(null)
    val selectedRestaurant: StateFlow<Restaurant?> = _selectedRestaurant.asStateFlow()

    private val _selectedDinnerEvent = MutableStateFlow<DinnerEvent?>(null)
    val selectedDinnerEvent: StateFlow<DinnerEvent?> = _selectedDinnerEvent.asStateFlow()

    // --- Code Generation & UI Action feedback ---
    private val _isAiGenerating = MutableStateFlow(false)
    val isAiGenerating: StateFlow<Boolean> = _isAiGenerating.asStateFlow()

    private val _actionErrorMessage = MutableStateFlow<String?>(null)
    val actionErrorMessage: StateFlow<String?> = _actionErrorMessage.asStateFlow()

    init {
        // Seed initial data if first run
        viewModelScope.launch {
            if (!userSettings.hasSeeded) {
                repository.checkAndSeedDatabase()
                userSettings.hasSeeded = true
            } else {
                // Ensure checking backup seeds
                repository.checkAndSeedDatabase()
            }
        }
    }

    fun selectRestaurant(restaurant: Restaurant?) {
        _selectedRestaurant.value = restaurant
    }

    fun selectDinnerEvent(event: DinnerEvent?) {
        _selectedDinnerEvent.value = event
    }

    fun clearActionError() {
        _actionErrorMessage.value = null
    }

    /**
     * Book a seat at an outstanding chef event
     */
    fun bookDinnerSeat(
        event: DinnerEvent,
        seatsCount: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val name = userSettings.userName
            val email = userSettings.userEmail
            val success = repository.createReservation(
                dinnerEventId = event.id,
                userName = name,
                userEmail = email,
                seatsToBook = seatsCount
            )
            if (success) {
                // Refresh the current selected event state
                val freshEvent = repository.getDinnerEventById(event.id)
                if (freshEvent != null) {
                    _selectedDinnerEvent.value = freshEvent
                }
                onSuccess()
            } else {
                onError("Failed to book seats. Not enough seats remaining or event is full.")
            }
        }
    }

    /**
     * Cancel an existing reservation
     */
    fun cancelReservation(reservation: Reservation) {
        viewModelScope.launch {
            repository.cancelReservation(
                reservationId = reservation.id,
                dinnerEventId = reservation.dinnerEventId,
                seatsRefunded = reservation.bookedSeats
            )
            // If the deleted event was selected, refresh it
            val selected = _selectedDinnerEvent.value
            if (selected != null && selected.id == reservation.dinnerEventId) {
                val freshEvent = repository.getDinnerEventById(selected.id)
                _selectedDinnerEvent.value = freshEvent
            }
        }
    }

    /**
     * Updates user's locally stored profile info
     */
    fun saveProfile(name: String, email: String, apiKey: String) {
        userSettings.userName = name
        userSettings.userEmail = email
        userSettings.geminiApiKey = apiKey
    }

    /**
     * Create/Publish a private dinner event at a restaurant.
     * Includes a chef menu generated dynamically using the Gemini API.
     */
    fun hostDinnerEvent(
        restaurant: Restaurant,
        title: String,
        chefConcept: String,
        price: Double,
        maxSeats: Int,
        dateString: String,
        timeString: String,
        useAi: Boolean,
        onSuccess: () -> Unit,
        onInfoMessage: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isAiGenerating.value = true
            _actionErrorMessage.value = null

            var app = "Chef's Selected Amuse-Bouche"
            var main = "Artisanal Wood-Fired Special Signature Course"
            var des = "Sweet Tasting Confection from the Pastry Studio"
            var pairs = "Custom Wine Blend paired to modern preference"
            var wasAiSuccessful = false

            if (useAi) {
                val localKey = userSettings.geminiApiKey.trim()
                val apiResponse = GeminiClient.generateMenu(
                    chefConcept = chefConcept,
                    cuisineType = restaurant.cuisineType,
                    restaurantName = restaurant.name,
                    userCustomKey = if (localKey.isNotEmpty()) localKey else null
                )

                when (apiResponse) {
                    is GeneratedMenuResult.Success -> {
                        app = apiResponse.appetizer
                        main = apiResponse.mainCourse
                        des = apiResponse.dessert
                        pairs = apiResponse.pairings
                        wasAiSuccessful = true
                        onInfoMessage("Epic Menu generated successfully using Chef AI!")
                    }
                    is GeneratedMenuResult.Error -> {
                        // Graciously handle missing API keys or backend issues
                        Log.w("FriendlyEats", "Gemini Menu Fallback initialized. Cause: ${apiResponse.errorMessage}")
                        _actionErrorMessage.value = apiResponse.errorMessage
                        
                        // Dynamic local fallback based on cuisine
                        val (fApp, fMain, fDes, fPairs) = createFallbackGourmetMenu(restaurant.cuisineType, chefConcept)
                        app = fApp
                        main = fMain
                        des = fDes
                        pairs = fPairs
                        onInfoMessage("Hosted successfully using Local Gourmet Menu pairing (Chef AI requires API key).")
                    }
                }
            } else {
                // Non-AI manual menu text
                val (fApp, fMain, fDes, fPairs) = createFallbackGourmetMenu(restaurant.cuisineType, chefConcept)
                app = fApp
                main = fMain
                des = fDes
                pairs = fPairs
                onInfoMessage("Hosted private dinner using custom manual details.")
            }

            val newEvent = DinnerEvent(
                restaurantId = restaurant.id,
                title = title.ifBlank { "Exclusive Dining Concept" },
                chefName = userSettings.userName,
                chefBio = "An entrepreneurial culinary artist specialized in ${restaurant.cuisineType}. Concept: $chefConcept.",
                chefImageUrl = "https://images.unsplash.com/photo-1595273670150-db0a3e368157?auto=format&fit=crop&w=300&q=80",
                dateString = dateString.ifBlank { "2026-07-04" },
                timeString = timeString.ifBlank { "19:00" },
                description = chefConcept,
                appetizer = app,
                mainCourse = main,
                dessert = des,
                pairings = pairs,
                price = if (price <= 0) 120.00 else price,
                maxSeats = if (maxSeats <= 0) 10 else maxSeats,
                bookedSeats = 0,
                isAiGenerated = wasAiSuccessful
            )

            repository.createDinnerEvent(newEvent)
            _isAiGenerating.value = false
            onSuccess()
        }
    }

    private fun createFallbackGourmetMenu(cuisine: String, concept: String): List<String> {
        val conceptLower = concept.lowercase()
        return when {
            cuisine.contains("French", ignoreCase = true) -> listOf(
                "Herb-Encrusted Escargot in compound garlic butter with double-baked brioche toast.",
                "Classic French Duck à l'Orange served with honeyed glazed carrots and crispy scalloped potatoes.",
                "Delicate Chocolate Soufflé with Grand Marnier sauce and orange zest cream.",
                "2019 Cabernet Sauvignon and Aged Port selection"
            )
            cuisine.contains("Italian", ignoreCase = true) || conceptLower.contains("pasta") -> listOf(
                "House-baked Focaccia served with emulsified rosemary glaze and soft buffalo mozzarella.",
                "Handcrafted Pappardelle tossed in slow-cooked wild boar ragù and aged parmigiano-reggiano.",
                "Espresso-steeped Tiramisu with Madagascar vanilla bean mascarpone and dusty cocoa dust.",
                "Chianti Classico Riserva and Cold Amaro digestif"
            )
            cuisine.contains("Japanese", ignoreCase = true) || conceptLower.contains("sushi") -> listOf(
                "Crispy Tempura Shrimp tossed in spicy yuzu aioli and toasted sesame seed pearls.",
                "Modern Seared Tuna Omakase featuring sweet unagi tare and pickled wasabi greens.",
                "Light Matcha Gelato accompanied by toasted sweet adzuki paste and matcha crisps.",
                "Premium Cold Junmai Daiginjo Sake"
            )
            cuisine.contains("Seafood", ignoreCase = true) || conceptLower.contains("fish") -> listOf(
                "Chilled Pacific Oysters with a champagne cucumber mignonette and pink peppercorns.",
                "Herb-basted Wild Halibut fillet over buttered asparagus spears and rich saffron broth.",
                "Salted Rosemary Caramel Tart served with house-made vanilla clotted whipped cream.",
                "Cru Sancerre Sauvignon Blanc and Ginger Beer mocktail"
            )
            cuisine.contains("Jamaican", ignoreCase = true) || cuisine.contains("Caribbean", ignoreCase = true) -> listOf(
                "Spiced Jerk Chicken Empanadas with mango chutney and toasted coconut flakes.",
                "Chef's Landmark Goat Curry or Jerk Lobster served with traditional red beans and rice.",
                "Mount Gay Rum Bundt Cake served with lime zest double cream and roasted pineapple wedges.",
                "Handcrafted Jamaican Sorrel Punch and Red Stripe Lager"
            )
            else -> listOf(
                "Artisanal garden toast loaded with seasonal heirloom tomatoes, basil, and aged modena crema.",
                "Chef's Signature pan-roasted tenderloin served alongside forest mushrooms and roasted sweet fingerlings.",
                "Deconstructed lemon tart topped with fresh toasted meringue and wild marionberries.",
                "Fine Dry Reserve Wine selection tailored by the kitchen team"
            )
        }
    }

    // --- Reviews Management ---
    fun submitReview(
        targetId: Int,
        targetType: String,
        targetName: String,
        rating: Float,
        comment: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val review = Review(
                targetId = targetId,
                targetType = targetType,
                targetName = targetName,
                rating = rating,
                comment = comment,
                reviewerName = userSettings.userName,
                reviewerEmail = userSettings.userEmail
            )
            repository.createReview(review)
            onSuccess()
        }
    }

    // --- Private Bookings System ---
    fun submitPrivateBooking(
        restaurantId: Int,
        restaurantName: String,
        dateString: String,
        timeString: String,
        guestCount: Int,
        specialRequirements: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val booking = PrivateBookingRequest(
                restaurantId = restaurantId,
                restaurantName = restaurantName,
                dateString = dateString,
                timeString = timeString,
                guestCount = guestCount,
                specialRequirements = specialRequirements,
                userName = userSettings.userName,
                userEmail = userSettings.userEmail
            )
            repository.submitPrivateBooking(booking)
            onSuccess()
        }
    }

    // --- Chef Profiles ---
    fun getChefProfileByName(chefName: String, onResult: (ChefProfile?) -> Unit) {
        viewModelScope.launch {
            val profile = repository.getChefProfileByName(chefName)
            onResult(profile)
        }
    }

    fun saveChefProfile(
        chefName: String,
        bio: String,
        specialties: String,
        dishPhotos: String,
        websiteUrl: String,
        socialMediaUrl: String,
        imageUrl: String = "https://images.unsplash.com/photo-1595273670150-db0a3e368157?auto=format&fit=crop&w=300&q=80",
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val existing = repository.getChefProfileByName(chefName)
            val profile = ChefProfile(
                id = existing?.id ?: 0,
                chefName = chefName,
                bio = bio,
                specialties = specialties,
                dishPhotos = dishPhotos,
                websiteUrl = websiteUrl,
                socialMediaUrl = socialMediaUrl,
                imageUrl = imageUrl
            )
            repository.saveChefProfile(profile)
            onSuccess()
        }
    }
}
