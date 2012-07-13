package com.hotmail.idonno;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetConsecDelayCommand extends WaterDamageCommand {

	public SetConsecDelayCommand(WaterDamage plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(args.length != 1)
		{
			return false;
		}
		
		if(sender.hasPermission("WaterDamage.wdsetconsecdelay"))
		{
			try
			{
				plugin.setConsecutiveDelay(Integer.parseInt(args[0]) * 1000);
				sender.sendMessage("Time between consecutive damage is now " + args[0] + " seconds");
			}
			catch(NumberFormatException e)
			{
				sender.sendMessage("There weren't any numbers in there you dummyhead");
			}
		}
		return true;
	}

}
