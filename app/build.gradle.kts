
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}



android {
    namespace = "ru.mygames.vk"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.mygames.vk"
        minSdk = 24
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation ("androidx.core:core-ktx:1.16.0")
    implementation ("androidx.compose.ui:ui:1.8.3")
    implementation ("androidx.compose.material:material:1.8.3")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.8.3")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    implementation ("androidx.activity:activity-compose:1.10.1")
    implementation ("androidx.navigation:navigation-compose:2.9.0")
    implementation ("androidx.compose.runtime:runtime-livedata:1.8.3")
    implementation ("io.coil-kt:coil-compose:2.5.0")
    implementation ("com.google.code.gson:gson:2.11.0")
    implementation ("com.vk:android-sdk-core:4.1.0")
    implementation ("com.vk:android-sdk-api:4.1.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("androidx.compose.foundation:foundation:1.8.3")
    implementation(libs.androidx.material3.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}