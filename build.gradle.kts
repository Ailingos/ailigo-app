buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://dl.bintray.com/icerockdev/plugins")
    }
    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.22.0")
        classpath("app.cash.sqldelight:gradle-plugin:2.0.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://dl.bintray.com/icerockdev/moko")
    }
}

plugins {
    alias(libs.plugins.multiplatform).apply(false)
    alias(libs.plugins.compose).apply(false)
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.libres).apply(false)
    alias(libs.plugins.buildConfig).apply(false)
}