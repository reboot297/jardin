/*
 * Copyright (c) 2023. Viktor Pop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reboot297.jardin.info

import android.app.GrammaticalInflectionManager
import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.GRAMMATICAL_GENDER_FEMININE
import android.content.res.Configuration.GRAMMATICAL_GENDER_MASCULINE
import android.content.res.Configuration.GRAMMATICAL_GENDER_NEUTRAL
import android.content.res.Configuration.GRAMMATICAL_GENDER_NOT_SPECIFIED
import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject

/**
 * Get info about locales.
 */
internal class LocalesInfo internal constructor(
    private val context: Context,
    json: JSONObject,
) : Base(context) {

    private val localeManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(Context.LOCALE_SERVICE) as LocaleManager
    } else {
        null
    }

    private val grammaticalInflectionManager =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            context.getSystemService(GrammaticalInflectionManager::class.java)
        } else {
            null
        }

    private val root = json.getOrCreate("info-locales")

    /**
     * List of locales available in the application.
     *
     * Example output:
     * ```
     * {
     *   "info-locales": {
     *     "localeManager": {
     *       "applicationLocales": "[es_ES]"
     *     }
     *   }
     * }
     * ```
     * @see [LocaleManager.getApplicationLocales]
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    internal fun application() {
        // TODO(Viktor) check if works
        root.getOrCreate("localeManager")
            .put("applicationLocales", localeManager?.applicationLocales)
    }

    /**
     * Grammatical gender.
     *
     * Example output:
     * ```
     * {
     *   "info-locales": {
     *     "grammaticalInflectionManager": {
     *       "applicationGrammaticalGender": "0 (not specified)"
     *     },
     *     "configuration": {
     *       "grammaticalGender": "0 (not specified)"
     *     }
     *   }
     * }
     * ```
     * @see [Configuration.getGrammaticalGender]
     * @see [GrammaticalInflectionManager.getApplicationGrammaticalGender]
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    internal fun grammaticalGender() {
        // TODO(Viktor) check if works
        val applicationGrammaticalGender =
            grammaticalInflectionManager?.applicationGrammaticalGender
        root.getOrCreate("grammaticalInflectionManager")
            .put(
                "applicationGrammaticalGender",
                "$applicationGrammaticalGender ${
                    grammaticalGenderDetails(applicationGrammaticalGender)
                }",
            )
        val grammaticalGender = context.resources.configuration.grammaticalGender
        root.getOrCreate("configuration")
            .put(
                "grammaticalGender",
                "$grammaticalGender ${grammaticalGenderDetails(grammaticalGender)}",
            )
    }

    /**
     * System locals.
     *
     * Example output:
     * ```
     * {
     *   "info-locales": {
     *     "configuration": {
     *       "locale": "es_ES",
     *       "locales": "[es_ES,en_US]"
     *     },
     *     "localeManager": {
     *       "systemLocales": "[es_ES,en_US]"
     *     }
     *   }
     * }
     * ```
     * @see [Configuration.getLocales]
     * @see [Configuration.locale]
     * @see [LocaleManager.getSystemLocales]
     */
    internal fun system() {
        @Suppress("DEPRECATION")
        root.getOrCreate("configuration")
            .put("locale", context.resources.configuration.locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            root.getOrCreate("configuration")
                .put("locales", context.resources.configuration.locales)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                root.getOrCreate("localeManager")
                    .put("systemLocales", localeManager?.systemLocales)
            }
        }
    }

    /**
     * Get All information related to locales.
     *
     * Example output for API 34:
     * ```
     * {
     *   "info-locales": {
     *     "configuration": {
     *       "locale": "es_ES",
     *       "locales": "[es_ES,en_US]",
     *       "grammaticalGender": "0 (not specified)"
     *     },
     *     "localeManager": {
     *       "systemLocales": "[es_ES,en_US]",
     *       "applicationLocales": "[]"
     *     },
     *     "grammaticalInflectionManager": {
     *       "applicationGrammaticalGender": "0 (not specified)"
     *     }
     *   }
     * }
     * ```
     * @see [Configuration.getLocales]
     * @see [Configuration.locale]
     * @see [LocaleManager.getApplicationLocales]
     * @see [LocaleManager.getSystemLocales]
     * @see [Configuration.getGrammaticalGender]
     * @see [GrammaticalInflectionManager.getApplicationGrammaticalGender]
     */
    internal fun full() {
        system()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            application()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                grammaticalGender()
            }
        }
    }

    private fun grammaticalGenderDetails(value: Int?): String {
        return when (value) {
            GRAMMATICAL_GENDER_NOT_SPECIFIED -> "(not specified)"
            GRAMMATICAL_GENDER_NEUTRAL -> "(neutral)"
            GRAMMATICAL_GENDER_FEMININE -> "(feminine)"
            GRAMMATICAL_GENDER_MASCULINE -> "(masculine)"
            else -> ""
        }
    }
}
