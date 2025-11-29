package com.example.nutriscan.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.nutriscan.domain.model.InteractionType
import com.example.nutriscan.domain.model.SynergyMap
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SynergyGraph(
    synergies: List<SynergyMap>,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 3

        // Draw Central Node (The Meal)
        drawCircle(
            color = Color.White,
            radius = 40f,
            center = Offset(centerX, centerY)
        )

        val nodeCount = synergies.size
        val angleStep = (2 * Math.PI) / nodeCount

        synergies.forEachIndexed { index, synergy ->
            val angle = index * angleStep
            val nodeX = centerX + (radius * cos(angle)).toFloat()
            val nodeY = centerY + (radius * sin(angle)).toFloat()

            // Draw Connection Line
            val lineColor = when (synergy.interactionType) {
                InteractionType.SYNERGY -> Color.Green
                InteractionType.ANTAGONISM -> Color.Red
                InteractionType.NEUTRAL -> Color.Gray
            }
            
            val pathEffect = if (synergy.interactionType == InteractionType.ANTAGONISM) {
                PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            } else {
                null
            }

            drawLine(
                color = lineColor,
                start = Offset(centerX, centerY),
                end = Offset(nodeX, nodeY),
                strokeWidth = 5f,
                pathEffect = pathEffect
            )

            // Draw Satellite Node (Nutrient)
            drawCircle(
                color = lineColor,
                radius = 20f,
                center = Offset(nodeX, nodeY)
            )
        }
    }
}
