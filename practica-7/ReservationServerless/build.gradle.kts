plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"}

group = "com.pucmm.eict"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Test dependencies
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Regular dependencies
    implementation(group = "com.amazonaws", name = "aws-lambda-java-core", version = "1.2.2")
    implementation(group = "com.amazonaws", name = "aws-lambda-java-events", version = "3.11.2")
    implementation(group = "com.amazonaws", name = "aws-java-sdk-dynamodb", version = "1.12.496")
    implementation("com.amazonaws:aws-lambda-java-log4j2:1.5.1")
    implementation(group = "com.googlecode.json-simple", name = "json-simple", version = "1.1.1")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.8.9")

    compileOnly ("org.projectlombok:lombok:1.18.30")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")

    testCompileOnly ("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor ("org.projectlombok:lombok:1.18.30")
}


tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveFileName.set("${project.name}-${project.version}-all.jar") // Nombre del archivo JAR sombra
    mergeServiceFiles() // Fusiona los archivos de servicio, si es necesario
    dependencies {
        exclude(dependency("org.slf4j:slf4j-log4j12")) // Excluye cualquier dependencia específica, si es necesario
    }
}