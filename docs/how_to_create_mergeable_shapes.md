# 🟢 How to Create Mergeable Shapes

Expressive Glass components look incredibly fluid because they avoid sharp, jagged edges and rely on mathematically continuous curves, such as Squircles and Capsules.

## Using `ExpressGLCapsule`

If you are building your own components that need to "merge" visually with the rest of the library, use `ExpressGLCapsule`. This is a custom Compose `Shape` that creates a perfect, continuous pill shape without standard UI clipping artifacts.

```kotlin
import ark.development.expressgl.library.shapes.ExpressGLCapsule

Box(
    modifier = Modifier
        .size(100.dp, 50.dp)
        .clip(ExpressGLCapsule)
        .background(Color.Blue)
)
```

## Making Shapes Liquid and Mergeable

To make shapes feel like Apple's liquid glass, you must animate their dimensions (width/height) using `ExpressGLSprings` rather than tween/linear animations.

When a shape stretches horizontally while maintaining a `ExpressGLCapsule` clip, the rounded corners remain perfectly smooth, creating a "slosh" or "merge" effect.

### 🪄 The `liquidSquashAndStretch` Modifier
The most powerful tool in the Expressive Glass arsenal is the custom `liquidSquashAndStretch` modifier. 

It uses a mathematical approach to volume preservation. As an object moves quickly (measured via a velocity Float), it organically stretches in the direction of movement and squashes perpendicularly, just like a falling drop of water. 

```kotlin
val thumbCenter = remember { Animatable(0f) }

Box(
    modifier = Modifier
        .liquidSquashAndStretch(
            velocity = thumbCenter.velocity, // Driven by an Animatable's current velocity!
            componentWidth = thumbWidth.toPx(),
            maxStretchRatio = 0.2f,          // Limits how far it can distort to prevent sharp edges
            volumePreservationFactor = 0.3f  // How much the height squishes as the width stretches
        )
        .width(thumbWidth)
        .height(thumbHeight)
        .clip(ExpressGLCapsule)
)
```

### Animating Sizes with ExpressGLSprings
If you are changing the actual hard width or height of a component, bind it to one of our curated springs:

```kotlin
val dynamicWidth by animateDpAsState(
    targetValue = if (isPressed) 120.dp else 100.dp,
    animationSpec = ExpressGLSprings.fluid() // The Apple liquid feel
)
```
- **`fluid()`**: Low stiffness, medium bouncy. Perfect for large state changes (like pill active states).
- **`snappy()`**: Medium-low stiffness. Great for rapidly recovering an object's size after a state ends (e.g. shrinking a tab quickly so it doesn't lag).
- **`bouncy()`**: High bounce. Used for micro-interactions and rejection shakes.
