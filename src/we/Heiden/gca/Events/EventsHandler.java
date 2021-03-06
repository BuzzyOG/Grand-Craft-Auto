package we.Heiden.gca.Events;

import org.bukkit.plugin.Plugin;

import we.Heiden.gca.Functions.Wand;
import we.Heiden.gca.Weapons.WeaponHandler;

/**
 * *********************************************
 * <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 * <b>All rights reserved
 * <p>
 * Heiden Team 2015
 * <p>
 * </b> *********************************************
 **/
public class EventsHandler {

	public EventsHandler(Plugin pl) {
		new PlayerJoin(pl);
		new InventoryClick(pl);
		new PlayerDropItem(pl);
		new PlayerInteract(pl);
		new PlayerQuit(pl);
		new InventoryClose(pl);
		new PlayerCommandPreproccess(pl);
		new VehicleMove(pl);
		new PlayerInteractEntity(pl);
		new VehicleExit(pl);
		new VehicleBlockCollision(pl);
		new Wand(pl);
		new PlayerMove(pl);
		new NPCInteract(pl);
		new AsyncPlayerChat(pl);
		new WeaponHandler(pl);
		new PlayerToggleSneak(pl);
		new PlayerDamage(pl);
		new ProjectileHit(pl);
		new EntityExplode(pl);
		new PlayerRespawn(pl);
		new EntityDeath(pl);
		new PlayerPickupItem(pl);
		new EntityDamageByEntity(pl);
	}
}
