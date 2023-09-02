import app.mbl.hcmute.buildsrc.Depends
import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

val localProperties = Properties()
try {
    localProperties.load(FileInputStream(rootProject.file("local.properties")))
} catch (e: Exception) {
    logger.warn("No Local Properties File Found!")
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "app.mbl.hcmute.base"
    compileSdk = Depends.Versions.androidCompileSdkVersion

    defaultConfig {
        minSdk = Depends.Versions.minSdkVersion
        targetSdk = Depends.Versions.targetSdkVersion

        testInstrumentationRunner = Depends.Libraries.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "BASE64_ENCODED_PUBLIC_KEY", "\"" + localProperties["base64EncodedPublicKey"] + "\"")
    }

    sourceSets {
        named("main") {
            java.srcDir("src/${this.name}/kotlin")
            res.srcDir("src/${this.name}/res/dimens")
        }
    }

    buildTypes {
        debug {
            // All keys put at local.properties file
            buildConfigField("String", "BANNER_AD_UNIT_ID", "${properties["BANNER_AD_UNIT_ID_DEBUG"]}")
            buildConfigField("String", "INTERSTITIAL_AD_UNIT_ID", "${properties["INTERSTITIAL_AD_UNIT_ID_DEBUG"]}")
            buildConfigField("String", "REWARDED_AD_UNIT_ID", "${properties["REWARDED_AD_UNIT_ID_DEBUG"]}")
            buildConfigField("String", "OPEN_AD_UNIT_ID", "${properties["OPEN_AD_UNIT_ID_DEBUG"]}")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "BANNER_AD_UNIT_ID", "${properties["BANNER_AD_UNIT_ID_RELEASE"]}")
            buildConfigField("String", "INTERSTITIAL_AD_UNIT_ID", "${properties["INTERSTITIAL_AD_UNIT_ID_RELEASE"]}")
            buildConfigField("String", "REWARDED_AD_UNIT_ID", "${properties["REWARDED_AD_UNIT_ID_RELEASE"]}")
            buildConfigField("String", "OPEN_AD_UNIT_ID", "${properties["OPEN_AD_UNIT_ID_RELEASE"]}")
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_11
        sourceCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    lint {
        lint.abortOnError = false
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

}

dependencies {
    // google billing
    implementation(Depends.Libraries.billing_ktx)

    //google ads
    implementation(Depends.Libraries.google_ads)

    implementation(Depends.Libraries.kotlin)
    implementation(Depends.Libraries.android_core_ktx)
    implementation(Depends.Libraries.appcompat)
    implementation(Depends.Libraries.material)
    implementation(Depends.Libraries.timber)

    implementation(Depends.Libraries.coroutines_android)

    implementation(Depends.Libraries.lifecycle_viewmodel_ktx)
    implementation(Depends.Libraries.android_core_ktx)
    implementation(Depends.Libraries.fragment_ktx)
    implementation(Depends.Libraries.rx_java)
    implementation(Depends.Libraries.rx_java_android)
    implementation(Depends.Libraries.gson)
    implementation(Depends.Libraries.glide)
    implementation(Depends.Libraries.paging_runtime_ktx)
    implementation(Depends.Libraries.constraintlayout)

    //others
    api(Depends.Libraries.timber)

//    testImplementation(Depends.Libraries.junit)
//    androidTestImplementation(Depends.Libraries.test_runner)
//    androidTestImplementation(Depends.Libraries.test_ext_junit)
//    androidTestImplementation(Depends.Libraries.espresso_core)
}