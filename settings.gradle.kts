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

rootProject.name = "AlarmMess"
include(":app")
include(":core")
include(":core:common")
include(":core:ui")
include(":data")
include(":data:alarm")
include(":domain")
include(":domain:alarm")
include(":feature")
include(":feature:alarm")
include(":service")
include(":service:alarm")
include(":feature:alarm_list")
