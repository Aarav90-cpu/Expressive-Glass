package ark.development.expressgl.library.layout

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asAndroidColorFilter
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A container that applies a metaball "gooey" merging effect to all its children.
 * When children of this container (like circles, pills, or polygons) overlap or 
 * come close to each other, they will fluidly bridge and merge together.
 *
 * NOTE: This effect relies on RenderEffect which is only available on API 31+.
 * Since this library targets API 36+, this will work perfectly.
 * 
 * Also note that this effect works best when the children are solid opaque colors.
 *
 * @param modifier Modifier for the container.
 * @param blurRadius The radius of the blur. Higher values create a thicker bridge.
 * @param alphaThreshold The alpha threshold multiplier. Higher values create sharper edges.
 * @param content The composable children.
 */
@Composable
fun ExpressGLGooeyContainer(
    modifier: Modifier = Modifier,
    blurRadius: Float = 40f,
    alphaThreshold: Float = 80f,
    content: @Composable BoxScope.() -> Unit
) {
    // 1. Define the ColorMatrix for alpha thresholding
    // The last row manipulates the alpha channel.
    // Multiplying alpha by alphaThreshold and subtracting a large offset
    // creates a sharp transition from transparent to opaque.
    val colorMatrix = ColorMatrix(
        floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, alphaThreshold, -1000f
        )
    )

    // 2. Combine Blur and ColorFilter into a RenderEffect
    val blurEffect = RenderEffect.createBlurEffect(blurRadius, blurRadius, Shader.TileMode.DECAL)
    val colorFilterEffect = RenderEffect.createColorFilterEffect(
        ColorFilter.colorMatrix(colorMatrix).asAndroidColorFilter(),
        blurEffect
    )

    Box(
        modifier = modifier.graphicsLayer {
            renderEffect = colorFilterEffect.asComposeRenderEffect()
        },
        content = content
    )
}
