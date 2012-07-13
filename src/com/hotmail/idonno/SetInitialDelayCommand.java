package com.hotmail.idonno;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetInitialDelayCommand extends WaterDamageCommand {

	public SetInitialDelayCommand(WaterDamage plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(args.length != 1)
		{
			return false;
		}
		
		if(sender.hasPermission("WaterDamage.wdsetinitdelay"))
		{
			try
			{
				plugin.setInitialDelay(Integer.parseInt(args[0]) * 1000);
				sender.sendMessage("Initial damage delay now set to " + args[0] + " seconds");
			}
			catch(NumberFormatException e)
			{
				sender.sendMessage("There weren't any numbers in there you dummyhead");
			}
		}
		return true;
	}
	
	
}
