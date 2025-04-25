pluginManagement {
    repositories {
        google {  // Repositorio de Google para plugins como el de Firebase
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()  // Repositorio principal
        gradlePluginPortal()  // Repositorio de plugins Gradle
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()  // Repositorio para Firebase y otros servicios de Google
        mavenCentral()  // Repositorio principal para dependencias
    }
}

rootProject.name = "myapli"
include(":app")
