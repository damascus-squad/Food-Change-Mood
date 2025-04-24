import de.undercouch.gradle.tasks.download.Download

plugins {
    kotlin("jvm") version "2.1.10"
    id("de.undercouch.download") version "5.6.0"
}

group = "org.damascus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.insert-koin:koin-core:4.0.2")

    testImplementation(kotlin("test"))

    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")

    testImplementation("com.google.truth:truth:1.4.4")
    testImplementation("io.mockk:mockk:1.14.0")
}

tasks.test {
    useJUnitPlatform()
}

val downloadFoodCSV by tasks.registering(Download::class) {
    src("https://drive.usercontent.google.com/download?id=1px860X8gO_AFHNkcNFe64e_il_bDaKSI&export=download&authuser=0&confirm=t&uuid=51b6e15b-0cbc-4e92-932a-f226c6ddec08&at=APcmpozkRgttvXbbZitl5BiFIdg_%3A1744613357267")
    dest(file("$rootDir/assets/food.csv"))
    overwrite(false)
}

tasks.register("prepareAssets") {
    doLast {
        file("$rootDir/assets").mkdirs()
    }
}

tasks.named("compileKotlin") {
    dependsOn(downloadFoodCSV)
}

kotlin {
    jvmToolchain(17)
}