import app.mbl.hcmute.buildsrc.Depends
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = Depends.Versions.androidCompileSdkVersion

    defaultConfig {
        multiDexEnabled = true
        applicationId = "app.mbl.hcmute.chatApp"
        minSdk = Depends.Versions.minSdkVersion
        targetSdk = Depends.Versions.targetSdkVersion
        versionCode = Depends.Versions.appVersionCode
        versionName = Depends.generateVersionName()
        testInstrumentationRunner = Depends.Libraries.testInstrumentationRunner
        javaCompileOptions.annotationProcessorOptions.arguments += mapOf(
            "room.schemaLocation" to "$projectDir/schemas"
        )
    }
    sourceSets {
        map { it.java.srcDir("src/${it.name}/kotlin") }
    }
    buildTypes {
        named("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            signingConfig = signingConfigs.getByName("debug")
            buildConfigField("String", "BASE_URL", "\"" + Depends.Environments.debugBaseUrl + "\"")
        }
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
            buildConfigField("String", "BASE_URL", "\"" + Depends.Environments.releaseBaseUrl + "\"")
        }
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_11
        sourceCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    lintOptions {
        isAbortOnError = false
    }
    //testOptions.unitTests.returnDefaultValues = true
    packagingOptions {
        exclude("META-INF/rxjava.properties")
        exclude("META-INF/proguard/androidx-annotations.pro")
        resources.excludes += "DebugProbesKt.bin"
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = freeCompilerArgs + listOf("-XXLanguage:+InlineClasses")
    }
}

dependencies {
    //ML Kit dependencies
    implementation(Depends.Libraries.mlkitTextRecognition)
    implementation(Depends.Libraries.mlkitLanguageId)
    implementation(Depends.Libraries.mlkitTranslate)


    // Crop image dependencies
    implementation(Depends.Libraries.imageCropper)

    //cameraX dependencies
    implementation(Depends.Libraries.cameraCore)
    implementation(Depends.Libraries.camera2)
    implementation(Depends.Libraries.cameraLifecycle)
    implementation(Depends.Libraries.cameraView)
    implementation(Depends.Libraries.cameraExtensions)

    //chat app dependencies
    implementation(Depends.Libraries.multiViewAdapter)
    implementation(Depends.Libraries.multiViewAdapter_dataBinding_ext)
    implementation(Depends.Libraries.multiViewAdapter_decoration_ext)
    implementation(Depends.Libraries.multiViewAdapter_diffUtil_ext)

    implementation(platform(Depends.Libraries.openAiClientBOM))
    implementation(Depends.Libraries.openAiKotlin)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    runtimeOnly(Depends.Libraries.ktor)

    implementation(Depends.Libraries.chat_kit)
    implementation(Depends.Libraries.voice_dialog)
    implementation(Depends.Libraries.markwon_lib)
    implementation(Depends.Libraries.markwon_table_ext_lib)
    implementation(Depends.Libraries.markwon_latex_ext_lib)

    //app dependencies
    implementation(Depends.Libraries.voice_dialog)
    implementation("com.github.Jay-Goo:RangeSeekBar:3.0.0")

    // room database
    implementation(Depends.Libraries.roomRuntime)
    kapt(Depends.Libraries.roomCompiler)
    implementation(Depends.Libraries.roomKtx)
    implementation(project(":module:base"))
    implementation(project(":module:chat"))

    implementation(Depends.Libraries.kotlin)
    implementation(Depends.Libraries.kotlin_reflect)
    implementation(Depends.Libraries.android_core_ktx)
    implementation(Depends.Libraries.multidex)
    implementation(Depends.Libraries.fragment_ktx)
    implementation(Depends.Libraries.paging_runtime_ktx)
    implementation(Depends.Libraries.paging_rx)
    implementation(Depends.Libraries.dataStore_preferences)
    implementation(Depends.Libraries.coroutines_android)
    //reactive
    implementation(Depends.Libraries.rx_java_android)
    implementation(Depends.Libraries.rx_binding3)
    implementation(Depends.Libraries.rx_kotlin)
    implementation(Depends.Libraries.autodispose)
    implementation(Depends.Libraries.autodispose_android)
    implementation(Depends.Libraries.autodispose_android_arch)
    //navigation
    implementation(Depends.Libraries.navigation_fragment_ktx)
    implementation(Depends.Libraries.navigation_ui_ktx)

    //dependency injection
    implementation(Depends.Libraries.hilt_android)
    implementation(Depends.Libraries.hilt_navigation_fragment)
    kapt(Depends.Libraries.hilt_android_compiler)
    kapt(Depends.Libraries.hilt_compiler)
    implementation(Depends.Libraries.java_inject)
    //network
    implementation(Depends.Libraries.retrofit)
    implementation(Depends.Libraries.retrofit_adapter_rx)
    implementation(Depends.Libraries.logging_interceptor)
    //parser
    implementation(Depends.Libraries.gson)
    api(Depends.Libraries.converter_gson)
    //ui
    implementation(Depends.Libraries.glide)
    kapt(Depends.Libraries.glide_compiler)
    implementation(Depends.Libraries.lottie)
    implementation(Depends.Libraries.swipeRefresh)
    implementation(Depends.Libraries.autoBlurView)
    implementation(Depends.Libraries.material)
    implementation(Depends.Libraries.splashScreen) // splash screen

    //other
//    debugImplementation(Depends.Libraries.leak_canary)
    debugImplementation(Depends.Libraries.chucker)
    releaseImplementation(Depends.Libraries.chucker_no_op)
    //    debugImplementation(Depends.Libraries.leak_canary) //todo: Enable later.
    //test
    testImplementation(Depends.Libraries.junit)
    androidTestImplementation(Depends.Libraries.test_runner)
    androidTestImplementation(Depends.Libraries.espresso_core)
}