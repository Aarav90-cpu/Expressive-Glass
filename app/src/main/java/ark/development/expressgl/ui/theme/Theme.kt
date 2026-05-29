package ark.development.expressgl.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val GlassLightColorScheme = lightColorScheme(
    primary = ExpressivePurple,
    onPrimary = SurfaceWhite,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = OnPurpleContainer,
    secondary = ExpressiveTeal,
    onSecondary = SurfaceWhite,
    secondaryContainer = TealContainer,
    onSecondaryContainer = OnSurfaceMain,
    tertiary = ExpressivePeach,
    onTertiary = SurfaceWhite,
    tertiaryContainer = PeachContainer,
    onTertiaryContainer = OnSurfaceMain,
    background = ExpressiveBackground,
    onBackground = OnSurfaceMain,
    surface = SurfaceWhite,
    onSurface = OnSurfaceMain,
    surfaceVariant = ExpressiveBackground,
    onSurfaceVariant = OnSurfaceVariant,
    outline = ExpressiveOutline,
    outlineVariant = SubtleOutline,
    inverseSurface = OnSurfaceMain,
    inverseOnSurface = SurfaceWhite,
)

@Composable
fun ExpressiveGlassTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = GlassLightColorScheme,
        typography = Typography,
        content = content,
    )
}