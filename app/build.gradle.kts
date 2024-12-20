plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.transportpro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.transportpro"

        minSdk = 24
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


    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildToolsVersion = "34.0.0"

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:deprecation")
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.gridlayout:gridlayout:1.0.0")

    implementation("com.google.firebase:firebase-database:20.2.2")
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // for password hashing
    implementation ("at.favre.lib:bcrypt:0.9.0")
    implementation ("org.mindrot:jbcrypt:0.4")

    implementation("com.google.firebase:firebase-analytics-ktx:22.1.2")
    implementation("com.google.firebase:firebase-analytics:22.1.2")

    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-auth")

    // Google Play Services Authentication
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("com.google.android.gms:play-services-safetynet:18.0.1")
}