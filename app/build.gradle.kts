import java.util.regex.Pattern.compile

plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.wortgewant"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.wortgewant"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    //webClient
    /*implementation(fileTree(mapOf(
        "dir" to "/Users/naz/Downloads/jar_files/",
        "include" to listOf("*.aar", "*.jar"),
    )))*/
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //volley
    implementation(libs.volley)

    //deepL
    implementation(libs.deepl.java)

    //web scraping
    implementation (libs.retrofit)
    implementation (libs.jsoup)
    implementation(libs.okhttp)


    //jsoup
    implementation("org.jsoup:jsoup:1.17.2")

    //noinspection UseTomlInstead
    //compileOnly("org.apache.commons.io.FileUtils:2.4")
    implementation("commons-io:commons-io:2.16.1")

    // https://mvnrepository.com/artifact/com.google.collections/google-collections
    // MultiMap as an alternative for HashMap
    implementation ("com.google.collections:google-collections:1.0-rc2")

    // Serialize MultiMap
    implementation ("com.google.code.gson:gson:2.10.1")

}