import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(27)
    buildToolsVersion = "27.0.3"

    defaultConfig {
        applicationId = "com.zredna.bitfolio"
        minSdkVersion(19)
        targetSdkVersion(27)
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
}

dependencies {
    val supportVersion = "27.1.1"

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.android.support:appcompat-v7:$supportVersion")
    implementation("com.android.support:recyclerview-v7:$supportVersion")
    implementation("com.android.support:design:$supportVersion")
    implementation("com.android.support.constraint:constraint-layout:1.1.0")
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

    // Koin for Android Architecture Components
    implementation("org.koin:koin-android-architecture:0.9.2")

    // ViewModel and LiveData
    val lifecycleVersion = "1.1.1"
    implementation("android.arch.lifecycle:extensions:$lifecycleVersion")

    // Room
    val roomVersion = "1.1.0"
    implementation("android.arch.persistence.room:runtime:$roomVersion")
    kapt("android.arch.persistence.room:compiler:$roomVersion")

    // RxJava
    implementation("io.reactivex.rxjava2:rxjava:2.1.14")
    implementation("io.reactivex.rxjava2:rxandroid:2.0.2")

    // Testing
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.18.3")
    testImplementation("android.arch.core:core-testing:1.1.1")

    compile(project(":binanceapiclient"))
    compile(project(":bittrexapiclient"))

}

tasks.register("copyTestClasses", Copy::class.java) {
    from("build/tmp/kotlin-classes/debug")
    into("build/intermediates/classes/debug")
}
