package pl.edu.pwr.student.damian_fryc.lab4_gui;
import org.json.JSONObject;
import pl.edu.pwr.student.damian_fryc.lab4_client.ApiClient;
import org.json.JSONArray;

public class Main {
	public static void main(String[] args) {
		ApiClient client = new ApiClient();
		JSONArray data = client.getData();
		for (int i = 0; i < data.length(); i++) {
			JSONObject obj = data.getJSONObject(i);
			System.out.println(obj.toString());
		}
	}
}
