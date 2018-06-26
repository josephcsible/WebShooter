# WebShooter

## Basics

### What does this mod do?
Spiders create webs at the feet of players (or other things) that
they attack, and can sling webs from a distance at targets . This 
makes spiders more dangerous, since it's harder to run away from them.
It also makes webs a renewable resource.

### How do I use this mod?
You need Minecraft Forge installed first. Once that's done, just drop
webshooter-*version*.jar in your Minecraft instance's mods/ directory.
Optionally, you can configure it to taste (see below).

### What settings does this mod have?
You can configure the chance that each attack creates a web, from 0.0
(effectively disabling the mod), to 1.0 (every attack generates a web when
possible). The default is 0.15. Also, you can configure whether replaceable
blocks (like snow) can be overwritten with webs, and web slinging timing,
variance, and inaccuracy.

## Development

### How do I compile this mod from source?
See https://mcforge.readthedocs.io/en/latest/gettingstarted/

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
