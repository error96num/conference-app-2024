plugins {
    id("droidkaigi.convention.kmpfeature")
}

android.namespace = "io.github.droidkaigi.confsched.feature.profilecard"
roborazzi.generateComposePreviewRobolectricTests.packages =
    listOf("io.github.droidkaigi.confsched.profilecard")
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.composeNavigation)
                implementation(compose.materialIconsExtended)
                implementation(libs.peekabooImagePicker)
                implementation(libs.qrcodeKotlin)
            }
        }
        androidTarget {
            dependencies {
                implementation(projects.core.model)
                implementation(libs.composeMaterialWindowSize)
            }
        }
        androidUnitTest {
            dependencies {
                implementation(projects.core.testing)
            }
        }
    }
}
