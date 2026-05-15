package com.example.nimmaguru

import android.app.Activity
import java.util.Locale

fun setLocale(activity: Activity, languageCode: String) {

    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val resources = activity.resources
    val config = resources.configuration

    config.setLocale(locale)

    resources.updateConfiguration(config, resources.displayMetrics)

    activity.recreate()
}