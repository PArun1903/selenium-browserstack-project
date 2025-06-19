package Tests;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class Translator {

	static List<String> spanishTitlesList = new ArrayList<>();
	public static String englishResult;

	public static void main(String[] args) throws IOException {

		WebScraping.main(null);
		for (String title : WebScraping.articleTitles) {
			spanishTitlesList.add(title);
		}

		String combinedSentence = String.join(". ", spanishTitlesList) + ".";
		System.out.println("combined Spanish Sentence : " + combinedSentence);


		String apiKey = "58dbf5c583msh8fa5e6d529bf341p19ffc6jsnf67e05c5fdab";
		String apiHost = "rapid-translate-multi-traduction.p.rapidapi.com";

		//Our final sentence(Translated one[eng])
		englishResult = translateAllSpanishTitles(combinedSentence, apiKey, apiHost);
		System.out.println("Translated English Sentence : " + englishResult);
		System.out.println();

	}

	public static String translateAllSpanishTitles(String combinedSentence, String apiKey, String apiHost) {
		try {
			return translateSentence(combinedSentence, apiKey, apiHost);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}

	public static String translateSentence(String sentence, String apiKey, String apiHost) throws Exception {
		// Prepare JSON body
		String json = String.format("{\"from\":\"es\",\"to\":\"en\",\"q\":\"%s\"}", sentence);

		// Create HTTP request
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://rapid-translate-multi-traduction.p.rapidapi.com/t"))
				.header("x-rapidapi-key", apiKey)
				.header("x-rapidapi-host", apiHost)
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(json))
				.build();

		// Send request
		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		JSONArray jsonArray = new JSONArray(response.body());
		String translated = jsonArray.getString(0).trim();

		return translated;


	}

}



