package ark.development.expressgl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Rocket
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ark.development.expressgl.library.components.ExpressGLBottomBar
import ark.development.expressgl.library.components.ExpressGLTabItem
import ark.development.expressgl.ui.theme.ExpressivePeach
import ark.development.expressgl.ui.theme.ExpressivePurple
import ark.development.expressgl.ui.theme.ExpressiveExpressGLTheme
import ark.development.expressgl.ui.theme.ExpressiveTeal
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import ark.development.expressgl.library.layout.ExpressGLGooeyContainer
import ark.development.expressgl.library.shapes.ExpressGLCapsule
import ark.development.expressgl.library.shapes.SmoothPolygonShape
import ark.development.expressgl.library.shapes.TriangleShape
import ark.development.expressgl.library.theme.ExpressGLColors
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpressiveExpressGLTheme {
                ShowcaseApp()
            }
        }
    }
}

@Composable
fun ShowcaseApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    ),
                ),
            )
            .padding(top = 48.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "ExpressGL Expansion",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        // 1. Shapes
        Text(
            text = "Smooth Shapes",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(Modifier.size(60.dp).clip(TriangleShape()).background(ExpressiveTeal))
            Box(Modifier.size(60.dp).clip(SmoothPolygonShape(sides = 5)).background(ExpressivePurple))
            Box(Modifier.size(60.dp).clip(SmoothPolygonShape(sides = 8)).background(ExpressivePeach))
            Box(Modifier.size(60.dp).clip(ExpressGLCapsule).background(MaterialTheme.colorScheme.primary))
        }

        // 3. Gooey Merging
        Text(
            text = "Gooey Merging",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        var dragOffset by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }
        ExpressGLGooeyContainer(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(32.dp))
        ) {
            // Static Circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
                    .background(Color.Red, CircleShape)
            )

            // Draggable Capsule
            Box(
                modifier = Modifier
                    .offset { IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt()) }
                    .size(80.dp, 120.dp)
                    .align(Alignment.Center)
                    .background(Color.Red, ExpressGLCapsule)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            dragOffset += dragAmount
                        }
                    }
            )
        }

        // 4. Original Bottom Bar
        var selectedTab by remember { mutableIntStateOf(0) }
        val tabs = listOf(
            ExpressGLTabItem(Icons.Rounded.TouchApp, "Shapes"),
            ExpressGLTabItem(Icons.Rounded.Star, "Gooey"),
            ExpressGLTabItem(Icons.Rounded.AutoAwesome, "Tilt")
        )
        ExpressGLBottomBar(
            items = tabs,
            selectedIndex = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}