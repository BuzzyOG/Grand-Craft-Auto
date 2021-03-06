package we.Heiden.gca.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import we.Heiden.gca.Messages.Messager;
import we.Heiden.hs2.Messages.Chat;

public class TradeCommand {

	public static HashMap<Player, Player> pending = new HashMap<Player, Player>();
	public static HashMap<Player, Integer> remover = new HashMap<Player, Integer>();
	public static HashMap<Player, Integer> cooldown = new HashMap<Player, Integer>();
	public static HashMap<Player, Integer> delay = new HashMap<Player, Integer>();
	public static List<Player> denied = new ArrayList<Player>();
	public static HashMap<Player, Player> finished = new HashMap<Player, Player>();

	public static void trade(Player p, String[] args) {
		Messager.load(p);
		if (cooldown.containsKey(p))
			Messager.e1("You must wait " + cooldown.get(p) + " second(s) more");
		else if (denied.contains(p))
			Messager.e1("You are already under a trade");
		else if (args.length < 1)
			Messager.e1("Specify a Target");
		else {
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null)
				Messager.e1("Could not find " + args[0]);
			else if (target.equals(p))
				Messager.e1("You can`t trade yourself");
			else {
				if (!pending.containsKey(p)) {
					pending.put(target, p);
					remover.put(target, 10);
					new Chat(target).msg(" &d&l►► &a" + p.getName()
							+ " has invited you to a trade",
							"  &d&l►► &6&o/trade " + p.getName()
									+ " &bto accept it");
					Messager.s1("Invitation Sent");
				} else {
					remover.remove(p);
					Player sender = pending.get(p);
					pending.put(sender, p);
					new Chat(sender).msg("&eYour trade with " + p.getName()
							+ " will start on &b5 seconds");
					new Chat(p).msg("&eYour trade with " + sender.getName()
							+ " will start on &b5 seconds");
					delay.put(p, 5);
				}
				denied.add(p);
			}
		}
	}
}
