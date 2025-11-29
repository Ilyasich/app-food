package com.example.nutriscan.domain.logic

import com.example.nutriscan.data.local.entity.FoodEntity
import com.example.nutriscan.domain.model.CircadianRule
import com.example.nutriscan.domain.model.NutrientProfile
import java.time.Instant
import java.time.ZoneId

class NutrientCalculator {

    /**
     * Calculates a Nutritional Score adjusted for bioavailability based on time of day.
     *
     * @param food The food item being analyzed.
     * @param profile The detailed nutrient profile of the food.
     * @param currentTimestamp The time of consumption (epoch millis).
     * @param rules List of circadian rules to apply.
     * @return A score representing the bioavailability efficiency (0.0 - 100.0+).
     */
    fun calculateBioavailabilityScore(
        food: FoodEntity,
        profile: NutrientProfile,
        currentTimestamp: Long,
        rules: List<CircadianRule>
    ): Float {
        val currentHour = Instant.ofEpochMilli(currentTimestamp)
            .atZone(ZoneId.systemDefault())
            .hour

        var baseScore = 100f
        var multiplier = 1.0f

        // Check each rule against the nutrient profile
        // In a real app, we would iterate through all nutrients present in the profile
        // For this example, we check specific ones if they exist in the rules

        rules.forEach { rule ->
            val isRelevant = when (rule.nutrient) {
                "Vitamin C" -> profile.vitaminC > 0
                "Iron" -> profile.iron > 0
                "Calcium" -> profile.calcium > 0
                "Vitamin D" -> profile.vitaminD > 0
                else -> false
            }

            if (isRelevant) {
                if (currentHour in rule.optimalWindowStart..rule.optimalWindowEnd) {
                    multiplier *= rule.efficiencyFactor
                } else {
                    // Penalty for eating outside optimal window
                    multiplier *= 0.9f 
                }
            }
        }

        return baseScore * multiplier
    }
}
