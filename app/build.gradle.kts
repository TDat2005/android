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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
// CÁC DÒNG CODE THÊM VÀO: Thư viện cho gọi API và tải ảnh
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")



    implementation("com.android.volley:volley:1.2.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.android.material:material:1.12.0")
    // Firebase BOM (Bill of Materials) - Quản lý phiên bản các thư viện Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    // Thư viện Firebase Authentication
    implementation("com.google.firebase:firebase-auth")
    implementation ("me.relex:circleindicator:2.1.6")
    implementation ("com.google.code.gson:gson:2.10.1")
}
