# ✨ Expressive Glass

> **Material Expressive Look. Apple Fluid Feel.**

A Jetpack Compose UI library that fuses Material Design's bold, expressive visual language with Apple's physics-driven interaction model. Every component *looks* Material Expressive and *feels* like Apple's Liquid Glass.

Woo Hoo... Even thought this project is marked in development I think we are done... the Sliders a already pretty good and so are the loading bars in material Expressive 3.
All I have to do is optimise now and lower down the api level slowly but gradually.

Just a note but i am focussing on another project so for a while do not expect updates until at least next month!

[![](https://jitpack.io/v/Aarav90-cpu/Expressive-Glass.svg)](https://jitpack.io/#Aarav90-cpu/Expressive-Glass)
![API](https://img.shields.io/badge/API-36%2B-brightgreen.svg)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.05-blue.svg)
![License](https://img.shields.io/badge/License-Apache%202.0-orange.svg)

DEMOVIDEO = https://github.com/Aarav90-cpu/Expressive-Glass/blob/main/DEMOVIDEO/expressgl_demo.mp4

---

## 🫧 Philosophy

| | Material Expressive | Apple Liquid Glass | **Expressive Glass** |
|---|---|---|---|
| **Shapes** | Bold squircles, expressive radii | Smooth, continuous curves | ✅  Both |
| **Motion** | Emphasized easing | Spring physics, fluid drag |  Spring physics everywhere |
| **Surfaces** | Tonal elevation, color | Frosted glass, translucency |  Frosted glass + tonal color |
| **Feedback** | Ripple | Scale, stretch, rubber-band |  Scale + dynamic glow |
| **Highlights** | — | Static edge refraction |  Dynamic reflections |

---

## 📚 Documentation

Dive deep into the Expressive Glass components, their physics, and how to customize them down to the last pixel and color stop.

- [🗂️ ExpressGLBottomBar](docs/bottom_tabs.md)
- [🎚️ ExpressGLToggle](docs/toggle.md)
- [🎨 Theme Usage](docs/theme_usage.md)
- [🟢 How to Create Mergeable Shapes](docs/how_to_create_mergeable_shapes.md)

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
├── theme/
│   └── ExpressGLTheme.kt          — ExpressGLStyle, CompositionLocal, defaults
└── components/
    └── ExpressGLBottomBar.kt      — Draggable pill bottom nav
```

---

## 💖 Credits

A huge thank you to [Kyant0](https://github.com/Kyant0) for their amazing [capsule](https://github.com/Kyant0/capsule) library.

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
