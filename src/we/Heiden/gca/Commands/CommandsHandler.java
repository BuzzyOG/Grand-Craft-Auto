package we.Heiden.gca.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import we.Heiden.gca.Functions.Wand;
import we.Heiden.gca.Messages.Messager;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Utils.ItemUtils;
import we.Heiden.hs2.Core.HS2Api.Perms;
import we.Heiden.hs2.Messages.Chat;

public class CommandsHandler {

	public CommandsHandler(CommandSender sender, Command cmd, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("Home"))
				HomeCommand.home(p, args);
			else if (cmd.getName().equalsIgnoreCase("Trade"))
				TradeCommand.trade(p, args);
			else if (cmd.getName().equalsIgnoreCase("Store"))
				StoreCommand.store(p, args);
			else if (cmd.getName().equalsIgnoreCase("Contact"))
				ContactCommand.contact(p, args);
			else if (!Perms.hasPermission(sender, "admin"))
				p.sendMessage(Messager.e("You Can`t do that").get(0));
			else if (cmd.getName().equalsIgnoreCase("setGarageEntry"))
				Wand.give(p, "Garage Entry");
			else if (cmd.getName().equalsIgnoreCase("GarageWarp"))
				GaragewarpCommand.gw(p);
			else if (cmd.getName().equalsIgnoreCase("GarageExit"))
				Wand.give(p, "Garage Exit");
			else if (cmd.getName().equalsIgnoreCase("GarageSlot"))
				Wand.give(p, "Garage Slot", false);
			else if (cmd.getName().equalsIgnoreCase("AirportSpawn"))
				Functions.setWarp(p, "Airport", "Airport");
			else if (cmd.getName().equalsIgnoreCase("AirportArea"))
				Wand.give(p, "Airport");
			else if (cmd.getName().equalsIgnoreCase("TutoSpawn"))
				TutospawnCommand.ts(p, args);
			else if (cmd.getName().equalsIgnoreCase("setHospital"))
				Wand.give(p, "Hospital");
			else if (cmd.getName().equalsIgnoreCase("HospitalWarp"))
				Functions.setWarp(p, "Hospital", "Hospital.Warp");
			else if (cmd.getName().equalsIgnoreCase("setRespawn"))
				Functions.setWarp(p, "Respawn", "Hospital.Respawn");
			else if (cmd.getName().equalsIgnoreCase("setNpc"))
				SetnpcCommand.setNpc(p, args);
			else if (cmd.getName().equalsIgnoreCase("setHome"))
				SethomeCommand.setHome(p, args);
			else if (cmd.getName().equalsIgnoreCase("items")) {
				Player target = p;
				if (args.length > 0) target = Bukkit.getPlayer(args[0]);
				if (target == null) new Chat(p).e("Could not find " + args[0]);
				else ItemUtils.items(target);
			}
		} else
			new Chat(sender).e("Console not supported");
	}
}
