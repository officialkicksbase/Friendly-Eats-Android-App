package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Restaurant
import com.example.data.model.ChefProfile
import com.example.data.model.Reservation
import com.example.ui.theme.HoneySecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostEventScreen(
    restaurants: List<Restaurant>,
    preselectedRestaurant: Restaurant?,
    hasCustomApiKey: Boolean,
    isGenerating: Boolean,
    chefProfile: ChefProfile?,
    reservations: List<Reservation>,
    chefName: String,
    onPublish: (Restaurant, String, String, Double, Int, String, String, Boolean) -> Unit,
    onSaveChefProfile: (ChefProfile) -> Unit,
    onSubmitReview: (targetId: Int, targetType: String, targetName: String, rating: Float, comment: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) } // 0: Host Event, 1: Edit Chef Profile, 2: Review Guests

    // --- Tab 0: Input States for hosting event ---
    var selectedRestaurant by remember { mutableStateOf<Restaurant?>(preselectedRestaurant ?: restaurants.firstOrNull()) }
    var title by remember { mutableStateOf("") }
    var chefConcept by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("150") }
    var maxSeatsText by remember { mutableStateOf("10") }
    var dateText by remember { mutableStateOf("2026-06-25") }
    var timeText by remember { mutableStateOf("19:30") }
    var useChefAi by remember { mutableStateOf(true) }
    var expandedDropdown by remember { mutableStateOf(false) }

    // --- Tab 1: Input States for Chef Profile edit ---
    var bioText by remember { mutableStateOf(chefProfile?.bio ?: "Gourmet private dining chef based in the USA.") }
    var specialtiesText by remember { mutableStateOf(chefProfile?.specialties ?: "Italian, Sourdough, Pastry, Wine Pairing") }
    var dishPhotosText by remember { mutableStateOf(chefProfile?.dishPhotos ?: "Truffle Tagliolini; Meyer Lemon Soufflé; Hand-crimped Agnolotti") }
    var websiteUrlText by remember { mutableStateOf(chefProfile?.websiteUrl ?: "https://friendly-eats.com/chef") }
    var socialUrlText by remember { mutableStateOf(chefProfile?.socialMediaUrl ?: "https://instagram.com/friendly_eats") }
    var imageUrlText by remember { mutableStateOf(chefProfile?.imageUrl ?: "https://images.unsplash.com/photo-1595273670150-db0a3e368157?auto=format&fit=crop&w=300&q=80") }

    // Sync state if chefProfile is loaded
    LaunchedEffect(chefProfile) {
        chefProfile?.let {
            bioText = it.bio
            specialtiesText = it.specialties
            dishPhotosText = it.dishPhotos
            websiteUrlText = it.websiteUrl
            socialUrlText = it.socialMediaUrl
            imageUrlText = it.imageUrl
        }
    }

    // --- Tab 2: Guest Reviews States ---
    val chefGuests = reservations.filter { it.chefName.equals(chefName, ignoreCase = true) }
    var showReviewDinerDialog by remember { mutableStateOf(false) }
    var reviewTargetResId by remember { mutableStateOf(0) }
    var reviewTargetGuestName by remember { mutableStateOf("") }
    var ratingDinerStars by remember { mutableStateOf(5f) }
    var ratingDinerComment by remember { mutableStateOf("") }

    // Sync selected restaurant when dropdown selections change
    LaunchedEffect(preselectedRestaurant) {
        if (preselectedRestaurant != null) {
            selectedRestaurant = preselectedRestaurant
        } else if (selectedRestaurant == null && restaurants.isNotEmpty()) {
            selectedRestaurant = restaurants.first()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            ) {
                Column {
                    Text(
                        text = "CHEF WORKSPACE PORTAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Chef Portal",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Tabs controller
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Publish dinner", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("My profile", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Review guests (${chefGuests.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }

            // Container according to tab selection
            when (selectedTab) {
                0 -> {
                    // TAB 0: Host dinner drafting form
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .verticalScroll(scrollState)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Select Partner Restaurant",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable { expandedDropdown = true }
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = selectedRestaurant?.name ?: "No Venue Selected",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = selectedRestaurant?.let { "${it.cuisineType} • ${it.city}, ${it.state}" } ?: "Choose Location",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown venue selector",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            DropdownMenu(
                                expanded = expandedDropdown,
                                onDismissRequest = { expandedDropdown = false },
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                restaurants.forEach { venue ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(venue.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                                Text("${venue.cuisineType} • ${venue.city}, ${venue.state}", fontSize = 11.sp, color = Color.Gray)
                                            }
                                        },
                                        onClick = {
                                            selectedRestaurant = venue
                                            expandedDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Dinner Narrative & Concept",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Private Event Title") },
                            placeholder = { Text("e.g. Midnight Truffle Secrets") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("host_dinner_title"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = chefConcept,
                            onValueChange = { chefConcept = it },
                            label = { Text("Culinary Concept / Story") },
                            placeholder = { Text("Outline your focus ingredients, chef philosophy and course inspirations for attendees...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .testTag("host_dinner_concept"),
                            shape = RoundedCornerShape(8.dp),
                            maxLines = 4
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = dateText,
                                onValueChange = { dateText = it },
                                label = { Text("Target Date") },
                                placeholder = { Text("YYYY-MM-DD") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("host_dinner_date"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = timeText,
                                onValueChange = { timeText = it },
                                label = { Text("Serving Time") },
                                placeholder = { Text("HH:MM") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("host_dinner_time"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = priceText,
                                onValueChange = { priceText = it },
                                label = { Text("Price per Seat ($)") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("host_dinner_price"),
                                shape = RoundedCornerShape(8.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = maxSeatsText,
                                onValueChange = { maxSeatsText = it },
                                label = { Text("Max Seat Capacity") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("host_dinner_capacity"),
                                shape = RoundedCornerShape(8.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = "Chef AI icon",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "Architect Menu directly via AI",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "Generates pairing menu via Gemini instantly",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                            )
                                        }
                                    }

                                    Switch(
                                        checked = useChefAi,
                                        onCheckedChange = { useChefAi = it },
                                        modifier = Modifier.testTag("use_chef_ai_switch")
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                val priceVal = priceText.toDoubleOrNull() ?: 120.00
                                val seatsVal = maxSeatsText.toIntOrNull() ?: 10
                                val venue = selectedRestaurant

                                if (venue == null) {
                                    Toast.makeText(context, "Please choose a partner venue first.", Toast.LENGTH_SHORT).show()
                                } else if (title.isBlank() || chefConcept.isBlank()) {
                                    Toast.makeText(context, "Please fill in your dinner title and cooking narrative.", Toast.LENGTH_SHORT).show()
                                } else {
                                    onPublish(
                                        venue,
                                        title,
                                        chefConcept,
                                        priceVal,
                                        seatsVal,
                                        dateText,
                                        timeText,
                                        useChefAi
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("publish_event_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = if (useChefAi) "Architect & Publish Event" else "Draft & Publish Event",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                1 -> {
                    // TAB 1: Edit Chef Profile Specialties & Dishes
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "My Chef Profile Settings",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        OutlinedTextField(
                            value = bioText,
                            onValueChange = { bioText = it },
                            label = { Text("Chef Bio Description") },
                            placeholder = { Text("Describe your professional background, cooking styles, secret approaches...") },
                            modifier = Modifier.fillMaxWidth().height(100.dp).testTag("chef_edit_bio"),
                            shape = RoundedCornerShape(8.dp),
                            maxLines = 4
                        )

                        OutlinedTextField(
                            value = specialtiesText,
                            onValueChange = { specialtiesText = it },
                            label = { Text("My Specialties (comma separated)") },
                            placeholder = { Text("e.g. Italian, Vegan, Pastry, Sourdough") },
                            modifier = Modifier.fillMaxWidth().testTag("chef_edit_specialties"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = dishPhotosText,
                            onValueChange = { dishPhotosText = it },
                            label = { Text("Featured Food Dishes (separated by semicolon ';')") },
                            placeholder = { Text("e.g. Meyer Lemon Soufflé; Hand-rolled Gnocchi; Truffle Pasta") },
                            modifier = Modifier.fillMaxWidth().testTag("chef_edit_dishes"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = websiteUrlText,
                            onValueChange = { websiteUrlText = it },
                            label = { Text("My Website domain URL") },
                            modifier = Modifier.fillMaxWidth().testTag("chef_edit_website"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = socialUrlText,
                            onValueChange = { socialUrlText = it },
                            label = { Text("My Instagram URL") },
                            modifier = Modifier.fillMaxWidth().testTag("chef_edit_social"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = imageUrlText,
                            onValueChange = { imageUrlText = it },
                            label = { Text("Chef Avatar URL") },
                            modifier = Modifier.fillMaxWidth().testTag("chef_edit_avatar"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                val editedProfile = ChefProfile(
                                    id = chefProfile?.id ?: 0,
                                    chefName = chefName,
                                    bio = bioText,
                                    specialties = specialtiesText,
                                    dishPhotos = dishPhotosText,
                                    websiteUrl = websiteUrlText,
                                    socialMediaUrl = socialUrlText,
                                    imageUrl = imageUrlText
                                )
                                onSaveChefProfile(editedProfile)
                                Toast.makeText(context, "Chef Profile saved successfully!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("save_chef_profile_btn"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Save Chef Profile Attributes", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }

                2 -> {
                    // TAB 2: Review diner guests who reserved seats at Chef events
                    if (chefGuests.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = "No attendees",
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(50.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No guest bookings listed yet",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "When diner guests reserve seats at your hosted events, details and review forms will show up here.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.outline,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Attendee Dinner Guests (${chefGuests.size})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            chefGuests.forEach { guestRes ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "Guest Diner: ${guestRes.userName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Event: \"${guestRes.eventTitle}\" at ${guestRes.restaurantName}",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Reserved Seat Count: ${guestRes.bookedSeats} seats",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.outline
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Button(
                                            onClick = {
                                                reviewTargetResId = guestRes.id
                                                reviewTargetGuestName = guestRes.userName
                                                ratingDinerStars = 5f
                                                ratingDinerComment = ""
                                                showReviewDinerDialog = true
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                                contentColor = MaterialTheme.colorScheme.primary
                                            ),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                            modifier = Modifier.testTag("review_guest_btn_${guestRes.id}")
                                        ) {
                                            Icon(Icons.Default.Star, contentDescription = "Review guest feedback", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Review Diner Guest", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Shimmer loading card for AI menus
        AnimatedVisibility(
            visible = isGenerating,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Architecting Tasting Menu via Chef AI...",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Chef-to-Guest review popup dialog
        if (showReviewDinerDialog) {
            AlertDialog(
                onDismissRequest = { showReviewDinerDialog = false },
                title = { Text("Write Review for Guest Diner $reviewTargetGuestName") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Rate this attendee's promptness, friendliness, and etiquette.", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)

                        Text("Rating: ${ratingDinerStars.toInt()} Stars", fontWeight = FontWeight.Bold)
                        Slider(
                            value = ratingDinerStars,
                            onValueChange = { ratingDinerStars = it },
                            valueRange = 1f..5f,
                            steps = 3,
                            modifier = Modifier.testTag("guest_review_slider")
                        )

                        OutlinedTextField(
                            value = ratingDinerComment,
                            onValueChange = { ratingDinerComment = it },
                            label = { Text("Guest Review Feedback") },
                            placeholder = { Text("e.g. Friendly diner, respected timing, exciting culinary curiosity!") },
                            modifier = Modifier.fillMaxWidth().height(100.dp).testTag("guest_review_input"),
                            maxLines = 4
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (ratingDinerComment.isBlank()) {
                                Toast.makeText(context, "Review feedback comments cannot be empty.", Toast.LENGTH_SHORT).show()
                            } else {
                                onSubmitReview(
                                    reviewTargetResId,
                                    "GUEST",
                                    reviewTargetGuestName,
                                    ratingDinerStars,
                                    ratingDinerComment
                                )
                                showReviewDinerDialog = false
                                Toast.makeText(context, "Guest review recorded!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.testTag("submit_guest_review_btn")
                    ) {
                        Text("Publish Guest Review")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showReviewDinerDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
