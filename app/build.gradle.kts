plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.dicoding.picodiploma.loginwithanimation"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dicoding.picodiploma.loginwithanimation"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += ("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.9.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")
    implementation("androidx.test.ext:junit-ktx:1.2.1")
    implementation("androidx.test.espresso:espresso-idling-resource:3.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:5.7.0")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")
    androidTestImplementation ("androidx.test.uiautomator:uiautomator:2.3.0")


    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    implementation("androidx.activity:activity-ktx:1.9.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation ("androidx.paging:paging-runtime:3.3.0")

}