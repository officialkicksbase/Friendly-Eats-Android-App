package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// --- Moshi Request & Response Classes ---

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class Part(
    val text: String
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    val candidates: List<Candidate>?
)

@JsonClass(generateAdapter = true)
data class Candidate(
    val content: Content?
)

// --- Retrofit API Service Interface ---

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val api: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    /**
     * Determines the correct API key to use. Returns the injected build config key,
     * or a user-provided fallback key if defined.
     */
    fun getApiKey(userCustomKey: String?): String {
        val buildKey = BuildConfig.GEMINI_API_KEY
        return if (!userCustomKey.isNullOrBlank()) {
            userCustomKey.trim()
        } else if (buildKey.isNotEmpty() && buildKey != "MY_GEMINI_API_KEY" && !buildKey.contains("placeholder", ignoreCase = true)) {
            buildKey
        } else {
            ""
        }
    }

    /**
     * Query Gemini to create a full multi-course custom culinary menu.
     */
    suspend fun generateMenu(
        chefConcept: String,
        cuisineType: String,
        restaurantName: String,
        userCustomKey: String?
    ): GeneratedMenuResult {
        val apiKey = getApiKey(userCustomKey)
        if (apiKey.isEmpty()) {
            return GeneratedMenuResult.Error("API Key is missing. Please add your Gemini API Key in the Settings panel.")
        }

        val prompt = """
            Create an exclusive private four-course fine-dining tasting menu based on:
            Chef Concept: "$chefConcept"
            Cuisine/Style: "$cuisineType"
            Restaurant: "$restaurantName"

            Rules:
            1. Output exactly four sections, split by a double pipe symbol "||".
            2. The sections should be: 
               Appetizer || Main Course || Dessert || Beverage Pairings
            3. Each section should include a gourmet, high-fidelity title and a brief decadent description.
            4. Keep the text elegant and appetizing.
            5. Do NOT output any markdown tags, JSON, numbers or prefixes like "Appetizer: ". Simple raw paragraphs separated by ||.
            
            Example output format:
            Whispering Forests Morel Soufflé with wild herbs and toasted hazelnut cream. || Aged Prime Ribeye Rubbed in Roasted Cacao served over a velvety parsnip mousse. || Saffron and Rosewater Infused Crème Brûlée with sugar crystal sheet. || 2018 Reserve Pinot Noir of Willamette Valley & Jasmine Infused Pear Elixir
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            systemInstruction = Content(parts = listOf(Part(text = "You are a professional culinary menu writer for elite private-chef dinner concepts across the United States.")))
        )

        return try {
            val response = api.generateContent(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!text.isNullOrBlank()) {
                val parts = text.split("||").map { it.trim() }
                if (parts.size >= 4) {
                    GeneratedMenuResult.Success(
                        appetizer = parts[0],
                        mainCourse = parts[1],
                        dessert = parts[2],
                        pairings = parts[3]
                    )
                } else {
                    // Fallback to manual break if delimiter was missed
                    GeneratedMenuResult.Success(
                        appetizer = text.take(150),
                        mainCourse = text.drop(150).take(200),
                        dessert = "Signature Dessert Selection",
                        pairings = "Chef's Selected Wine & Elixir"
                    )
                }
            } else {
                GeneratedMenuResult.Error("No response returned from the Chef AI model.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error generating menu", e)
            GeneratedMenuResult.Error("Connection failed: ${e.message}")
        }
    }
}

sealed class GeneratedMenuResult {
    data class Success(
        val appetizer: String,
        val mainCourse: String,
        val dessert: String,
        val pairings: String
    ) : GeneratedMenuResult()
    data class Error(val errorMessage: String) : GeneratedMenuResult()
}
