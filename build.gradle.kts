plugins {
    id("java")
    id("maven-publish")
}

group = "com.github.Lumitani-pixel"
version = "2.1.6"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
            groupId = "com.github.Lumitani-pixel"
            artifactId = "logger"
            version = "2.1.6"
        }
    }
}