
plugins {
    id ("org.springframework.boot")
}

dependencies {
    implementation ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")
    implementation("org.hibernate.orm:hibernate-core")

    implementation("ch.qos.logback:logback-classic")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    implementation ("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")

    implementation("org.freemarker:freemarker")
}
