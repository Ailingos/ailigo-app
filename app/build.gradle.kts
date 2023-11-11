import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.libres)
    alias(libs.plugins.buildConfig)
    kotlin("plugin.serialization") version "1.9.20"
    id("dev.icerock.mobile.multiplatform-resources")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

//    listOf(
//            iosX64(),
//            iosArm64(),
//            iosSimulatorArm64()
//        ).forEach {
//            it.binaries.framework {
//                baseName = "ailingo"
//                isStatic = true
//                export("dev.icerock.moko:resources:0.23.0")
//                export("dev.icerock.moko:graphics:0.9.0") // toUIColor here
//            }
//        }

    jvm("desktop")

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(libs.libres)
                implementation(libs.voyager.navigator)
                implementation(libs.composeImageLoader)
                implementation(libs.napier)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.moko.mvvm)
                implementation(libs.ktor.core)
                implementation(libs.composeIcons.featherIcons)
                implementation(libs.koin.core)
                
                //api call
                implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
                implementation("io.ktor:ktor-client-core:2.3.5")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                //stings, images, fonts res
                api("dev.icerock.moko:resources:0.23.0")
                api("dev.icerock.moko:resources-compose:0.23.0") // for compose multiplatform
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activityCompose)
                implementation(libs.compose.uitooling)
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.ktor.client.okhttp)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.client.okhttp)

                //Speech client
                implementation(libs.google.cloud.library)
                //GoogleCredentials
                implementation ("com.google.auth:google-auth-library-oauth2-http:1.7.0")
                //Logs for speech request
                implementation("ch.qos.logback:logback-classic:1.2.6")
                //Playing audio
                implementation("javazoom:jlayer:1.0.1")

            }
        }

        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
            }
        }

//        val iosMain by getting {
//            dependencies {
//                implementation(libs.ktor.client.darwin)
//            }
//        }

    }
}

android {
    namespace = "org.ailingo.app"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        applicationId = "org.ailingo.app.androidApp"
        versionCode = 1
        versionName = "1.0.0"
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.ailingo.app.desktopApp"
            packageVersion = "1.0.0"
        }
    }
}

compose.experimental {
    web.application {}
}

libres {
    // https://github.com/Skeptick/libres#setup
}
tasks.getByPath("desktopProcessResources").dependsOn("libresGenerateResources")
tasks.getByPath("desktopSourcesJar").dependsOn("libresGenerateResources")
tasks.getByPath("jsProcessResources").dependsOn("libresGenerateResources")

buildConfig {
    // BuildConfig configuration here.
    // https://github.com/gmazzo/gradle-buildconfig-plugin#usage-in-kts
}

multiplatformResources {
    multiplatformResourcesPackage = "org.ailingo.app" // required
    multiplatformResourcesClassName = "SharedRes" // optional, default MR
}