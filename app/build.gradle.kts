val retrofitVersion = "2.9.0"
val krossbowVersion = "7.0.0"
val kotlinxVersion = "1.7.3"
val okhttpVersion = "4.12.0"
val androidxCoreVersion = "1.9.0"
val androidxLifecycleVersion = "2.6.1"
val androidxActivityVersion = "1.7.0"
val androidxAppcompatVersion = "1.6.1"
val androidxRecyclerviewVersion = "1.3.1"
val androidxNavigationVersion = "2.7.7"
val kotlinxSerializationVersion = "1.6.3"
val junitVersion = "5.8.1"
val mockitoVersion = "5.2.0"

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.sonarqube") version "5.1.0.4882"
    id("jacoco")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
}

android {
    namespace = "com.example.boomboomfrontend"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.boomboomfrontend"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    lint {
        xmlReport = true
        htmlReport = false
    }


    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    testOptions {
        unitTests {
            all {
                it.useJUnitPlatform()
                it.finalizedBy(tasks.named("jacocoTestReport"))
            }
        }
    }

    tasks.register<JacocoReport>("jacocoTestReport") {
        group = "verification"
        description = "Generates code coverage report for the test task."
        dependsOn("testDebugUnitTest")

        reports {
            xml.required.set(true)
            xml.outputLocation.set(file("${project.projectDir}/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"))
        }

        val fileFilter = listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "android/**/*.*",
            "**/gameUI/**/*.*"
        )

        val debugTree =
            fileTree("${project.layout.buildDirectory.get().asFile}/tmp/kotlin-classes/debug") {
                exclude(fileFilter)
            }

        val javaDebugTree =
            fileTree("${project.layout.buildDirectory.get().asFile}/intermediates/javac/debug") {
                exclude(fileFilter)
            }

        val mainSrc = listOf(
            "${project.projectDir}/src/main/java",
            "${project.projectDir}/src/main/kotlin"
        )

        sourceDirectories.setFrom(files(mainSrc))
        classDirectories.setFrom(files(debugTree, javaDebugTree))
        executionData.setFrom(fileTree(project.layout.buildDirectory.get().asFile) {
            include("jacoco/testDebugUnitTest.exec")
            include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
        })
    }
    dependencies {
        implementation("com.squareup.retrofit2:retrofit:${retrofitVersion}")
        implementation("com.squareup.retrofit2:converter-gson:${retrofitVersion}")
        implementation("org.hildan.krossbow:krossbow-websocket-okhttp:${krossbowVersion}")
        implementation("org.hildan.krossbow:krossbow-stomp-core:${krossbowVersion}")
        implementation("org.hildan.krossbow:krossbow-websocket-builtin:${krossbowVersion}")

        implementation("com.squareup.okhttp3:okhttp:${okhttpVersion}")

        implementation("androidx.core:core-ktx:${androidxCoreVersion}")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:${androidxLifecycleVersion}")
        implementation("androidx.activity:activity-compose:${androidxActivityVersion}")
        implementation(platform("androidx.compose:compose-bom:2023.03.00"))
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-graphics")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.compose.material3:material3")
        implementation("androidx.appcompat:appcompat:${androidxAppcompatVersion}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinxVersion}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${kotlinxVersion}")
        implementation("androidx.recyclerview:recyclerview:${androidxRecyclerviewVersion}")
        implementation("androidx.navigation:navigation-compose:${androidxNavigationVersion}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${kotlinxSerializationVersion}")


        testImplementation("org.junit.jupiter:junit-jupiter:${junitVersion}")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${kotlinxVersion}")
        testImplementation("org.mockito:mockito-core:${mockitoVersion}")
        testImplementation(kotlin("test"))
        testImplementation("org.mockito.kotlin:mockito-kotlin:${mockitoVersion}")


        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
        androidTestImplementation("androidx.compose.ui:ui-test-junit4")

        debugImplementation("androidx.compose.ui:ui-tooling")
        debugImplementation("androidx.compose.ui:ui-test-manifest")
    }

    sonar {
        properties {
            property("sonar.projectKey", "SE2-BOOM-BOOM-KITTENS_BOOOM-BOOM-FRONTEND")
            property("sonar.organization", "se2-boom-boom-kittens")
            property("sonar.host.url", "https://sonarcloud.io")
            property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
            property("sonar.androidLint.reportPaths", "build/reports/lint-results-debug.xml")
        }
    }
}
