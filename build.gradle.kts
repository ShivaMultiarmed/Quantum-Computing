plugins {
    id("java")
    kotlin("jvm")
    `maven-publish`
    signing
}

group = "ru.digit-verse"
version = "1.0.0-alpha01"

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            group = "ru.digit-verse"
            artifactId = "quantum-computing"
            version = "1.0.0-alpha01"
            description = "A library for simulating quantum computing concepts."
            pom {
                name.set("Quantum Computing")
                description.set("A library to simulate quantum computing concepts.")
                url.set("") // TODO
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
                    val github = "github.com/mikhail-shell/quantum-computing" // TODO
                    connection.set("scm:git:git://$github.git")
                    developerConnection.set("scm:git:ssh://$github.git")
                    url.set("https://$github")
                }
            }
        }
    }

    repositories {
        maven {
            name = "QuantumComputing"
            url = uri(layout.buildDirectory.dir("repo"))
            credentials {
                username = project.findProperty("maven.central.userName")?.toString() ?: ""
                password = project.findProperty("maven.central.password")?.toString() ?: ""
            }
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
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(kotlin("stdlib-jdk8"))
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