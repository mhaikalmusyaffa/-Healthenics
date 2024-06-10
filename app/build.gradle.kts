import com.android.build.api.variant.BuildConfigField

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.haikal.healthenics"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.haikal.healthenics"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "CLIENT_ID", "\"30b2c6211d0b49efa65151ffdc9c6a90\"")
        buildConfigField("String", "CLIENT_SECRET", "\"9371ed8db0464638882382e16a477248\"")
        buildConfigField("String", "API_URL", "\"https://platform.fatsecret.com/rest/\"")
        buildConfigField(
            "String",
            "TOKEN",
            "\"Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ4NDUzNUJFOUI2REY5QzM3M0VDNUNBRTRGMEJFNUE2QTk3REQ3QkMiLCJ0eXAiOiJhdCtqd3QiLCJ4NXQiOiJTRVUxdnB0dC1jTno3Rnl1VHd2bHBxbDkxN3cifQ.eyJuYmYiOjE3MTYxMDc3MjcsImV4cCI6MTcxNjE5NDEyNywiaXNzIjoiaHR0cHM6Ly9vYXV0aC5mYXRzZWNyZXQuY29tIiwiYXVkIjoiYmFzaWMiLCJjbGllbnRfaWQiOiIzMGIyYzYyMTFkMGI0OWVmYTY1MTUxZmZkYzljNmE5MCIsInNjb3BlIjpbImJhc2ljIl19.VUOJ44sYmyDmUfZydnnuYjYEQjBBTba1zLS81kmSVHJ0C-TFQa5zqlAu_83ylFtjyRDjc7PHPQbyj_hrAVAWu-nx-o3zDI5tu9ZxtXX1bXFCAwq_EXIItBifprgoZI7MGjX0i1tiIb6Dqq4PZsCw632M3skE6d4WJCkTyNXUuuYiVOzARN7DqYhFxeBtOb7cvemzbWB3cvn0wUBnSwpgEDCB33juzitnMAC4WXy8gg7wJGeHtC66aYIIrDOr6zz1fK24HRKz_BwdkF2ENzWJRRCFUqvF-gEoZuNhPGbgMCYzn-VEhS9tBeQdQJcWWciayn4P-3DIRjzt4-IU7g53bQ\""
        )
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
        mlModelBinding = true
    }
}

dependencies {


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.room.common)
    implementation(libs.firebase.crashlytics.buildtools)

    val lifecycle_version = "2.2.0"
    val roomVersion = "2.6.1"

    //ROOM
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:$roomVersion")

    //LIFECYCLE
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version")

    //API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    //UTILS
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.loopj.android:android-async-http:1.4.11")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    kapt("com.android.databinding:compiler:3.1.4")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("io.insert-koin:koin-core:3.1.0")
    implementation("io.insert-koin:koin-android:3.1.0")

    implementation("io.coil-kt:coil:2.5.0")



    // tf lite
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-task-vision:0.4.4")

    // crop
    implementation("com.github.yalantis:ucrop:2.2.8")
}