package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.DinnerEvent
import com.example.data.model.Restaurant
import com.example.ui.theme.AccentLime
import com.example.ui.theme.HoneySecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    event: DinnerEvent,
    restaurant: Restaurant,
    initialUserName: String,
    initialUserEmail: String,
    onBack: () -> Unit,
    onBookSeats: (Int, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val seatsRemaining = event.maxSeats - event.bookedSeats
    val isFull = seatsRemaining <= 0

    var numSeatsToBook by remember { mutableStateOf(1) }
    var userNameInput by remember { mutableStateOf(initialUserName) }
    var userEmailInput by remember { mutableStateOf(initialUserEmail) }

    var showConfirmationDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App bar
        TopAppBar(
            title = { Text("Private Tasting Event", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Info
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "$${String.format("%.0f", event.price)}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "at ${restaurant.name} • ${restaurant.city}, ${restaurant.state}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Day logo",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = event.dateString,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Clock logo",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = event.timeString,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // Chef Profile Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    ) {
                        AsyncImage(
                            model = event.chefImageUrl,
                            contentDescription = event.chefName,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Featured Chef: ${event.chefName}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = event.chefBio,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Exquisite Menu Cards
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Chef's Four-Course Menu",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Displays Sparkles label if AI generated
                    if (event.isAiGenerated) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(HoneySecondary.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Chef AI icon",
                                    tint = HoneySecondary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Chef AI Designed",
                                    color = HoneySecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Appetizer Course Card
                MenuCourseCard(
                    courseLabel = "I. Appetizer",
                    title = event.appetizer,
                    icon = Icons.Default.RestaurantMenu
                )

                // Main Course Card
                MenuCourseCard(
                    courseLabel = "II. Main Course",
                    title = event.mainCourse,
                    icon = Icons.Default.Restaurant
                )

                // Dessert Course Card
                MenuCourseCard(
                    courseLabel = "III. Dessert",
                    title = event.dessert,
                    icon = Icons.Default.BakeryDining
                )

                // Beverage Pairings Card
                MenuCourseCard(
                    courseLabel = "IV. Pairings & Elixirs",
                    title = event.pairings,
                    icon = Icons.Default.LocalBar
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // Interactive Seating Booking Sheet
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("booking_section"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Secure Your Seats",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    if (isFull) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.error.copy(alpha = 0.08f))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "FULLY BOOKED\nJoin the waitlist or explore local events.",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Display Seats remaining
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Available Seating:",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "$seatsRemaining seats of ${event.maxSeats} open",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentLime
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Selected Quantity selector counter
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Number of Seats:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                IconButton(
                                    onClick = { if (numSeatsToBook > 1) numSeatsToBook-- },
                                    enabled = numSeatsToBook > 1,
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease count", modifier = Modifier.size(16.dp))
                                }

                                Text(
                                    text = numSeatsToBook.toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.testTag("booked_seats_quantity")
                                )

                                IconButton(
                                    onClick = { if (numSeatsToBook < seatsRemaining) numSeatsToBook++ },
                                    enabled = numSeatsToBook < seatsRemaining,
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase count", modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // User attendee information fields
                        OutlinedTextField(
                            value = userNameInput,
                            onValueChange = { userNameInput = it },
                            label = { Text("Your Complete Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("booking_name_input"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = userEmailInput,
                            onValueChange = { userEmailInput = it },
                            label = { Text("Contact Email Address") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("booking_email_input"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        val totalValue = event.price * numSeatsToBook

                        // Large action Button
                        Button(
                            onClick = {
                                if (userNameInput.isBlank() || userEmailInput.isBlank()) {
                                    Toast.makeText(context, "Please configure your name and email settings to complete reservation.", Toast.LENGTH_SHORT).show()
                                } else {
                                    showConfirmationDialog = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("submit_booking_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = "Reserve $numSeatsToBook Seating Experiences • $${String.format("%.0f", totalValue)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // --- Booking Confirmation Dialog ---
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Confirm Fine-Dining Seat Allocation") },
            text = {
                Text("Confirm standard booking of $numSeatsToBook seats for private event \"${event.title}\" at ${restaurant.name}.\n\nAttendee: $userNameInput\nA confirmation bill of $${String.format("%.2f", event.price * numSeatsToBook)} will be scheduled.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false
                        onBookSeats(numSeatsToBook, userNameInput, userEmailInput)
                    },
                    modifier = Modifier.testTag("confirm_dialog_btn")
                ) {
                    Text("Reserve Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Modify Details")
                }
            }
        )
    }
}

@Composable
fun MenuCourseCard(
    courseLabel: String,
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = courseLabel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = title.ifBlank { "Chef's custom selection of seasonal elements selected freshly of local organic fields." },
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}
