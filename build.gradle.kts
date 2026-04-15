plugins {
    id("java")
    kotlin("jvm")
    `maven-publish`
    signing
}

group = "ru.digit-verse"
version = "1.0.0"

val github = "github.com/ShivaMultiarmed/Quantum-Computing"

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            group = "ru.digit-verse"
            artifactId = "quantum-computing"
            version = "1.0.0"
            description = "A library for simulating quantum computing concepts."

            pom {
                name.set("Quantum Computing")
                inceptionYear.set("2026")
                description.set("A library to simulate quantum computing concepts.")
                url.set("https://$github")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id = "mikhail_shell"
                        name = "Mikhail Shell"
                        email = "theoutstandingme@gmail.com"
                    }
                }
                scm {
                    connection.set("scm:git:git://$github.git")
                    developerConnection.set("scm:git:ssh://$github.git")
                    url.set("https://$github")
                }
            }
        }
    }

    repositories {
        mavenCentral {
            name = "QuantumComputing"
            url = uri("https://central.sonatype.com/api/v1/publisher/deployment/05cdf1aa-7b4a-4165-b7fd-d3b819bd1086")
            credentials {
                username = project.findProperty("maven.central.userName")?.toString() ?: ""
                password = project.findProperty("maven.central.password")?.toString() ?: ""
            }
        }
        mavenLocal {
            name = "QuantumComputing"
            url = uri("${System.getProperty("user.home")}/.m2/repository")
        }
    }
}

signing {
    val isPublishTask = gradle.startParameter.taskNames.any { it.contains("publish", ignoreCase = true) }
    isRequired = isPublishTask
    sign(publishing.publications["maven"])
}

repositories {
    mavenCentral()
    mavenLocal {
        val releasesRepoUrl = uri(layout.buildDirectory.dir("repos/releases"))
        val snapshotsRepoUrl = uri(layout.buildDirectory.dir("repos/snapshots"))
        url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}
java {
    withSourcesJar()
    withJavadocJar()
}