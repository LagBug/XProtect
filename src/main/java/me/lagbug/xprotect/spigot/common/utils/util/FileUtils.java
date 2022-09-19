package me.lagbug.xprotect.spigot.common.utils.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class FileUtils {

	private JavaPlugin plugin;

	private Map<String, YamlConfiguration> files;
	private Map<String, File> filesData;

	public void initiate(JavaPlugin plugin, String... fileNames) {
		this.plugin = plugin;
		this.files = new HashMap<>();
		this.filesData = new HashMap<>();

		if (!files.isEmpty() || !filesData.isEmpty()) {
			files.clear();
			filesData.clear();
		}

		new File(plugin.getDataFolder().getPath() + File.separator + "pictures").mkdirs();
		String[] pictures = { "book", "hat", "knife", "pen" };

		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}
		
		File langFolder = new File(plugin.getDataFolder(), "lang");
		if (!langFolder.exists()) {
			langFolder.mkdirs();
		}
		
		for (String fileName : fileNames) {
			File file = new File(plugin.getDataFolder(), fileName);
			if (!file.isDirectory()) {
				if (!file.exists()) {
					plugin.saveResource(fileName, false);
				}

				if (fileName.endsWith(".yml")) {
					files.put(fileName, YamlConfiguration.loadConfiguration(file));
					filesData.put(fileName, file);	
				}
			}
		}
		
		for (File file : langFolder.listFiles()) {
			if (!file.isDirectory()) {
				files.put("lang/" + file.getName(), YamlConfiguration.loadConfiguration(file));
				filesData.put("lang/" + file.getName(), file);
			}
		}

		for (String s : pictures) {
			File lFile = new File(plugin.getDataFolder() + "/pictures/", s + ".jpg");
			if (!lFile.exists()) {
				plugin.saveResource("pictures/" + s + ".jpg", false);
			}
		}
	}

	public void reloadFiles() {
		initiate(plugin);
	}

	public YamlConfiguration getFile(String path) {
		return files.get(path);
	}

	public File getFileData(String path) {
		return filesData.get(path);
	}

	public void saveFile(String path) {
		try {
			getFile(path).save(getFileData(path));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}