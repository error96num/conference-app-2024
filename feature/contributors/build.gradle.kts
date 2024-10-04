import com.github.takahirom.roborazzi.ExperimentalRoborazziApi

plugins {
    id("droidkaigi.convention.kmpfeature")
}

android.namespace = "io.github.droidkaigi.confsched.feature.contributors"
@OptIn(ExperimentalRoborazziApi::class)
roborazzi.generateComposePreviewRobolectricTests.packages = listOf("io.github.droidkaigi.confsched.contributors")
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.droidkaigiui)
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
