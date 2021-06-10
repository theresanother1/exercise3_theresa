package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiException;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Controller {

	public static final String APIKEY = "03f88a13272b471a805932b58b86a49c";  //TODO add your api key

	//added NewsApi to get current values
	private NewsApi current = null;

//String option, String Category übergeben möglich
	public void process() {
		System.out.println("Start process");

		//TODO implement Error handling

		NewsResponse newsResponse = null;
		List<Article> articles = null;
		try {
			newsResponse = current.getNews();
			articles  = newsResponse.getArticles();

		} catch (NewsApiException e) {
			System.out.println("NewsResponse cannot be empty.");
			e.printStackTrace();
		} catch (NullPointerException n){
			System.out.println("NewsResponse cannot be NULL");
			n.printStackTrace();
		}
		if (articles != null && articles.size() != 0) {
			articles.stream()
					.forEach(article -> System.out.println(article.toString()));


			//TODO load the news based on the parameters

			//TODO implement methods for analysis
			//count number of articles
			long sum = articles.stream()
					.count();
			System.out.println(sum);


			//get provider with most articles
			//gelöst anhand Unterlagen/Internetquellen wie:
			//https://mkyong.com/java8/java-8-collectors-groupingby-and-mapping-example/

			String providesMostArticles = "No provider found";
			try {
				providesMostArticles = getProviderWithMostArticles(articles);
			} catch (NullPointerException ne){
				System.out.println("Couldn't determine provider with most articles.");
				ne.printStackTrace();
			}
			System.out.println("Provides most Articles: " + providesMostArticles);

			//get author with shortest name

			String shortestAuthorName = "";
			try {
				shortestAuthorName = getShortestAuthorName(articles);
			} catch (NullPointerException ne){
				System.out.println("Couldn't determine the shortest Author.");
				ne.printStackTrace();
			}

			System.out.println(shortestAuthorName);

			// d? - was ist das gewünschte Ergebnis

		} else{
			System.out.println("No articles for your query found, please try again.");
		}
			// ADD-ON Originalartikel über HTTP URL in der Klasse Artikel herunterladen
		//-> wie drauf zugreifen, was genau runterladen wo programmieren?
		System.out.println("End process");
	}

	public String getProviderWithMostArticles(List<Article> articles) throws NullPointerException{
		return articles.stream()
				.filter(name -> name.getSource().getName() != null)
				.map(article -> article.getSource().getName())
				.collect(
						Collectors.groupingBy(
								Function.identity(), Collectors.counting()
						)
				)
				.entrySet()
				.stream()
				//.sorted(Map.Entry.comparingByValue())
				.max(Map.Entry.comparingByValue())
				.get()
				.getKey();
	}

	public String getShortestAuthorName(List<Article> articles) throws NullPointerException {
		Comparator<Article> byAuthorLength = (a1, a2) -> a1.getAuthor().length() >= a2.getAuthor().length() ? -1 : 1;
		String shortest = "No Author found";
		if (articles.size() == 1){
			shortest = articles.get(0).getAuthor();
			if (shortest==null||shortest.equals("null")){
				shortest = "We couldn't determine the author.";
			}
		} else {
			Optional<Article> author = articles.stream()
					.filter(e -> e.getAuthor() != null)
					.sorted(byAuthorLength.reversed())
					.findFirst();
			shortest = author.get().getAuthor();
		}
		return shortest;
	}


	//added to set NewsAPI to get current response from API
	public void setData(NewsApi myNewsAPI){
		this.current = myNewsAPI;
	}

	//returns current NewsAPI
	public Object getData() {
		return current;
	}


}
