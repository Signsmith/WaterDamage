package com.hotmail.idonno;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class WaterDamageCommand implements CommandExecutor {
	protected WaterDamage plugin;
	
	public WaterDamageCommand(WaterDamage plugin)
	{
		this.plugin = plugin;
	}
	@Override
	public abstract boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args);

}
