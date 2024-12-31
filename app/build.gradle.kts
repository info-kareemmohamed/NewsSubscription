import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.daggerHiltAndroid)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.example.newssubscription"
    compileSdk = 35

    //To Load Firebase Server Client ID from local.properties file
    val properties = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }


    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }

    defaultConfig {
        applicationId = "com.example.newssubscription"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        forEach {
            it.buildConfigField(
                "String",
                "FIREBASE_SERVER_CLIENT_ID",
                properties.getProperty("FIREBASE_SERVER_CLIENT_ID")
            )
            it.buildConfigField(
                "String",
                "API_KEY",
                properties.getProperty("API_KEY")
            )
            it.buildConfigField(
                "String",
                "BASE_URL",
                properties.getProperty("BASE_URL")
            )
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
        compose = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.kotlinx.serialization.json)

    debugImplementation(libs.bundles.debug)
    testImplementation(libs.bundles.testing)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.androidTest)

    implementation(libs.bundles.hilt)
    ksp(libs.hilt.android.compiler)

    implementation(libs.bundles.retrofit)

    implementation(libs.bundles.paging)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.lottie.compose)

    implementation(libs.coil.compose)

    implementation(libs.paymob.sdk)

    implementation(libs.bundles.firebase)
    implementation(libs.bundles.google.auth)
    implementation(platform(libs.firebase.bom))

    implementation(libs.androidx.datastore.preferences)

}