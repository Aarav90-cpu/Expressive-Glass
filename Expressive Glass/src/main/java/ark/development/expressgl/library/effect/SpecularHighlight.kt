package ark.development.expressgl.library.effect

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Draws a subtle animated specular highlight along the top-left edge
 * of the composable, simulating glass refraction / light sweep.
 *
 * @param highlightColor Base color for the specular highlight.
 * @param animationDurationMs Duration of one full sweep cycle.
 * @param intensity Alpha intensity of the highlight (0f–1f).
 */
fun Modifier.specularHighlight(
    highlightColor: Color = Color.White,
    animationDurationMs: Int = 3000,
    intensity: Float = 0.35f,
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "specular")
    val sweepProgress by infiniteTransition.animateFloat(
        initialValue = -0.3f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDurationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "specularSweep",
    )

    drawWithContent {
        drawContent()

        val w = size.width
        val h = size.height

        // Sweep a diagonal highlight band across the surface
        val bandWidth = w * 0.4f
        val centerX = w * sweepProgress
        val startX = centerX - bandWidth / 2f
        val endX = centerX + bandWidth / 2f

        val brush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                highlightColor.copy(alpha = intensity * 0.3f),
                highlightColor.copy(alpha = intensity),
                highlightColor.copy(alpha = intensity * 0.3f),
                Color.Transparent,
            ),
            start = Offset(startX, 0f),
            end = Offset(endX, h * 0.5f),
        )

        drawRect(brush = brush)
    }
}

/**
 * Draws a static specular edge highlight along the top and left edges,
 * giving the composable a "glass edge" effect.
 *
 * @param highlightColor Color of the edge highlight.
 * @param edgeWidth Width of the highlight band in pixels.
 * @param intensity Alpha intensity.
 */
fun Modifier.specularEdge(
    highlightColor: Color = Color.White,
    edgeWidth: Float = 2f,
    intensity: Float = 0.4f,
): Modifier = this.drawWithContent {
    drawContent()

    val w = size.width
    val h = size.height

    // Top edge highlight
    val topBrush = Brush.verticalGradient(
        colors = listOf(
            highlightColor.copy(alpha = intensity),
            Color.Transparent,
        ),
        startY = 0f,
        endY = edgeWidth * 4f,
    )
    drawRect(brush = topBrush, size = size.copy(height = edgeWidth * 4f))

    // Left edge highlight
    val leftBrush = Brush.horizontalGradient(
        colors = listOf(
            highlightColor.copy(alpha = intensity * 0.6f),
            Color.Transparent,
        ),
        startX = 0f,
        endX = edgeWidth * 3f,
    )
    drawRect(brush = leftBrush, size = size.copy(width = edgeWidth * 3f))
}
