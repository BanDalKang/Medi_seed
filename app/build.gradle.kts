import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

val properties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "com.mediseed.mediseed"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mediseed.mediseed"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "NAVER_MAP_CLIENT_ID", properties["NAVER_MAP_CLIENT_ID"].toString())
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            manifestPlaceholders["MANIFEST_NAVER_MAP_CLIENT_ID"] = properties["MANIFEST_NAVER_MAP_CLIENT_ID"] as String
        }
        release {
            isMinifyEnabled = false
            manifestPlaceholders["MANIFEST_NAVER_MAP_CLIENT_ID"] = properties["MANIFEST_NAVER_MAP_CLIENT_ID"] as String
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // Naver Map Sdk
    implementation("com.naver.maps:map-sdk:3.18.0")
    // Google Location Service
    implementation("com.google.android.gms:play-services-location:20.0.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}