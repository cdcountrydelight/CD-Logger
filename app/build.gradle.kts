plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.countrydelight.countrydelightlogger"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.countrydelight.countrydelightlogger"
        minSdk = 23
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
    //implementation
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    //cdLogger
    implementation(project(":cdlogger"))
    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    //splash screen
    implementation(libs.androidx.core.splashscreen)


    //leaky canary
    debugImplementation(libs.leakcanary.android)


    //test implementation
    testImplementation(libs.junit)

    //android test implementation
    androidTestImplementation(libs.androidx.junit.v120)
    androidTestImplementation(libs.androidx.espresso.core.v360)

}