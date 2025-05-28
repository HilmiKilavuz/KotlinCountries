plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
}

android {
    namespace = "com.kilavuzhilmi.kotlincountries"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kilavuzhilmi.kotlincountries"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    dataBinding{
        enable = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    
    // Add lint configuration to handle issues
    lint {
        baseline = file("lint-baseline.xml")
        abortOnError = false
    }
    
    // Fix for dependency conflicts
    configurations.all {
        resolutionStrategy {
            // Force using only AndroidX versions
            force("androidx.core:core:1.16.0")
            force("androidx.core:core-ktx:1.16.0")
            
            // Exclude old support libraries
            exclude(group = "com.android.support")
            exclude(module = "support-compat")
            exclude(module = "support-v4")
            exclude(module = "support-annotations")
            exclude(module = "support-fragment")
            exclude(module = "support-core-utils")
            exclude(module = "support-core-ui")
        }
    }
}

// Add specific dependency substitutions for AndroidX migration
configurations.all {
    resolutionStrategy.dependencySubstitution {
        substitute(module("com.android.support:support-compat"))
            .using(module("androidx.core:core:1.16.0"))
        substitute(module("com.android.support:support-v4"))
            .using(module("androidx.legacy:legacy-support-v4:1.0.0"))
    }
}

dependencies {
    val retrofitVersion = "2.9.0"
    val glideVersion = "4.15.1"
    val rxJavaVersion = "2.1.1"
    val roomVersion = "2.4.3"
    val navVersion = "2.7.6"
    val preferencesVersion = "1.2.1"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("com.google.android.material:material:1.10.0")

    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    implementation("androidx.room:room-rxjava2:$roomVersion")

    implementation("io.reactivex.rxjava2:rxjava:$rxJavaVersion")
    implementation("io.reactivex.rxjava2:rxandroid:$rxJavaVersion")

    implementation("com.github.bumptech.glide:glide:$glideVersion")
    
    // Use only AndroidX libraries (no more old support libraries)
    implementation("androidx.palette:palette:1.0.0")
    implementation("androidx.preference:preference:$preferencesVersion")

    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}