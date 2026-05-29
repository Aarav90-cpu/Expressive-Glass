# ✨ Expressive Glass

> **Material Expressive Look. Apple Fluid Feel.**

A Jetpack Compose UI library that fuses Material Design's bold, expressive visual language with Apple's physics-driven interaction model. Every component *looks* Material Expressive and *feels* like Apple's Liquid Glass.

[![](https://jitpack.io/v/Aarav90-cpu/Expressive-Glass.svg)](https://jitpack.io/#Aarav90-cpu/Expressive-Glass)
![API](https://img.shields.io/badge/API-36%2B-brightgreen.svg)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.05-blue.svg)
![License](https://img.shields.io/badge/License-Apache%202.0-orange.svg)

---

## 🫧 Philosophy

| | Material Expressive | Apple Liquid Glass | **Expressive Glass** |
|---|---|---|---|
| **Shapes** | Bold squircles, expressive radii | Smooth, continuous curves | Both |
| **Motion** | Emphasized easing | Spring physics, fluid drag |  Spring physics everywhere |
| **Surfaces** | Tonal elevation, color | Frosted glass, translucency |  Frosted glass + tonal color |
| **Feedback** | Ripple | Scale, stretch, rubber-band |  Scale + specular glow |
| **Highlights** | — | Specular edge refraction |  Animated specular sweep |

---

## 🧩 Components

### `ExpressGLBottomBar`
Material Bottom Navigation with a draggable fluid pill indicator.

- **Draggable pill** — Grab the selection pill and drag it between tabs
- **Spring snap** — Pill snaps to nearest tab with bouncy physics
- **Frosted bar** — Translucent glass background with specular edge
- **Morphing icon** — Selected icon scales up expressively

```kotlin
ExpressGLBottomBar(
    items = listOf(
        ExpressGLTabItem(Icons.Rounded.Home, "Home"),
        ExpressGLTabItem(Icons.Rounded.Search, "Search"),
        ExpressGLTabItem(Icons.Rounded.Settings, "Settings"),
    ),
    selectedIndex = selectedTab,
    onTabSelected = { selectedTab = it },
)
```

---

## 🎨 Shared Effects

### Specular Highlight
```kotlin
// Static edge glow
Modifier.specularEdge()
```

### Glass Theme
```kotlin
// Override glass defaults anywhere in the tree
CompositionLocalProvider(
    LocalExpressGLStyle provides ExpressGLStyle(
        specularIntensity = 0.5f,
        cornerRadius = 32.dp,
    )
) {
    ExpressGLBottomBar(...)
}
```

---

## 📦 Installation

### Step 1: Add JitPack repository

**`settings.gradle.kts`**
```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2: Add the dependency

**`build.gradle.kts`** (app module)
```kotlin
	dependencies {
	        implementation("com.github.Aarav90-cpu:Expressive-Glass:<VERSION>")
	}
```

---

## ⚙️ Requirements

| Requirement | Version |
|---|---|
| Min SDK | **36** (Android 16) |
| Compose BOM | **2026.05.01** |
| Kotlin | **2.3.21** |
| AGP | **9.2.1** |

---

## 📐 Architecture

```
Expressive Glass/
├── effect/
│   └── SpecularHighlight.kt   — Static glass edge highlight
├── theme/
│   └── ExpressGLTheme.kt          — ExpressGLStyle, CompositionLocal, defaults
└── components/
    └── ExpressGLBottomBar.kt      — Draggable pill bottom nav
```

---

## 📄 License

```
Copyright 2024 Aarav Ravindra Kharade

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
