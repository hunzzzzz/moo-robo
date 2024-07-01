plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.asciidoctor.jvm.convert") version "3.3.2" // Spring REST Docs
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("kapt") version "2.0.0" // QUERYDSL
}

group = "hunzz.study"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

repositories {
    mavenCentral()
}

// snippet 파일 생성 경로 설정
val snippetsDir = file("build/generated-snippets")

dependencies {
    // DB
    runtimeOnly("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // QUERYDSL
    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
    // Spring REST Docs
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    // TEST
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // TEST (SWAGGER)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    // VALIDATION
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // WEB
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    test {
        // Test 결과를 snippet 경로에 출력
        outputs.dir(snippetsDir)
    }

    asciidoctor {
        // test 통과 시, 아래 Task 실행
        dependsOn(test)

        // 문서 최신화 (기존에 존재하는 Docs 삭제)
        doFirst { delete(file("src/main/resources/static/docs")) }

        // snippet 경로 설정
        inputs.dir(snippetsDir)

        // ascii docs 파일 복사 (배포를 위한)
        doLast {
            copy {
                from("build/docs/asciidoc")
                into("src/main/resources/static/docs")
            }
        }
    }

    build {
        // ascii docs 파일이 정상적으로 생성 시, build 완료
        dependsOn(asciidoctor)
    }
}