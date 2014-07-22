package me.darkwiiplayer.showid;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ShowID extends JavaPlugin implements Listener {
	
	public final Logger logger = Logger.getLogger("minecraft");
	private static ShowID instance;
	FileLoggerNames nameLogger = new FileLoggerNames();

	public static Plugin getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		instance = this;
		logger.info("ShowID has been enabled.");
	}

	@Override
	public void onDisable() {
		logger.info("ShowID has been disabled.");
	}
	
	
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (cmd.getName().equalsIgnoreCase("showid")) {
			if (sender instanceof Player) { // Command used by player
											// ==========================================================
				if (args.length > 0) { // Request other player's ID
					if (sender.hasPermission("uuid.others")) { // Check Permissions
						Player player = Bukkit.getServer().getPlayer(args[0]);
						//Gets the player from the name used

						if (player != null) { // Valid Player
							sender.sendMessage(args[0] + "'s UUID is: "
									+ player.getUniqueId());
							if (player.hasPermission("uuid.tell.player") & !sender.hasPermission("uuid.others.silent")) {
								player.sendMessage("The player " + sender.getName() + " requested your UUID.\nYour UUID is: " + player.getUniqueId());
							}
						} else { // Not a valid player
							sender.sendMessage("Player \"" + args[0] + "\" not found!");
						}
					} else {
						sender.sendMessage("You need the permission node uuid.others to do this!");
					}

				} else { // Request own ID
					if (sender.hasPermission("uuid.self")) {
						sender.sendMessage("Your ID is: " + ((Player) sender).getUniqueId());
					} else {
						sender.sendMessage("You need the permission node uuid.self to do this!");
					}
				}
			return true;
			} else { // Command used by console ================================================================================
				if (args.length > 0) {
					Player player = Bukkit.getServer().getPlayer(args[0]);
					if (player != null) {
						sender.sendMessage(args[0] + "'s UUID is: "	+ player.getUniqueId());
						if (player.hasPermission("uuid.tell.console")) {
							player.sendMessage("The console requested your UUID.\nYour UUID is: " + player.getUniqueId());
						}
					} else {
						sender.sendMessage("Player \"" + args[0] + "\" Not found! (type /list to get a list of all the players online)");
					}
				} else {
					sender.sendMessage("You are a console. You have no UUID.");
				}
			}
			return true;
		}
	return false;
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		logger.info("Hello World!");
		nameLogger.logName(event.getPlayer().getUniqueId(), event.getPlayer().getName());
	}

}
