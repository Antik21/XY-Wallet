plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlinxSerialization)
    application
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.call.id)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.compression)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.server.logback)
    implementation(projects.dto)
    implementation(projects.api)

    testImplementation(kotlin("test"))
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.ktor.serialization.kotlinx.json)
}

application {
    mainClass.set("com.antik.wallet.server.ServerKt")
}

kotlin {
    jvmToolchain(21)
}

tasks.register<JavaExec>("runProd") {
    group = "application"
    description = "Run server with production-like environment variables"
    mainClass.set(application.mainClass)
    classpath = sourceSets["main"].runtimeClasspath
    val prodHost = project.findProperty("prodHost") as String?
    val prodPort = project.findProperty("prodPort") as String?
    val prodPublicUrl = project.findProperty("prodPublicUrl") as String?

    require(!prodHost.isNullOrBlank()) {
        "Missing required property: prodHost (use -PprodHost=...)"
    }
    require(!prodPort.isNullOrBlank()) {
        "Missing required property: prodPort (use -PprodPort=...)"
    }
    require(!prodPublicUrl.isNullOrBlank()) {
        "Missing required property: prodPublicUrl (use -PprodPublicUrl=...)"
    }

    environment("SERVER_HOST", prodHost)
    environment("SERVER_PORT", prodPort)
    environment("SERVER_PUBLIC_URL", prodPublicUrl)
}
