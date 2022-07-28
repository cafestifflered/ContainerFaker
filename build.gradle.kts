plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "com.stifflered"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.codemc.io/repository/maven-releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
}

bukkit {
    main = "com.stifflered.containerfaker.Main"
    name = rootProject.name
    apiVersion = "1.18"
    version = "1.0.0"
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    shadowJar {
        dependencies {

        }

    }

    runServer {
        minecraftVersion("1.19.1")
    }

}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}