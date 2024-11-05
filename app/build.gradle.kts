plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.parcelize")
    alias(libs.plugins.google.gms.google.services)
    //id("com.google.devtools.ksp")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.epic_choice"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.epic_choice"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/ASL2.0",
                "META-INF/INDEX.LIST",
                "*.so"
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.junit)
    implementation(libs.volley)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose dependencies
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.coil.compose)

    // Third-party libraries
    implementation(libs.leandroborgesferreira.loading.button.android)
    implementation(libs.glide)
    kapt(libs.compiler)
    //ksp("androidx.room:room-compiler:2.5.0")
    implementation(libs.circleimageview)
    implementation(libs.viewpager.indicator)
    implementation(libs.stepview)

    // Android KTX extensions for Navigation
    implementation(libs.androidx.navigation.fragment.ktx)

    // Dagger Hilt for dependency injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Firebase dependencies
    implementation(libs.firebase.firestore.v2510) {
        exclude(group = "com.google.api.grpc", module = "proto-google-common-protos")
    }
    implementation(libs.firebase.auth.v2300)

    // Navigation component
    implementation(libs.androidx.navigation.fragment.ktx.v252)
    implementation(libs.androidx.navigation.ui.ktx)

    // Translator
    implementation(libs.google.cloud.translate) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
        exclude(group = "com.google.api.grpc", module = "proto-google-common-protos")
    }

    implementation (libs.volley)


    // Force specific versions
    implementation("com.google.protobuf:protobuf-javalite:3.25.1")
    implementation("com.google.firebase:protolite-well-known-types:18.0.0") {
        exclude(group = "com.google.api.grpc", module = "proto-google-common-protos")
    }

    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.google.zxing:core:3.4.1")
}

configurations.all {
    resolutionStrategy {
        force ("com.google.protobuf:protobuf-javalite:3.25.1")
    }
}

