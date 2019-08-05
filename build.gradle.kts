
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
	repositories {
		maven("http://binary/artifactory/public")
	}
	dependencies {
		classpath("com.bmuschko:gradle-docker-plugin:3.2.5")
	}
}

plugins {
	id("org.springframework.boot") version "2.1.6.RELEASE"
	id("io.spring.dependency-management") version "1.0.7.RELEASE"
	kotlin("jvm") version "1.2.71"
	kotlin("plugin.spring") version "1.2.71"
}

apply(plugin = "com.bmuschko.docker-remote-api")

group = "ru.alfabank.testing"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	maven("http://binary/artifactory/public")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("com.oracle:ojdbc6:11.2.0.4")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.register<Dockerfile>("createDockerFile") {
	from("java:8u92-alpine")
	exposePort(8080)
	addFile("./libs/${project.name}-${project.version}.jar", "${project.name}-${project.version}.jar")
	destFile = file("${project.buildDir}/Dockerfile")
	entryPoint("java")
	defaultCommand("-jar", "${project.name}-${project.version}.jar")
}

tasks.register<DockerBuildImage>("buildImage") {
	dependsOn("createDockerFile")

	inputDir = file("build")
	tags.add("docker.moscow.alfaintra.net/${project.name}:${project.version}")
}

tasks.register<DockerPushImage>("dockerPushImage") {
	imageName = "docker.moscow.alfaintra.net/${project.name}"
	tag = "${project.version}"
}

tasks.register("publish") {
	dependsOn("clean")
	dependsOn("build")
	dependsOn("buildImage")
	dependsOn("dockerPushImage")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
