# AC Test Client (Fabric 1.21.1)

Build: install Gradle 8.8+ and JDK 21, run `gradle wrapper` then `./gradlew build`.
Jar lands in build/libs/actest-1.0.0.jar. Drop it in .minecraft/mods with Fabric API.

Commands (client-side, in chat):
  /actest list
  /actest toggle <module>
  /actest set <module> <setting> <value>

Examples:
  /actest set speed multiplier 1.15
  /actest toggle speed
  /actest set killaura range 3.6
  /actest set killaura jitter 40
  /actest toggle killaura

Modules: Speed, Fly, NoFall, KillAura, Sprint
Use only on servers you own/administer.
