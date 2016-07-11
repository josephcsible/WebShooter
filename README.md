# WebShooter

## Basics

### What does this mod do?
It causes spiders to create webs at the feet of players (or other things) that
they attack. This makes spiders a little bit more dangerous, since it's harder
to run away from them, and it also makes webs a renewable resource.

### How do I use this mod?
You need Minecraft Forge installed first. Once that's done, just drop
webshooter-*version*.jar in your Minecraft instance's mods/ directory.
Optionally, you can configure it to taste (see below).

### What settings does this mod have?
You can configure the chance that each attack creates a web, from 0.0
(effectively disabling the mod), to 1.0 (every attack generates a web when
possible). The default is 0.15. Also, you can configure whether replaceable
blocks (like snow) can be overwritten with webs.

## Development

### How do I compile this mod from source?
You need a JDK installed first. Start a command prompt or terminal in the
directory you downloaded the source to. If you're on Windows, type
`gradlew.bat build`. Otherwise, type `./gradlew build`. Once it's done, the mod
will be saved to build/libs/webshooter-*version*.jar.

### How can I contribute to this mod's development?
Send pull requests. Note that by doing so, you agree to release your
contributions under this mod's license.

### My mod adds a new kind of spider, and I want it to shoot webs too.
Make your spider's entity a subclass of EntitySpider (see EntityCaveSpider
in vanilla for an example).

## Licensing/Permissions

### What license is this released under?
It's released under the GPL v2 or later.

### Can I use this in my modpack?
Yes, even if you monetize it with adf.ly or something, and you don't need to
ask me for my permission first.
