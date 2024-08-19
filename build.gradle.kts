

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
//    classpath ("com.google.gms:google-services:4.3.15")
    id("com.google.gms.google-services") version "4.4.2" apply false


}


buildscript{
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.google.gms:google-services:4.3.15")
    }
}
