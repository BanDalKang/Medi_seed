import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
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
        buildConfigField("String", "NAVER_MAP_CLIENT_ID", properties["NAVER_MAP_CLIENT_ID"] as String)
        buildConfigField("String","PUBLIC_DATA_PHARMACY_ENCODING",properties["PUBLIC_DATA_PHARMACY_ENCODING"] as String)
        buildConfigField("String","PUBLIC_DATA_PHARMACY_DECODING",properties["PUBLIC_DATA_PHARMACY_DECODING"] as String)

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
    implementation("com.airbnb.android:lottie:3.7.0")
    // Naver Map Sdk
    implementation("com.naver.maps:map-sdk:3.18.0")
    // Google Location Service
    implementation("com.google.android.gms:play-services-location:21.0.1")
    // Retrofit2
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    // firebase
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation(libs.firebase.database.ktx)
    //카드뷰
    implementation("androidx.cardview:cardview:1.0.0")
    //lottie
    implementation("com.airbnb.android:lottie:3.7.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}