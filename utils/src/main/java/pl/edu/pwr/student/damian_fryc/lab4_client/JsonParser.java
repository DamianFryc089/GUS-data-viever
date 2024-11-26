package pl.edu.pwr.student.damian_fryc.lab4_client;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class JsonParser {
	static JSONArray getCategories(JSONArray areas){
        JSONArray categories = new JSONArray();
        Set<Integer> uniqueCategories = new HashSet<>();

        for (int i = 0; i < areas.length(); i++) {
            JSONObject item = areas.getJSONObject(i);
            if (item.getBoolean("czy-zmienne") && !uniqueCategories.contains(item.getInt("id-poziom"))) {
                JSONObject selectedItem = new JSONObject();
                uniqueCategories.add(item.getInt("id-poziom"));
                selectedItem.put("id-poziom", item.getInt("id-poziom"));
                selectedItem.put("nazwa-poziom", item.getString("nazwa-poziom"));
                categories.put(selectedItem);
            }
        }
        return categories;
    }
    static JSONArray getTopics(JSONArray areas, int categoryId){
        JSONArray topics = new JSONArray();

        for (int i = 0; i < areas.length(); i++) {
            JSONObject item = areas.getJSONObject(i);
            if (item.getBoolean("czy-zmienne") && item.getInt("id-poziom") == categoryId) {
                JSONObject selectedItem = new JSONObject();
                selectedItem.put("id", item.getInt("id"));
                selectedItem.put("nazwa", item.getString("nazwa"));
                topics.put(selectedItem);
            }
        }
        return topics;
    }
}
