/*
 * Copyright (c) 2019 Owain van Brakel <https://github.com/Owain94>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

object ProjectVersions {
    const val rlVersion = "1.6.6-SNAPSHOT"
    const val apiVersion = "0.0.1"
}

object Libraries {
    private object Versions {
        const val annotations = "18.0.0"
        const val apacheCommonsLang = "3.9"
        const val apacheCommonsText = "1.8"
        const val discord = "1.1"
        const val gson = "2.8.6"
        const val guava = "28.1-jre"
        const val guice = "4.2.2"
        const val hamcrest = "2.2"
        const val javax = "1.3.2"
        const val jogamp = "2.3.2"
        const val jopt = "5.0.4"
        const val jooq = "3.12.3"
        const val junit = "4.12"
        const val logback = "1.2.3"
        const val lombok = "1.18.10"
        const val mockito = "3.1.0"
        const val okhttp3 = "4.2.2"
        const val pf4j = "3.2.0"
        const val rxjava = "2.2.14"
        const val slf4j = "1.7.29"
        const val naturalMouse = "2.0.2"
    }

    const val annotations = "org.jetbrains:annotations:${Versions.annotations}"
    const val apacheCommonsLang = "org.apache.commons:commons-lang3:${Versions.apacheCommonsLang}"
    const val apacheCommonsText = "org.apache.commons:commons-text:${Versions.apacheCommonsText}"
    const val discord = "net.runelite:discord:${Versions.discord}"
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val guava = "com.google.guava:guava:${Versions.guava}"
    const val guice = "com.google.inject:guice:${Versions.guice}:no_aop"
    const val guiceTestlib = "com.google.inject.extensions:guice-testlib:${Versions.guice}"
    const val hamcrest = "org.hamcrest:hamcrest-library:${Versions.hamcrest}"
    const val javax = "javax.annotation:javax.annotation-api:${Versions.javax}"
    const val jogampJogl = "org.jogamp.jogl:jogl-all:${Versions.jogamp}"
    const val jogampGluegen = "org.jogamp.gluegen:gluegen-rt:${Versions.jogamp}"
    const val jopt = "net.sf.jopt-simple:jopt-simple:${Versions.jopt}"
    const val jooq = "org.jooq:jooq:${Versions.jooq}"
    const val junit = "junit:junit:${Versions.junit}"
    const val logback = "ch.qos.logback:logback-classic:${Versions.logback}"
    const val lombok = "org.projectlombok:lombok:${Versions.lombok}"
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
    const val mockitoInline = "org.mockito:mockito-inline:${Versions.mockito}"
    const val okhttp3 = "com.squareup.okhttp3:okhttp:${Versions.okhttp3}"
    const val pf4j = "org.pf4j:pf4j:${Versions.pf4j}"
    const val rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"
    const val slf4jApi = "org.slf4j:slf4j-api:${Versions.slf4j}"
    const val naturalMouse = "com.github.joonasvali.naturalmouse:naturalmouse:${Versions.naturalMouse}"
}
