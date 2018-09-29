import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(28)
    buildToolsVersion = "28.0.2"

    defaultConfig {
        applicationId = "com.zredna.bitfolio"
        minSdkVersion(19)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
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
    val supportVersion = "28.0.0"

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.android.support:appcompat-v7:$supportVersion")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("com.android.support:design:$supportVersion")
    implementation("com.android.support.constraint:constraint-layout:1.1.3")
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

    // Koin
    val koinVersion = "1.0.1"
    implementation("org.koin:koin-androidx-viewmodel:$koinVersion")

    // ViewModel and LiveData
    val lifecycleVersion = "2.0.0-beta01"
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")

    // Room
    val roomVersion = "2.0.0-beta01"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // RxJava
    implementation("io.reactivex.rxjava2:rxjava:2.1.14")
    implementation("io.reactivex.rxjava2:rxandroid:2.0.2")

    // Testing
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.19.0")
    testImplementation("androidx.arch.core:core-testing:$lifecycleVersion")

    compile(project(":binanceapiclient"))
    compile(project(":bittrexapiclient"))
}

tasks.register("copyTestClasses", Copy::class) {
    from("build/tmp/kotlin-classes/debug")
    into("build/intermediates/classes/debug")
}
