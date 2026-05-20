package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Restaurant
import com.example.ui.theme.HoneySecondary
import com.example.ui.theme.TerracottaPrimary

import com.example.data.model.Review
import com.example.data.model.DinnerEvent
import androidx.compose.material.icons.filled.CalendarMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    restaurants: List<Restaurant>,
    reviews: List<Review>,
    dinnerEvents: List<DinnerEvent>,
    onRestaurantSelected: (Restaurant) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCity by remember { mutableStateOf("All Locations") }
    var searchQuery by remember { mutableStateOf("") }
    var showAvailableOnly by remember { mutableStateOf(false) }

    val cities = listOf("All Locations", "New York City", "Brooklyn", "Buffalo", "Yountville", "Napa", "Dayton", "Stonewall", "Chicago", "Austin", "Los Angeles", "New Orleans", "Seattle", "Boston", "Portland", "Miami")

    val filteredRestaurants = restaurants.filter { restaurant ->
        val matchesCity = selectedCity == "All Locations" || restaurant.city.equals(selectedCity, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() || 
                restaurant.name.contains(searchQuery, ignoreCase = true) || 
                restaurant.cuisineType.contains(searchQuery, ignoreCase = true)
        val matchesAvailability = !showAvailableOnly || dinnerEvents.any { it.restaurantId == restaurant.id && (it.maxSeats - it.bookedSeats) > 0 }
        matchesCity && matchesSearch && matchesAvailability
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- Custom App Title Area ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CURRENT EXPEDITION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Friendly Eats",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Private gastronomics & tasting events",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Current Market",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // --- Custom Search & Filters Area ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search cuisines, restaurants...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("restaurant_search_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // City chips list
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(cities) { city ->
                    val isSelected = selectedCity == city
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCity = city },
                        label = { Text(city) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = MaterialTheme.colorScheme.outline,
                            selectedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Availability Filter Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Availability icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Show Only Available Dinners",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    )
                }
                Switch(
                    checked = showAvailableOnly,
                    onCheckedChange = { showAvailableOnly = it },
                    modifier = Modifier.testTag("show_available_only_switch")
                )
            }
        }

        // --- Restaurants List Area ---
        if (filteredRestaurants.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "No results",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No restaurants found matching restrictions.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(filteredRestaurants) { restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        reviews = reviews,
                        onClick = { onRestaurantSelected(restaurant) }
                    )
                }
            }
        }
    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    reviews: List<Review>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val resReviews = reviews.filter { it.targetType == "RESTAURANT" && it.targetId == restaurant.id }
    val avgRating = if (resReviews.isNotEmpty()) {
        resReviews.map { it.rating }.average()
    } else {
        restaurant.rating
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("restaurant_card_${restaurant.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                PremiumImage(
                    url = restaurant.imageUrl,
                    contentDescription = restaurant.name,
                    modifier = Modifier.fillMaxSize()
                )

                // Rating & City labels overlaid
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CuisineChip(cuisine = restaurant.cuisineType)
                    RatingBadge(rating = avgRating)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = restaurant.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location pin",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${restaurant.address}, ${restaurant.city}, ${restaurant.state}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.NavigateNext,
                        contentDescription = "View detail details",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = restaurant.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
