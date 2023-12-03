import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

class YouTubeSearch {

	public String Search (String song, String artist)  {
		String url   = "https://www.youtube.com/results";
		String query = song + " " + artist;

		String vid_url = "";

		try{
			Document doc = Jsoup.connect(url)
				.data("search_query", query)
				.userAgent("Chrome")
				.get();

			for (Element a : doc.select(".yt-lockup-title > a[title]")) {
				vid_url = "https://www.youtube.com" + a.attr("href");
				break;
			}

		} catch(Exception e){
			System.out.println( e );
		}
		return(vid_url);
	}

	public static void main(String[]args)  {
		String s = "Never Gonna Give You Up";
		String a = "Rick Astley";

		YouTubeSearch yts = new YouTubeSearch();
		String url = "https://www.youtube.com" + yts.Search(s, a);
		System.out.println( url );

	}
}
