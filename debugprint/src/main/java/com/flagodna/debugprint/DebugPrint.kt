package com.flagodna.debugprint

import android.util.Log

/**
 *
 * Usage:
 *   DebugPrint.d("hello")
 *   DebugPrint.e("Tag", "oops")
 *   DebugPrint.d { "lazy $expensive" }
 *
 * Aliases — all hit the same object:
 *   dprint.d("hello")
 *   dlog.e("oops")
 */
object DebugPrint {

    /** Set automatically by [DebugPrintInit]. If false, every call is a no-op. */
    @JvmField
    var enabled: Boolean = false

    /**
     * When true (default), a default tag is derived from the caller's
     * file / function / line via a stack walk — Flutter-style.
     *
     * Turn OFF for hot paths (stack walking is expensive).
     */
    @JvmField
    var autoTag: Boolean = true

    /** Tag used when [autoTag] is false or the caller cannot be detected. */
    @JvmField
    var defaultTag: String = "DebugPrint"

    /** Pluggable output. Defaults to Logcat. */
    @JvmField
    var sink: DebugPrintSink = LogcatSink()

    // ---------------------------------------------------------------
    // Levels — Verbose
    // ---------------------------------------------------------------
    @Suppress("NOTHING_TO_INLINE")
    inline fun v(message: String) = log(Log.VERBOSE, null) { message }
    @Suppress("NOTHING_TO_INLINE")
    inline fun v(tag: String, message: String) = log(Log.VERBOSE, tag) { message }
    inline fun v(message: () -> String) = log(Log.VERBOSE, null, message)

    // ---------------------------------------------------------------
    // Levels — Debug
    // ---------------------------------------------------------------
    @Suppress("NOTHING_TO_INLINE")
    inline fun d(message: String) = log(Log.DEBUG, null) { message }
    @Suppress("NOTHING_TO_INLINE")
    inline fun d(tag: String, message: String) = log(Log.DEBUG, tag) { message }
    inline fun d(message: () -> String) = log(Log.DEBUG, null, message)

    // ---------------------------------------------------------------
    // Levels — Info
    // ---------------------------------------------------------------
    @Suppress("NOTHING_TO_INLINE")
    inline fun i(message: String) = log(Log.INFO, null) { message }
    @Suppress("NOTHING_TO_INLINE")
    inline fun i(tag: String, message: String) = log(Log.INFO, tag) { message }
    inline fun i(message: () -> String) = log(Log.INFO, null, message)

    // ---------------------------------------------------------------
    // Levels — Warning
    // ---------------------------------------------------------------
    @Suppress("NOTHING_TO_INLINE")
    inline fun w(message: String) = log(Log.WARN, null) { message }
    @Suppress("NOTHING_TO_INLINE")
    inline fun w(tag: String, message: String) = log(Log.WARN, tag) { message }
    inline fun w(message: () -> String) = log(Log.WARN, null, message)

    // ---------------------------------------------------------------
    // Levels — Error
    // ---------------------------------------------------------------
    @Suppress("NOTHING_TO_INLINE")
    inline fun e(message: String) = log(Log.ERROR, null) { message }
    @Suppress("NOTHING_TO_INLINE")
    inline fun e(tag: String, message: String) = log(Log.ERROR, tag) { message }
    inline fun e(message: () -> String) = log(Log.ERROR, null, message)

    // ---------------------------------------------------------------
    // Internal write path
    // ---------------------------------------------------------------

    inline fun log(level: Int, tag: String?, message: () -> String) {
        if (!enabled) return
        val finalTag = tag ?: resolveTag(null)
        try {
            sink.print(level, finalTag, message())
        } catch (_: Throwable) {
            // A logger must never crash the app.
        }
    }

    fun resolveTag(userTag: String?): String {
        userTag?.let { return it }
        if (!autoTag) return defaultTag
        return try {
            val stack = Throwable().stackTrace
            // Skip frames that belong to our library, pick first external frame
            val ste = stack.firstOrNull { frame ->
                val cn = frame.className ?: ""
                !cn.startsWith("com.flagodna.debugprint") &&
                        !cn.startsWith("java.lang.Thread")
            } ?: return defaultTag
            val file = ste.fileName?.substringBeforeLast('.') ?: defaultTag
            "${file}.${ste.methodName}:${ste.lineNumber}"
        } catch (_: Throwable) {
            defaultTag
        }
    }
}

/** Short aliases — same object, zero cost. */
typealias dprint = DebugPrint
typealias dlog = DebugPrint
typealias debugPrint = DebugPrint