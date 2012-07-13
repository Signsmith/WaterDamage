package com.hotmail.idonno;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;



public class WorldAddCommand extends WaterDamageCommand{	
	public WorldAddCommand(WaterDamage plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(args.length != 1)
		{
			return false;
		}
				
		if(sender.hasPermission("WaterDamage.wdadd"))
		{
			if(!plugin.addWorld(args[0]))
			{
				sender.sendMessage("World not found");
			}
			else
			{
				sender.sendMessage("Players will now take water damage in: " + args[0]);
			}
		}
		return true;
	}
	
}
