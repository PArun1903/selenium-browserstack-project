package Tests;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class WebScraping {

	public static  List<String> articleTitles = new ArrayList<>();


	public static void main(String[] args) throws IOException {
		String homepage = "https://elpais.com/opinion/";
		Document homeDoc = Jsoup.connect(homepage).userAgent("Mozilla/5.0").get();


		// For article of OPINIONS -1st
		Element container = homeDoc.selectFirst("body");
		Elements opinionLinks = container.select("article");


		Elements combinedLinks = new Elements();
		Elements validLinks = new Elements();
		combinedLinks.addAll(opinionLinks);


		//add valid url articles to the elements
		for (Element card : combinedLinks) {
			Element link = card.selectFirst("a");
			if (link != null) {
				String articleUrl = link.absUrl("href");
				if (articleUrl.endsWith(".html")) {
					validLinks.add(card); // Only add valid articles
				}
			}
		}


		for (int i = 0; i < Math.min(5, validLinks.size()); i++) {
			Element card = validLinks.get(i);
			Element link = card.selectFirst("a");
			if (link != null) {
				String articleUrl = link.absUrl("href");
				scrapeOpinionArticle(articleUrl, i + 1);
			}

		}

	}

	private static void scrapeOpinionArticle(String url, int idx) throws IOException {
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla/5.0")
				.get();

		Element titleEl = doc.selectFirst("h1");
		String title = titleEl != null ? titleEl.text() : "No title found";
		articleTitles.add(title);
		Element contentEl = doc.selectFirst("#main-content > header > div.a_e_txt._df > h2");

		String content = contentEl != null ? contentEl.text() : "No article body found.";


		System.out.println("Title " + idx + ". " + title);
		System.out.println("Content : " +content + "\n");

		// Image download – adjust selector to actual cover image element
		Element img = doc.selectFirst("#main-content > header > div.a_e_m > figure > span > img");
		if (img != null) {
			String imgUrl = img.absUrl("src");
			String ext = imgUrl.substring(imgUrl.lastIndexOf('.'));
			String outPath = "cover_" + idx + ext;
			downloadImage(imgUrl, outPath);
			System.out.println("Saved image as : " + outPath + "\n");
			System.out.println(" ");
		} else {
			System.out.println("No cover image found.\n");
		}
	}

	private static void downloadImage(String src, String fileName) throws IOException {
		try (InputStream in = URI.create(src).toURL().openStream();
				OutputStream out = new FileOutputStream(fileName)) {
			byte[] buf = new byte[8 * 1024];
			int n;
			while ((n = in.read(buf)) != -1) out.write(buf, 0, n);
		}

	}



}

