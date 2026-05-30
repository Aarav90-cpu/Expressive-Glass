package ark.development.expressgl.library.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import ark.development.expressgl.library.shapes.ExpressGLCapsule
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ark.development.expressgl.library.modifiers.liquidSquashAndStretch
import ark.development.expressgl.library.theme.LocalExpressGLStyle
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Represents a single tab item for [ExpressGLBottomBar].
 *
 * @param icon The icon vector for this tab.
 * @param label The text label below the icon.
 */
data class ExpressGLTabItem(
    val icon: ImageVector,
    val label: String,
    val isBlocked: Boolean = false,
    val pillColor: Color? = null,
    val activeColor: Color? = null,
)

/**
 * A Material-styled bottom navigation bar with Apple's Liquid Glass feel.
 *
 * Features:
 * - Translucent frosted-glass bar background
 * - Animated pill indicator behind the selected tab
 * - Horizontal drag gesture on the pill to fluidly slide between tabs
 * - Spring physics for pill snapping
 * - Selected icon scales up with expressive spring
 *
 * @param items List of tab items to display.
 * @param selectedIndex Currently selected tab index.
 * @param onTabSelected Callback when a tab is selected.
 * @param modifier Modifier for the bar.
 * @param barHeight Height of the bottom bar.
 * @param pillColor Background color of the selection pill.
 * @param activeColor Color for the active tab icon/label.
 * @param inactiveColor Color for inactive tab icons/labels.
 */
