# CLAUDE.md

## What this is

`weloveyou.tweaks` is the small Fabric mod behind the weloveyou pack: balance changes that a
datapack cannot make. Today that is one thing. Every item with durability (tools, armour,
modded gear) lasts twice as long, as in Raspberry Flavoured. The pack design lives in
`weloveyou.pack/docs/PROGRESSION.md`.

**Public on purpose**, unlike the other weloveyou repos: packwiz installs the jar from this
repo's GitHub releases. Nothing personal belongs here.

**One subproject per Minecraft version.** `mc-1.21.1` is the stable channel: obfuscated,
remapping Loom plugin (`fabric-loom`), Yarn names, Java 21. `mc-26.2` is for the planned edge
channel: unobfuscated, `net.fabricmc.fabric-loom`, Mojang names, Java 25. The class is a few
lines and is written twice on purpose, because the same idea is spelled differently in each.
Keep `MULTIPLIER` equal in both.

It must be installed on both sides: the server decides when an item breaks, the client draws
the bar, and default item components are not synced.

## Commands

```bash
gradle build \
  -Porg.gradle.java.installations.paths="C:/path/to/jdk-21,C:/path/to/jdk-25" \
  -Porg.gradle.java.installations.auto-download=false
# jars land in mc-*/build/libs/

git tag v0.1.1 && git push origin v0.1.1   # CI builds both and attaches them to a release
```

No Gradle wrapper is committed, because the repo rules forbid committed jars. Use Gradle
9.8.1; CI downloads it and checks its sha256.

A server logs `wly: durability x2.0 on N items` at startup when the mod is working.
