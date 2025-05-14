import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.plugin)
    alias(libs.plugins.ksp)
}

val secretsDev = Properties().apply {
    load(rootProject.file("secrets.dev.properties").inputStream())
}
val secretsProd = Properties().apply {
    load(rootProject.file("secrets.prod.properties").inputStream())
}

android {

    sourceSets {
        getByName("main") {
            assets.srcDirs("src/main/assets")
        }
    }

    namespace = "android.test.forexwatch"
    compileSdk = 35

    defaultConfig {
        applicationId = "android.test.forexwatch"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        debug {
            buildConfigField("String", "API_KEY", "\"${secretsDev["API_KEY"]}\"")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "API_KEY", "\"${secretsProd["API_KEY"]}\"")
        }

        create("mock") {
            initWith(getByName("debug"))
            buildConfigField("String", "API_KEY", "\"mock-key-not-used\"")
            matchingFallbacks += listOf("debug")
        }
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    hilt {
        enableAggregatingTask = false
    }



}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.dagger.hilt.android)
    debugImplementation(libs.ui.tooling)
    ksp(libs.dagger.hilt.compiler.ksp)
    implementation(libs.retrofit)
    implementation(libs.gson.converter)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler.ksp)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.compose.material)
    implementation(libs.landscapist.glide)
    implementation(libs.mpandroidchart)
    implementation(libs.accompanist.systemuicontroller)
    testImplementation(libs.room.testing)

}
