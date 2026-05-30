package ark.development.expressgl.library.modifiers

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Applies Apple-style Liquid Glass physics to any composable shape.
 *
 * The component stretches horizontally from its center based on drag speed.
 * When it abruptly stops, a spring-driven "squish" bounce plays:
 *   1. Squash width + expand height
 *   2. Rebound the other way
 *   3. Settle back to rest
 *
 * @param velocity The current horizontal velocity of the component.
 * @param componentWidth The base physical width of the component in px.
 * @param stretchFactor Multiplier for how much velocity affects the stretch.
 * @param maxStretchRatio Max horizontal stretch as a fraction of componentWidth.
 * @param volumePreservationFactor How much height squashes when width stretches.
 */
fun Modifier.liquidSquashAndStretch(
    velocity: Float,
    componentWidth: Float,
    stretchFactor: Float = 0.05f,
    maxStretchRatio: Float = 0.6f,
    volumePreservationFactor: Float = 0.3f
): Modifier = composed {
    // Bounce overlay for squish-on-stop effect
    val bounceX = remember { Animatable(1f) }
    val bounceY = remember { Animatable(1f) }

    // Detect when velocity drops from high to zero (= user released / stopped)
    val absVel = abs(velocity)
    val wasMoving = absVel < 50f

    LaunchedEffect(wasMoving) {
        if (wasMoving && bounceX.value == 1f) {
            // Phase 1: squash width, expand height
            launch {
                bounceX.animateTo(
                    0.90f,
                    spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
                )
                bounceX.animateTo(
                    1f,
                    spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
                )
            }
            launch {
                bounceY.animateTo(
                    1.10f,
                    spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
                )
                bounceY.animateTo(
                    0.96f,
                    spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
                )
                bounceY.animateTo(
                    1f,
                    spring(Spring.DampingRatioNoBouncy, Spring.StiffnessMedium)
                )
            }
        }
    }

    this.graphicsLayer {
        if (componentWidth > 0f) {
            // Direct velocity-based stretch (immediate, no animation delay)
            val rawStretch = abs(velocity) * stretchFactor
            val stretch = rawStretch.coerceAtMost(componentWidth * maxStretchRatio)
            val squash = 1f - (stretch / componentWidth) * volumePreservationFactor

            // Multiply the direct stretch with the bounce overlay
            scaleX = ((componentWidth + stretch) / componentWidth) * bounceX.value
            scaleY = squash * bounceY.value

            // NO transformOrigin offset — stretch from the center, no directional lag
        }
    }
}
