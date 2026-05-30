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
- **Velocity-Driven Liquid Stretching:** If you drag the thumb manually, it morphs and stretches based on your finger's exact drag velocity! It uses the custom `liquidSquashAndStretch` modifier with volume preservation, stretching up to a max ratio of `0.2f`—giving it an organic slosh without creating jagged sharp corners.
- **Bouncy snap:** Letting go of the thumb snaps it to the nearest state (ON or OFF) using `ExpressGLSprings.bouncy()`.

## Deep Color Customization & Dynamic Tracks

You can control exactly how the toggle looks across all states down to the exact stroke outline color. You can even pass an `activeTrackColor` to crossfade the entire frosted glass container when the toggle is on!

```kotlin
ExpressGLToggle(
    checked = isChecked,
    onCheckedChange = { isChecked = it },
    // 1. The solid color of the thumb when ON
    checkedColor = ExpressivePurple,
    // 2. The muted color of the thumb when OFF
    uncheckedColor = Color.Gray.copy(alpha = 0.3f),
    // 3. The frosted translucent track background (Default state)
    trackColor = Color.Black.copy(alpha = 0.85f),
    // 4. (NEW) The track background when ON! Crossfades seamlessly.
    activeTrackColor = ExpressivePurple.copy(alpha = 0.3f),
    // 5. The glowing inner stroke border around the track
    outlineColor = Color.White.copy(alpha = 0.4f),
    // Dimensions
    width = 80.dp,
    height = 36.dp
)
```
*Note: The `activeTrackColor` dynamically crossfades not just on tap, but in real-time as you drag the thumb across the 50% midpoint threshold!*

## The Blocked State (Rejection Physics)
Just like the bottom bar, the toggle supports `isBlocked = true`.

```kotlin
ExpressGLToggle(
    checked = false,
    onCheckedChange = {},
    isBlocked = true
)
```

Attempting to interact with a blocked toggle will trigger a physics-driven rejection animation where the toggle track expands and physically vibrates left and right, while the thumb bounces helplessly inside the container.
