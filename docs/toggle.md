# 🎚️ ExpressGLToggle

A fluid, squishy toggle switch that merges Material 3 boldness with Apple-like spring physics.

## Usage

```kotlin
var isChecked by remember { mutableStateOf(true) }

ExpressGLToggle(
    checked = isChecked,
    onCheckedChange = { isChecked = it }
)
```

## Physics Breakdown

- **Squish on press:** Pressing and holding the toggle causes the thumb to expand its width dynamically (`1.35x` the normal size) and height (`1.15x`).
- **Stretch on drag:** If you drag the thumb manually, its shape morphs and stretches based on your finger's drag velocity, creating an organic liquid slosh.
- **Bouncy snap:** Letting go of the thumb snaps it to the nearest state (ON or OFF) using `ExpressGLSprings.bouncy()`.

## Deep Color Customization

You can control exactly how the toggle looks across all states down to the exact stroke outline color:

```kotlin
ExpressGLToggle(
    checked = isChecked,
    onCheckedChange = { isChecked = it },
    // 1. The solid color of the thumb when ON
    checkedColor = ExpressivePurple,
    // 2. The muted color of the thumb when OFF
    uncheckedColor = Color.Gray.copy(alpha = 0.3f),
    // 3. The frosted translucent track background
    trackColor = Color.Black.copy(alpha = 0.85f),
    // 4. The glowing inner stroke border around the track
    outlineColor = Color.White.copy(alpha = 0.4f),
    // Dimensions
    width = 80.dp,
    height = 36.dp
)
```

## The Blocked State
Just like the bottom bar, the toggle supports `isBlocked = true`.

```kotlin
ExpressGLToggle(
    checked = false,
    onCheckedChange = {},
    isBlocked = true
)
```

Attempting to interact with a blocked toggle will trigger a physics-driven rejection animation where the toggle track expands and vibrates, while the thumb bounces helplessly inside.
