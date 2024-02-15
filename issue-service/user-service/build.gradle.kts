
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // 설정을 configuration으로 관리하기 위해서 kapt 플러그인에 추가
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    implementation("at.favre.lib:bcrypt:0.9.0")

    implementation("io.r2dbc:r2dbc-h2")
}

