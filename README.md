# DebugPrint

A Flutter-inspired, debug-only logger for Android, written in Kotlin.

`DebugPrint` gives you a familiar `debugPrint.d("hello")` API, auto-detects debug
builds, auto-tags logs with `File.fn:line`, and stays completely silent in release.

## Features

- 🎯 **Flutter-style API** — `debugPrint.d("msg")`, `dprint.e("Tag", "oops")`, `dlog.w { ... }`
- 🔇 **Silent in release** — auto-enabled in debug, auto-disabled in release
- ⚙️ **Zero config** — no `init()` call, no `Application` setup, no `BuildConfig` wiring
- 📍 **Auto tag** — `File.function:line` derived from the caller (toggleable)
- 🧵 **Lazy messages** — `debugPrint.d { "expensive $x" }` never builds the string when disabled
- 🎚️ **Log levels** — `.v .d .i .w .e`
- 🏷️ **Custom tags** — `debugPrint.d("Auth", "token expired")`
- 🔌 **Pluggable sinks** — swap Logcat for file / Crashlytics / remote
- 🛡️ **Never crashes** — logging errors are swallowed silently
- 📦 **Tiny** — pure Kotlin, one small AAR

## Requirements

- Android `minSdk 21+`
- Kotlin (via AGP 9+ built-in Kotlin)
- R8 / ProGuard recommended for release builds

## Installation

```kotlin
implementation("com.flagodna:debugprint:0.1.1")
```

## Usage

### Basic

```kotlin
import com.flagodna.debugprint.debugPrint

debugPrint.d("Hello from debugPrint!")
debugPrint.i("User signed in")
debugPrint.w("Cache miss")
debugPrint.e("Payment failed")
debugPrint.v("Verbose detail")
```

### With a custom tag

```kotlin
debugPrint.d("Auth", "token about to expire")
debugPrint.e("Payments", "charge declined")
```

### Lazy messages

Use the lambda overload for anything expensive:

```kotlin
debugPrint.d { "user = $user, size = ${bigList.size}" }
```

The string is **only built if logging is enabled**.

### Aliases

All three names point to the same object:

```kotlin
debugPrint.d("short")
dprint.d("shorter")
dlog.d("shortest")
```

### Levels

| Method | Logcat level |
| ------ | ------------ |
| `.v`   | VERBOSE      |
| `.d`   | DEBUG        |
| `.i`   | INFO         |
| `.w`   | WARN         |
| `.e`   | ERROR        |

Each level supports:

```kotlin
debugPrint.d("message")           // auto tag
debugPrint.d("Tag", "message")    // custom tag
debugPrint.d { "message" }        // lazy
```

### Manual control (optional)

```kotlin
// Force-enable logging (e.g. in a QA build)
DebugPrint.enabled = true

// Turn off auto-tagging for hot paths (faster)
DebugPrint.autoTag = false

// Change the fallback tag
DebugPrint.defaultTag = "MyApp"

// Plug in a custom output
DebugPrint.sink = MyCustomSink()
```

## Behavior

| Build type | `DebugPrint.enabled` | Output      |
| ---------- | -------------------- | ----------- |
| Debug      | `true` (auto)        | Logcat      |
| Release    | `false` (auto)       | _(nothing)_ |

The `enabled` flag is set once, automatically, before `Application.onCreate()`
runs. No setup in your `Application` class is required.

## Custom sinks

Implement `DebugPrintSink` to route logs anywhere:

```kotlin
import com.flagodna.debugprint.DebugPrintSink

class FileSink(private val file: File) : DebugPrintSink {
    override fun print(level: Int, tag: String, message: String) {
        file.appendText("[$level] $tag: $message\n")
    }
}

DebugPrint.sink = FileSink(File(context.filesDir, "app.log"))
```

A sink must never throw — `DebugPrint` swallows any exception from a sink.

## Performance notes

- **Non-lambda calls** — `debugPrint.d("msg")` is a field read + a branch when disabled.
- **Lambda calls** — `debugPrint.d { "msg" }` skips string construction entirely when disabled.
- **Auto-tag** — costs a stack walk per call. Disable for hot loops:

```kotlin
DebugPrint.autoTag = false
for (i in 1..100_000) debugPrint.d("iteration $i")  // fast, tag = defaultTag
DebugPrint.autoTag = true
```

Or pass a custom tag, which also skips the stack walk:

```kotlin
for (i in 1..100_000) debugPrint.d("Loop", "iteration $i")
```

## License

The Apache License, Version 2.0
