plugins {
    id("com.android.application")
}

android {
    namespace = "com.krispy.mobilelegacy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.krispy.mobilelegacy"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")

            resValue("string", "app_name", "Mobile Legacy")
            manifestPlaceholders["des"] = "Mobile Legacy (OpenGL 4.0, 1.12.2+)"
            manifestPlaceholders["renderer"] = "Mobile Legacy:libmobileglues.so:libmobileglues.so"
            manifestPlaceholders["minMCVer"] = "1.12.2"
            manifestPlaceholders["maxMCVer"] = ""
            manifestPlaceholders["pojavEnv"] =
                "LIBGL_ES=3:POJAV_RENDERER=opengles3:POJAVEXEC_EGL=libmobileglues.so:LIBGL_EGL=libmobileglues.so"
        }
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
