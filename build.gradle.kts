import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.61"
}

apply(plugin = "io.spring.dependency-management")

java.sourceCompatibility = JavaVersion.VERSION_11

dependencyManagement {
	val springBootVersion: String by project
	imports {
		mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
	}
}

repositories {
	jcenter()
	mavenCentral()

	maven("https://jitpack.io")
	maven("https://dl.bintray.com/spekframework/spek-dev")
	maven("https://plugins.gradle.org/m2/")

//	maven("https://oss.sonatype.org/content/repositories/snapshots"
//		mavenContent {
//			snapshotsOnly()
//		}
//	)
}

dependencies {
	val arrowVersion: String by project
	val kotlinCourotinesVersion: String by project
	val spekVersion: String by project

	//some standard libraries to get started
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.0")
	implementation("ch.qos.logback:logback-classic:1.2.3")
	implementation("com.typesafe:config:1.3.2")

	// Use spring boot to get the correct libraries for webflux
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")

	// Use Arrow to add FP functionality to Kotlin
	implementation( "io.arrow-kt:arrow-fx:$arrowVersion")
	implementation(  "io.arrow-kt:arrow-optics:$arrowVersion")
	implementation(  "io.arrow-kt:arrow-syntax:$arrowVersion")
	implementation(  "io.arrow-kt:arrow-fx-reactor:$arrowVersion")
	//kapt "io.arrow-kt:arrow-meta:$arrowVersion"
	implementation("io.arrow-kt:arrow-meta:$arrowVersion")
	implementation(  "io.arrow-kt:arrow-mtl:$arrowVersion")


	implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCourotinesVersion")

	// Setup tests and assertion library
	testImplementation ("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")  {
		exclude("org.jetbrains.kotlin")
	}
	testImplementation("org.amshove.kluent:kluent:1.45")
	testRuntimeOnly ("org.spekframework.spek2:spek-runner-junit5:$spekVersion") {
		exclude("org.junit.platform")
		exclude("org.jetbrains.kotlin")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}