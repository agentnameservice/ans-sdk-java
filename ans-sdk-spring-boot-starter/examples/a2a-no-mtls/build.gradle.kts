plugins {
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    implementation(project(":ans-sdk-spring-boot-starter"))
    implementation(project(":ans-sdk-pop"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.register<JavaExec>("runClient") {
    group = "application"
    description = "Runs the DPoP client that attaches identity headers to an outbound request"
    mainClass.set("com.godaddy.ans.examples.a2anomtls.PopClientExample")
    classpath = sourceSets["main"].runtimeClasspath
}