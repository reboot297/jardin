# Jardin
Android library for collecting logs.

## Core library [![Maven Central](https://img.shields.io/maven-central/v/io.github.reboot297/jardin-core?label=MavenCentral&logo=apache-maven)](https://search.maven.org/artifact/io.github.reboot297/jardin-core)

## Info library [![Maven Central](https://img.shields.io/maven-central/v/io.github.reboot297/jardin-info?label=MavenCentral&logo=apache-maven)](https://search.maven.org/artifact/io.github.reboot297/jardin-info)

## Download

The libraries are available on `mavenCentral()`.

```kotlin
// core:
implementation("io.github.reboot297:jardin-core:0.1.0")

// info:
implementation("io.github.reboot297:jardin-info:0.2.0")
```
The sample of using "Info" library
```
Info(applicationContext)
    .batteryStatus()
    .locales()
    .print()
```