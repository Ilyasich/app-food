package com.example.nutriscan.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class FoodAnalysisResponse(
    val foodName: String,
    val estimatedCalories: Int,
    val nutrients: List<NutrientInfo>,
    val benefits: List<String>,
    val chronoAdvice: ChronoAdvice,
    val synergy: List<SynergyInfo>
)

@Serializable
data class NutrientInfo(
    val name: String,
    val amount: String, // e.g. "10mg"
    val dailyValuePercentage: Int
)

@Serializable
data class ChronoAdvice(
    val bestTime: String,
    val reasoning: String,
    val currentBioavailabilityScore: Int // 0-100
)

@Serializable
data class SynergyInfo(
    val nutrientA: String,
    val nutrientB: String,
    val interaction: String, // "Synergy" or "Antagonism"
    val description: String
)
