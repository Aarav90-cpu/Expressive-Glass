# 🗂️ ExpressGLBottomBar

The `ExpressGLBottomBar` is a next-generation Material 3 bottom navigation bar infused with Apple Liquid Glass physics. Instead of standard ripples and static indicators, the active tab is wrapped by a "squishy pill" that sloshes around as it moves, stretching dynamically based on your swipe velocity, and bouncing into place using heavy spring physics.

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

## Features Deep Dive

### 📏 Dynamic Content Wrapping & Resizing
The pill doesn't just guess its width—it actively measures the actual visual footprint of the Icon and Text inside every tab! As the pill slides from a short word (like "Pro") to a long word (like "Dashboard"), its width smoothly morphs to wrap the new content perfectly.
- **Minimum Size Constraints**: If a tab's word is too small, the pill falls back to a minimum width ratio (0.85x of the tab cell when resting, 1.05x when active) to keep it from looking squished.
- **Snappy Spring Resizing**: The width and height use a custom `ExpressGLSprings.snappy()` animation spec, meaning it snaps to its new size incredibly fast without lagging behind the fluid sliding motion.

### 🎨 Active and Inactive Colors & Proximity Tinting
You can control the precise tint of the icons and text. But there's a twist: the color of the text and icon **dynamically crossfades** based on the exact proximity of the pill! As the pill slides over a tab, the tint gradually lerps from the inactive color to the active color.

```kotlin
ExpressGLBottomBar(
    items = tabs,
    selectedIndex = selectedTab,
    onTabSelected = { selectedTab = it },
    activeColor = Color.White,
    inactiveColor = Color.Gray.copy(alpha = 0.5f),
)
```

### 🚫 The Blocked State (Rejection Animation)
You can declare a tab as `isBlocked = true` in the `ExpressGLTabItem` data class.

```kotlin
ExpressGLTabItem(Icons.Rounded.Lock, "Pro Feature", isBlocked = true)
```

When a user taps or drags the pill to a blocked tab, the bar will dynamically expand and shake horizontally (a physics-driven "No" vibration), and the pill will be violently thrown back to the original selected tab with a heavy initial velocity, causing a massive liquid "slosh" effect.

### 🧮 Multi-Tab Layouts (2 to 7 Tabs)
The bottom bar mathematically aligns perfectly no matter how many tabs you throw at it. Whether you are using a spacious 2-tab layout or a tightly-packed 7-tab bar, the tabs are perfectly divided, padding is perfectly negated, and the pill centers flawlessly under the content.

### 🧊 Glass Translucency & Styling
The background of the bottom bar is rendered as frosted glass. This relies on the global `LocalExpressGLStyle` to render its blur and translucent overlay. It features a dual-layer inner border stroke (a glowing white top edge fading into transparency) to simulate glass depth.
