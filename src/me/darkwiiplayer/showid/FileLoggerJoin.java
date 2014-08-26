package me.darkwiiplayer.showid;

//Makes use of SnakeYaml 1.13

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class FileLoggerJoin {
	String fileName;

	DumperOptions options = new DumperOptions();
	Yaml yaml;

	public FileLoggerJoin() {
		fileName = "logs/uuid/joins.yml"; // Sorry for the word "Joins", but
											// join events would be a bit too
											// long.
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		yaml = new Yaml(options);
	}

	public boolean logJoin(Player player) {
		GregorianCalendar calendar = new GregorianCalendar();
		String date = calendar.get(Calendar.YEAR) + "-"
				+ calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH);
		String entry = StringUtils.leftPad(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)), 2, "0") + ":"
				+ StringUtils.leftPad(String.valueOf(calendar.get(Calendar.MINUTE)), 2, "0") + ":"
				+ StringUtils.leftPad(String.valueOf(calendar.get(Calendar.SECOND)), 2, "0") + " - " + player.getUniqueId()
				+ " (" + player.getName() + ")";
		File file = new File(fileName);
		file.getParentFile().mkdirs();
		try { // Create the file of it doesn't exist yet
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			((ShowID) ShowID.getInstance()).logger.log(Level.SEVERE,
					"UUID/Join list file could not be created - Java error: "
							+ e.getMessage());
			return false;
		}
		Object input; // This is where the input data is saved

		try {
			FileInputStream IStream = new FileInputStream(file);

			input = yaml.load(IStream);
			HashMap<Object, Object> map;

			if (input instanceof HashMap) {
				map = (HashMap) input;
			} else {
				map = new HashMap();
			}

			if (map.get(date) instanceof ArrayList) {
				((ArrayList) map.get(date)).add(entry);
			} else {
				map.put(date, new ArrayList());
				((ArrayList) map.get(date)).add(entry);
			}

			IStream.close();

			FileWriter FWriter = new FileWriter(file);
			yaml.dump(map, FWriter);
			FWriter.close();
			((ShowID)ShowID.getInstance()).logger.log(Level.INFO, "Logging " + entry + " to \"" + fileName + "\"");
			return true;

		} catch (FileNotFoundException e) {
			((ShowID) ShowID.getInstance()).logger.log(
					Level.SEVERE,
					"UUID/Join list could not be opened - Java error: "
							+ e.getMessage());
			return false;
		} catch (IOException e) {
			((ShowID) ShowID.getInstance()).logger.log(Level.SEVERE,
					"Input/Output exception while saving UUID/Join list - Java error: "
							+ e.getMessage());
			return false;
		}
	}
}
