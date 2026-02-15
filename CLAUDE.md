# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ContinuousPlayer is an Android application (package: `jp.nagu.continuousplayer`). Single-module project currently in early scaffold stage.

## Build & Test Commands

```bash
# Build
./gradlew assembleDebug
./gradlew assembleRelease

# Unit tests
./gradlew test
# Single test class
./gradlew testDebugUnitTest --tests "jp.nagu.continuousplayer.ExampleUnitTest"

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Clean build
./gradlew clean assembleDebug
```

## SDK & Toolchain

- **Compile SDK:** 36 (minorApiLevel 1)
- **Min SDK:** 34, **Target SDK:** 36
- **Gradle:** 9.2.1, **AGP:** 9.0.1
- **Java:** source/target 11, toolchain 21
- **Kotlin** build scripts (.kts)

## Project Structure

- Single `:app` module
- Version catalog at `gradle/libs.versions.toml` for dependency management
- Test runner: JUnit 4 + AndroidX Test + Espresso
