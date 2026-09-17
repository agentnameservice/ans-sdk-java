val junitVersion: String by project
val assertjVersion: String by project
val slf4jVersion: String by project

val springBootVersion = "4.1.1"

dependencies {
    // ANS SDK modules
    api(project(":ans-sdk-core"))
    api(project(":ans-sdk-registration"))
    api(project(":ans-sdk-discovery"))

    // Spring Boot auto-configuration
    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    implementation("org.springframework.boot:spring-boot-autoconfigure:$springBootVersion")

    // Logging
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    // Optional annotation processor for configuration metadata
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:$springBootVersion")

    // Testing
    testImplementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
}
