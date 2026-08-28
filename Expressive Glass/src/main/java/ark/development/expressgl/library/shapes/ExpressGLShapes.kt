package ark.development.expressgl.library.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import com.kyant.capsule.continuities.G2Continuity
import com.kyant.capsule.continuities.G2ContinuityProfile
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Custom Apple-like G2 Continuity profile for ExpressGL surfaces.
 */
val ExpressGLContinuity = G2Continuity(
    profile = G2ContinuityProfile.RoundedRectangle.copy(
        extendedFraction = 0.5,
        arcFraction = 0.5,
        bezierCurvatureScale = 1.1,
        arcCurvatureScale = 1.1
    ),
    capsuleProfile = G2ContinuityProfile.Capsule.copy(
        extendedFraction = 0.5,
        arcFraction = 0.25
    )
)

/**
 * A standard pill/capsule shape using Apple-like continuous G2 curves.
 */
val ExpressGLCapsule: Shape = ContinuousCapsule(continuity = ExpressGLContinuity)

/**
 * A rounded rectangle with Apple-like continuous G2 corners.
 */
fun ExpressGLRectCapsule(
    radius: Dp = 24.dp
): Shape = ContinuousRoundedRectangle(radius, continuity = ExpressGLContinuity)

/**
 * A triangle with smooth corners.
 */
fun TriangleShape(cornerRadius: Dp = 8.dp): Shape = SmoothPolygonShape(sides = 3, cornerRadius = cornerRadius)

/**
 * A regular polygon (4 to 20 sides) with smooth corners.
 *
 * @param sides The number of sides (e.g. 5 for pentagon, 8 for octagon).
 * @param cornerRadius The radius of the rounded corners.
 */
class SmoothPolygonShape(
    private val sides: Int,
    private val cornerRadius: Dp = 8.dp
) : Shape {
    init {
        require(sides in 3..20) { "Polygon sides must be between 3 and 20" }
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val radiusPx = with(density) { cornerRadius.toPx() }
        
        val width = size.width
        val height = size.height
        val cx = width / 2f
        val cy = height / 2f
        val radius = min(width, height) / 2f

        // We calculate vertices of a regular polygon inscribed in the circle.
        // -PI/2 offset starts the first vertex at the top center.
        val angleStep = (2 * PI / sides).toFloat()
        val startAngle = (-PI / 2).toFloat()

        val vertices = Array(sides) { i ->
            val angle = startAngle + i * angleStep
            androidx.compose.ui.geometry.Offset(
                x = cx + radius * cos(angle),
                y = cy + radius * sin(angle)
            )
        }

        // To make rounded corners, we use quadratic beziers.
        // For each corner i, we find the midpoints of the adjacent edges, 
        // or a certain distance away from the vertex based on cornerRadius.
        
        // The distance from the vertex to the tangent point of the corner arc.
        // We cap it at half the edge length so corners don't overlap.
        val edgeLength = 2 * radius * sin(PI / sides).toFloat()
        val maxCornerRadius = edgeLength / 2f
        val actualRadius = min(radiusPx, maxCornerRadius)

        // The fraction of the edge length to move from the vertex to the tangent point.
        val t = if (edgeLength > 0) actualRadius / edgeLength else 0f

        for (i in 0 until sides) {
            val prev = vertices[(i - 1 + sides) % sides]
            val curr = vertices[i]
            val next = vertices[(i + 1) % sides]

            // Vector from curr to prev
            val dx1 = prev.x - curr.x
            val dy1 = prev.y - curr.y
            
            // Vector from curr to next
            val dx2 = next.x - curr.x
            val dy2 = next.y - curr.y

            // Point on the edge towards prev
            val p1x = curr.x + dx1 * t
            val p1y = curr.y + dy1 * t

            // Point on the edge towards next
            val p2x = curr.x + dx2 * t
            val p2y = curr.y + dy2 * t

            if (i == 0) {
                path.moveTo(p1x, p1y)
            } else {
                path.lineTo(p1x, p1y)
            }

            // Draw rounded corner using quadratic bezier curve using the vertex as the control point
            path.quadraticTo(curr.x, curr.y, p2x, p2y)
        }

        path.close()
        return Outline.Generic(path)
    }
}
