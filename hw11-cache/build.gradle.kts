dependencies {
    implementation ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")
    implementation("org.hibernate.orm:hibernate-core")

    implementation("ch.qos.logback:logback-classic")

    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
}
