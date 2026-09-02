val junitVersion: String by project
val mockitoVersion: String by project
val assertjVersion: String by project
val slf4jVersion: String by project

val springBootVersion = "4.1.0"

dependencies {
    // POP protocol (transitively exposes core/crypto/api/transparency types)
    api(project(":ans-sdk-pop"))

    // Spring Boot BOM aligns spring-web / servlet-api versions
    compileOnly(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

    // Servlet filter surface + Spring OncePerRequestFilter base (provided by the consuming web app)
    compileOnly("org.springframework:spring-web")
    compileOnly("org.springframework:spring-context")
    compileOnly("jakarta.servlet:jakarta.servlet-api")

    // Logging
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    // Testing
    testImplementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    // Servlet + Spring mock-web helpers under test (compileOnly in main)
    testImplementation("org.springframework:spring-web")
    testImplementation("org.springframework:spring-context")
    testImplementation("org.springframework:spring-test")
    testImplementation("jakarta.servlet:jakarta.servlet-api")
    testRuntimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
}
