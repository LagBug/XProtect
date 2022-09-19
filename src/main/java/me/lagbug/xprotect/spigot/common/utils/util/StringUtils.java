package me.lagbug.xprotect.spigot.common.utils.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.lagbug.xprotect.spigot.XProtect;

/**
 * This class contains many useful methods to do things
 * like get the similarity between two string, or determine
 * if a string contains bad/disallowed words.
 *
 * @version 1.0
 */
public class StringUtils {

	//Map of all the bad words
	private static final Map<String, String[]> words = new HashMap<>();
	//The length of the largest word
	private static int largestWordLength = 0;
	//The instance of the plugin
	private static final XProtect plugin = XProtect.getPlugin(XProtect.class);

	/**
	 * This method simply initiates the required file for the
	 * bad word checking to run
	 */
	public static void initiate() {
		//Trying to read the bad words file
		try {
			BufferedReader reader = new BufferedReader(new FileReader(plugin.getDataFolder() + File.separator + "files/bad_words.txt"));
			String line = "";
			int counter = 0;

			//Reading all lines
			while ((line = reader.readLine()) != null) {
				counter++;
				String[] content = null;
				try {
					content = line.split(",");
					if (content.length == 0) {
						continue;
					}
					String word = content[0];
					String[] ignore_in_combination_with_words = new String[] {};

					if (content.length > 1) {
						ignore_in_combination_with_words = content[1].split("_");
					}

					if (word.length() > largestWordLength) {
						largestWordLength = word.length();
					}
					//Adding the word to the Map
					words.put(word.replaceAll(" ", ""), ignore_in_combination_with_words);

				} catch (Exception e) {
					CommonUtils.forceLog("Could not read bad words file");
				}

			}

			//Closing the reader to prevent memory leaks
			reader.close();
			CommonUtils.forceLog("Loaded " + counter + " words to filter out");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used to check if a given string is most likely
	 * to contain a bad/disallowed word.
	 * 
	 * @param input - the input string
	 * @return - if the string contains bad words
	 */
	public static boolean containsBadWords(String input) {
		//If there's no input, we return false
		if (input == null || input.isEmpty()) {
			return false;
		}

		//Replacing characters with letters
		input = input.replace("1", "i").replace("!", "i").replace("3", "e").replace("4", "a").replace("@", "a")
				.replace("5", "s").replace("7", "t").replace("0", "o").replace("9", "g");

		boolean found = false;
		//Applying regex filter
		input = input.toLowerCase().replaceAll("[^a-zA-Z]", "");

		//Getting all combinations of the string
		for (int start = 0; start < input.length(); start++) {
			for (int offset = 1; offset < (input.length() + 1 - start) && offset < largestWordLength; offset++) {
				String wordToCheck = input.substring(start, start + offset);

				for (String c : plugin.getChatFile().getStringList("whitelist")) {
					if (wordToCheck.equals(c)) {
						continue;
					}	
				}
				
				//Using levenshtein distance algorithm to check if the word to check matches any bad word
				for (String key : words.keySet()) {
					if (stringSimilarity(key, wordToCheck) >= plugin.getChatFile().getInt("settings.sensitivity")) {
						wordToCheck = key;
						break;
					}
				}

				//Return if there was a bad word found
				if (words.containsKey(wordToCheck)) {
					String[] ignoreCheck = words.get(wordToCheck);
					boolean ignore = false;
					for (String value : ignoreCheck) {
						if (input.contains(value)) {
							ignore = true;
							break;
						}
					}

					if (!ignore) {
						found = true;
					}
				}
			}
		}
		return found;

	}

	/**
	 * Converting the distance between 2 strings to an integer
	 * between 0 and 100 representing the probability.
	 * 
	 * @param s1 - the first string
	 * @param s2 - the second string
	 * @return - an integer between 0-100 of the similarity
	 */
	public static int stringSimilarity(String s1, String s2) {
		String longer = s1, shorter = s2;
		if (s1.length() < s2.length()) {
			longer = s2;
			shorter = s1;
		}
		int longerLength = longer.length();

		if (longerLength == 0) {
			return 100;
		}
		return (int) Math.round(((longerLength - distance(longer, shorter)) / (double) longerLength) * 100.0);
	}

	/**
	 * A Java implementation of the levenshtein distance algorithm
	 * 
	 * @param a - the first string
	 * @param b - the second string
	 * @return - the distance between the two string
	 */
	private static int distance(String a, String b) {
		a = a.toLowerCase();
		b = b.toLowerCase();

		int[] costs = new int[b.length() + 1];
		for (int j = 0; j < costs.length; j++)
			costs[j] = j;
		for (int i = 1; i <= a.length(); i++) {
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= b.length(); j++) {
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
						a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}
}