package com.example.nutriscan.data.remote

import android.graphics.Bitmap
import com.example.nutriscan.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class GeminiService {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun analyzeFood(image: Bitmap, currentHour: Int): FoodAnalysisResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val prompt = """
                    Analyze this food image and provide a detailed nutritional breakdown in JSON format.
                    The current time is $currentHour:00.
                    
                    Return ONLY raw JSON (no markdown code blocks) with the following structure:
                    {
                      "foodName": "Name of the dish",
                      "estimatedCalories": 0,
                      "nutrients": [
                        { "name": "Vitamin C", "amount": "10mg", "dailyValuePercentage": 15 }
                      ],
                      "benefits": ["Benefit 1", "Benefit 2"],
                      "chronoAdvice": {
                        "bestTime": "Lunch (12PM - 2PM)",
                        "reasoning": "Explanation based on circadian rhythms...",
                        "currentBioavailabilityScore": 85
                      },
                      "synergy": [
                        { "nutrientA": "Iron", "nutrientB": "Vitamin C", "interaction": "Synergy", "description": "Vitamin C enhances Iron absorption" }
                      ]
                    }
                """.trimIndent()

                val response = generativeModel.generateContent(
                    content {
                        image(image)
                        text(prompt)
                    }
                )

                val responseText = response.text?.replace("```json", "")?.replace("```", "")?.trim()
                
                if (responseText != null) {
                    json.decodeFromString<FoodAnalysisResponse>(responseText)
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
