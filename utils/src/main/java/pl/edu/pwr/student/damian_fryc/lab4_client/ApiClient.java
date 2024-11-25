package pl.edu.pwr.student.damian_fryc.lab4_client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.CacheRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// ApiClient will get data once per category and will sort data locally,
// filtering data will be on server side (not great)
// TOO MUCH DATA, on top of the table will be "<-- 0 -->", number will be a text input field

// Gets all the categories (only when "czy zmienne" == true)
// in GUI   "nazwa-poziom"  ->  "nazwa"             ->  "nazwa-przekroj"
// from url "area/area-area"->  "area/area-variable"->  "variable/variable-section-periods"
// from API "id"            ->  "id-zmienna"        ->  "id-przekroj", "id-okres"

// data from url "variable/variable-data-section"
// from url "variable/variable-section-position" and "dictionaries/way-of-presentation"
// all      "id-pozycja-X" -> "nazwa-pozycja" and "id-sposob-prezentacji-miara" -> "nazwa"

// saving by CacheHandler, but it saves and reads only JSONArray's so the data must be marged
// dictionaries/way-of-presentation fake 2 pages

public class ApiClient {
	private static final String url = "https://api-dbw.stat.gov.pl/api/ ";

	private JSONArray areas = new JSONArray();
	private JSONArray variableSectionPeriods = new JSONArray();

	JSONArray currentData = null;

	public ApiClient(){
		// load area/area-area and variable/variable-section-periods

	}

	private JSONArray fetchData(String request) throws URISyntaxException, IOException, InterruptedException {
		String fileRequest = request;
		switch (request) {
			case "way-of-presentation_pl":
				fileRequest = "dictionaries?way-of-presentation_pl";
				request = "dictionaries/way-of-presentation?page=0&page-size=5000&lang=pl";
				break;
			case "variable-section-periods_pl":
				fileRequest = "variable?variable-section-periods_pl";
				// 2 requests
				request = "dictionaries/periods-dictionary?page=1&page-size=5000&lang=pl";
				break;
		}
		JSONArray data = CacheHandler.readCache(fileRequest);
		if (data != null) {
			return data;
		}

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(new URI(url))
				.build();
		HttpResponse<String> responseData = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

		// Zapisz odpowiedź do pliku
		CacheHandler.saveToCache(request, new JSONArray(responseData.body()));
		return new JSONArray(responseData.body());
	}

	public JSONArray getData(){
		JSONArray mainObject = new JSONArray();
		mainObject.put(new JSONObject().put("id-rok", 1928));
		mainObject.put(new JSONObject().put("id-rok", 1932));
		mainObject.put(new JSONObject().put("id-rok", 1936));
		mainObject.put(new JSONObject().put("id-rok", 1948));
		mainObject.put(new JSONObject().put("id-rok", 1952));
		this.currentData = mainObject;
		return mainObject;
	}
}
