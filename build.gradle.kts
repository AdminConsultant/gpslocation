// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
// Project-level build.gradle
buildscript {
    repositories {
        google() // Assicurati che Google repository sia presente
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2") // La versione del plugin Google Services
    }
}