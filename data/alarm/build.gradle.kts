plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.data.alarm"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    testOptions {
        //unitTests.includeAndroidResources = true
    }
}

dependencies {

    implementation(project(":domain:alarm"))


    implementation(project(":domain:alarm"))

    // Gson
    implementation(libs.gson)

    // Room dependencies
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // SQLite (if needed for bundling)
    implementation(libs.sqlite.bundled)

    // Koin dependencies for DI
    implementation(libs.koin.core)

    // AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.monitor)
    implementation(libs.androidx.junit.ktx)

    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.androidx.room.testing)  // For Room database testing
    testImplementation(libs.kotlinx.coroutines.test)  // For testing coroutines
    testImplementation(libs.androidx.core.testing)  // For mocking Android components
    testImplementation(libs.mockk)  // For mocking dependencies in tests

    // Android Test dependencies (for instrumentation tests)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//    implementation(libs.gson)
//
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    implementation(libs.androidx.monitor)
//    implementation(libs.androidx.junit.ktx)
////    testImplementation(libs.junit)
//    testImplementation(libs.androidx.runner)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
////    testImplementation(libs.androidx.room.testing)
////    testImplementation(libs.kotlinx.coroutines.test)
// //   testImplementation(libs.androidx.core.testing)
// //   testImplementation(libs.mockk)
////
////    implementation(libs.bundles.retrofit)
////
////    implementation(libs.androidx.room.runtime)
////    implementation(libs.androidx.room.ktx)
////    ksp(libs.androidx.room.compiler)
////
////
////    implementation(libs.koin.compose.viewmodel)
////    implementation(libs.koin.compose)
////    implementation(libs.koin.androidx.compose)
////    implementation(libs.koin.android)
////    implementation(libs.koin.core)
//
//    // Зависимость на общий модуль domain
//        //implementation(project(":domain:alarm"))
//
//    // Room
//    implementation(libs.androidx.room.runtime)
//    implementation(libs.androidx.room.ktx)
//    testImplementation(libs.junit.junit)
//    ksp(libs.androidx.room.compiler)
//
//    // SQLite
//    implementation(libs.sqlite.bundled)
//
//    // Koin для DI
//    implementation(libs.koin.core)
//
//    // Общие зависимости
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//
//    // Тестовые зависимости
//    testImplementation(libs.junit)
//    testImplementation(libs.androidx.room.testing)
//    testImplementation(libs.kotlinx.coroutines.test)
//    testImplementation(libs.androidx.core.testing)
//    testImplementation(libs.mockk)

}