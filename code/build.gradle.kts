plugins {
  alias(libs.plugins.indra)
}

dependencies {
  implementation(libs.cloud.core)
  implementation(libs.cloud.minecraft.extras)
}

indra {
  javaVersions {
    minimumToolchain(8)
    target(8)
  }
}

tasks {
  withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-Xlint:-processing,-classfile,-serial", "-Werror"))
  }
}
