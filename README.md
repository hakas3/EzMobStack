# EzMobStack
Speed up your server by stacking hundreds of mobs into one!

*Small lightweight plugin!*

Easily stack mobs and speed up your server! Simply install the plugin and mobs will begin stacking.

# How it works
When a mob spawns, if it near another mob of the same type (this radius is configurable) then the mobs are combined into one mob. 

![img](https://proxy.spigotmc.org/d9b78de71d1887eee076ee7245606597869be5ba?url=https%3A%2F%2Fi.imgur.com%2FPAmB3As.jpg)

When you kill a mob, the stack number is decreased and the same mob remains. The mob killed drops what is normally dropped. 

# Commands
This plugin comes with a simple command to edit the config file in-game

* /mobstack <radius> - Edit the radius mobs must be within in-order to be considered for stack. (i.e /mobstack 4 - Will stack mobs of same type within 4 blocks of eachother)
* /mobstack save - Save the plugin cache

# Permissions
The permissions for this plugin is mainly for the build-in command

ezmobstack.edit - Use the /mobstack command
