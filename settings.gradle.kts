pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Biometric Hack"
include(":app")
include(":feature-biometric")
include(":feature-biometric:presentation")
include(":feature-biometric:data")
include(":feature-biometric:domain")
include(":feature-biometric:api")
include(":common")
include(":core")
