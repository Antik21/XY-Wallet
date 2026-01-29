rootProject.name = "XY-Wallet"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":shared")
include(":composeApp")
include(":uikit")
include(":dto")
include(":api")
include(":server")

project(":shared").projectDir = file("apps/shared")
project(":composeApp").projectDir = file("apps/composeApp")
project(":uikit").projectDir = file("apps/uikit")
project(":dto").projectDir = file("api-contracts/dto")
project(":api").projectDir = file("api-contracts/api")
