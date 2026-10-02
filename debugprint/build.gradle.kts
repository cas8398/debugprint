plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.vanniktech.maven.publish)
}

android {
    namespace = "com.flagodna.debugprint"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 21
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
}

// -----------------------------------------------------------------
// Maven Central publishing (Vanniktech plugin)
// -----------------------------------------------------------------
mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = "com.flagodna",
        artifactId = "debugprint",
        version = "0.1.1"
    )

    pom {
        name.set("DebugPrint")
        description.set("Flutter-inspired debug-only logger for Android")
        url.set("https://github.com/cas8398/debugprint")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("flagodna")
                name.set("Flagodna")
                email.set("dev@flagodna.com")
            }
        }

        scm {
            connection.set("scm:git:github.com/cas8398/debugprint.git")
            developerConnection.set("scm:git:ssh://github.com/cas8398/debugprint.git")
            url.set("https://github.com/cas8398/debugprint")
        }
    }
}