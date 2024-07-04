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

package com.reboot297.jardin.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.format.Formatter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.reboot297.jardin.R
import com.reboot297.jardin.core.Jardin

data class ShareItem(val name: String, val uri: Uri, var isChecked: Boolean = false)

fun Context.shareLogs() {
    val shareItems = (Jardin.getStorage(applicationContext).listFiles() ?: arrayOf())
        .map { file ->
            val name =
                "${file.name} (${Formatter.formatFileSize(applicationContext, file.length())})"
            val uri =
                FileProvider.getUriForFile(applicationContext, "$packageName.fileProvider", file)
            ShareItem(name, uri)
        }

    val names = shareItems.map { it.name }.toTypedArray()

    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder
        .setTitle(R.string.sharing_title)
        .setNegativeButton(R.string.dialog_share_cancel, null)
        .setPositiveButton(R.string.dialog_share_ok) { _, _ ->
            val uris = shareItems.filter { it.isChecked }.map { it.uri }
            shareMultipleFiles(ArrayList(uris))
        }
        .setMultiChoiceItems(names, null) { _, which, isChecked ->
            shareItems[which].isChecked = isChecked
        }

    val dialog: AlertDialog = builder.create()
    dialog.show()
}

private fun Context.shareMultipleFiles(list: ArrayList<Uri>) {
    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND_MULTIPLE
        putExtra(Intent.EXTRA_TEXT, "Jardin logs")
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, list)
        type = "text/plain"
    }
    startActivity(Intent.createChooser(shareIntent, null))
}
