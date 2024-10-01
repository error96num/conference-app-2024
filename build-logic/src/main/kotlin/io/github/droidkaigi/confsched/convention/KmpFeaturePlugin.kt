package io.github.droidkaigi.confsched.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KmpFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("droidkaigi.primitive.kmp")
                apply("droidkaigi.primitive.kmp.android")
                apply("droidkaigi.primitive.kmp.ios")
                apply("droidkaigi.primitive.kmp.compose")
                apply("droidkaigi.primitive.kmp.android.hilt")
                apply("droidkaigi.primitive.kmp.roborazzi")
                apply("droidkaigi.primitive.detekt")
            }

            dependencies {
                add("implementation", project(":core:model"))
                add("implementation", project(":core:droidkaigiui"))
                add("implementation", project(":core:designsystem"))
            }
        }
    }
}
