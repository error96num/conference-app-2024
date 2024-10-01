import com.github.takahirom.roborazzi.ExperimentalRoborazziApi

plugins {
    id("droidkaigi.convention.kmpfeature")
}

android.namespace = "io.github.droidkaigi.confsched.feature.main"
@OptIn(ExperimentalRoborazziApi::class)
roborazzi.generateComposePreviewRobolectricTests.packages = listOf("io.github.droidkaigi.confsched.main")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.materialIconsExtended)
                implementation(libs.haze)
            }
        }
    }
}
