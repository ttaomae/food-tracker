plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
}

apply(plugin = "androidx.navigation.safeargs.kotlin")
android {
    namespace = "ttaomae.foodtracker"

    compileSdk = Versions.Android.compileSdk
    buildToolsVersion = Versions.Android.buildToolsVersion

    defaultConfig {
        applicationId = "ttaomae.foodtracker"
        minSdk = Versions.Android.minSdk
        targetSdk = Versions.Android.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}")
    implementation("androidx.core:core-ktx:${Versions.coreKtx}")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.nav}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.nav}")
    implementation("androidx.room:room-runtime:${Versions.room}")
    implementation("androidx.room:room-ktx:${Versions.room}")
    implementation("com.google.android.material:material:${Versions.material}")
    implementation("com.google.dagger:hilt-android:${Versions.hilt}")
    implementation("com.squareup.moshi:moshi:${Versions.moshi}")

    kapt("androidx.room:room-compiler:${Versions.room}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.hilt}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}")
}
