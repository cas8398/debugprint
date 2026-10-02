package com.flagodna.debugprint

import android.util.Log

/**
 * Default [DebugPrintSink] — writes to Android's Logcat via [android.util.Log].
 * The level comes from the caller (d/i/w/e/v), so each call keeps its priority.
 */
class LogcatSink : DebugPrintSink {

    override fun print(level: Int, tag: String, message: String) {
        Log.println(level, tag, message)
    }
}