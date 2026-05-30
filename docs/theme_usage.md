# 🎨 Theme Usage

Expressive Glass components rely on a shared design language provided by `ExpressGLTheme`. This theme merges Apple's glass translucency with Material's tonal elevation.

## Wrapping your app

To ensure components like `ExpressGLBottomBar` and `ExpressGLToggle` look right, you should wrap your app (or at least the component tree) in the `ExpressiveExpressGLTheme` (or your custom equivalent) which injects the `LocalExpressGLStyle` composition local.

```kotlin
ExpressiveExpressGLTheme {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Your expressive glass components here
    }
}
```

## Deep Customization with Composition Locals

If you want to control the microscopic aesthetic details—like the exact corner radii of all squishy components, or the depth of the glass blur—you can override the `LocalExpressGLStyle` anywhere in your tree.

```kotlin
CompositionLocalProvider(
    LocalExpressGLStyle provides ExpressGLStyle(
        // Makes all expressive components rounder
        cornerRadius = 32.dp,
    )
) {
    // Components inside here will inherit the 32.dp corner radius
    ExpressGLBottomBar(...)
}
```

The `ExpressGLStyle` data class allows you to inject these variables deeply without having to pass `cornerRadius` or `blurRadius` into every single component's constructor!

## The Physics Theme (ExpressGLSprings)
While `ExpressGLStyle` handles the visuals, the library's feel is dictated by `ExpressGLSprings`. Whenever you create a custom component using the theme, ensure you bind its sizes and translations to these springs:
- `fluid()`: Use this for primary shape morphing (squashing/stretching) and sliding.
- `snappy()`: Use this for fast fallbacks, such as a component snapping back to its original dimensions without lag.
- `bouncy()`: Use this for playful interactions, like a toggle snapping to a side or a component shaking when rejected.
