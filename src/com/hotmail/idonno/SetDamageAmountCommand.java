package com.hotmail.idonno;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetDamageAmountCommand extends WaterDamageCommand {

	
	public SetDamageAmountCommand(WaterDamage plugin)
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
		
		if(sender.hasPermission("WaterDamage.wdsetdamage"))
		{
			try
			{
				plugin.setDamageAmount(Integer.parseInt(args[0]));
				sender.sendMessage("Water will now do " + args[0] + " damage.");
			}
			catch(NumberFormatException e)
			{
				sender.sendMessage("There weren't any numbers in there you dummyhead");
			}
		}
		
		
		return true;
	}
}
