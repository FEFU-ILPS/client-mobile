pluginManagement {
    repositories {
        google()
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

rootProject.name = "MobileClient"
include(":app")
include(":core:ui")
include(":core:data:network")
include(":feature:text_list")
include(":feature:text")
include(":feature:feedback")
include(":core:data:data_store")
include(":feature:auth:sign_in")
include(":feature:auth:sign_up")
