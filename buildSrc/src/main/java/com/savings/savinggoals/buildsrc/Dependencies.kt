package com.savings.savinggoals.buildsrc

object Dependencies {

    /*
     * Under normal simple implementation this is how I do it
     * implementation 'androidx.appcompat:appcompat:1.1.0'
     *
     * But now with this form of gradle dependency management (From here on referred to as GDM) this is how I do it
     * P.S at the point of doing this I haven't come across a faster or automated way of doing this so for now my fingers will have to toil as hard as possible
     *
     * For another example scroll down to retrofit
     * Old constraint version is 1.1.3
     */

    const val junit = "junit:junit:4.13"

    object AndroidBuildTools {
        const val version = "4.0.1"
        const val artifact = "com.android.tools.build:gradle"
        const val gradle_plugin = "$artifact:$version"
    }

    const val google_services = "com.google.gms:google-services:4.3.4"

    const val crashlytics_project = "com.google.firebase:firebase-crashlytics-gradle:2.3.0"

    // Add the Firebase Crashlytics SDK.
    const val crashlytics = "com.google.firebase:firebase-crashlytics:17.2.2"

    // Recommended: Add the Google Analytics SDK.
    const val analytics = "com.google.firebase:firebase-analytics:17.6.0"

    object AndroidX {
        const val core_ktx = "androidx.core:core-ktx:1.3.2"
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.2"
        const val legacy = "androidx.legacy:legacy-support-v4:1.0.0"

        const val ext_junit = "androidx.test.ext:junit:1.1.1"
        const val espresso = "androidx.test.espresso:espresso-core:3.2.0"

        object Lifecycle {
            private const val lifecycle_version = "2.2.0"

            const val extensions = "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
            const val compiler = "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
            const val common = "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
        }

        object Navigation {
            private const val version = "2.3.1"

            const val dynamic_features_fragment = "androidx.navigation:navigation-dynamic-features-fragment:$version"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val test = "androidx.navigation:navigation-testing:$version"
            const val safe_args_gradle_plugin = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Recycler {
            // RecyclerView
            private const val recyclerview_version = "1.2.0-alpha06"
            const val recyclerView = "androidx.recyclerview:recyclerview:$recyclerview_version"
        }
    }

    object Koin {
        private const val version = "2.1.5"
        const val core = "org.koin:koin-core:$version"
        const val android_core = "org.koin:koin-android-scope:$version"
        const val viewmodel = "org.koin:koin-androidx-viewmodel:$version"
        const val test = "org.koin:koin-core:$version"
    }

    object Kotlin {
        private const val version = "1.4.10"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"

        object Coroutines {
            private const val version = "1.3.5"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        }

        object GradlePlugin {
            const val artifact = "org.jetbrains.kotlin:kotlin-gradle-plugin"
            const val gradle_plugin = "$artifact:$version"
        }
    }

    /*
     * Before (GDM)
     * // -- Retrofit2
     * def retrofit2_version = "2.6.0"
     * implementation "com.squareup.retrofit2:retrofit:$retrofit2_version"
     * implementation "com.squareup.retrofit2:converter-gson:$retrofit2_version"
     *
     * After GDM SEE BELOW
     */

    object Moshi {
        private const val version = "1.9.3"
        const val core = "com.squareup.moshi:moshi-kotlin:$version"
        const val adapters = "com.squareup.moshi:moshi-adapters:$version"
        const val kotlin_codegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val moshi_converter = "com.squareup.retrofit2:converter-moshi:$version"

        object Interceptor {
            private const val version = "4.8.0"
            const val core = "com.squareup.okhttp3:logging-interceptor:$version"
        }
    }

    object RoomDB {
        // -- Room DB
        private const val room_version = "2.2.5"

        const val room_runtime = "androidx.room:room-runtime:$room_version"
        const val room_compiler = "androidx.room:room-compiler:$room_version"
        const val room_ktx = "androidx.room:room-ktx:$room_version"
    }

    object Material {
        private const val material_version = "1.2.0-rc01"
        const val google_material = "com.google.android.material:material:$material_version"
    }

    object Glide {
        private const val glide_version = "4.11.0"
        const val glide = "com.github.bumptech.glide:glide:$glide_version"
        const val compiler = "com.github.bumptech.glide:compiler:$glide_version"
    }

    object Timber {
        private const val version = "4.7.1"
        const val core = "com.jakewharton.timber:timber:$version"
    }

    const val Coil = "io.coil-kt:coil:0.13.0"

    const val Lottie = "com.airbnb.android:lottie:3.4.4"

    const val FlowBinding = "io.github.reactivecircus.flowbinding:flowbinding-android:0.11.1"

    const val AdmobAds = "com.google.android.gms:play-services-ads:19.4.0"

    const val KTLint = "org.jlleitschuh.gradle:ktlint-gradle:9.2.1"

    const val Jsoup = "org.jsoup:jsoup:1.12.1"

    const val JodaTime = "joda-time:joda-time:2.9.1"

    const val cicView = "de.hdodenhof:circleimageview:3.1.0"

    const val iconics = "com.mikepenz:iconics-core:4.0.0"
}
