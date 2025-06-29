# README

# 📱 Android Java – Environment Setup Guide

This guide walks you through setting up a development environment for an Android frontend project using Java and Android Studio.

---

## 🧰 Prerequisites

* **Operating System**: Windows 10/11, macOS 10.14+, or Linux (Ubuntu/Debian/RHEL)
* **Memory**: ≥8 GB RAM (16 GB+ recommended)
* **Disk Space**: ≥10 GB free

---

## 1. Install Java Development Kit (JDK)

Android Studio requires a compatible JDK. We recommend **OpenJDK 11** or newer.

### macOS / Linux / Windows

1. **Download and install OpenJDK 11** from [AdoptOpenJDK](https://adoptopenjdk.net/) or [Azul Zulu](https://www.azul.com/downloads/).
2. **Verify installation**:

   ```bash
   java -version
   ```

   Should display `openjdk version "11.x.x"` or similar.

---

## 2. Install Android Studio

Android Studio bundles the Android SDK, emulator, and Gradle.

1. **Download** the latest Android Studio from [developer.android.com](https://developer.android.com/studio).
2. **Run the installer** and follow the wizard:

   * On first launch, choose **Standard** installation.
   * Ensure **Android SDK**, **Android SDK Platform-Tools**, and **Android SDK Build-Tools** are selected.
3. **Finish** and launch Android Studio.

---

## 3. Configure SDK & Command-Line Tools

1. In Android Studio, go to **File → Settings** (macOS: **Android Studio → Preferences**).
2. Navigate to **Appearance & Behavior → System Settings → Android SDK**.

   * Under **SDK Platforms**, select the desired Android API levels (e.g., API 30+).
   * Under **SDK Tools**, ensure **Android SDK Command-line Tools**, **NDK (optional)**, and **Android Emulator** are installed.
3. Click **Apply** to install.

---

## 4. Set Environment Variables (Optional)

If you need to run SDK tools from the terminal, add the following to your shell/profile:

```bash
# Example for macOS/Linux (~/.bashrc or ~/.zshrc)
export ANDROID_HOME="$HOME/Android/Sdk"
export PATH="$PATH:$ANDROID_HOME/emulator"
export PATH="$PATH:$ANDROID_HOME/tools"
export PATH="$PATH:$ANDROID_HOME/tools/bin"
export PATH="$PATH:$ANDROID_HOME/platform-tools"
```

On Windows, set these in **System Properties → Environment Variables**.

---

## 5. Clone & Open the Project

1. **Clone the repository**:

   ```bash
   git clone https://github.com/your-org/your-android-app.git
   cd your-android-app
   ```
2. **Open Android Studio**:

   * Choose **Open an Existing Project** and select the cloned folder.
3. **Sync Gradle**:

   * Click **Sync Now** in the yellow banner or **File → Sync Project with Gradle Files**.

---

## 6. Run on Emulator or Device

1. **Start an emulator** via **AVD Manager** (icon in the toolbar), or connect a physical device with **USB debugging** enabled.
2. **Select a run target** and click the **Run ▶️** button.
3. **Monitor logs** in **Logcat** and fix any build/runtime issues.

---

## 7. Useful Commands

| Command                   | Description                                    |
| ------------------------- | ---------------------------------------------- |
| `./gradlew assembleDebug` | Build debug APK                                |
| `./gradlew installDebug`  | Install debug APK on connected device/emulator |
| `adb devices`             | List connected Android devices                 |
| `adb logcat`              | Stream device logs                             |

---

## 8. Additional Tips

* **Enable Instant Run** for faster deploys (Android Studio feature).
* **Use Android Profiler** to analyze performance.
* **Configure Lint** in `build.gradle` for code quality checks.

---

*Happy Android development!* 🚀
