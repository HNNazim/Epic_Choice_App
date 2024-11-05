buildscript {
    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.kotlin.kapt") version "1.9.0"
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.0" apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.compose.compiler) apply false
        id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false

}

tasks.register<Delete>("customClean") {
    delete(rootProject.buildDir)
}

