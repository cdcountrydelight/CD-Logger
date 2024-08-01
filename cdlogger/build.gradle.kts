plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("maven-publish")
}

android {
    namespace = "com.countrydelight.cdlogger"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    // advertising id
    implementation(libs.play.services.ads.identifier)
    //gson
    implementation(libs.gson)
    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.play.services.base)
    annotationProcessor(libs.androidx.room.compiler)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    //work manager
    implementation(libs.androidx.work.runtime.ktx)
    //ktor
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)


    //test
    testImplementation(libs.junit)


    //android test
    androidTestImplementation(libs.androidx.junit.v120)
    androidTestImplementation(libs.androidx.espresso.core.v360)

}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.github.cdcountrydelight"
                artifactId = "CD-Logger"
                version = "1.0.17"
                from(components["release"])
            }
        }
    }
}