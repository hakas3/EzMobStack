package io.edkek.mc.ezmobstack.commands;

import io.edkek.mc.ezmobstack.EzMobStack;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class ConfigEdit implements ICommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.isOp() && !player.hasPermission("ezmobstack.edit")) {
            player.sendMessage("You don't have permission to use this command!");
            return;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.AQUA + "Usage: /mobstack <radius> - Set search radius when a mob spawns");
            player.sendMessage(ChatColor.AQUA + "Usage: /mobstack save - Save the plugin cache");
        } else {
            try {
                int radius = Integer.parseInt(args[0]);

                EzMobStack.INSTANCE.getMobListener().setStackRadius(radius);
                EzMobStack.INSTANCE.saveCache();
                player.sendMessage(ChatColor.AQUA + "Set stack radius to " + radius + " blocks!");
            } catch (Throwable t) {
                if (args[0].equalsIgnoreCase("save")) {
                    EzMobStack.INSTANCE.saveCache();
                    player.sendMessage(ChatColor.AQUA + "Saved cache!");
                } else {
                    player.sendMessage(ChatColor.AQUA + "Usage: /mobstack <radius> - Set search radius when a mob spawns");
                    player.sendMessage(ChatColor.AQUA + "Usage: /mobstack save - Save the plugin cache");
                }
            }
        }
    }

    @Override
    public String getName() {
        return "mobstack";
    }
}
