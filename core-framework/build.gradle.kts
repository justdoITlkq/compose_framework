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
        // 建议统一到 Java 17 或 21（取决于你的 Android Studio 版本）
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        // 这里的版本必须和上面一致
        jvmTarget = "17"
    }

    // 如果你使用的是最新的 Kotlin 插件，建议也配置一下编译链
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
            // 设置你的库坐标
            groupId = "com.cecbrain.android"
            artifactId = "core-framework"
            version = "1.0.0"

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
}