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
            text = "Fluid Glass Bars",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        // Generate bars from 2 to 7 tabs
        for (count in 2..7) {
            var selectedTab by remember { mutableIntStateOf(0) }
            
            val tabs = remember(count) {
                List(count) { i ->
                    val icons = listOf(
                        Icons.Rounded.TouchApp,
                        Icons.Rounded.Dashboard,
                        Icons.Rounded.Info,
                        Icons.Rounded.Star,
                        Icons.Rounded.Palette,
                        Icons.Rounded.Rocket,
                        Icons.Rounded.AutoAwesome
                    )
                    val labels = listOf("Controls", "Cards", "About", "Favorites", "Theme", "Boost", "Magic")
                    ExpressGLTabItem(
                        icon = icons[i % icons.size],
                        label = labels[i % labels.size]
                    )
                }
            }

            // Alternate colors for variety
            val (containerColor, pillColor, activeColor) = when (count) {
                3 -> Triple(ExpressiveTeal.copy(alpha = 0.3f), ExpressiveTeal, Color.White)
                5 -> Triple(ExpressivePurple.copy(alpha = 0.2f), ExpressivePurple, Color.White)
                7 -> Triple(ExpressivePeach.copy(alpha = 0.2f), ExpressivePeach, Color.White)
                else -> Triple(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "$count Tabs",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
                
                ExpressGLBottomBar(
                    items = tabs,
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it },
                    containerColor = containerColor,
                    pillColor = pillColor,
                    activeColor = activeColor,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}