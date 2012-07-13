package com.hotmail.idonno;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WorldListCommand extends WaterDamageCommand {
	public WorldListCommand(WaterDamage plugin)
	{
		super(plugin);
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		sender.sendMessage(plugin.listEffectedWorlds());
		
		return true;
	}

}
