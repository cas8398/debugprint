package com.flagodna.debugprint

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ApplicationInfo
import android.database.Cursor
import android.net.Uri

/**
 * Auto-initializes [DebugPrint] before Application.onCreate() runs.
 *
 * Enables logging automatically when the app is debuggable
 * (debug builds) and leaves it disabled in release builds.
 *
 * No app code required — the manifest wires this provider in.
 */
class DebugPrintInit : ContentProvider() {

    override fun onCreate(): Boolean {
        val ctx = context ?: return false
        DebugPrint.enabled = isDebuggable(ctx)
        return true
    }

    private fun isDebuggable(ctx: Context): Boolean =
        (ctx.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    // Unused — ContentProvider requires these overrides.
    override fun query(
        uri: Uri, projection: Array<out String>?,
        selection: String?, selectionArgs: Array<out String>?, sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(
        uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?
    ): Int = 0
}