@Composable
fun ExpressGLBottomBar(
    items: List<ExpressGLTabItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    barHeight: Dp = 64.dp,
    containerColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
    outlineColor: Color = Color.White.copy(alpha = 0.4f),
    pillColor: Color = MaterialTheme.colorScheme.primaryContainer,
    activeColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    inactiveColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Track bar width for pill positioning
    var barWidthPx by remember { mutableFloatStateOf(0f) }
    val tabCount = items.size
    val tabWidthPx = if (tabCount > 0) barWidthPx / tabCount else 0f

    // Pill position animated with spring
    val pillCenter = remember { Animatable(0f) }
    var dragCenter by remember { mutableFloatStateOf(0f) }
    var currentDragIndex by remember { mutableIntStateOf(selectedIndex) }
    var isDraggingPill by remember { mutableStateOf(false) }

    val targetPillCenter = tabWidthPx * selectedIndex + tabWidthPx * 0.5f

    // Animate pill to selected index whenever it changes
    androidx.compose.runtime.LaunchedEffect(selectedIndex, tabWidthPx) {
        if (!isDraggingPill && barWidthPx > 0f) {
            pillCenter.animateTo(
                targetValue = targetPillCenter,
                animationSpec = ExpressGLSprings.bouncy(),
            )
        }
    }

    val rejectShake = remember { Animatable(0f) }
    val rejectScale = remember { Animatable(1f) }

    fun triggerRejectAnimation() {
        scope.launch {
            launch {
                rejectScale.animateTo(1.05f, animationSpec = androidx.compose.animation.core.tween(100))
                rejectScale.animateTo(1f, animationSpec = ExpressGLSprings.bouncy())
            }
            launch {
                rejectShake.animateTo(25f, animationSpec = androidx.compose.animation.core.tween(50))
                rejectShake.animateTo(-25f, animationSpec = androidx.compose.animation.core.tween(50))
                rejectShake.animateTo(15f, animationSpec = androidx.compose.animation.core.tween(50))
                rejectShake.animateTo(-15f, animationSpec = androidx.compose.animation.core.tween(50))
                rejectShake.animateTo(0f, animationSpec = ExpressGLSprings.bouncy())
            }
        }
    }

    var isBarPressed by remember { mutableStateOf(false) }
    val displayCenter = if (isDraggingPill) dragCenter else pillCenter.value
    val isMoving = isDraggingPill || pillCenter.isRunning
    val isPillActive = isBarPressed || isMoving

    val tabContentWidths = remember { androidx.compose.runtime.mutableStateMapOf<Int, Float>() }
    
    val fractionalIndex = if (tabWidthPx > 0f) ((displayCenter - tabWidthPx * 0.5f) / tabWidthPx).coerceIn(0f, (tabCount - 1).toFloat()) else 0f
    val leftIndex = kotlin.math.floor(fractionalIndex).toInt().coerceIn(0, tabCount - 1)
    val rightIndex = kotlin.math.ceil(fractionalIndex).toInt().coerceIn(0, tabCount - 1)
    val fraction = fractionalIndex - leftIndex

    val leftContentWidth = tabContentWidths[leftIndex] ?: (tabWidthPx * 0.6f)
    val rightContentWidth = tabContentWidths[rightIndex] ?: (tabWidthPx * 0.6f)
    val currentContentWidth = androidx.compose.ui.util.lerp(leftContentWidth, rightContentWidth, fraction)

    val minRestingWidth = tabWidthPx * 0.85f
    val minActiveWidth = tabWidthPx * 1.05f

    val targetPillWidthPx = if (isPillActive) {
        (currentContentWidth + 80f).coerceAtLeast(minActiveWidth)
    } else {
        (currentContentWidth + 60f).coerceAtLeast(minRestingWidth)
    }

    val pillWidthDp by animateDpAsState(
        targetValue = with(density) { targetPillWidthPx.toDp() },
        animationSpec = ExpressGLSprings.snappy(),
        label = "pillWidth",
    )
    val pillHeightDp by animateDpAsState(
        targetValue = if (isPillActive) barHeight + 3.dp else barHeight - 8.dp,
        animationSpec = ExpressGLSprings.snappy(),
        label = "pillHeight",
    )


    val leftPillColor = items[leftIndex].pillColor ?: pillColor
    val rightPillColor = items[rightIndex].pillColor ?: pillColor
    val targetPillColor = androidx.compose.ui.graphics.lerp(leftPillColor, rightPillColor, fraction)
    
    val animatedPillColor by androidx.compose.animation.animateColorAsState(
        targetValue = targetPillColor,
        animationSpec = ExpressGLSprings.fluid(),
        label = "pillColor",
    )
    
    // Drag Velocity for stretch physics
    var dragVelocity by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 16.dp)
            .height(barHeight)
            .graphicsLayer {
                scaleX = rejectScale.value
                scaleY = rejectScale.value
                translationX = rejectShake.value
            }
            .onSizeChanged { barWidthPx = it.width.toFloat() }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(androidx.compose.ui.input.pointer.PointerEventPass.Initial)
                        isBarPressed = event.changes.any { it.pressed }
                    }
                }
            }
            .pointerInput(tabCount) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragCenter = pillCenter.value
                        currentDragIndex = selectedIndex
                        isDraggingPill = true
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragVelocity = dragAmount.x * 60f
                        dragCenter = (dragCenter + dragAmount.x).coerceIn(tabWidthPx * 0.4f, barWidthPx - tabWidthPx * 0.4f)
                        val closestTab = ((dragCenter - tabWidthPx * 0.5f) / tabWidthPx).roundToInt().coerceIn(0, tabCount - 1)
                        if (closestTab != currentDragIndex) {
                            currentDragIndex = closestTab
                        }
                    },
                    onDragEnd = {
                        var finalTab = currentDragIndex
                        dragVelocity = 0f
                        
                        val isRejected = items[finalTab].isBlocked
                        if (isRejected) {
                            finalTab = selectedIndex
                            triggerRejectAnimation()
                        }
                        
                        val finalTarget = tabWidthPx * finalTab + tabWidthPx * 0.5f
                        val initVel = if (isRejected) (finalTarget - dragCenter) * 15f else 0f
                        
                        scope.launch {
                            pillCenter.snapTo(dragCenter)
                            isDraggingPill = false
                            if (finalTab != selectedIndex && !isRejected) {
                                onTabSelected(finalTab)
                            }
                            pillCenter.animateTo(
                                targetValue = finalTarget,
                                animationSpec = ExpressGLSprings.bouncy(),
                                initialVelocity = initVel
                            )
                        }
                    },
                    onDragCancel = {
                        var finalTab = currentDragIndex
                        dragVelocity = 0f
                        
                        val isRejected = items[finalTab].isBlocked
                        if (isRejected) {
                            finalTab = selectedIndex
                            triggerRejectAnimation()
                        }
                        
                        val finalTarget = tabWidthPx * finalTab + tabWidthPx * 0.5f
                        val initVel = if (isRejected) (finalTarget - dragCenter) * 15f else 0f

                        scope.launch {
                            pillCenter.snapTo(dragCenter)
                            isDraggingPill = false
                            if (finalTab != selectedIndex && !isRejected) {
                                onTabSelected(finalTab)
                            }
                            pillCenter.animateTo(
                                targetValue = finalTarget,
                                animationSpec = ExpressGLSprings.bouncy(),
                                initialVelocity = initVel
                            )
                        }
                    }
                )
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // Bar Background
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(ExpressGLCapsule)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            containerColor,
                            containerColor.copy(alpha = containerColor.alpha + 0.1f),
                        ),
                    ),
                )
                .border(
                    width = 0.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            outlineColor,
                            Color.Transparent,
                        ),
                    ),
                    shape = ExpressGLCapsule,
                )
        )
        // Pill indicator
        if (barWidthPx > 0f && tabCount > 0) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        val halfW = pillWidthDp.toPx() / 2f
                        translationX = displayCenter - halfW
                    }
                    .liquidSquashAndStretch(
                        velocity = if (isDraggingPill) dragVelocity else pillCenter.velocity,
                        componentWidth = pillWidthDp.value * density.density,
                        maxStretchRatio = 0.6f,
                        volumePreservationFactor = 0.3f
                    )
                    .width(pillWidthDp)
                    .height(pillHeightDp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                animatedPillColor,
                                animatedPillColor.copy(alpha = 0.85f),
                            ),
                        ),
                    )
                    .border(
                        width = 0.5.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent,
                            ),
                        ),
                        shape = ExpressGLCapsule,
                    ),
            )
        }

        // Tab items
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                val iconScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.15f else 1f,
                    animationSpec = ExpressGLSprings.bouncy(),
                    label = "iconScale_$index",
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .pointerInput(index) {
                            detectTapGestures {
                                if (item.isBlocked) {
                                    triggerRejectAnimation()
                                } else {
                                    onTabSelected(index)
                                }
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    val tabActiveColor = item.activeColor ?: activeColor
                    val tabCenter = tabWidthPx * index + tabWidthPx * 0.5f
                    val distance = kotlin.math.abs(displayCenter - tabCenter)
                    val proximity = (1f - (distance / tabWidthPx)).coerceIn(0f, 1f)
                    
                    val dynamicTintColor = androidx.compose.ui.graphics.lerp(inactiveColor, tabActiveColor, proximity)

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.onSizeChanged { size ->
                            tabContentWidths[index] = size.width.toFloat()
                        }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = dynamicTintColor,
                                modifier = Modifier
                                    .size(24.dp)
                                    .graphicsLayer {
                                        scaleX = iconScale
                                        scaleY = iconScale
                                    },
                            )
                            Text(
                                text = item.label,
                                color = dynamicTintColor,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
                                ),
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}



/**
 * Curated spring animation specifications that blend Material Expressive's
 * bold motion with Apple's fluid, physics-driven feel.
 */
object ExpressGLSprings {
    /**
     * Fluid spring — low stiffness, medium damping.
     * Used for primary interactions like press/release, pill sliding.
     * Gives the signature "Apple liquid" feel.
     */
    fun <T> fluid() = androidx.compose.animation.core.spring<T>(
        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
        stiffness = androidx.compose.animation.core.Spring.StiffnessLow,
    )

    /**
     * Snappy spring — medium-low stiffness, medium damping.
     * Faster than fluid, used for resizing so the pill doesn't lag when shrinking.
     */
    fun <T> snappy() = androidx.compose.animation.core.spring<T>(
        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
        stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow,
    )

    /**
     * Bouncy spring — medium stiffness, high bounce.
     * Used for playful micro-interactions like toggle knob.
     */
    fun <T> bouncy() = androidx.compose.animation.core.spring<T>(
        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioLowBouncy,
        stiffness = androidx.compose.animation.core.Spring.StiffnessMedium,
    )
}
