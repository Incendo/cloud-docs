plugins {
  alias(libs.plugins.indra)
}

dependencies {
  implementation(libs.cloud.core)

  // Minecraft
  implementation(libs.cloud.minecraft.extras)

  // Processors
  implementation(libs.cloud.processors.cooldown)
}

indra {
  javaVersions {
    minimumToolchain(17)
    target(17)
  }
}

tasks {
  withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-Xlint:-processing,-classfile,-serial", "-Werror"))
  }
}
