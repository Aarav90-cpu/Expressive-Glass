# 🗂️ ExpressGLBottomBar

The `ExpressGLBottomBar` is a Material 3 bottom navigation bar with Apple Liquid Glass physics. Instead of standard ripples and static indicators, the active tab is indicated by a "squishy pill" that sloshes around as it moves, bouncing into place using heavy spring physics.

## Usage

```kotlin
var selectedTab by remember { mutableIntStateOf(0) }
val tabs = listOf(
    ExpressGLTabItem(Icons.Rounded.Home, "Home"),
    ExpressGLTabItem(Icons.Rounded.Search, "Search"),
    ExpressGLTabItem(Icons.Rounded.Settings, "Settings"),
)

ExpressGLBottomBar(
    items = tabs,
    selectedIndex = selectedTab,
    onTabSelected = { selectedTab = it },
)
```

## Deep Customization

### Active and Inactive Colors
You can control the precise tint of the icons and text depending on their state.

```kotlin
ExpressGLBottomBar(
    items = tabs,
    selectedIndex = selectedTab,
    onTabSelected = { selectedTab = it },
    activeColor = Color.Black,
    inactiveColor = Color.Gray.copy(alpha = 0.5f),
)
```

### The Blocked State (Rejection Animation)
You can declare a tab as `isBlocked = true` in the `ExpressGLTabItem` data class.

```kotlin
ExpressGLTabItem(Icons.Rounded.Lock, "Pro Feature", isBlocked = true)
```

When a user taps or drags the pill to a blocked tab, the bar will dynamically expand and shake horizontally (a physics-driven "No" animation), and the pill will be thrown back to the original selected tab with a heavy initial velocity, causing a massive "slosh" effect.

### Glass Translucency
The background of the bottom bar is rendered as frosted glass. This relies on the global `LocalExpressGLStyle` to render its blur and translucent overlay. Check the [Theme Usage](theme_usage.md) guide for how to adjust the glass depth globally.
