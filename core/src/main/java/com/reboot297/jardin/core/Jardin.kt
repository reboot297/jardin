/*
 * Copyright (c) 2024. Viktor Pop
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

package com.reboot297.jardin.core

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal const val TAG = "Jardin"

/**
 * Jardin logs manager.
 */
object Jardin : CoroutineScope {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        handleException(e)
    }

    override val coroutineContext: CoroutineContext =
        Dispatchers.Main.immediate + coroutineExceptionHandler

    private val logcatManager = LogcatManager()
    private fun handleException(e: Throwable) {
        e.printStackTrace()
    }

    /**
     * Start recording.
     * @param context application context
     */
    fun startRecording(context: Context) {
        Log.d(TAG, "startRecording")
        launch(Dispatchers.IO) {
            logcatManager.start(context)
        }
    }

    /**
     * Stop recording.
     */
    fun stopRecording() {
        Log.d(TAG, "stopRecording")
        launch(Dispatchers.IO) {
            logcatManager.stop()
        }
    }
}
