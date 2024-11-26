package pl.edu.pwr.student.damian_fryc.lab4_client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// ApiClient will get data once per category and will sort data locally,
// filtering data will be on server side (not great)
// TOO MUCH DATA, on top of the table will be "<-- 0 -->", number will be a text input field

// Gets all the categories (only when "czy zmienne" == true)
// in GUI   "nazwa-poziom"  ->  "nazwa"             ->  "nazwa-przekroj"->"id-okres"
// from url "area/area-area"->  "area/area-variable"->  "variable/variable-section-periods"
// from API "id"            ->  "id-zmienna"        ->  "id-przekroj", "id-okres"

// data from url "variable/variable-data-section"
// from url "variable/variable-section-position" and "dictionaries/way-of-presentation"
// all      "id-pozycja-X" -> "nazwa-pozycja" and "id-sposob-prezentacji-miara" -> "nazwa"

// saving by CacheHandler, but it saves and reads only JSONArray's so the data must be marged

public class ApiClient {
	private static final String url = "https://api-dbw.stat.gov.pl/api/";
	public enum Language {
		PL,
		EN
	}

	JSONArray currentData = null;
	public String getLanguage(Language language){
		if(language == Language.EN) return "en";
		else return "pl";
	}

	private JSONArray fetchData(String request) {
		String fileRequest = request;
		JSONArray data = CacheHandler.readCache(fileRequest);
		if (data != null) {
			return data;
		}

		JSONArray finalData = new JSONArray();

		// Getting data from server as JSONArray, asks for all pages of data
		try(HttpClient client = HttpClient.newHttpClient()) {
			int i = 0;
			JSONObject dataObject = new JSONObject();
			JSONArray dataArray;
			do {
				request = request.replaceFirst("numer-strony=\\d+", "numer-strony=" + i);
				HttpRequest httpRequest = HttpRequest.newBuilder()
						.uri(new URI(url + request))
						.build();
				HttpResponse<String> responseData = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

				if (responseData.statusCode() != 200) {
					System.out.println("Something bad happened D:");
					return finalData;
				}

				// Make JSONObject or make JSONArray and put all the data to final array
				try {
					dataObject = new JSONObject(responseData.body());
				}catch (JSONException ignored){}

				if (dataObject.isEmpty()) dataArray = new JSONArray(responseData.body());
				else dataArray = dataObject.getJSONArray("data");
				for (int j = 0; j < dataArray.length(); j++)
					finalData.put(dataArray.get(j));
				i++;
			} while (!dataObject.isEmpty() && (dataObject.get("page-number") != dataObject.get("page-count")) && dataArray.length() == 5000);
			CacheHandler.saveToCache(fileRequest, finalData);
		}
		catch(URISyntaxException | InterruptedException | IOException e){
			System.out.println(e.getMessage());
		}

		return finalData;
	}

	public JSONArray getData(Language language, int variableId, int sectionId, int yearId, int periodId) {
		String languageStr = getLanguage(language);
		String request = "variable/variable-data-section?id-zmienna="+variableId
				+"&id-przekroj="+sectionId
				+"&id-rok="+yearId
				+"&id-okres="+periodId
				+"&ile-na-stronie=5000&numer-strony=0&lang="+languageStr;
		return fetchData(request);
	}
	public JSONArray getAreas(Language language) {
		String languageStr = getLanguage(language);
		String request = "area/area-area?lang="+languageStr;
		return fetchData(request);
	}
	public JSONArray getAreaVariables(Language language, int areaId) {
		String languageStr = getLanguage(language);
		String request = "area/area-variable?id-obszaru="+areaId+"&lang="+languageStr;
		return fetchData(request);
	}
	public JSONArray getVariableSectionPeriods(Language language) {
		String languageStr = getLanguage(language);
		String request = "variable/variable-section-periods?ile-na-stronie=5000&numer-strony=0&lang="+languageStr;
		return fetchData(request);
	}


	public static void main(String[] args) {
		ApiClient test = new ApiClient();
//		JSONArray data = test.getData(Language.PL,506, 869, 2023, 247);
		JSONArray data = JsonParser.getTopics(test.getAreas(Language.EN), 1);
		System.out.println(data);
	}
}
