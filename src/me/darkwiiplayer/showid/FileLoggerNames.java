package me.darkwiiplayer.showid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.UUID;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class FileLoggerNames {
	String fileName;
	File file;  //YAML file where every UUID and all of his usernames are stored
	FileInputStream IStream;
	Writer FWriter;
	
	DumperOptions options = new DumperOptions();
	Yaml yaml = new Yaml(options);
	
	public FileLoggerNames() {
		fileName = "\\UUID\\login.yml";
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	}
	
	public boolean logName(UUID id, String name) {
		Object input;
		file = new File(fileName);
		file.mkdir();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return false;
			}
		}
		try {
			IStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return false;
		}
		
		input = yaml.load(IStream);
		if (input instanceof Map) {
			//TODO: Add name to the map
		} else {
			//TODO: Create map
		}
		
		//Should never reach this point once implemented, but eclipse won't shut up otherwise =/
		return false;
	}
}
