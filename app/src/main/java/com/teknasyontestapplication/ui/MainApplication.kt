package com.teknasyontestapplication.ui

import android.app.Activity
import android.app.Application
import java.io.IOException
import java.nio.charset.Charset

class MainApplication : Application() {

    companion object {

        fun loadJSONFromAsset(activity: Activity, type: JsonFileType): String? {
            val json = try {
                val inputStream = activity.assets.open(
                    when (type) {
                        JsonFileType.LIST ->
                            "satellite_list.json"
                        JsonFileType.DETAIL ->
                            "satellite_detail.json"
                        JsonFileType.POSITION ->
                            "positions.json"
                    }
                )
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                String(buffer, Charset.forName("UTF-8"))
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }
            return json
        }
    }

    enum class JsonFileType {
        LIST,
        DETAIL,
        POSITION
    }
}