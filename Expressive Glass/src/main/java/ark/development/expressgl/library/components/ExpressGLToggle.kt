package ark.development.expressgl.library.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ark.development.expressgl.library.modifiers.liquidSquashAndStretch
import ark.development.expressgl.library.shapes.ExpressGLCapsule
import ark.development.expressgl.library.theme.LocalExpressGLStyle
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * An Expressive Glass Toggle (Switch) component.
 * Merges Material Expressive boldness with Apple Liquid Glass physics.
 * 
 * - Thumb stretches based on drag velocity (sloshes)
 * - Track is a frosted translucent capsule with a glowing border
 * - Rejection animation if `isBlocked` is true
 * 
 * @param checked Whether the toggle is ON.
 * @param onCheckedChange Callback when the toggle state changes.
 * @param modifier Modifier for the toggle.
 * @param isBlocked If true, the toggle will reject interaction and vibrate.
 * @param width Width of the toggle track.
 * @param height Height of the toggle track.
 */
@Composable
fun ExpressGLToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isBlocked: Boolean = false,
    width: Dp = 80.dp,
    height: Dp = 36.dp,
    checkedColor: Color = MaterialTheme.colorScheme.primary,
    uncheckedColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    trackColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
    activeTrackColor: Color? = null,
    outlineColor: Color = Color.White.copy(alpha = 0.4f),
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val thumbPadding = 4.dp
    val thumbHeight = height - (thumbPadding * 2)
    val thumbBaseWidth = thumbHeight * 1.4f
    
    var trackWidthPx by remember { mutableFloatStateOf(0f) }
    var trackHeightPx by remember { mutableFloatStateOf(0f) }
    
    val thumbRadiusPx = with(density) { thumbBaseWidth.toPx() / 2f }
    val paddingPx = with(density) { thumbPadding.toPx() }
    
    val offPosition = paddingPx + thumbRadiusPx
    val onPosition = trackWidthPx - paddingPx - thumbRadiusPx

    // State
    val thumbCenter = remember { Animatable(0f) }
    var dragCenter by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val rejectShake = remember { Animatable(0f) }
    val rejectScale = remember { Animatable(1f) }

    val targetCenter = if (checked) onPosition else offPosition

    // Animate to target when checked state changes externally
    LaunchedEffect(checked, trackWidthPx) {
        if (!isDragging && trackWidthPx > 0f) {
            thumbCenter.animateTo(
                targetValue = targetCenter,
                animationSpec = ExpressGLSprings.bouncy()
            )
        }
    }

    // Rejection animation
    fun triggerRejectAnimation() {
        scope.launch {
            launch {
                rejectScale.animateTo(1.05f, animationSpec = androidx.compose.animation.core.tween(100))
                rejectScale.animateTo(1f, animationSpec = ExpressGLSprings.bouncy())
            }
            launch {
                rejectShake.animateTo(15f, animationSpec = androidx.compose.animation.core.tween(50))
                rejectShake.animateTo(-15f, animationSpec = androidx.compose.animation.core.tween(50))
                rejectShake.animateTo(10f, animationSpec = androidx.compose.animation.core.tween(50))
                rejectShake.animateTo(-10f, animationSpec = androidx.compose.animation.core.tween(50))
                rejectShake.animateTo(0f, animationSpec = ExpressGLSprings.bouncy())
            }
        }
    }

    fun attemptToggle(targetChecked: Boolean) {
        if (isBlocked) {
            triggerRejectAnimation()
            scope.launch {
                thumbCenter.animateTo(
                    targetValue = targetCenter,
                    animationSpec = ExpressGLSprings.bouncy()
                )
            }
        } else {
            onCheckedChange(targetChecked)
        }
    }

    val displayCenter = if (isDragging) dragCenter else thumbCenter.value
    
    // Determine dynamic colors
    val isCheckedState = checked || (isDragging && displayCenter > trackWidthPx / 2f)
    
    val animatedThumbColor by animateColorAsState(
        targetValue = if (isCheckedState) checkedColor else uncheckedColor,
        animationSpec = ExpressGLSprings.fluid(),
        label = "thumbColor"
    )

    val animatedTrackColor by animateColorAsState(
        targetValue = if (isCheckedState) (activeTrackColor ?: trackColor) else trackColor,
        animationSpec = ExpressGLSprings.fluid(),
        label = "trackColor"
    )

    // Dynamic width for squash effect
    val dynamicThumbWidth by animateDpAsState(
        targetValue = if (isPressed && !isDragging) thumbBaseWidth * 1.35f else thumbBaseWidth,
        animationSpec = ExpressGLSprings.fluid(),
        label = "thumbWidth"
    )
    
    val dynamicThumbHeight by animateDpAsState(
        targetValue = if (isPressed && !isDragging) thumbHeight * 1.15f else thumbHeight,
        animationSpec = ExpressGLSprings.fluid(),
        label = "thumbHeight"
    )

    var dragVelocity by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .graphicsLayer {
                scaleX = rejectScale.value
                scaleY = rejectScale.value
                translationX = rejectShake.value
            }
            .onSizeChanged { 
                trackWidthPx = it.width.toFloat() 
                trackHeightPx = it.height.toFloat()
            }
            .clip(ExpressGLCapsule)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        animatedTrackColor,
                        animatedTrackColor.copy(alpha = animatedTrackColor.alpha.coerceAtMost(0.9f) + 0.1f),
                    ),
                ),
            )
            .border(
                width = 0.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(outlineColor, Color.Transparent)
                ),
                shape = ExpressGLCapsule
            )
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(androidx.compose.ui.input.pointer.PointerEventPass.Initial)
                        isPressed = event.changes.any { it.pressed }
                    }
                }
            }
            .pointerInput(isBlocked, targetCenter) {
                detectTapGestures {
                    attemptToggle(!checked)
                }
            }
            .pointerInput(isBlocked, targetCenter) {
                detectDragGestures(
                    onDragStart = {
                        dragCenter = thumbCenter.value
                        isDragging = true
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragVelocity = dragAmount.x * 60f
                        dragCenter = (dragCenter + dragAmount.x).coerceIn(offPosition, onPosition)
                    },
                    onDragEnd = {
                        val isCheckedTarget = dragCenter > trackWidthPx / 2f
                        dragVelocity = 0f
                        
                        if (isBlocked) {
                            val initVel = if (isCheckedTarget != checked) (targetCenter - dragCenter) * 15f else 0f
                            triggerRejectAnimation()
                            scope.launch {
                                thumbCenter.snapTo(dragCenter)
                                isDragging = false
                                thumbCenter.animateTo(
                                    targetValue = targetCenter,
                                    animationSpec = ExpressGLSprings.bouncy(),
                                    initialVelocity = initVel
                                )
                            }
                        } else {
                            scope.launch {
                                thumbCenter.snapTo(dragCenter)
                                isDragging = false
                                if (isCheckedTarget != checked) {
                                    onCheckedChange(isCheckedTarget)
                                }
                                val finalTarget = if (isCheckedTarget) onPosition else offPosition
                                thumbCenter.animateTo(
                                    targetValue = finalTarget,
                                    animationSpec = ExpressGLSprings.bouncy()
                                )
                            }
                        }
                    },
                    onDragCancel = {
                        val isCheckedTarget = dragCenter > trackWidthPx / 2f
                        dragVelocity = 0f
                        
                        if (isBlocked) {
                            val initVel = if (isCheckedTarget != checked) (targetCenter - dragCenter) * 15f else 0f
                            triggerRejectAnimation()
                            scope.launch {
                                thumbCenter.snapTo(dragCenter)
                                isDragging = false
                                thumbCenter.animateTo(
                                    targetValue = targetCenter,
                                    animationSpec = ExpressGLSprings.bouncy(),
                                    initialVelocity = initVel
                                )
                            }
                        } else {
                            scope.launch {
                                thumbCenter.snapTo(dragCenter)
                                isDragging = false
                                if (isCheckedTarget != checked) {
                                    onCheckedChange(isCheckedTarget)
                                }
                                val finalTarget = if (isCheckedTarget) onPosition else offPosition
                                thumbCenter.animateTo(
                                    targetValue = finalTarget,
                                    animationSpec = ExpressGLSprings.bouncy()
                                )
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.CenterStart
    ) {
        if (trackWidthPx > 0f) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        val halfW = dynamicThumbWidth.toPx() / 2f
                        translationX = displayCenter - halfW
                    }
                    .liquidSquashAndStretch(
                        velocity = if (isDragging) dragVelocity else thumbCenter.velocity,
                        componentWidth = dynamicThumbWidth.value * density.density,
                        maxStretchRatio = 0.2f,
                        volumePreservationFactor = 0.3f
                    )
                    .width(dynamicThumbWidth)
                    .height(dynamicThumbHeight)
                    .clip(ExpressGLCapsule)
                    .background(animatedThumbColor)
            )
        }
    }
}
