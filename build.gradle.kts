import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.23"
    /* org.jetbrains.kotlin.kapt was added to generate configuration properties metadata */
    id("org.jetbrains.kotlin.kapt") version "1.9.23"
}

group = "io.github.artemptushkin.procedures.api"
version = "0.0.5"

extra["springCloudVersion"] = "2023.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
}

sourceSets {
    create("integrationTest") {
        java.srcDir("src/integrationTest/kotlin")
        resources.srcDir("src/integrationTest/resources")
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath + sourceSets["test"].runtimeClasspath
    }
}

configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

val integrationTestImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    /**  spring-cloud-starter-bootstrap is needed since 2.4.0, see for more https://github.com/spring-projects/spring-boot/issues/24216 */
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.postgresql:postgresql:42.3.1")
    implementation("mysql:mysql-connector-java:8.0.27")
    implementation("com.h2database:h2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    integrationTestImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-test")
    integrationTestImplementation("com.playtika.testcontainers:embedded-postgresql:3.1.5")
    integrationTestImplementation("com.playtika.testcontainers:embedded-mysql:3.1.5")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

task<Test>("postgreSQLTest") {
    description = "Runs the integration tests against PostgreSQL database using `postgresql` Spring profile"
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath

    useJUnitPlatform()

    systemProperty("spring.profiles.active", "postgresql, test")
    systemProperty("embedded.postgresql.enabled", "true")
}

task<Test>("mySQLTest") {
    description = "Runs the integration tests against MySQL database using `mysql` Spring profile"
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath

    useJUnitPlatform()

    systemProperty("spring.profiles.active", "mysql, test")
    systemProperty("embedded.mysql.enabled", "true")
}

task<Test>("h2Test") {
    description = "Runs the integration tests against h2 database using `h2` Spring profile"
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath

    useJUnitPlatform()

    systemProperty("spring.profiles.active", "h2, test")
}

task<Test>("multipleDatabaseTest") {
    description = "Runs the integration tests against mysql and postgresql databases using `mix` Spring profile"
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath

    useJUnitPlatform()

    systemProperty("spring.profiles.active", "mix, test")
    systemProperty("embedded.mysql.enabled", "true")
    systemProperty("embedded.postgresql.enabled", "true")
}

tasks.getByName<BootJar>("bootJar") {
    layered {
        enabled = true
    }
}

tasks.getByName<BootBuildImage>("bootBuildImage") {
    imageName = "${project.name}:${project.version}"
}
