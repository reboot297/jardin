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

package com.reboot297.jardin

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.reboot297.jardin.info.Info
import com.reboot297.jardin.info.InfoAdapter
import com.reboot297.jardin.info.Item

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = InfoAdapter(data)
        val recyclerView = findViewById<RecyclerView>(R.id.itemsListView)
        recyclerView.adapter = adapter
    }

    private val data = arrayOf(
        Item("Battery Full") { Info(applicationContext).battery().print() },
        Item("Battery Characteristics") {
            Info(applicationContext).batteryCharacteristics().print()
        },
        Item("Battery Energy") { Info(applicationContext).batteryEnergy().print() },
        Item("Battery Status") { Info(applicationContext).batteryStatus().print() },
        Item("") { },

        Item("Locales Full") { Info(applicationContext).locales().print() },
        Item("Locales Application") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Info(applicationContext).localesApplication().print()
            } else {
                Toast.makeText(this, "Available since API 33", Toast.LENGTH_LONG).show()
            }
        },
        Item("Locales GrammaticalGender") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                Info(applicationContext).localesGrammaticalGender().print()
            } else {
                Toast.makeText(this, "Available since API 34", Toast.LENGTH_LONG).show()
            }
        },
        Item("Locales System") { Info(applicationContext).localesSystem().print() },
        Item("") { },
    )
}
