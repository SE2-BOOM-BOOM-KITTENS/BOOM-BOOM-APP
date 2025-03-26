plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.sonarqube") version "5.1.0.4882"
    id("jacoco")
}

android {
    namespace = "com.example.boomboomfrontend"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.boomboomfrontend"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests {
            all {
                it.useJUnitPlatform()  // Ensuring JUnit 5 compatibility
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
            "android/**/*.*"
        )

        val debugTree = fileTree(mapOf("dir" to "${buildDir}/intermediates/javac/debug", "excludes" to fileFilter))

        val kotlinDebugTree = fileTree(mapOf("dir" to "${buildDir}/tmp/kotlin-classes/debug", "excludes" to fileFilter))

        val mainSrc = listOf(
            "${project.projectDir}/src/main/java",
            "${project.projectDir}/src/main/kotlin"
        )

        sourceDirectories.setFrom(files(mainSrc))
        classDirectories.setFrom(files(debugTree, kotlinDebugTree))
        executionData.setFrom(fileTree(buildDir) {
            include("jacoco/testDebugUnitTest.exec")
            include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
        })
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
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
    }
}
