plugins {
    id("java")
    id("maven-publish")
}

group = "com.github.Lumitani-pixel"
version = "2.1.3"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = "logger"
            version = project.version.toString()
        }
    }

    repositories {
        mavenLocal() // for testing
    }
}