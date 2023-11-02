plugins {
    alias(libs.plugins.multiplatform).apply(false)
    alias(libs.plugins.compose).apply(false)
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.libres).apply(false)
    alias(libs.plugins.buildConfig).apply(false)
}

buildscript {
    val kotlin_version by extra("1.9.20")
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath ("dev.icerock.moko:resources-generator:0.23.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}
