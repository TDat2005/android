plugins {
    alias(libs.plugins.android.application)
    //them
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.myapplication10"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myapplication10"
        minSdk = 29
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    // DÒNG CODE THÊM VÀO: Bật tính năng ViewBinding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX cơ bản
    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.material) // GIỮ cái này, đừng thêm material 1.12.0 thủ công nữa

    // Firebase (BOM giúp đồng bộ version tất cả lib Firebase)
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")

    // Tiện ích app đang dùng
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("me.relex:circleindicator:2.1.6")
    implementation("com.google.code.gson:gson:2.10.1")

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

