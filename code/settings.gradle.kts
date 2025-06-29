pluginManagement {
  repositories {
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/") {
      mavenContent { snapshotsOnly() }
    }
    maven("https://m2.dv8tion.net/releases") {
      name = "dv8tion"
      mavenContent { releasesOnly() }
    }
    maven("https://repo.papermc.io/repository/maven-public/") {
      name = "papermc"
    }
  }
}

rootProject.name = "cloud-docs-snippets"

