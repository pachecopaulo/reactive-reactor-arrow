import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
}

group "com.paulopacheco.study.arrow.reactor"
version "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	jcenter()
	mavenCentral()

	maven { url "https://jitpack.io" }
	maven { url "https://dl.bintray.com/spekframework/spek-dev" }
	maven { url "https://plugins.gradle.org/m2/" }

	maven {
		url "https://oss.sonatype.org/content/repositories/snapshots"
		mavenContent {
			snapshotsOnly()
		}
	}
}

dependencies {
	//some standard libraries to get started
	implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
	implementation "org.jetbrains.kotlin:kotlin-reflect:1.3.0"
	implementation "ch.qos.logback:logback-classic:1.2.3"
	implementation 'com.typesafe:config:1.3.2'

	// Use spring boot to get the correct libraries for webflux
	implementation('org.springframework.boot:spring-boot-starter-webflux')
	implementation('org.springframework.boot:spring-boot-starter-data-mongodb-reactive')

	implementation "com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+"

	// Use Arrow to add FP functionality to Kotlin
	implementation "io.arrow-kt:arrow-fx:$arrow_version"
	implementation "io.arrow-kt:arrow-optics:$arrow_version"
	implementation "io.arrow-kt:arrow-syntax:$arrow_version"
	implementation "io.arrow-kt:arrow-fx-reactor:$arrow_version"
	kapt    "io.arrow-kt:arrow-meta:$arrow_version"
	implementation "io.arrow-kt:arrow-mtl:$arrow_version"


	implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_courotines_version"

	// Setup tests and assertion library
	testImplementation ("org.spekframework.spek2:spek-dsl-jvm:$spek_version")  { exclude group: 'org.jetbrains.kotlin' }
	testImplementation 'org.amshove.kluent:kluent:1.45'
	testRuntimeOnly ("org.spekframework.spek2:spek-runner-junit5:$spek_version") {
		exclude group: 'org.junit.platform'
		exclude group: 'org.jetbrains.kotlin'
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
