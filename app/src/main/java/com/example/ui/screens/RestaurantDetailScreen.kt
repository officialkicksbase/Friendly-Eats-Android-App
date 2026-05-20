package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.DinnerEvent
import com.example.data.model.Restaurant
import com.example.data.model.Review
import com.example.ui.theme.AccentLime
import com.example.ui.theme.HoneySecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    restaurant: Restaurant,
    dinnerEvents: List<DinnerEvent>,
    reviews: List<Review>,
    onBack: () -> Unit,
    onEventSelected: (DinnerEvent) -> Unit,
    onHostHere: () -> Unit,
    onChefSelected: (String) -> Unit,
    onSubmitPrivateBooking: (date: String, time: String, guests: Int, requirements: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showBookingDialog by remember { mutableStateOf(false) }
    var reqDate by remember { mutableStateOf("2026-07-15") }
    var reqTime by remember { mutableStateOf("19:00") }
    var reqGuests by remember { mutableStateOf("4") }
    var reqRequirements by remember { mutableStateOf("") }

    val resReviews = reviews.filter { it.targetType == "RESTAURANT" && it.targetId == restaurant.id }
    val avgRating = if (resReviews.isNotEmpty()) {
        resReviews.map { it.rating }.average()
    } else {
        restaurant.rating
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Hero Header Block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            PremiumImage(
                url = restaurant.imageUrl,
                contentDescription = restaurant.name,
                modifier = Modifier.fillMaxSize()
            )

            // Back button overlaid elegantly
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.5f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back back")
                }
            }

            // Category tag
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.65f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = restaurant.cuisineType,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Title & Info Card
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    RatingBadge(rating = avgRating)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Address link",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${restaurant.address}, ${restaurant.city}, ${restaurant.state}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = restaurant.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    lineHeight = 22.sp
                )
            }

            // Divider line
            item {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            }

            // Host Dinner CTA Section
            item {
                Card(
                    onClick = onHostHere,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Are you a private chef?",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Host your own private dinner event at ${restaurant.name}.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Button(
                            onClick = onHostHere,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Host Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Events List Header
            item {
                Text(
                    text = "Upcoming Private Dinners",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Render Events or Empty placeholder
            if (dinnerEvents.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "No events",
                                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No dinners listed. Be the first chef to host here!",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            } else {
                items(dinnerEvents) { event ->
                    DinnerEventRow(
                        event = event,
                        onClick = { onEventSelected(event) },
                        onChefClick = onChefSelected
                    )
                }
            }

            // Divider
            item {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            }

            // Request Custom Private Dinner Card/CTA
            item {
                Card(
                    onClick = { showBookingDialog = true },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("req_private_dinner_cta")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Request a Private Dinner",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Select guest count & special requirements, and submit custom requests.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        IconButton(
                            onClick = { showBookingDialog = true },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Request Custom Booking")
                        }
                    }
                }
            }

            // Divider
            item {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            }

            // Reviews Header
            item {
                Text(
                    text = "Reviews & Guest Feedback (${resReviews.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (resReviews.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No guest reviews available yet. Be the first to leave a feedback form!",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(resReviews) { r ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = r.reviewerName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                RatingBadge(rating = r.rating.toDouble())
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = r.comment,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (showBookingDialog) {
        AlertDialog(
            onDismissRequest = { showBookingDialog = false },
            title = { Text("Request Private Dinner") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Customize your private culinary event at ${restaurant.name}.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    OutlinedTextField(
                        value = reqDate,
                        onValueChange = { reqDate = it },
                        label = { Text("Event Date") },
                        placeholder = { Text("YYYY-MM-DD") },
                        modifier = Modifier.fillMaxWidth().testTag("booking_req_date"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = reqTime,
                        onValueChange = { reqTime = it },
                        label = { Text("Serving Time") },
                        placeholder = { Text("HH:MM") },
                        modifier = Modifier.fillMaxWidth().testTag("booking_req_time"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = reqGuests,
                        onValueChange = { reqGuests = it },
                        label = { Text("Number of Guests") },
                        placeholder = { Text("e.g. 8") },
                        modifier = Modifier.fillMaxWidth().testTag("booking_req_guests"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = reqRequirements,
                        onValueChange = { reqRequirements = it },
                        label = { Text("Special Requirements") },
                        placeholder = { Text("Describe specific requests such as vegan courses, organic ingredients...") },
                        modifier = Modifier.fillMaxWidth().height(100.dp).testTag("booking_req_specs"),
                        maxLines = 4
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val gNum = reqGuests.toIntOrNull() ?: 4
                        onSubmitPrivateBooking(reqDate, reqTime, gNum, reqRequirements)
                        showBookingDialog = false
                    },
                    modifier = Modifier.testTag("submit_private_booking_btn")
                ) {
                    Text("Submit Booking Request")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookingDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DinnerEventRow(
    event: DinnerEvent,
    onClick: () -> Unit,
    onChefClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val seatsLeft = event.maxSeats - event.bookedSeats
    val isFull = seatsLeft <= 0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("dinner_event_row_${event.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mini chef avatar - interactive
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable { onChefClick(event.chefName) }
            ) {
                AsyncImage(
                    model = event.chefImageUrl,
                    contentDescription = "Chef placeholder",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                // Interactive Chef Profile Link
                Text(
                    text = "Hosted by Chef ${event.chefName}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onChefClick(event.chefName) }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Date and time",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${event.dateString} at ${event.timeString}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Pricing & Seats badge and button
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$${String.format("%.0f", event.price)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "per seat",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isFull) MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                            else AccentLime.copy(alpha = 0.1f)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isFull) "SOLD OUT" else "$seatsLeft seats left",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFull) MaterialTheme.colorScheme.error else AccentLime
                    )
                }
            }
        }
    }
}
