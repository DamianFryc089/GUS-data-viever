package pl.edu.pwr.student.damian_fryc.lab4_client;
import org.json.JSONArray;

import java.io.*;
import java.util.Scanner;

public class CacheHandler {
	public static JSONArray readCache(String request) {
		File file = new File(parseRequestToFileName(request)+".json");
		if (!file.exists()) return null;

		String content = readFileContent(file);
		JSONArray data = new JSONArray(content);

//		String jsonData = cache.optString(url, null);
//		if (jsonData == null) return null;
//		return cache.optString(url, null); // Zwróć dane, jeśli istnieją

		return data;
	}

	public static void saveToCache(String request, JSONArray data) {
		File file = new File(parseRequestToFileName(request)+".json");
		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write(data.toString(4));
		} catch (IOException ignored) {}
	}
	private static String parseRequestToFileName(String request){
		String fileName;
		fileName = request.replaceAll("/", "_");
		fileName = fileName.replaceAll("\\?", "%");
		return fileName;
	}

	private static String readFileContent(File file) {
		StringBuilder content = new StringBuilder();
		try (Scanner reader = new Scanner(new FileReader(file))) {
			String line;
			while ((line = reader.nextLine()) != null) {
				content.append(line).append("\n");
			}
		}
		catch (FileNotFoundException ignored){}
		return content.toString().trim();
	}
}
