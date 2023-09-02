package app.mbl.hcmute.buildsrc

object Depends {

    object Versions {
        //Mlkit version
        const val mlkitTextRecognitionVersion = "19.0.0"
        const val mlkitLanguageIdVersion = "17.0.4"
        const val mlkitTranslateVersion = "17.0.1"

        //ImageCropper version
        const val imageCropperVersion = "4.5.0"

        //CameraX version
        const val cameraXVersion = "1.1.0-beta01"

        //app lib version
        const val multiAdapterVersion = "3.0.0"
        const val openAiVersion = "3.2.3"
        const val chatKit = "0.4.1"
        const val voiceDialog = "1.1.0"
        const val markWon = "4.6.2"

        // Splash screen
        const val splashScreen = "1.0.0"

        // Crop photo
        const val crop_photo = "4.5.0"

        // Room database
        const val room = "2.5.0"

        // Google billing
        const val billingKtx = "5.1.0"

        //ui
        const val autoBlurLib = "1.2.1"

        // Google ads
        const val googleAds = "21.5.0"

        const val appVersionCode = 1_000_000
        const val gradleVersion = "7.4.2"
        const val googleServiceVersion = "4.3.15"
        const val fireBomVersion = "31.2.3"
        const val androidCompileSdkVersion = 33
        const val targetSdkVersion = 33
        const val minSdkVersion = 21
        const val kotlinVersion = "1.7.21"
        const val rxKotlinVersion = "3.0.1"
        const val rxAndroidVersion = "3.0.2"
        const val rxJavaVersion = "3.1.5"
        const val rxBinding = "4.0.0"
        const val retrofit2Version = "2.9.0"
        const val okhttpLoggingVersion = "5.0.0-alpha.10"
        const val chuckerVersion = "3.5.2"
        const val gsonVersion = "2.10"
        const val retrofitConverterGson = "2.9.0"
        const val lifecycleVersion = "2.4.0"
        const val constraintLayoutVersion = "2.1.4"
        const val supportVersion = "1.4.1"
        const val materialVersion = "1.7.0"
        const val coreKtxVersion = "1.9.0"
        const val navigationVersion = "2.5.2"
        const val pagingVersion = "3.1.1"
        const val multidexVersion = "2.0.1"
        const val fragmentExtVersion = "1.4.0"
        const val recyclerviewVersion = "1.3.0-rc01"
        const val hiltVersion = "2.44"
        const val hiltNavigationFragment = "1.0.0"
        const val hiltCompilerVersion = "1.0.0"
        const val hiltNavigationComposeVersion = "1.0.0-alpha03"
        const val javaxInjectVersion = "1"
        const val timberVersion = "5.0.1"
        const val lottieVersion = "5.2.0"
        const val swipeRefresh = "1.1.0"
        const val glideVersion = "4.14.2"
        const val autoDispose = "2.1.1"
        const val dataStorePreferenceVersion = "1.0.0"
        const val coroutinesAndroidVersion = "1.6.4"
        const val playServicesLocation = "21.0.1"

        const val mockitoKotlinVersion = "2.2.0"
        const val mockitoCoreVersion = "4.10.0"
        const val mockitoInlineVersion = "4.10.0"
        const val espressoVersion = "3.5.0"
        const val junitVersion = "4.13.2"
        const val supportTestVersion = "1.5.0"
        const val testCoreVersion = "1.4.0"
        const val testExtJunitVersion = "1.1.3"
        const val sonarqubeVersion = "3.5.0.2730"
        const val detektVersion = "1.22.0"
        const val checkDependencyVersionsVersion = "0.44.0"
        const val gradleDoctorVersion = "0.8.1"
        const val dependencyAnalysisVersion = "1.17.0"
        const val sonatypeScanGradleVersion = "2.5.3"
        const val leakCanaryVersion = "2.10"
        const val coroutinesTestVersion = "1.6.4"
        const val mockkVersion = "1.13.3"
        const val archCoreTestingVersion = "2.1.0"
    }

