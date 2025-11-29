package com.example.nutriscan.domain.model

data class NutrientProfile(
    val vitaminC: Float, // mg
    val iron: Float,     // mg
    val calcium: Float,  // mg
    val vitaminD: Float, // IU
    val magnesium: Float,// mg
    val zinc: Float      // mg
)

enum class InteractionType {
    SYNERGY,    // Enhances absorption
    ANTAGONISM, // Inhibits absorption
    NEUTRAL
}

data class SynergyMap(
    val nutrientA: String,
    val nutrientB: String,
    val interactionType: InteractionType,
    val multiplier: Float, // e.g., 1.5f for 50% boost
    val description: String
)

data class CircadianRule(
    val nutrient: String,
    val optimalWindowStart: Int, // Hour of day (0-23)
    val optimalWindowEnd: Int,
    val efficiencyFactor: Float, // e.g., 1.2f for 20% boost
    val reason: String
)
