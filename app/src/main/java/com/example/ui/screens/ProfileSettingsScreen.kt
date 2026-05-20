package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HoneySecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(
    currentName: String,
    currentEmail: String,
    currentKey: String,
    onSave: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }
    var apiKey by remember { mutableStateOf(currentKey) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Status header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Column {
                Text(
                    text = "VISITOR PREFERENCES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Gourmet Preferences",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Configure your guest profile and custom Gemini AI authentication tokens.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section title: Attendee Profile info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Chef / Guest Profile",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Display Name") },
                placeholder = { Text("your name...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("settings_name_input"),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Display Email Address") },
                placeholder = { Text("username@mail.com") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("settings_email_input"),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // Section 2: Gemini AI Settings configuration
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = null,
                    tint = HoneySecondary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Gemini API Configuration",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Security Disclaimer Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = HoneySecondary.copy(alpha = 0.05f)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Alert banner",
                        tint = HoneySecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Neural Gastronomy Integration",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Private chefs can provide their proprietary Gemini API keys (standard AIzaSy format) in the secret field below to authorize custom tasting menu blueprints from the AI.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = { Text("Gemini API Key") },
                placeholder = { Text("AIzaSy...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("settings_key_input"),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Large Save Button
            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank()) {
                        Toast.makeText(context, "Full Name and Email Address keys cannot be empty.", Toast.LENGTH_SHORT).show()
                    } else {
                        onSave(name, email, apiKey)
                        Toast.makeText(context, "Gourmet preferences updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_settings_btn"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Save settings label", modifier = Modifier.size(18.dp))
                    Text("Save Preference Configuration", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
