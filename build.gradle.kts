plugins {
    id("com.android.application") version "8.13.1" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    // Arreglo: Declarar el plugin de Compose aquí, con la misma versión que Kotlin
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}

// Clean task
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
