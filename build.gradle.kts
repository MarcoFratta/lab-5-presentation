plugins {
    java
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "it.unibo.ds.presentation"
    version = "0.1.0"
}

subprojects {
    apply(plugin = "java")

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.8")
}
