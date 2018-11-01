import com.zredna.bitfolio.Config

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(Config.Android.compileSdkVersion)
    buildToolsVersion = Config.Android.buildToolsVersion

    defaultConfig {
        applicationId = Config.Android.applicationId
        minSdkVersion(Config.Android.minSdkVersion)
        targetSdkVersion(Config.Android.targetSdkVersion)
        versionCode = Config.Android.versionCode
        versionName = Config.Android.versionName
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    // This should be default, so not sure why I have to do this
    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }
}

dependencies {
    implementation(Config.Libs.kotlinStdlib)

    // Support
    implementation(Config.Libs.appcompat)
    implementation(Config.Libs.recyclerview)
    implementation(Config.Libs.design)
    implementation(Config.Libs.constraintLayout)

    // Koin
    implementation(Config.Libs.koin)

    // ViewModel and LiveData
    implementation(Config.Libs.lifecycleExtensions)

    // Room
    implementation(Config.Libs.room)
    kapt(Config.Libs.roomCompiler)

    // Coroutines
    implementation(Config.Libs.coroutinesAndroid)

    // Testing
    testImplementation(Config.TestLibs.junit)
    testImplementation(Config.TestLibs.mockito)
    testImplementation(Config.TestLibs.archCoreTesting)

    implementation(project(":bittrexapiclient"))
    implementation(project(":binanceapiclient"))
}

tasks.register("copyTestClasses", Copy::class) {
    from("build/tmp/kotlin-classes/debug")
    into("build/intermediates/classes/debug")
}
