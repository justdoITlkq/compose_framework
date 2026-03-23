pluginManagement {
    repositories {
        google ()
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

rootProject.name = "compose_framework"
include(":app")
include(":core-framework")
include(":core-framework:datastore")
include(":core-framework:database")
include(":core-framework:network")
include(":core-framework:common")
include(":core-framework:ui")
include(":core-framework:model")
include(":core-framework:analytics")
include(":core-framework:media")
include(":core-framework:utils")
