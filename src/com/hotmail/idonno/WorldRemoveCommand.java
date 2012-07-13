package com.hotmail.idonno;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WorldRemoveCommand extends WaterDamageCommand {
	public WorldRemoveCommand(WaterDamage plugin)
	{
		super(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(args.length != 1)
		{
			return false;
		}
		
		if(sender.hasPermission("WaterDamage.wdremove"))
		{
			if(!plugin.removeWorld(args[0]))
			{
				sender.sendMessage("World not found");
			}
			else
			{
				sender.sendMessage("Players will no longer take water damage in: " + args[0]);
			}
		}
		return true;
	}

}
