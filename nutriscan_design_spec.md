# NutriScan: Comprehensive Technical Specification

## 1. Executive Summary
**NutriScan** is an advanced Android application designed to revolutionize personal nutrition tracking. By leveraging Computer Vision and Chronobiology, it goes beyond simple calorie counting to analyze *what* you eat, *when* you eat it, and *how* nutrients interact.

## 2. Core Features & Functionality

### 2.1 AI Food Recognition & Volumetrics
- **Function**: Uses the camera to scan food in real-time.
- **Technology**: On-device ML (ML Kit / TensorFlow Lite) for low latency and privacy.
- **Capabilities**:
    - Identify dishes (e.g., "Grilled Salmon with Asparagus").
    - Estimate portion size/weight using depth estimation or reference objects (Volumetrics).
    - Calculate exact caloric and macronutrient intake.

### 2.2 Micronutrient Profiling
- **Function**: Deconstructs food into vitamins (A, C, D, K, B-complex) and minerals (Iron, Magnesium, Zinc).
- **Analysis**: Provides a "Pros & Cons" breakdown of the meal's composition.

### 2.3 Nutrient Synergy Visualization
- **Concept**: Visualizes how nutrients interact (Synergy vs. Antagonism).
- **Example**: Vitamin C enhances Iron absorption; Calcium inhibits Iron absorption.
- **UI**: Interactive Node Graph where nodes (nutrients) attract or repel each other based on interaction type.

### 2.4 Chrononutrition Engine
- **Function**: Optimizes nutrient intake based on circadian rhythms.
- **Logic**:
    - Input: Current Timestamp + Food Content.
    - Output: Bioavailability Score.
    - **Recommendation**: "Eat this at 10:00 AM for 20% better absorption."

### 2.5 Predictive Analytics
- **Function**: Logs data to predict weight trends (Gain/Loss/Maintain).
- **Input**: Nutrient density + Meal Timing.

## 3. Technical Architecture

### 3.1 Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: Clean Architecture (MVVM/MVI)
- **Database**: Room (Local), optional Firebase sync.
- **ML/AI**: Google ML Kit (Object Detection), TensorFlow Lite (Custom Food Model).
- **Charting**: Vico or MPAndroidChart (for graphs).
- **DI**: Hilt.
- **Async**: Coroutines & Flow.

### 3.2 Data Modeling (Kotlin)

```kotlin
// Core Food Entity
@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey val id: String,
    val name: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val timestamp: Long
)

// Detailed Nutrient Profile
data class NutrientProfile(
    val vitaminC: Float, // mg
    val iron: Float,     // mg
    val calcium: Float,  // mg
    val vitaminD: Float  // IU
)

// Synergy Definition
data class SynergyMap(
    val nutrientA: String,
    val nutrientB: String,
    val interactionType: InteractionType, // SYNERGY, ANTAGONISM, NEUTRAL
    val multiplier: Float // e.g., 1.5x absorption
)

enum class InteractionType { SYNERGY, ANTAGONISM }

// Chrono Rule
data class CircadianRule(
    val nutrient: String,
    val optimalWindowStart: Int, // Hour of day (0-23)
    val optimalWindowEnd: Int,
    val efficiencyFactor: Float
)
```

### 3.3 Algorithm Logic: Bioavailability Score

```kotlin
fun calculateNutritionalScore(
    food: FoodEntity,
    profile: NutrientProfile,
    currentTimestamp: Long,
    rules: List<CircadianRule>
): Float {
    val currentHour = Instant.ofEpochMilli(currentTimestamp)
        .atZone(ZoneId.systemDefault())
        .hour

    var score = 100f
    
    // Apply Chrono Rules
    rules.forEach { rule ->
        // Simplified logic: Check if current nutrient is in the food
        // In reality, we'd map profile fields to rules
        if (isOptimalTime(currentHour, rule)) {
            score *= rule.efficiencyFactor
        } else {
            score *= 0.9f // Penalty for sub-optimal timing
        }
    }
    
    return score
}
```

## 4. UI/UX Design

### 4.1 Design Philosophy
- **Aesthetic**: Premium, "Dark Mode" default, Glassmorphism elements.
- **Colors**: Deep Midnight Blue background, Neon Green (Good), Warning Orange (Caution), Electric Purple (Synergy).
- **Typography**: 'Inter' or 'Outfit' for modern, clean readability.

### 4.2 "Analysis Result" Screen Layout
1.  **Header**: High-res photo of the scanned food with a "Scanning Complete" animation.
2.  **Chronobiology Widget (Top Right)**:
    - A circular "Bio-Clock" widget.
    - Shows current time vs. optimal time.
    - Text: "Absorption Efficiency: 85% (High)"
3.  **Macro Cards**: Row of glass cards showing Calories, Protein, Carbs, Fat.
4.  **Synergy Map (Centerpiece)**:
    - A canvas-drawn interactive graph.
    - Central Node: The Meal.
    - Satellite Nodes: Vitamins/Minerals.
    - **Visuals**:
        - Green pulsing lines connecting Vitamin C -> Iron (Synergy).
        - Red dashed lines connecting Calcium -| Iron (Inhibition).
5.  **Pros & Cons Card**:
    - "✅ High in Protein"
    - "⚠️ High Sodium - Drink water!"
6.  **Future Recommendation**:
    - "Next time, try this at 10:00 AM to maximize Iron uptake."

## 5. User Journey
1.  **Capture**: User opens app, points camera at lunch. Taps "Scan".
2.  **Process**: App identifies "Spinach Salad", estimates 300g.
3.  **Analyze**: Engine calculates nutrients and checks time (2:00 PM).
4.  **Result**: User sees the "Analysis Result" screen.
    - Notices the "Synergy Map" showing Vitamin C boosting Iron.
    - Sees "Bio-Clock" at 85%.
5.  **Log**: User taps "Add to Diary".
