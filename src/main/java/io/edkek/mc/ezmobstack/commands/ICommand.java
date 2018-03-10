package io.edkek.mc.ezmobstack.commands;

import org.bukkit.entity.Player;

public interface ICommand
{
  void execute(Player paramPlayer, String[] paramArrayOfString);
  
  String getName();
}
