import com.zredna.bitfolio.Config

plugins {
    id("kotlin")
}

dependencies {
    implementation(Config.Libs.kotlinStdlib)

    // Retrofit
    implementation(Config.Libs.retrofit)
    implementation(Config.Libs.retrofitGson)
    implementation(Config.Libs.retrofitCoroutinesAdapter)
    implementation(Config.Libs.okhttpLogging)
}

repositories {
    mavenCentral()
}