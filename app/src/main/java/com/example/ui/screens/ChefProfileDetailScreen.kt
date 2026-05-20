package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ChefProfile
import com.example.data.model.Review
import com.example.ui.theme.HoneySecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChefProfileDetailScreen(
    chefName: String,
    chefProfile: ChefProfile?,
    chefReviews: List<Review>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Dynamic calculation of average chef rating
    val avgRating = if (chefReviews.isNotEmpty()) {
        chefReviews.map { it.rating }.average()
    } else {
        4.8 // default fallback
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(chefName, fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back icon")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { pad ->
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 30.dp, top = pad.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Header card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth().testTag("chef_profile_header")
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        ) {
                            AsyncImage(
                                model = chefProfile?.imageUrl ?: "https://images.unsplash.com/photo-1595273670150-db0a3e368157?auto=format&fit=crop&w=300&q=80",
                                contentDescription = chefName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = chefName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // Specialties
                        val specialtiesList = chefProfile?.specialties?.split(",") ?: listOf("Private Dinners", "Creative Cuisines")
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
                        ) {
                            specialtiesList.take(3).forEach { specialty ->
                                Card(
                                    shape = RoundedCornerShape(6.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Text(
                                        text = specialty.trim(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        // Average Rating badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(HoneySecondary.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star rating",
                                tint = HoneySecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = String.format("%.1f Average Rating", avgRating),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Bio
            item {
                Column {
                    Text(
                        text = "Chef Biography",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = chefProfile?.bio ?: "No bio provided yet for this private dining chef.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        lineHeight = 22.sp
                    )
                }
            }

            // Dish Photos
            item {
                val listPhotos = chefProfile?.dishPhotos?.split(";")?.filter { it.isNotBlank() } ?: emptyList()
                if (listPhotos.isNotEmpty()) {
                    Column {
                        Text(
                            text = "Featured Dishes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        listPhotos.forEach { photoText ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RestaurantMenu,
                                        contentDescription = "Dish icon",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = photoText.trim(),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Personal Website and Social Channels
            item {
                Column {
                    Text(
                        text = "Connect with Chef",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                        ) {
                            Icon(Icons.Default.Language, contentDescription = "Website info", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Website", color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Social channel", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Instagram", color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Reviews List
            item {
                Text(
                    text = "What Diners Say (${chefReviews.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (chefReviews.isEmpty()) {
                item {
                    Text(
                        text = "No reviews published yet for this chef.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                    )
                }
            } else {
                items(chefReviews) { review ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = review.reviewerName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating score",
                                        tint = HoneySecondary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = review.rating.toString(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = review.comment,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
