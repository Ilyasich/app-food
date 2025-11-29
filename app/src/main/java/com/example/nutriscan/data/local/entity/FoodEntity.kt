package com.example.nutriscan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey val id: String,
    val name: String,
    val calories: Int,
    val protein: Float, // grams
    val carbs: Float,   // grams
    val fat: Float,     // grams
    val weightGrams: Float,
    val timestamp: Long
)
