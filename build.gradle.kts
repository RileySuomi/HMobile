// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.9.0" apply false
    id("org.jetbrains.kotlin.android") version "2.1.20" apply false
    id("com.google.devtools.ksp") version "2.1.20-1.0.32" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("com.google.dagger.hilt.android") version "2.53.1" apply false

//    id("com.google.dagger.hilt.android") version "2.50" apply false
}