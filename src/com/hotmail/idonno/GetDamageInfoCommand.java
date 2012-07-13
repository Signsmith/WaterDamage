package com.hotmail.idonno;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class GetDamageInfoCommand extends WaterDamageCommand {

	public GetDamageInfoCommand(WaterDamage plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (sender.hasPermission("WaterDamage.wdgetdamage")) {
			sender.sendMessage("Water currently does "
					+ plugin.getDamageAmount() + " damage "
					+ "with an initial delay of " + plugin.getInitialDelay() / 1000
					+ " seconds and a delay between hits of "
					+ plugin.getConsecutiveDelay() / 1000 + " seconds");
		}

		return true;
	}

}
