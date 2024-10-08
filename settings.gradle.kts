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
        maven ( url = "https://jitpack.io")
        maven ( url ="https://s01.oss.sonatype.org/content/groups/public" )
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ( url = "https://jitpack.io")
        maven ( url ="https://s01.oss.sonatype.org/content/groups/public" )
    }
}

rootProject.name = "PlayAndroidKotlin"
include(":app")
include(":library_base")
