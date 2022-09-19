package me.lagbug.xprotect.bungee.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class FileUtils {

	private Plugin plugin;

	private Map<String, Configuration> files;
	private Map<String, File> filesData;

	public FileUtils initiate(Plugin plugin, String... fileNames) {
		this.plugin = plugin;
		this.files = new HashMap<>();
		this.filesData = new HashMap<>();

		if (!files.isEmpty() || !filesData.isEmpty()) {
			files.clear();
			filesData.clear();
		}

		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}

		try {
			for (String fileName : fileNames) {
				File file = new File(plugin.getDataFolder(), fileName);
				File parentFile = file.getParentFile();

				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}

				if (!file.isDirectory()) {
					if (!file.exists()) {
						Files.copy(plugin.getResourceAsStream(fileName), file.toPath());
					}

					if (fileName.endsWith(".yml")) {
						files.put(fileName, ConfigurationProvider.getProvider(YamlConfiguration.class).load(file));
						filesData.put(fileName, file);
					}
				}
			}

			for (File file : new File(plugin.getDataFolder() + File.separator + "lang").listFiles()) {
				if (!file.isDirectory()) {
					files.put("lang/" + file.getName(),
							ConfigurationProvider.getProvider(YamlConfiguration.class).load(file));
					filesData.put("lang/" + file.getName(), file);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return this;
	}

	public void reloadFiles() {
		initiate(plugin);
	}

	public Configuration getFile(String path) {
		return files.get(path);
	}

	public File getFileData(String path) {
		return filesData.get(path);
	}

	public void saveFile(String path) {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(getFile(path), getFileData(path));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}