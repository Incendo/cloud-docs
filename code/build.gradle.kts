plugins {
  alias(libs.plugins.indra)
}

dependencies {
  implementation(libs.cloud.core)

  // Minecraft
  implementation(libs.cloud.minecraft.extras)
  implementation(libs.cloud.minecraft.paper)
  implementation(libs.paper)

  // Processors
  implementation(libs.cloud.processors.cooldown)
}

indra {
  javaVersions {
    minimumToolchain(21)
    target(21)
  }
}

tasks {
  withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-Xlint:-processing,-classfile,-serial", "-Werror"))
  }
}
