package com.example.nutriscan

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutriscan.data.remote.GeminiService
import com.example.nutriscan.ui.analysis.AnalysisScreen
import com.example.nutriscan.ui.scan.CameraScreen
import com.example.nutriscan.ui.theme.NutriScanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val geminiService = GeminiService() // In a real app, inject this with Hilt

        setContent {
            NutriScanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

                    NavHost(navController = navController, startDestination = "camera") {
                        composable("camera") {
                            CameraScreen(
                                onImageCaptured = { bitmap ->
                                    capturedImage = bitmap
                                    navController.navigate("analysis")
                                }
                            )
                        }
                        composable("analysis") {
                            capturedImage?.let { bitmap ->
                                AnalysisScreen(
                                    image = bitmap,
                                    geminiService = geminiService
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
