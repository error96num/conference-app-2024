import com.github.takahirom.roborazzi.ExperimentalRoborazziApi

plugins {
    id("droidkaigi.convention.kmpfeature")
}

android.namespace = "io.github.droidkaigi.confsched.feature.favorites"
@OptIn(ExperimentalRoborazziApi::class)
roborazzi.generateComposePreviewRobolectricTests.packages = listOf("io.github.droidkaigi.confsched.favorites")
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinxCoroutinesCore)
                implementation(libs.moleculeRuntime)
            }
        }
        androidUnitTest {
            dependencies {
                implementation(projects.core.testing)
            }
        }
    }
}
