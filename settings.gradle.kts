pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "8.5.2"
        id("org.jetbrains.kotlin.android") version "1.9.0"
        id("androidx.navigation.safeargs.kotlin") version "2.8.0"
        id("com.google.dagger.hilt.android") version "2.48"
        id("org.jetbrains.kotlin.plugin.parcelize") version "1.9.0"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Epic_Choice"
include(":app")

 