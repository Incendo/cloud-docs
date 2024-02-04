pluginManagement {
  repositories {
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/") {
      name = "sonatypeOssSnapshots"
      mavenContent {
        snapshotsOnly()
      }
    }
    maven("https://m2.dv8tion.net/releases") {
      name = "dv8tion"
      mavenContent { releasesOnly() }
    }
  }
}

rootProject.name = "cloud-docs-snippets"

