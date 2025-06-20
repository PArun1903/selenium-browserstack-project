package Tests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

public class WordCounter extends BaseTest {
    @Test
	public void WordCounting() throws IOException, URISyntaxException {
    	
    	driver.get("https://www.google.com");
		
		boolean found = false;

		Translator.main(null);
		// Accessing the sentence from MainClass
		String sentence = Translator.englishResult;

		// Convert to lowercase and split into words
		String[] words = sentence.toLowerCase().split("\\s+");

		// Use a HashMap to count occurrences
		Map<String, Integer> wordCount = new HashMap<>();

		for (String word : words) {
			wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
		}

		// Print words with count > 2
		for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
			if (entry.getValue() > 2) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
				found = true;
			}
		}
		
		if (!found) {
		    System.out.println("No words repeated more than twice");
		}
		driver.quit();
	}

}
