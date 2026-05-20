package com.example.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.DinnerEvent
import com.example.data.model.Restaurant
import com.example.ui.screens.*
import com.example.ui.viewmodel.FriendlyEatsViewModel

sealed class AppScreen {
    object Explore : AppScreen()
    data class RestaurantDetail(val restaurant: Restaurant) : AppScreen()
    data class EventDetail(val event: DinnerEvent, val restaurant: Restaurant) : AppScreen()
    object Bookings : AppScreen()
    data class Host(val preselectedRestaurant: Restaurant? = null) : AppScreen()
    object Settings : AppScreen()
    data class ChefProfileDetail(val chefName: String) : AppScreen()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FriendlyEatsApp(
    viewModel: FriendlyEatsViewModel = viewModel()
) {
    val context = LocalContext.current

    // Observe DB States
    val restaurants by viewModel.restaurants.collectAsStateWithLifecycle()
    val dinnerEvents by viewModel.dinnerEvents.collectAsStateWithLifecycle()
    val reservations by viewModel.reservations.collectAsStateWithLifecycle()
    val reviews by viewModel.reviews.collectAsStateWithLifecycle()
    val privateBookings by viewModel.privateBookings.collectAsStateWithLifecycle()
    val chefProfiles by viewModel.chefProfiles.collectAsStateWithLifecycle()

    // Observe UI Action States
    val isGenerating by viewModel.isAiGenerating.collectAsStateWithLifecycle()
    val actionError by viewModel.actionErrorMessage.collectAsStateWithLifecycle()

    // Settings Profile Values
    val userSettings = viewModel.userSettings
    var currentName by remember { mutableStateOf(userSettings.userName) }
    var currentEmail by remember { mutableStateOf(userSettings.userEmail) }
    var currentKey by remember { mutableStateOf(userSettings.geminiApiKey) }

    // Navigation Stack State
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Explore) }
    val screenHistory = remember { mutableStateListOf<AppScreen>() }

    // Safe navigation helper
    fun navigateTo(screen: AppScreen) {
        screenHistory.add(currentScreen)
        currentScreen = screen
    }

    // Back handling helper
    fun navigateBack() {
        if (screenHistory.isNotEmpty()) {
            currentScreen = screenHistory.removeLast()
        } else if (currentScreen != AppScreen.Explore) {
            currentScreen = AppScreen.Explore
        }
    }

    // Intercept hardware Android back button
    BackHandler(enabled = currentScreen != AppScreen.Explore) {
        navigateBack()
    }

    // Sync state for Settings updates
    fun updateLocalCache() {
        currentName = userSettings.userName
        currentEmail = userSettings.userEmail
        currentKey = userSettings.geminiApiKey
    }

    // Launch error toasts gracefully
    LaunchedEffect(actionError) {
        actionError?.let {
            Toast.makeText(context, "AI Chef Alert: $it", Toast.LENGTH_LONG).show()
            viewModel.clearActionError()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("app_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavigationBarItem(
                    selected = currentScreen is AppScreen.Explore || currentScreen is AppScreen.RestaurantDetail || currentScreen is AppScreen.EventDetail,
                    onClick = {
                        screenHistory.clear()
                        currentScreen = AppScreen.Explore
                    },
                    icon = { Icon(Icons.Default.Restaurant, contentDescription = "Explore restaurants selector") },
                    label = { Text("Explore") },
                    modifier = Modifier.testTag("nav_explore_tab"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                NavigationBarItem(
                    selected = currentScreen is AppScreen.Bookings,
                    onClick = {
                        screenHistory.clear()
                        currentScreen = AppScreen.Bookings
                    },
                    icon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = "Bookings summary selector") },
                    label = { Text("My Bookings") },
                    modifier = Modifier.testTag("nav_bookings_tab"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                NavigationBarItem(
                    selected = currentScreen is AppScreen.Host,
                    onClick = {
                        screenHistory.clear()
                        currentScreen = AppScreen.Host()
                    },
                    icon = { Icon(Icons.Default.SoupKitchen, contentDescription = "Host chef portal") },
                    label = { Text("Host Diner") },
                    modifier = Modifier.testTag("nav_host_tab"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                NavigationBarItem(
                    selected = currentScreen is AppScreen.Settings,
                    onClick = {
                        screenHistory.clear()
                        currentScreen = AppScreen.Settings
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Gourmet settings profile") },
                    label = { Text("Profile") },
                    modifier = Modifier.testTag("nav_settings_tab"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    (fadeIn() + slideInHorizontally { width -> width / 4 }).togetherWith(
                        fadeOut() + slideOutHorizontally { width -> -width / 4 }
                    )
                },
                label = "ScreenTransition"
            ) { state ->
                when (state) {
                    is AppScreen.Explore -> {
                        ExploreScreen(
                            restaurants = restaurants,
                            reviews = reviews,
                            dinnerEvents = dinnerEvents,
                            onRestaurantSelected = { restaurant ->
                                navigateTo(AppScreen.RestaurantDetail(restaurant))
                            }
                        )
                    }

                    is AppScreen.RestaurantDetail -> {
                        val eventsThisRestaurant = dinnerEvents.filter { it.restaurantId == state.restaurant.id }
                        RestaurantDetailScreen(
                            restaurant = state.restaurant,
                            dinnerEvents = eventsThisRestaurant,
                            reviews = reviews,
                            onBack = { navigateBack() },
                            onEventSelected = { event ->
                                navigateTo(AppScreen.EventDetail(event, state.restaurant))
                            },
                            onHostHere = {
                                navigateTo(AppScreen.Host(state.restaurant))
                            },
                            onChefSelected = { name ->
                                navigateTo(AppScreen.ChefProfileDetail(name))
                            },
                            onSubmitPrivateBooking = { dateStr, timeStr, guestsCount, specs ->
                                viewModel.submitPrivateBooking(
                                    restaurantId = state.restaurant.id,
                                    restaurantName = state.restaurant.name,
                                    dateString = dateStr,
                                    timeString = timeStr,
                                    guestCount = guestsCount,
                                    specialRequirements = specs,
                                    onSuccess = {
                                        Toast.makeText(context, "Private dining request submitted!", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        )
                    }

                    is AppScreen.EventDetail -> {
                        EventDetailScreen(
                            event = state.event,
                            restaurant = state.restaurant,
                            initialUserName = currentName,
                            initialUserEmail = currentEmail,
                            onBack = { navigateBack() },
                            onBookSeats = { count, name, email ->
                                viewModel.bookDinnerSeat(
                                    event = state.event,
                                    seatsCount = count,
                                    onSuccess = {
                                        Toast.makeText(context, "Dining Seats Booked Successfully!", Toast.LENGTH_SHORT).show()
                                        navigateTo(AppScreen.Bookings)
                                    },
                                    onError = { error ->
                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                    }
                                )
                            }
                        )
                    }

                    is AppScreen.Bookings -> {
                        MyBookingsScreen(
                            reservations = reservations,
                            privateBookings = privateBookings,
                            onCancelReservation = { reservation ->
                                viewModel.cancelReservation(reservation)
                                Toast.makeText(context, "Table reservation cancelled successfully.", Toast.LENGTH_SHORT).show()
                            },
                            onSubmitReview = { targetId, rType, targetName, rating, comment ->
                                viewModel.submitReview(
                                    targetId = targetId,
                                    targetType = rType,
                                    targetName = targetName,
                                    rating = rating,
                                    comment = comment,
                                    onSuccess = {
                                        Toast.makeText(context, "Review Published!", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        )
                    }

                    is AppScreen.Host -> {
                        val matchedProfile = chefProfiles.find { it.chefName.equals(currentName, ignoreCase = true) }
                        HostEventScreen(
                            restaurants = restaurants,
                            preselectedRestaurant = state.preselectedRestaurant,
                            hasCustomApiKey = currentKey.isNotBlank(),
                            isGenerating = isGenerating,
                            chefProfile = matchedProfile,
                            reservations = reservations,
                            chefName = currentName,
                            onPublish = { venue, eventTitle, concept, price, capacity, dateStr, timeStr, useAi ->
                                viewModel.hostDinnerEvent(
                                    restaurant = venue,
                                    title = eventTitle,
                                    chefConcept = concept,
                                    price = price,
                                    maxSeats = capacity,
                                    dateString = dateStr,
                                    timeString = timeStr,
                                    useAi = useAi,
                                    onSuccess = {
                                        Toast.makeText(context, "Private Tasting Published!", Toast.LENGTH_SHORT).show()
                                        screenHistory.clear()
                                        currentScreen = AppScreen.Explore
                                    },
                                    onInfoMessage = { msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                    }
                                )
                            },
                            onSaveChefProfile = { profile ->
                                viewModel.saveChefProfile(
                                    chefName = profile.chefName,
                                    bio = profile.bio,
                                    specialties = profile.specialties,
                                    dishPhotos = profile.dishPhotos,
                                    websiteUrl = profile.websiteUrl,
                                    socialMediaUrl = profile.socialMediaUrl,
                                    imageUrl = profile.imageUrl,
                                    onSuccess = {}
                                )
                            },
                            onSubmitReview = { targetId, rType, targetName, rating, comment ->
                                viewModel.submitReview(
                                    targetId = targetId,
                                    targetType = rType,
                                    targetName = targetName,
                                    rating = rating,
                                    comment = comment,
                                    onSuccess = {}
                                )
                            }
                        )
                    }

                    is AppScreen.Settings -> {
                        ProfileSettingsScreen(
                            currentName = currentName,
                            currentEmail = currentEmail,
                            currentKey = currentKey,
                            onSave = { name, email, key ->
                                viewModel.saveProfile(name, email, key)
                                updateLocalCache()
                            }
                        )
                    }

                    is AppScreen.ChefProfileDetail -> {
                        val matchedChef = chefProfiles.find { it.chefName.equals(state.chefName, ignoreCase = true) }
                        val chefReviews = reviews.filter { it.targetType == "CHEF" && it.targetName.equals(state.chefName, ignoreCase = true) }
                        ChefProfileDetailScreen(
                            chefName = state.chefName,
                            chefProfile = matchedChef,
                            chefReviews = chefReviews,
                            onBack = { navigateBack() }
                        )
                    }
                }
            }
        }
    }
}
