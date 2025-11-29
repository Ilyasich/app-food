package com.example.nutriscan.ui.analysis

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.nutriscan.data.remote.FoodAnalysisResponse
import com.example.nutriscan.data.remote.GeminiService
import com.example.nutriscan.ui.components.SynergyGraph
import com.example.nutriscan.domain.model.SynergyMap
import com.example.nutriscan.domain.model.InteractionType
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun AnalysisScreen(
    image: Bitmap,
    geminiService: GeminiService
) {
    var analysisResult by remember { mutableStateOf<FoodAnalysisResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(image) {
        scope.launch {
            isLoading = true
            val currentHour = LocalTime.now().hour
            analysisResult = geminiService.analyzeFood(image, currentHour)
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Image(
            bitmap = image.asImageBitmap(),
            contentDescription = "Scanned Food",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            Text("Analyzing food with Gemini AI...", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            analysisResult?.let { result ->
                Text(text = result.foodName, style = MaterialTheme.typography.headlineMedium)
                Text(text = "Estimated Calories: ${result.estimatedCalories}", style = MaterialTheme.typography.bodyLarge)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nutrients", style = MaterialTheme.typography.titleMedium)
                        result.nutrients.forEach { nutrient ->
                            Text("${nutrient.name}: ${nutrient.amount} (${nutrient.dailyValuePercentage}% DV)")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Chronobiology Advice", style = MaterialTheme.typography.titleMedium)
                        Text("Best Time: ${result.chronoAdvice.bestTime}")
                        Text("Score: ${result.chronoAdvice.currentBioavailabilityScore}/100")
                        Text(result.chronoAdvice.reasoning)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Nutrient Synergy", style = MaterialTheme.typography.titleMedium)
                // Convert Gemini SynergyInfo to Domain SynergyMap for the Graph
                val synergyMaps = result.synergy.map { 
                    SynergyMap(
                        nutrientA = it.nutrientA,
                        nutrientB = it.nutrientB,
                        interactionType = if (it.interaction.equals("Synergy", ignoreCase = true)) InteractionType.SYNERGY else InteractionType.ANTAGONISM,
                        multiplier = 1.0f, // Placeholder
                        description = it.description
                    )
                }
                SynergyGraph(synergies = synergyMaps)
            } ?: Text("Failed to analyze image.")
        }
    }
}
