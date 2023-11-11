plugins {
    id("java")
    id("java-library")
    id("com.diffplug.spotless") version ("6.11.0")
    id("com.github.johnrengelman.shadow") version ("7.1.1")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()

        maven(url = "https://jitpack.io")
        maven(url = "https://repo.nickuc.com/maven2/")
        maven(url = "https://repo.nickuc.com/maven-releases/")
        maven(url = "https://repo.dmulloy2.net/repository/public/")
        maven(url = "https://repo.bg-software.com/repository/nms/")
        maven(url = "https://oss.sonatype.org/content/groups/public/")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshot")
        maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

    dependencies {
        compileOnlyApi("com.fasterxml.jackson.core:jackson-core:2.13.4")
        compileOnlyApi("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
        compileOnlyApi("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.4")
        api("com.github.HenryFabio:sql-provider:9561f20fd2")
        implementation("com.github.DevNatan.inventory-framework:inventory-framework:6f6a634cc5478a16ff81773ed80187dde27d6edd")
        compileOnlyApi("org.jetbrains:annotations:23.0.0")

        compileOnlyApi(fileTree("libs"))

        compileOnly("me.lucko:helper:5.6.10")
        compileOnly("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")
    }

    tasks.shadowJar {
        relocate("org.bson", "com.origincubes.common.libraries.org.bson")
        relocate("org.aopalliance", "com.origincubes.common.libraries.org.aopalliance")
        relocate("org.checkerframework", "com.origincubes.common.libraries.org.checkerframework")
        relocate("io.leangen.geantyref", "com.origincubes.common.libraries.io.leangen.geantyref")
        relocate("javax.annotation", "com.origincubes.common.libraries.javax.annotation")
        relocate("javax.inject", "com.origincubes.common.libraries.javax.inject")
        relocate("com.github.benmanes.caffeine", "com.origincubes.common.libraries.com.github.benmanes.caffeine")
        relocate("redis.clients.jedis", "com.origincubes.common.libraries.redis.clients.jedis")
        relocate("org.apache.commons.pool2", "com.origincubes.common.libraries.org.apache.commons.pool2")
        relocate("org.json", "com.origincubes.common.libraries.org.json")
        relocate("org.intellij", "com.origincubes.common.libraries.org.intellij")
        relocate("org.jetbrains", "com.origincubes.common.libraries.org.jetbrains")
        relocate("com.fasterxml.jackson", "com.origincubes.common.libraries.com.fasterxml.jackson")
        relocate("edu.umd.cs", "com.origincubes.common.libraries.edu.umd.cs")
        relocate("io.github.classgraph", "com.origincubes.common.libraries.io.github.classgraph")
        relocate("net.bytebuddy", "com.origincubes.common.libraries.net.bytebuddy")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    spotless {
        java {
            removeUnusedImports()
            palantirJavaFormat()
            formatAnnotations()
            importOrder()
        }
    }
}