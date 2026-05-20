package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Reservation
import com.example.data.model.PrivateBookingRequest
import com.example.ui.theme.HoneySecondary
import com.example.ui.theme.AccentLime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    reservations: List<Reservation>,
    privateBookings: List<PrivateBookingRequest>,
    onCancelReservation: (Reservation) -> Unit,
    onSubmitReview: (targetId: Int, targetType: String, targetName: String, rating: Float, comment: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) } // 0: Reserved Seats, 1: Private Invitations
    var reservationToCancel by remember { mutableStateOf<Reservation?>(null) }
    var showCancelDialog by remember { mutableStateOf(false) }

    // State for Leave Review Dialog
    var showReviewDialog by remember { mutableStateOf(false) }
    var reviewTargetType by remember { mutableStateOf("RESTAURANT") } // "RESTAURANT" or "CHEF"
    var reviewTargetId by remember { mutableStateOf(0) }
    var reviewTargetName by remember { mutableStateOf("") }
    var reviewRating by remember { mutableStateOf(5f) }
    var reviewComment by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App header block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Column {
                Text(
                    text = "RESERVATIONS & BOOKINGS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "My Gourmet Dining",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Browse reserved dining seats and review custom booking requests.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        // Tab Selector Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Reserved Events (${reservations.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Private Requests (${privateBookings.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedTab == 0) {
            // Reserved seats list
            if (reservations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = "No dining bookings yet",
                            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Your Dining Table is Empty",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Explore the Friendly Eats portal to reserve custom private dinners at exceptional locations near you.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reservations) { reservation ->
                        ReservationCard(
                            reservation = reservation,
                            onCancelClick = {
                                reservationToCancel = reservation
                                showCancelDialog = true
                            },
                            onReviewClick = { type ->
                                reviewTargetType = type
                                reviewTargetId = if (type == "RESTAURANT") reservation.dinnerEventId else 100 // default mock id for chef
                                reviewTargetName = if (type == "RESTAURANT") reservation.restaurantName else reservation.chefName
                                reviewRating = 5f
                                reviewComment = ""
                                showReviewDialog = true
                            }
                        )
                    }
                }
            }
        } else {
            // Custom Private Bookings list
            if (privateBookings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "No private requests",
                            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Private Requests Yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "When you request a custom private dinner on any restaurant's page, details and approval states will appear here.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(privateBookings) { booking ->
                        PrivateBookingCard(
                            booking = booking,
                            onReviewClick = { type ->
                                reviewTargetType = type
                                reviewTargetId = booking.restaurantId
                                reviewTargetName = booking.restaurantName
                                reviewRating = 5f
                                reviewComment = ""
                                showReviewDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Cancel reservation Dialog
    if (showCancelDialog && reservationToCancel != null) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancel Private Dining Seats?") },
            text = {
                Text("Are you sure you want to cancel your reservation for ${reservationToCancel!!.bookedSeats} seats at \"${reservationToCancel!!.eventTitle}\"? This will return the seats to the database pool immediately.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCancelDialog = false
                        reservationToCancel?.let { onCancelReservation(it) }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("cancel_confirm_dialog_btn")
                ) {
                    Text("Release My Seats")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Keep Table Reservation")
                }
            }
        )
    }

    // Review prompt Dialog
    if (showReviewDialog) {
        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            title = { Text("Write Review for $reviewTargetName") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Submit dynamic feedback and star rating to friendly US locations.", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                    
                    Text("Selected Rating: ${reviewRating.toInt()} Stars", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Slider(
                        value = reviewRating,
                        onValueChange = { reviewRating = it },
                        valueRange = 1f..5f,
                        steps = 3,
                        modifier = Modifier.testTag("review_slider")
                    )

                    OutlinedTextField(
                        value = reviewComment,
                        onValueChange = { reviewComment = it },
                        label = { Text("Your Review Comment") },
                        placeholder = { Text("Share details of your private custom cooking experience...") },
                        modifier = Modifier.fillMaxWidth().height(100.dp).testTag("review_input"),
                        maxLines = 4
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (reviewComment.isBlank()) {
                            Toast.makeText(context, "Please write a comment.", Toast.LENGTH_SHORT).show()
                        } else {
                            onSubmitReview(
                                reviewTargetId,
                                reviewTargetType,
                                reviewTargetName,
                                reviewRating,
                                reviewComment
                            )
                            showReviewDialog = false
                            Toast.makeText(context, "Review published successfully!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.testTag("submit_review_dialog_btn")
                ) {
                    Text("Publish Review")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReviewDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ReservationCard(
    reservation: Reservation,
    onCancelClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val reservationCode = "FE-DIN-${(reservation.id + 1042).toString(16).uppercase()}"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("reservation_card_${reservation.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Confirmation Code & Seats Count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = "Confirmation slip icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = reservationCode,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${reservation.bookedSeats} seats booked",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dinner & Restaurant Specifics
            Text(
                text = reservation.eventTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Venue: ${reservation.restaurantName}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Text(
                text = "Chef: ${reservation.chefName}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Prompt Review Row Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onReviewClick("RESTAURANT") },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("review_restaurant_btn_${reservation.id}")
                ) {
                    Icon(Icons.Default.Star, contentDescription = "Review venue", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Review Venue", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onReviewClick("CHEF") },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("review_chef_btn_${reservation.id}")
                ) {
                    Icon(Icons.Default.Face, contentDescription = "Review chef", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Review Chef", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))

            Spacer(modifier = Modifier.height(12.dp))

            // Footer info layout: Calendar datetime and Cancel button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Datetime",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${reservation.dateString} at ${reservation.timeString}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Medium
                    )
                }

                TextButton(
                    onClick = onCancelClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("cancel_booking_btn_${reservation.id}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Cancel reservation",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cancel Table", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun PrivateBookingCard(
    booking: PrivateBookingRequest,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CUSTOM PRIVATE REQUEST",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                // Status badge
                val statusColor = when (booking.status.uppercase()) {
                    "PENDING" -> HoneySecondary
                    "APPROVED" -> AccentLime
                    else -> MaterialTheme.colorScheme.outline
                }
                Box(
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = booking.status.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = booking.restaurantName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Guests: ${booking.guestCount} | Requirements: ${booking.specialRequirements.ifBlank { "None requested" }}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (booking.status.uppercase() == "APPROVED") {
                Button(
                    onClick = { onReviewClick("RESTAURANT") },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("review_private_venue_btn_${booking.id}")
                ) {
                    Icon(Icons.Default.Star, contentDescription = "Review venue success", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Review Venue", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Datetime custom",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${booking.dateString} at ${booking.timeString}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
