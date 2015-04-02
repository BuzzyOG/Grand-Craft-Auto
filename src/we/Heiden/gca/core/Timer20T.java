package we.Heiden.gca.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import we.Heiden.gca.Commands.SetnpcCommand;
import we.Heiden.gca.Commands.TradeCommand;
import we.Heiden.gca.Configs.Config;
import we.Heiden.gca.Events.EntityExplode;
import we.Heiden.gca.Functions.Garage;
import we.Heiden.gca.Functions.Tutorial;
import we.Heiden.gca.NPCs.CustomVillager;
import we.Heiden.gca.Utils.Functions;
import we.Heiden.gca.Weapons.WeaponHandler;
import we.Heiden.gca.Weapons.WeaponUtils;
import we.Heiden.gca.Weapons.Weapons;
import we.Heiden.hs2.Messages.ActionBar;
import we.Heiden.hs2.Messages.Chat;

public class Timer20T extends BukkitRunnable {

	public static HashMap<Player, String> actionBar = new HashMap<Player, String>();
	public static HashMap<Player, List<Object>> recharging = new HashMap<Player, List<Object>>();
	public static HashMap<Player, List<Object>> toRemove = new HashMap<Player, List<Object>>();
	public static int reload = 0;
	
	@SuppressWarnings("deprecation")
	public void run() {
		if (!actionBar.isEmpty()) for (Player p : actionBar.keySet()) new ActionBar(p).msg(actionBar.get(p));
		if (!recharging.isEmpty()) try {
			for (Player p : recharging.keySet()) {
				int time = (int) recharging.get(p).get(1);
				time--;
				if (time > 0) {
					recharging.get(p).set(1, time);
					Weapons wep = (Weapons) recharging.get(p).get(0);
					String msg = "&bRecharging &d►► &a&l";
					boolean bol = true;
					double percentage = ((double) wep.rechargeDelay-time) / ((double) wep.rechargeDelay) * 100D;
					for (int n = 1; n <= 10; n++) {
						if (percentage < n*10 && bol) {
							msg += "&c&l";
							bol = false;
						}
						msg += "•";
					}
					msg += " &d◄◄";
					new ActionBar(p).msg(msg);
					p.setLevel(time);
				} else {
					Weapons wep = (Weapons) recharging.get(p).get(0);
					recharging.remove(p);
					WeaponUtils.recharge(p, wep);
					new ActionBar(p).msg("&a&lRecharged!");
					p.setLevel(0);
					ItemStack i = p.getItemInHand();
					ItemMeta im = i.getItemMeta();
					im.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&aCharge: &b" + wep.getCharge(p) + "&9/&c" + wep.shootCapacity)));
					i.setItemMeta(im);
					p.setItemInHand(i);
				}
			}
		} catch(Exception ex) { }
		
		if (reload != 0) {
			reload--;
			if (reload > 0) for (Player p : Bukkit.getOnlinePlayers()) new ActionBar(p).msg("&f&l•• &b&lReload &dincoming&9: &2" + reload + " &f&l••");
			else {
				Bukkit.reload();
				for (Player p : Bukkit.getOnlinePlayers()) new ActionBar(p).msg("&f&l•• &a&lReload Complete &f&l••");
			}
		}
		
		if (!toRemove.isEmpty())
		for (Player p : toRemove.keySet()) {
			int time = (int) toRemove.get(p).get(1);
			time--;
			if (time > 0) toRemove.get(p).set(1, time);
			else {
				Entity ent = (Entity) toRemove.get(p).get(0);
				toRemove.remove(p);
				Garage.remove(p, ent);
			}
		}
		
		if (!Tutorial.tuto.isEmpty()) 
			for (Player p : Tutorial.tuto.keySet()) {
				int time = Tutorial.tuto.get(p);
				Tutorial.tutorial(p, time);
				time++;
				if (Tutorial.tuto.containsKey(p)) Tutorial.tuto.put(p, time);
			}
		if (!WeaponHandler.grenades.isEmpty())
			for (Item item : WeaponHandler.grenades.keySet()) {
				int time = WeaponHandler.grenades.get(item);
				time--;
				if (time > 0) WeaponHandler.grenades.put(item, time);
				else {
					WeaponHandler.grenades.remove(item);
					WeaponUtils.explode(item);
				}
			}
		if (!EntityExplode.restore.isEmpty())
			for (Entity e : EntityExplode.restore.keySet()) {
				int time = EntityExplode.restore.get(e);
				time--;
				if (time > 0) EntityExplode.restore.put(e, time);
				else {
					if (Config.get().contains("Temp." + e.getUniqueId())) for (String s : Config.get().getConfigurationSection("Temp." + e.getUniqueId()).getKeys(false)) {
						Location loc = Functions.loadLoc("Temp." + e.getUniqueId() + "." + s + ".Location", Config.get());
						loc.getBlock().setType(Material.matchMaterial(Config.get().getString("Temp." + e.getUniqueId() + "." + s + ".Type")));
						loc.getBlock().setData((byte) Config.get().getInt("Temp." + e.getUniqueId() + "." + s + ".Data"));
					}
					Config.get().set("Temp." + e.getUniqueId(), null);
					Config.save();
					EntityExplode.restore.remove(e);
				}
			}
		if (!SetnpcCommand.respawn.isEmpty())
			for (String path : SetnpcCommand.respawn.keySet()) {
				int time = SetnpcCommand.respawn.get(path);
				time--;
				if (time > 0) SetnpcCommand.respawn.put(path, time);
				else {
					SetnpcCommand.villagers.put(CustomVillager.spawn(Functions.loadLoc(path, Config.get()), "&a&lCivilian"), path);
					SetnpcCommand.respawn.remove(path);
				}
			}
		if (!TradeCommand.remover.isEmpty())
			for (Player target : TradeCommand.remover.keySet()) {
				int time = TradeCommand.remover.get(target);
				time--;
				if (time > 0) TradeCommand.remover.put(target, time);
				else {
					TradeCommand.remover.remove(target);
					Player p = TradeCommand.pending.get(target);
					TradeCommand.pending.remove(target);
					new Chat(p).e("Your trade Invitation has been cancelled");
					TradeCommand.cooldown.put(p, 15);
				}
			}
	}
}
