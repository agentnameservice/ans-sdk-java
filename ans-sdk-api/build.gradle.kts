import java.net.URI

plugins {
    id("org.openapi.generator")
}

val jacksonVersion: String by project

// Authoritative source for the API spec
val apiSpecFile = layout.projectDirectory.file("spec/api-spec.yaml")

dependencies {
    // Jackson for JSON serialization
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    // Jakarta annotations
    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
}

openApiGenerate {
    generatorName.set("java")
    inputSpec.set(apiSpecFile)
    skipValidateSpec.set(true)
    outputDir.set(layout.buildDirectory.dir("generated").get().asFile.absolutePath)
    apiPackage.set("com.godaddy.ans.sdk.api.generated")
    modelPackage.set("com.godaddy.ans.sdk.model.generated")
    invokerPackage.set("com.godaddy.ans.sdk.client.generated")
    configOptions.set(mapOf(
        "library" to "native",
        "dateLibrary" to "java8",
        "useJakartaEe" to "true",
        "openApiNullable" to "false",
        "serializationLibrary" to "jackson",
        "hideGenerationTimestamp" to "true"
    ))
}

sourceSets {
    main {
        java {
            srcDir(layout.buildDirectory.dir("generated/src/main/java"))
        }
    }
}

tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}

// Disable checkstyle for generated code
tasks.named("checkstyleMain") {
    enabled = false
}

// Ensure all jar tasks (including sources/javadoc jars created by the publish plugin) wait for code generation.
tasks.withType<Jar>().configureEach {
    dependsOn(tasks.openApiGenerate)
}