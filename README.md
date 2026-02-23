# Jonion – A layered approach to Java plugins
[![Maven Central](https://img.shields.io/maven-central/v/dev.siea.jonion/Jonion?label=Maven%20Central)](https://central.sonatype.com/artifact/dev.siea.jonion/Jonion)
[![Documentation](https://img.shields.io/badge/docs-available-brightgreen)](https://docs.siea.dev/jonion/)

Jonion is a lightweight, flexible plugin framework for Java applications.
It provides a clean, modular architecture that makes it easy to build extensible systems by “layering” functionality through plugins.

## ✨ Features
- Lightweight & Simple — a clean, minimal API for easy integration.
- Per-Module Configurations — automatic configuration handling for each plugin.
- Descriptor System — built-in support for module metadata and custom descriptor templates.
- Multi-format Support — YAML and XML out of the box, with adapters for any other format.
- Extensible — customize loaders, lifecycles, and file formats with ease.
…and more. Designed to grow with your application's needs.

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven

## 📦 Installation
To include Jonion in your project, add the following dependency and repository to your ``pom.xml``:

### Dependency
```xml
<dependency>
    <groupId>dev.siea.jonion</groupId>
    <artifactId>Jonion</artifactId>
    <version>${jonionversion}</version>
</dependency>
```

Replace ```${jonionversion}``` with the latest available version.

## Contributing
We welcome contributions! To contribute to Jonion:
1. Fork the repository: [Jonion on GitHub](https://github.com/sieadev/jonion)
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -m 'Add feature'`
4. Push to the branch: `git push origin feature-name`
5. Submit a pull request.

## 🧅 Why “Jonion”?
Because good apps, like onions, have **layers**. Java software benefits from modularity — Jonion makes that possible.

(And because naming things is hard.)
