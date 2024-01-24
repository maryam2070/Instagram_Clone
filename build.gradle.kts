buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.20")
        // classpath("com.google.gms:google-services:4.4.0")
      //  classpath ("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
    }
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        gradlePluginPortal()
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false

    id("com.google.dagger.hilt.android") version "2.44" apply false

    id("com.google.gms.google-services") version "4.4.0" apply false
}