    object ClassPaths {
        const val gradle = "com.android.tools.build:gradle:${Versions.gradleVersion}"
        const val google_services = "com.google.gms:google-services:${Versions.googleServiceVersion}"
        const val kotlin_gradle_plugin = "gradle-plugin"
        const val navigation_safe_args_gradle_plugin =
            "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationVersion}"

        const val hilt_android_gradle_plugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltVersion}"
        const val sonarqube_gradle_plugin = "org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${Versions.sonarqubeVersion}"
        const val detekt_gradle_plugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detektVersion}"
    }

    object Libraries {
        //Mlkit
        const val mlkitTextRecognition = "com.google.android.gms:play-services-mlkit-text-recognition:${Versions.mlkitTextRecognitionVersion}"
        const val mlkitLanguageId = "com.google.mlkit:language-id:${Versions.mlkitLanguageIdVersion}"
        const val mlkitTranslate = "com.google.mlkit:translate:${Versions.mlkitTranslateVersion}"

        //CropImage
        const val imageCropper = "com.vanniktech:android-image-cropper:${Versions.imageCropperVersion}"

        //cameraX
        const val cameraCore = "androidx.camera:camera-core:${Versions.cameraXVersion}"
        const val camera2 = "androidx.camera:camera-camera2:${Versions.cameraXVersion}"
        const val cameraLifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraXVersion}"
        const val cameraView = "androidx.camera:camera-view:${Versions.cameraXVersion}"
        const val cameraExtensions = "androidx.camera:camera-extensions:${Versions.cameraXVersion}"

        //app lib

        // https://github.com/DevAhamed/MultiViewAdapter
        const val multiViewAdapter = "dev.ahamed.mva3:adapter:${Versions.multiAdapterVersion}"
        const val multiViewAdapter_dataBinding_ext = "dev.ahamed.mva3:ext-databinding:${Versions.multiAdapterVersion}"
        const val multiViewAdapter_decoration_ext = "dev.ahamed.mva3:ext-decorator:${Versions.multiAdapterVersion}"
        const val multiViewAdapter_diffUtil_ext = "dev.ahamed.mva3:ext-diffutil-rx:${Versions.multiAdapterVersion}"
        const val openAiClientBOM = "com.aallam.openai:openai-client-bom:${Versions.openAiVersion}"
        const val openAiKotlin = "com.aallam.openai:openai-client"
        const val ktor = "io.ktor:ktor-client-okhttp:2.3.0"
        const val chat_kit = "com.github.stfalcon-studio:Chatkit:${Versions.chatKit}"
        const val voice_dialog = "com.algolia.instantsearch:voice:${Versions.voiceDialog}"
        const val markwon_lib = "io.noties.markwon:core:${Versions.markWon}"
        const val markwon_table_ext_lib = "io.noties.markwon:ext-tables:${Versions.markWon}"
        const val markwon_latex_ext_lib = "io.noties.markwon:ext-latex:${Versions.markWon}"

        //firebase
        const val fireBOM = "com.google.firebase:firebase-bom:${Versions.fireBomVersion}"
        const val firebaseAnalytic = "com.google.firebase:firebase-analytics-ktx"
        const val fireStore = "com.google.firebase:firebase-firestore-ktx"

        // Splash screen
        const val splashScreen = "androidx.core:core-splashscreen:${Versions.splashScreen}"

        // crop photo
        const val cropPhoto = "com.vanniktech:android-image-cropper:${Versions.crop_photo}"
        // room database
        const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"

        // google billing
        const val billing_ktx = "com.android.billingclient:billing-ktx:${Versions.billingKtx}"

        // google ads
        const val google_ads = "com.google.android.gms:play-services-ads:${Versions.googleAds}"

        //ui
        const val autoBlurView = "com.github.mmin18:realtimeblurview:${Versions.autoBlurLib}"

        //common lib
        const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}"
        const val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlinVersion}"
        const val multidex = "androidx.multidex:multidex:${Versions.multidexVersion}"
        const val hilt_android = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
        const val hilt_navigation_fragment = "androidx.hilt:hilt-navigation-fragment:${Versions.hiltNavigationFragment}"
        const val hilt_android_compiler = "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"
        const val hilt_compiler = "androidx.hilt:hilt-compiler:${Versions.hiltCompilerVersion}"
        const val fragment_ktx = "androidx.fragment:fragment-ktx:${Versions.fragmentExtVersion}"
        const val android_core_ktx = "androidx.core:core-ktx:${Versions.coreKtxVersion}"
        const val paging_runtime_ktx = "androidx.paging:paging-runtime-ktx:${Versions.pagingVersion}"
        const val paging_rx = "androidx.paging:paging-rxjava3:${Versions.pagingVersion}"
        const val java_inject = "javax.inject:javax.inject:${Versions.javaxInjectVersion}"
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit2Version}"
        const val retrofit_adapter_rx = "com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit2Version}"
        const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttpLoggingVersion}"
        const val timber = "com.jakewharton.timber:timber:${Versions.timberVersion}"
        const val material = "com.google.android.material:material:${Versions.materialVersion}"
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val junit = "junit:junit:${Versions.junitVersion}"
        const val test_runner = "androidx.test:runner:${Versions.supportTestVersion}"
        const val test_rules = "androidx.test:rules:${Versions.supportTestVersion}"
        const val test_core = "androidx.test:core:${Versions.testCoreVersion}"
        const val test_ext_junit = "androidx.test.ext:junit:${Versions.testExtJunitVersion}"
        const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"
        const val gson = "com.google.code.gson:gson:${Versions.gsonVersion}"
        const val converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofitConverterGson}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.supportVersion}"
        const val mockito_core = "org.mockito:mockito-core:${Versions.mockitoCoreVersion}"
        const val mockito_inline = "org.mockito:mockito-inline:${Versions.mockitoInlineVersion}"
        const val mockito_kotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlinVersion}"
        const val coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTestVersion}"
        const val mockk = "io.mockk:mockk:${Versions.mockkVersion}"
        const val arch_core_testing = "androidx.arch.core:core-testing:${Versions.archCoreTestingVersion}"
        const val rx_kotlin = "io.reactivex.rxjava3:rxkotlin:${Versions.rxKotlinVersion}"
        const val rx_java = "io.reactivex.rxjava3:rxjava:${Versions.rxJavaVersion}"
        const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata:${Versions.lifecycleVersion}"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
        const val navigation_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}"
        const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigationVersion}"
        const val lifecycle_livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}"
        const val lifecycle_runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"
        const val lifecycle_common = "androidx.lifecycle:lifecycle-common:${Versions.lifecycleVersion}"
        const val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"
        const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerviewVersion}"
        const val rx_java_android = "io.reactivex.rxjava3:rxandroid:${Versions.rxAndroidVersion}"
        const val rx_binding3 = "com.jakewharton.rxbinding4:rxbinding:${Versions.rxBinding}"
        const val glide = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
        const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glideVersion}"
        const val lottie = "com.airbnb.android:lottie:${Versions.lottieVersion}"
        const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefresh}"
        const val autodispose = "com.uber.autodispose2:autodispose:${Versions.autoDispose}"
        const val autodispose_android = "com.uber.autodispose2:autodispose-android:${Versions.autoDispose}"
        const val autodispose_android_arch = "com.uber.autodispose2:autodispose-androidx-lifecycle:${Versions.autoDispose}"
        const val leak_canary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanaryVersion}"
        const val chucker = "com.github.chuckerteam.chucker:library:${Versions.chuckerVersion}"
        const val chucker_no_op = "com.github.chuckerteam.chucker:library-no-op:${Versions.chuckerVersion}"
        const val dataStore_preferences = "androidx.datastore:datastore-preferences:${Versions.dataStorePreferenceVersion}"
        const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroidVersion}"
        const val play_services_location = "com.google.android.gms:play-services-location:${Versions.playServicesLocation}"
    }

    object Environments {
        const val debugBaseUrl = "https://api.punkapi.com/v2/"
        const val releaseBaseUrl = "https://api.punkapi.com/v2/"
    }

    fun generateVersionName(): String {
        val patch: Int = Versions.appVersionCode.rem(1000)
        val minor: Int = (Versions.appVersionCode / 1000).rem(1000)
        val major: Int = (Versions.appVersionCode / 1000000).rem(1000)

        return "$major.$minor.$patch"
    }

}