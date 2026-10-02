# Keep DebugPrint public API so R8 in the consuming app
# doesn't strip or rename it accidentally.
-keep class com.flagodna.debugprint.DebugPrint { *; }
-keep interface com.flagodna.debugprint.DebugPrintSink { *; }
-keep class com.flagodna.debugprint.LogcatSink { *; }
-keep class com.flagodna.debugprint.DebugPrintInit { *; }