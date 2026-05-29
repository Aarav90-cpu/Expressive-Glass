# 🟢 How to Create Mergeable Shapes

Expressive Glass components look incredibly fluid because they avoid sharp, jagged edges and rely on mathematically continuous curves, such as Squircles and Capsules.

## Using `ExpressGLCapsule`

If you are building your own components that need to "merge" visually with the rest of the library, use `ExpressGLCapsule`. This is a custom Compose `Shape` that creates a perfect, continuous pill shape.

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

### Example: Sloshing a Shape

```kotlin
val dynamicWidth by animateDpAsState(
    targetValue = if (isPressed) 120.dp else 100.dp,
    animationSpec = ExpressGLSprings.fluid() // The secret sauce!
)

Box(
    modifier = Modifier
        .width(dynamicWidth)
        .height(50.dp)
        .clip(ExpressGLCapsule)
)
```

By binding `isPressed` or drag velocity to the width and clamping it with a `fluid()` spring, your custom shapes will instantly feel like they belong in the Expressive Glass ecosystem.
