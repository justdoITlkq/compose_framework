plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("maven-publish")

}

android {
    namespace = "com.cecbrain.core_framework"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures { compose = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    kotlin {
        jvmToolchain(17)
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}


publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.cecbrain"
            artifactId = "core-framework"
            version = libs.versions.compose.framework.get()

            // 关联 Android 变体
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

dependencies {

    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.material3)
    api(libs.androidx.lifecycle.runtime.compose)
    api(libs.androidx.lifecycle.viewmodel.compose)
    // 网络框架
    api(libs.retrofit.core)
    api(libs.retrofit.kotlin.serialization)
    api(libs.okhttp.core)
    api(libs.okhttp.logging)
    api(libs.kotlinx.serialization.json)

    //测试相关
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}