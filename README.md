# Kode
Kode is a **Kotlin Script IDE** built with **Kotlin Compose Multiplatform** for Desktop, designed to provide a modern, lightweight development environment for writing and executing Kotlin scripts (`kts files`).

It features syntax highlighting, live output streaming during execution, and clickable error navigation, all wrapped in a customizable, cross-platform interface.

> The goal was to create a functional GUI tool that allows users to write scripts, execute them, and view output in real-time within a clean and interactive interface.
>
> Throughout this project, I gained hands-on experience building a desktop application with **Compose for Desktop** and **Kotlin Multiplatform**. The implementation required managing external **processes** for script execution, handling **asynchronous** operations with Coroutines, and developing core IDE features like syntax highlighting and error navigation.

## Table of Contents

- [Project Demo](#project-demo)
- [Features](#features)
- [Building and Running](#building-and-running)
  - [Quick Start](#quick-start)
  - [Build from Source](#build-from-source)

## Project Demo

https://github.com/user-attachments/assets/f849b28e-88ed-431d-99db-faa689822ff4

> **Note:** The demo showcases only some of the features of the project.

## Features

- **Syntax Highlighting** - Kotlin syntax highlighting with support for keywords, strings, comments, and more
- **Error Navigation** - Clickable error locations (e.g., `script:2:1: error`) that jump directly to the problematic line in the editor
- **Code Execution** - Run Kotlin code directly from the IDE with real-time output streaming
- **Customizable UI** - Adjust themes and editor settings to match your preferences
- **File Management** - Open, edit, and save Kotlin script files
- **Cross-Platform** - Runs on Windows, macOS, and Linux using Compose Multiplatform

## Building and Running

### Quick Start

Pre-built binaries and detailed installation instructions are available on the [Releases page](https://github.com/JovanMosurovic/Kode/releases).

### Build from Source

**Prerequisites:**
- JDK 17 or higher
- [Kotlin compiler](https://kotlinlang.org/docs/command-line.html) (`kotlinc`) installed and available in PATH
- Gradle
  
> [!TIP]
> *You can use any IDE of your choice, but [IntelliJ IDEA](https://www.jetbrains.com/idea/) is recommended since the project and exports were developed and tested in it.*  

#### Running the Application

**On Windows:**
```cmd
.\gradlew.bat :composeApp:run
```

**On macOS/Linux:**
```bash
./gradlew :composeApp:run
```

#### Creating a Distributable Package
```bash
./gradlew :composeApp:packageDistributionForCurrentOS
```
This will generate a platform-specific distribution in `composeApp/build/compose/binaries/main/`.

### Generated README for Kotlin Multiplatform Compose Application

This is a Kotlin Multiplatform project targeting Desktop (JVM).

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
      Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
      folder is the appropriate location.

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
