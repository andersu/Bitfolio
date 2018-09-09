import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("kotlin")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

    // RxJava
    implementation("io.reactivex.rxjava2:rxjava:2.1.14")

    // Retrofit
    val retrofitVersion = "2.4.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:3.10.0")
}

repositories {
    mavenCentral()
}