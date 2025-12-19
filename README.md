# Logger Documentation

A lightweight Java logging utility with console output, file logging, automatic log rotation, and compression.

---

## Features

* Console logging with ANSI colors
* File logging to a `logs/` directory
* Automatic log rotation when file size exceeds limit
* Old log compression (`.zip`)
* Simple static API (`Logger.info`, `Logger.warn`, `Logger.debug`)
* Zero external runtime dependencies

---

## 📦 Installation (JitPack)

### Gradle

```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.Lumitani-pixel:Logger:2.0.0")
}
```

### Maven

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.Lumitani-pixel</groupId>
    <artifactId>Logger</artifactId>
    <version>2.0.0</version>
  </dependency>
</dependencies>
```

---

## Quick Start

```java
import net.normalv.Logger;

public class Main {
    public static void main(String[] args) {
        Logger.info("Application started");
        Logger.debug("Loading configuration");
        Logger.warn("Low memory warning");

        Logger.close();
    }
}
```

---

## 🧾 Log Output Format

### Console output

```
[Bobby Log]:[2025-01-01 12:00:00]:[INFO]: Application started
```

* Colored output (ANSI)
* Includes prefix, timestamp, and log level

### File output (`logs/current.log`)

```
[Bobby Log]:[2025-01-01 12:00:00]:[INFO]: Application started
```

* Plain text (no ANSI codes)
* Automatically rotated when size limit is reached

---

## 📁 Log Files & Rotation

* Logs are written to the `logs/` directory
* Active log file: `current.log`
* When size exceeds **5 MB**:

    * `current.log` is renamed to `log_<timestamp>.txt`
    * A new `current.log` is created
* Maximum of **10 log files** are kept
* Older logs are automatically compressed to `.zip`

---

## Configuration

### Disable file logging

```java
Logger.setLogToFile(false);
```

This will:

* Continue logging to console
* Stop writing to files

---

## Thread Safety

* File logging is synchronized
* Safe to use from multiple threads

---

## Notes & Limitations

* Not intended as a replacement for Log4j or SLF4J
* No log levels filtering yet (all enabled)
* Timestamp format is fixed

---

## Future Improvements (Planned)

* Custom log directory
* Configurable log size & file count
* Log level filtering

---

## License

MIT License

---

## Author

Created by **Lumitani-pixel**

GitHub: [https://github.com/Lumitani-pixel](https://github.com/Lumitani-pixel)
