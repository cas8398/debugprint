package com.flagodna.debugprint

/**
 * Output target for [DebugPrint].
 *
 * Implement this to send logs somewhere other than Logcat
 * (file, Crashlytics, remote server, in-memory buffer, etc.).
 *
 * A sink must NEVER throw — wrap risky work in try/catch.
 */
interface DebugPrintSink {

    /**
     * Write one log entry.
     *
     * @param level   Logcat priority — one of [android.util.Log.VERBOSE],
     *                [android.util.Log.DEBUG], [android.util.Log.INFO],
     *                [android.util.Log.WARN], [android.util.Log.ERROR].
     * @param tag     tag to associate with the message (never null)
     * @param message the fully-built message string
     */
    fun print(level: Int, tag: String, message: String)
}