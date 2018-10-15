import com.zredna.bitfolio.Config

plugins {
    id("kotlin")
}

dependencies {
    implementation(Config.Libs.kotlinStdlib)

    // RxJava
    implementation(Config.Libs.rxjava)

    // Retrofit
    implementation(Config.Libs.retrofit)
    implementation(Config.Libs.retrofitGson)
    implementation(Config.Libs.retrofitRxjava)
    implementation(Config.Libs.okhttpLogging)
}

repositories {
    mavenCentral()
}