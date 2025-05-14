plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.sonarqube") version "4.4.1.3373"
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    testOptions {
        unitTests {
            all {
                it.useJUnitPlatform()  // JUnit 5 support
            }
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Jacoco report task – muss außerhalb des android-Blocks stehen!
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

    val debugTree = fileTree(layout.buildDirectory.dir("intermediates/javac/debug")) {
        exclude(fileFilter)
    }

    val kotlinDebugTree = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(fileFilter)
    }

    val mainSrc = listOf(
        "${project.projectDir}/src/main/java",
        "${project.projectDir}/src/main/kotlin"
    )

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree, kotlinDebugTree))
    executionData.setFrom(fileTree(layout.buildDirectory) {
        include("jacoco/testDebugUnitTest.exec")
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    })
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.hildan.krossbow:krossbow-websocket-okhttp:7.0.0")
    implementation("org.hildan.krossbow:krossbow-stomp-core:7.0.0")
    implementation("org.hildan.krossbow:krossbow-websocket-builtin:7.0.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation ("androidx.navigation:navigation-compose:2.7.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")


    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation(kotlin("test"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")


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
        property("sonar.analysis.mode", "publish")
        property("sonar.coverage.jacoco.xmlReportPaths", "${project.projectDir}app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
        property("sonar.inclusions", "src/main/java/**, src/main/kotlin/**")
        property("sonar.coverage.exclusions", "src/test/**, **/generated/**")
    }
}
