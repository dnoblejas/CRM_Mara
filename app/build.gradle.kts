plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services") // para Firebase
}

android {
    namespace = "com.example.crm_mara"
    compileSdk = 35  // Actualizado de 34 a 35

    defaultConfig {
        applicationId = "com.example.crm_mara"
        minSdk = 26
        targetSdk = 35  // Actualizado de 34 a 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Dependencias básicas de Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    // Dependencia para usar ViewModel con Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    implementation ("androidx.compose.material3:material3:1.0.0") // Asegúrate de tener la versión correcta

    // Extended Iconsa
    implementation("androidx.compose.material:material-icons-extended:1.5.1")

    // Dependencias de Firebase
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation(platform(libs.firebase.bom))
    //INCLUIR FIREBASE PARA PODER USAR CLOUD FIRESTORE
    implementation(libs.firebase.analytics)

    // Dependencias de Google Services
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.appcompat)

    //Para el uso de iconos
    implementation ("androidx.compose.material:material-icons-extended:1.5.1")

}
