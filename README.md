# NutriScan Android Project

This project is structured using Clean Architecture and MVVM. 
The application is currently under development.

## Directory Structure

- `app/src/main/java/com/example/nutriscan/`
    - `data/`: Data layer (Repositories, Room Entities, Data Sources)
        - `local/`: Room Database and DAOs
        - `model/`: Data Transfer Objects (DTOs)
    - `domain/`: Domain layer (Use Cases, Domain Models, Repository Interfaces)
        - `model/`: Pure Kotlin data classes
    - `ui/`: Presentation layer (Jetpack Compose)
    - `di/`: Dependency Injection (Hilt)

## Key Features
- **AI Food Recognition**: Uses ML Kit.
- **Chrononutrition**: Calculates nutrient bioavailability based on time of day.
- **Synergy Visualization**: Shows how nutrients interact.
