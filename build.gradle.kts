// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    id("com.android.application") version "8.2.2" apply false
//    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
//    id("com.google.dagger.hilt.android") version "2.48" apply false
//}
buildscript {
//    ext {
//        val hilt_version = "2.44"
//        val kotlin_version = "1.8.0"
//    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}