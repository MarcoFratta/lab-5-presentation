plugins {
    java
}

allprojects {
    repositories {
        // Use Maven Central for resolving dependencies.
        mavenCentral()
    }

    group = "it.unibo.ds.presentation"
    version = "0.1.0"
}

subprojects {
    apply(plugin = "java")

    dependencies {
        // Use JUnit Jupiter for testing.
        testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    }

    tasks.test {
        // Use JUnit Platform for unit tests.
        useJUnitPlatform()
    }
}