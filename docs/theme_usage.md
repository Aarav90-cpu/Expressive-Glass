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
