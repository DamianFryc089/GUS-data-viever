package pl.edu.pwr.student.damian_fryc.lab4_client;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParser {
	public void parseJson(String jsonString) {
		JSONObject json = new JSONObject(jsonString);
		System.out.println("Nazwa kategorii: " + json.getString("name"));

		JSONArray variables = json.getJSONArray("variables");
		for (int i = 0; i < variables.length(); i++) {
			JSONObject variable = variables.getJSONObject(i);
			System.out.println("ID: " + variable.getInt("id"));
			System.out.println("Nazwa: " + variable.getString("name"));
			System.out.println("Wartość: " + variable.getInt("value"));
		}
	}
}
