package me.darkwiiplayer.showid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
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
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class ShowID extends JavaPlugin implements Listener {

	public final Logger logger = Logger.getLogger("minecraft");
	private static ShowID instance;
	FileLoggerNames nameLogger = new FileLoggerNames();

	public static Plugin getInstance() {
		return instance;
	}

	// === LOGGING OPTIONS ===

	private Boolean logNames = true;

	public boolean setNameLogging() {
		logNames = !logNames;
		if (logNames) {
			logger.info("[SHOWID] Join Logging has been turned on");
		} else {
			logger.info("[SHOWID] Join Logging has been turned off");
		}
		return logNames;
	}

	public boolean setNameLogging(boolean x) {
		logNames = x;
		if (logNames) {
			logger.info("[SHOWID] Join Logging has been turned on");
		} else {
			logger.info("[SHOWID] Join Logging has been turned off");
		}
		return logNames;
	}

	private Boolean logJoins = true;

	public boolean setJoinLogging() {
		logJoins = !logJoins;
		if (logJoins) {
			logger.info("[SHOWID] Join Logging has been turned on");
		} else {
			logger.info("[SHOWID] Join Logging has been turned off");
		}
		return logJoins;
	}

	public boolean setJoinLogging(boolean x) {
		logJoins = x;
		if (logJoins) {
			logger.info("[SHOWID] Join Logging has been turned on");
		} else {
			logger.info("[SHOWID] Join Logging has been turned off");
		}
		return logJoins;
	}

	// === END LOGGING OPTIONS ===

	@Override
	public void onEnable() {
		// Loading Options:
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(options);

		File file = new File("plugins/showid.yml"); // YAML file where every
													// UUID and all of his
													// usernames are stored
		FileInputStream IStream;
		Object input;

		if (!file.exists()) { // Create the file if it doesn't exist, exit with
								// FALSE if it doesn't work (file does not exist
								// and cannot be created)
			try {
				IStream = new FileInputStream(file);

				input = yaml.load(IStream);

				if (input instanceof HashMap) {
					if (((HashMap) input).get("logNames").equals(false)) {
						logNames = false;
					}
					if (((HashMap) input).get("logJoins").equals(false)) {
						logJoins = false;
					}
				}

				IStream.close();
			} catch (FileNotFoundException e) {
				logger.info("No config file found, loading default values.");
				// Not actually loading any dafault values, since the variables
				// are already set to their defaults.
			} catch (IOException e) {
				logger.info("Error loading config file! Java error: "
						+ e.getMessage());
			}
		}

		getServer().getPluginManager().registerEvents(this, this);
		instance = this;
		logger.info("ShowID has been enabled.");
	}

	@Override
	public void onDisable() {
		String fileName;
		File file; // YAML file where every UUID and all of his usernames are
					// stored
		FileWriter FWriter;

		DumperOptions options = new DumperOptions();
		Yaml yaml;

		logger.info("ShowID has been disabled.");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (cmd.getName().equalsIgnoreCase("showid")) {
			if (sender instanceof Player) { // Command used by player
				if (args.length > 0) { // Request other player's ID
					if (sender.hasPermission("uuid.others")) { // Check
																// Permissions
						Player player = Bukkit.getServer().getPlayer(args[0]);
						// Gets the player from the name used

						if (player != null) { // Valid Player
							sender.sendMessage("[SHOWID] " + args[0]
									+ "'s UUID is: " + player.getUniqueId());
							if (player.hasPermission("uuid.tell.player")
									& !sender
											.hasPermission("uuid.others.silent")) {
								player.sendMessage("[SHOWID] The player "
										+ sender.getName()
										+ " requested your UUID.\nYour UUID is: "
										+ player.getUniqueId());
							}
						} else { // Not a valid player
							sender.sendMessage("[SHOWID] Player \"" + args[0]
									+ "\" not found!");
						}
					} else {
						sender.sendMessage("[SHOWID] You need the permission node uuid.others to do this!");
					}

				} else { // Request own ID
					if (sender.hasPermission("uuid.self")) {
						sender.sendMessage("[SHOWID] Your ID is: "
								+ ((Player) sender).getUniqueId());
					} else {
						sender.sendMessage("[SHOWID] You need the permission node uuid.self to do this!");
					}
				}
				return true;
			} else { // Command used by console
				if (args.length > 0) {
					Player player = Bukkit.getServer().getPlayer(args[0]);
					if (player != null) {
						sender.sendMessage(args[0] + "'s UUID is: "
								+ player.getUniqueId());
						if (player.hasPermission("uuid.tell.console")) {
							player.sendMessage("[SHOWID] The console requested your UUID.\nYour UUID is: "
									+ player.getUniqueId());
						}
					} else {
						sender.sendMessage("[SHOWID] Player \""
								+ args[0]
								+ "\" Not found! (type /list to get a list of all the players online)");
					}
				} else {
					sender.sendMessage("[SHOWID] You are a console. You have no UUID.");
				}
			}
			return true;
		}
		
		//=== LOGNAMES ===

		if (cmd.getName().equalsIgnoreCase("lognames")) {
			if (sender instanceof Player) { // Check if called by player
				Player player = (Player) sender;
				if (player.hasPermission("uuid.options")) { // Has the required permission
					if (args.length > 0) {
						if (args[0].equalsIgnoreCase("on")) {
							setNameLogging(true);
							sender.sendMessage("[SHOWID] Name logging has been turned on");
							return true;
						} 
						if (args[0].equalsIgnoreCase("off")) {
							setNameLogging(false);
							sender.sendMessage("[SHOWID] Name logging has been turned off");
							return true;
						}
						
						// Invalid argument
						sender.sendMessage("[SHOWID] invalid command syntax");
						sender.sendMessage("[SHOWID] loggnames [on|off]");
						return true;
					} else { // Called with no arguments
						if (setNameLogging()) {
							sender.sendMessage("[SHOWID] Name logging has been turned on");
						} else {
							sender.sendMessage("[SHOWID] Name logging has been turned off");
						}
						return true;
					}
				} else { // Doesn't have the required permission
					player.sendMessage("[SHOWID] You don't have the permission to do that!");
				}
			} else { // Called by console
				if (args.length > 0) { // The actual command interpreting
					if (args[0].equalsIgnoreCase("on")) {
						setNameLogging(true);
						return true;
					}
					if (args[0].equalsIgnoreCase("off")) {
						setNameLogging(false);
						return true;
					}
					// Invalid argument
					logger.info("[SHOWID] lognames [on|off]");
				} else { // Called with no arguments
					setNameLogging();
					return true;
				}
			}
		}

		//=== LOGJOINS ===
		
		if (cmd.getName().equalsIgnoreCase("logjoins")) {
			if (sender instanceof Player) { // Check if called by player
				Player player = (Player) sender;
				if (player.hasPermission("uuid.options")) { // Has the required
															// permission
					if (args.length > 0) {
						if (args[0].equalsIgnoreCase("on")) {
							setJoinLogging(true);
							sender.sendMessage("[SHOWID] Join logging has been turned on");
							return true;
						}
						if (args[0].equalsIgnoreCase("off")) {
							setJoinLogging(false);
							sender.sendMessage("[SHOWID] Join logging has been turned off");
							return true;
						} 
						// Invalid argument
						sender.sendMessage("[SHOWID] invalid command syntax");
						sender.sendMessage("[SHOWID] loggjoins [on|off]");
						return true;
					} else {
						// Called with no arguments
						if (setJoinLogging()) {
							sender.sendMessage("[SHOWID] Join logging has been turned on");
						} else {
							sender.sendMessage("[SHOWID] Join logging has been turned off");
						}
						return true;
					}
				} else {
					// Doesn't have the required permission
					player.sendMessage("[SHOWID] You don't have the permission to do that!"); return true;
				}
				
			} else { // Called by console
				if (args.length > 0) { // The actual command interpreting
					if (args[0].equalsIgnoreCase("on")) {
						setJoinLogging(false);
						return true;
					}
					if (args[0].equalsIgnoreCase("off")) {
						setJoinLogging(false);
						return true;
					}
					// Invalid argument
					logger.info("[SHOWID] logjoins [on|off]");
					
				} else { // Called with no arguments
					logJoins = !logJoins;
					return true;
				}
			}
		}

		return false;
	}

	// ======== This is where the logging happenes:
	// =======================================================================

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		if (logNames) {
			nameLogger.logName(event.getPlayer().getUniqueId(), event
					.getPlayer().getName());
		}
	}

}
