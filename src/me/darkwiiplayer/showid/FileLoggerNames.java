package me.darkwiiplayer.showid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class FileLoggerNames {
	String fileName;
	File file;  //YAML file where every UUID and all of his usernames are stored
	FileInputStream IStream;
	FileWriter FWriter;
	
	DumperOptions options = new DumperOptions();
	Yaml yaml = new Yaml(options);
	
	public FileLoggerNames() {
		fileName = "/UUID/login.yml";
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	}
	
	public boolean logName(UUID id, String name) {
		Object  input; //This is what we read from the file
		HashMap<String, Object> output;
		file = new File(fileName); 
		try {
			file.createNewFile();
		} catch (IOException e1) {			
			// TODO Auto-generated catch block			
		}
		
		if (!file.exists()) { //Create the file if it doesn't exist, exit with FALSE if it doesn't work (file does not exist and cannot be created)
			try {
				file.createNewFile();
			} catch (IOException e) {
				return false;
			}
		}
		
		try { //Read data from the file and exit with FALSE if it doesn't work
			IStream = new FileInputStream(file);
			
			input = yaml.load(IStream);
			
			IStream.close();
			
		} catch (FileNotFoundException e) {
			((ShowID)ShowID.getInstance()).logger.log(Level.SEVERE, "UUID/Name list could not be opened - Java error: " + e.getMessage());
			return false;
		} catch (IOException e) {
			((ShowID)ShowID.getInstance()).logger.log(Level.SEVERE, "Input/Output exception while saving UUID/Name list - Java error: " + e.getMessage());
			return false;
		}
		
		if (input.getClass().equals(HashMap.class)) { //Input object is a hashmap (as expected)
			output = (HashMap)input;
			
			if (output.containsKey(id.toString())) { //Already knows the UUID
				if (output.get(id.toString()).getClass().equals(ArrayList.class)) { //Everything is fine
					((ArrayList)output.get(id.toString())).add(name);
				} else if (output.get(id.toString()).getClass().equals(String.class)) { //Value is string instead of list? Shouldn't happen, but CAN happen if the file is edited manually, so why nit catch it :)
					String tmp = (String) output.get(id.toString());
					output.put(id.toString(), new ArrayList());
					((ArrayList)output.get(id.toString())).add(tmp);
					((ArrayList)output.get(id.toString())).add(name);
				} else { //Value is some other data type. Just dump it and overwrite it with valid data.
					output.put(id.toString(), new ArrayList());
					((ArrayList)output.get(id.toString())).add(name);
				}
				
			} else {
				output.put(id.toString(), new ArrayList());
				((ArrayList)output.get(id.toString())).add(name);
			}
		} else {
			output = new HashMap<String, Object>();
			output.put(id.toString(), new ArrayList());
			((ArrayList)output.get(id.toString())).add(name);
		}
		
		try { //Write the map back into the file
			FWriter = new FileWriter(file);
			yaml.dump(output, FWriter);
			FWriter.close();
			((ShowID)ShowID.getInstance()).logger.log(Level.INFO, "Player " + name + " Joined with UUID " + id.toString() + ". Event has been logged to \"" + fileName + "\"");
			return true;
		} catch (IOException e) {
			((ShowID)ShowID.getInstance()).logger.log(Level.SEVERE, "Input/Output exception while saving UUID/Name list - Java error: " + e.getMessage());
			return false;
		}
	}
}
