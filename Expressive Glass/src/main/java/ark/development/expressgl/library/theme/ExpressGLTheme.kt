package ark.development.expressgl.library.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Glass-specific styling that layers on top of MaterialTheme.
 * Components read from this via [LocalExpressGLStyle].
 */
@Immutable
data class ExpressGLStyle(
    /** Base color for glass mask. */
    val glassMaskColor: Color = Color.White,
    /** Default corner radius for glass surfaces. */
    val cornerRadius: Dp = 24.dp,
    /** Default blur radius for frosted glass effects. */
    val blurRadius: Dp = 20.dp,
    /** Surface alpha for translucent glass backgrounds. */
    val surfaceAlpha: Float = 0.5f,
    /** Border alpha for glass edge borders. */
    val borderAlpha: Float = 0.4f,
    /** Default elevation for glass surfaces. */
    val elevation: Dp = 0.dp,
)

/**
 * CompositionLocal providing [ExpressGLStyle] defaults throughout the tree.
 * Consumers can override at any level.
 */
val LocalExpressGLStyle = staticCompositionLocalOf { ExpressGLStyle() }

/**
 * Convenience object for default colors used in glass components
 * when MaterialTheme colors are not sufficient.
 */
object ExpressGLColors {
    val ShadowBlue = Color(0x330055FF)
    val FrostWhite = Color(0x80FFFFFF)
    val FrostDark = Color(0x1A000000)
    val GlowBlue = Color(0x3366AAFF)
    val GlowPurple = Color(0x33AA66FF)
    val EdgeHighlight = Color(0xAAFFFFFF)
    val DisabledOverlay = Color(0x66FFFFFF)
}

/**
 * Convenience accessors bridging MaterialTheme colors into glass components.
 */
object ExpressGLDefaults {

    /** Primary action color from the current MaterialTheme. */
    val primaryColor: Color
        @Composable get() = MaterialTheme.colorScheme.primary

    /** On-primary (text/icon on primary) from the current MaterialTheme. */
    val onPrimaryColor: Color
        @Composable get() = MaterialTheme.colorScheme.onPrimary

    /** Surface color for glass backgrounds. */
    val surfaceColor: Color
        @Composable get() = MaterialTheme.colorScheme.surface

    /** On-surface color for text/icons on glass surfaces. */
    val onSurfaceColor: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface

    /** Secondary container for pill/indicator backgrounds. */
    val secondaryContainerColor: Color
        @Composable get() = MaterialTheme.colorScheme.secondaryContainer

    /** Outline color for subtle borders. */
    val outlineColor: Color
        @Composable get() = MaterialTheme.colorScheme.outline
}
