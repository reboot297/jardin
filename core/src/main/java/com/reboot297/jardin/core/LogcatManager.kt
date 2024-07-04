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
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader

internal const val FILES_DIR = "jardin/log-files"

/**
 * Manager for collecting logs from logcat.
 */
internal class LogcatManager {
    private var recording = false

    /**
     * Start log recording.
     */
    internal fun start(context: Context) {
        if (!recording) {
            recording = true
            executeLogcat(context)
        } else {
            stop()
        }
    }

    /**
     * Stop log recording.
     */
    internal fun stop() {
        recording = false
    }

    /**
     * Write logs to the file.
     * @param context applicationContext
     * @param args logcat argument
     */
    private fun executeLogcat(context: Context, args: String = "") {
        val file = File(getFilesDir(context), "log-${System.currentTimeMillis()}.txt")
        var fileWriter: FileWriter? = null
        var bufferedWriter: BufferedWriter? = null
        try {
            fileWriter = FileWriter(file)
            bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.appendLine("=============JARDIN START=============")
            Log.d(TAG, "executeLogcat: ")
            Runtime.getRuntime().exec("logcat -c")
            val process = Runtime.getRuntime().exec("logcat $args")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            var logValue: String?
            do {
                logValue = bufferedReader.readLine()
                bufferedWriter.appendLine(logValue)
            } while (logValue != null && recording)

            bufferedWriter.appendLine("=============JARDIN STOP=============")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bufferedWriter?.close()
                fileWriter?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Get directory with log files.
     */
    internal fun getFilesDir(context: Context): File {
        val dir = File(context.filesDir, FILES_DIR)
        dir.mkdirs()
        return dir
    }

    /**
     * Clear log files.
     */
    internal fun clearFilesDir(context: Context) {
        getFilesDir(context).listFiles()?.forEach { file ->
            file.delete()
        }
    }
}
