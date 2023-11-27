import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.libres)
    alias(libs.plugins.buildConfig)
    kotlin("plugin.serialization") version "1.9.20"
    id("dev.icerock.mobile.multiplatform-resources")
    id("app.cash.sqldelight")
}


@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    applyDefaultHierarchyTemplate()
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

    js(IR) {
        browser {
            useCommonJs()
            binaries.executable()
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
            commonWebpackConfig {
                scssSupport {
                    enabled.set(true)
                }
                outputFileName = "app.js"
            }
        }
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
                implementation(libs.composeIcons.featherIcons)
                //coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                //api call
                implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
                implementation("io.ktor:ktor-client-core:2.3.5")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
                implementation("io.ktor:ktor-client-logging:2.3.5")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                //more icons
                implementation(compose.materialIconsExtended)
                //local database
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.0")
                implementation("app.cash.sqldelight:runtime:2.0.0")
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
                //local database
                implementation("app.cash.sqldelight:android-driver:2.0.0")
            }
        }

        val desktopMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.client.okhttp)

                //Speech client
                implementation(libs.google.cloud.library)
                //GoogleCredentials
                implementation("com.google.auth:google-auth-library-oauth2-http:1.7.0")
                //Logs for speech request
                implementation("ch.qos.logback:logback-classic:1.2.6")
                //Playing audio
                implementation("javazoom:jlayer:1.0.1")
                //local database
                implementation("app.cash.sqldelight:sqlite-driver:2.0.0")

            }
        }

        val jsMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(compose.runtime)
                implementation(compose.html.core)
                //local database
                implementation("app.cash.sqldelight:web-worker-driver:2.0.0")
                implementation(npm("copy-webpack-plugin", "11.0.0"))
                implementation(npm("@sqlite.org/sqlite-wasm", "3.43.2-build1"))
            }
        }

//        val iosMain by getting {
//            dependencies {
//                implementation(libs.ktor.client.darwin)
//                implementation("app.cash.sqldelight:native-driver:2.0.0")
//            }
//        }
        targets.all {
            compilations.all {
                compilerOptions.configure {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
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
        mainClass = "org.ailingo.app.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AiLingo"
            packageVersion = "1.0.0"

            modules("java.sql")

            windows {
                menuGroup = "Compose Examples"
                // see https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                upgradeUuid = "BF9CDA6A-1391-46D5-9ED5-383D6E68CCEB"
            }
        }
    }
}

compose.experimental {
    web.application {}
}

libres {
    // https://github.com/Skeptick/libres#setup
}

tasks {
    withType<org.gradle.jvm.tasks.Jar> {
        exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
    }
}
tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions {
        freeCompilerArgs += "-Xexpect-actual-classes"
    }
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
    disableStaticFrameworkWarning = true
}

sqldelight {
    databases {
        create("HistoryDictionaryDatabase") {
            packageName.set("org.ailingo.app.database")
            generateAsync.set(true)
        }
    }
}

dependencies {
    implementation("androidx.core:core:1.10.1")

    //strings, images, fonts res
    commonMainApi("dev.icerock.moko:resources:0.22.0")
    commonMainApi("dev.icerock.moko:resources-compose:0.22.0")

    // only ViewModel, EventsDispatcher, Dispatchers.UI
    commonMainApi("dev.icerock.moko:mvvm-core:0.16.1")
    // api mvvm-core, getViewModel for Compose Multiplatform
    commonMainApi("dev.icerock.moko:mvvm-compose:0.16.1")
    // api mvvm-core, CFlow for native and binding extensions
    commonMainApi("dev.icerock.moko:mvvm-flow:0.16.1")
    // api mvvm-flow, binding extensions for Compose Multiplatform
    commonMainApi("dev.icerock.moko:mvvm-flow-compose:0.16.1")
}