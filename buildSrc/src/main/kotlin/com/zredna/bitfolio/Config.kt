package com.zredna.bitfolio

private const val kotlinVersion = "1.3.0"
private const val androidGradleVersion = "3.2.1"

// Compile dependencies
private const val supportVersion = "28.0.0"
private const val recyclerViewVersion = "1.0.0"
private const val constraintLayoutVersion = "1.1.3"

private const val retrofitVersion = "2.4.0"
private const val retrofitCoroutinesAdapterVersion = "0.9.2"
private const val okhttpVersion = "3.10.0"

private const val coroutinesVersion = "1.0.0"
private const val lifecycleVersion = "2.0.0"
private const val koinVersion = "1.0.1"
private const val roomVersion = "2.0.0"
private const val rxjavaVersion = "2.1.14"
private const val rxandroidVersion = "2.0.2"

// Tests
private const val junitVersion = "4.12"
private const val mockitoVersion = "2.23.0"
private const val androidxTestVersion = "1.1.0"
private const val espressoVersion = "3.1.0"

object Config {

    object BuildPlugins {
        val androidGradle = "com.android.tools.build:gradle:$androidGradleVersion"
        val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    }

    object Android {
        val buildToolsVersion = "28.0.3"
        val minSdkVersion = 19
        val targetSdkVersion = 28
        val compileSdkVersion = 28
        val applicationId = "com.zredna.bitfolio"
        val versionCode = 1
        val versionName = "0.1"
    }

    object Libs {
        val appcompat = "com.android.support:appcompat-v7:$supportVersion"
        val recyclerview = "androidx.recyclerview:recyclerview:$recyclerViewVersion"
        val design = "com.android.support:design:$supportVersion"
        val constraintLayout = "com.android.support.constraint:constraint-layout:1.1.3"

        val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
        val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
        val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
        val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
        val koin = "org.koin:koin-androidx-viewmodel:$koinVersion"
        val room = "androidx.room:room-runtime:$roomVersion"
        val roomCompiler = "androidx.room:room-compiler:$roomVersion"
        val rxjava = "io.reactivex.rxjava2:rxjava:$rxjavaVersion"
        val rxandroid = "io.reactivex.rxjava2:rxandroid:$rxandroidVersion"

        val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"
        val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        val retrofitGson = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
        val retrofitRxjava = "com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion"
        val retrofitCoroutinesAdapter = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:$retrofitCoroutinesAdapterVersion"
    }

    object TestLibs {
        val archCoreTesting = "androidx.arch.core:core-testing:$lifecycleVersion"
        val junit = "junit:junit:$junitVersion"
        val mockitoCore = "org.mockito:mockito-core:$mockitoVersion"
        val mockitoAndroid = "org.mockito:mockito-android:$mockitoVersion"
        val dexmaker = "com.google.dexmaker:dexmaker:1.2"
        val dexmaker_mockito = "com.google.dexmaker:dexmaker-mockito:1.2"
        val annotations = "com.android.support:support-annotations:$supportVersion"
        val espresso = "androidx.test.espresso:espresso-core:$espressoVersion"
        val testRunner = "androidx.test:runner:$androidxTestVersion"
        val testRules = "androidx.test:rules:$androidxTestVersion"
        val testJunit = "androidx.test.ext:junit:1.0.0"
    }
}