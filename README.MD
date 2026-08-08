<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.21-brightgreen" alt="Minecraft Version">
  <img src="https://img.shields.io/badge/Java-25-orange" alt="Java Version">
  <img src="https://img.shields.io/badge/Status-In%20Development-yellow" alt="Status">
  <img src="https://img.shields.io/badge/Built%20With-Minestom-blue" alt="Built With Minestom">
</p>

<h1 align="center">Vexiron</h1>

<p align="center">
  A large-scale custom Minecraft server built from scratch on
  <a href="https://minestom.net">Minestom</a>.
  <br>
  Focused on custom gameplay, performance, scalability, and long-term expansion.
</p>

<p align="center">
  <a href="https://vexiron.eu">Website</a>
</p>

---

## About

Vexiron is a custom Minecraft server written in Java and powered by [Minestom](https://minestom.net).

The project is being built from the ground up with a strong focus on:

- custom systems
- maintainable architecture
- performance under scale
- full technical control
- a polished long-term player experience

Unlike traditional Minecraft server setups, Vexiron does not rely on Bukkit, Spigot, or Paper.  
This allows every major system to be designed specifically for the server instead of being constrained by legacy APIs or plugin-based workarounds.

---

## Vision

Vexiron is intended to become a fully custom public Minecraft server with its own identity, progression, and gameplay systems.

The goal is not to extend vanilla through plugins, but to build a complete server platform with a strong technical foundation and room for long-term growth.

Core priorities:

- **Custom gameplay systems**
- **Modern, clean codebase**
- **Scalable architecture**
- **Strong performance**
- **Consistent player experience**
- **Freedom to expand without technical limitations**

---

## Current Features

The repository currently includes the base server foundation:

- **Custom MOTD**
    - branded server list response

- **Custom Tab List**
    - animated header alternating between the server IP and website

- **Simple Hub Instance**
    - generated spawn platform
    - predefined respawn point

- **Core Settings**
    - development-friendly data saving toggle

- **Player Join Configuration**
    - spawn instance assignment
    - respawn handling
    - default gamemode setup

---

## Planned Features

- **Player Islands**
- **Player Profiles**
- **Skills and Collections**
- **Leaderboards**
- **NPCs**
- **Custom Items**
- **Economy**
- **Custom GUIs**
- **Progression Systems**
- **Persistent Data Systems**

---

## Technology

| Area | Technology |
| --- | --- |
| Language | Java 25 |
| Server Framework | Minestom |
| Build Tool | Gradle |
| Logging | Logback |
| Serialization | Gson |

---

## Project Structure

```text
src/main/java/eu/vexiron/
├── Main.java
├── listener/
│   ├── ListenerProvider.java
│   └── PlayerListener.java
├── server/
│   ├── Motd.java
│   ├── ServerConfigurer.java
│   ├── Settings.java
│   └── TabList.java
└── world/
    ├── HubInstance.java
    └── InstanceProvider.java