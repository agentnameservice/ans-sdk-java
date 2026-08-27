val nimbusJoseVersion: String by project
val slf4jVersion: String by project
val cborVersion: String by project
val junitVersion: String by project
val mockitoVersion: String by project
val assertjVersion: String by project

dependencies {
    // Core, crypto, generated models
    api(project(":ans-sdk-core"))
    api(project(":ans-sdk-crypto"))
    api(project(":ans-sdk-api"))

    // Transparency for StatusToken/ScittReceipt/RootKeyManager/DefaultScittVerifier reuse
    api(project(":ans-sdk-transparency"))

    // Agent-client for verification/trust surface reuse
    api(project(":ans-sdk-agent-client"))

    // Nimbus JOSE + JWT for ES256 DPoP proof sign/verify
    implementation("com.nimbusds:nimbus-jose-jwt:$nimbusJoseVersion")

    // Logging
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("com.upokecenter:cbor:$cborVersion")
    testRuntimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
}
