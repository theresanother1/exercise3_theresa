package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiException;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.beans.Source;


import java.io.IOException;
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
			/*articles.stream()
					.forEach(article -> System.out.println(article.toString()));*/

			/**
			 * Verbessern auf nur Artikel Content - nicht gesamtes HTML Template der Website
			 */

			System.out.println("Articles: ");
			articles.stream()
					.forEach(article -> {
						System.out.println(article.getSource().getName());
						System.out.println(article.getTitle());
						try {
							System.out.println(article.getContent(article.getUrl()));
						} catch (IOException ioException){
							System.out.println("Couldn't read URL.");
							ioException.printStackTrace();
						}

						System.out.println();
					});



			//TODO implement methods for analysis
			//count number of articles
			//sum = articles.size();
			long sum = articles.stream()
					.count();
			System.out.println("Number of articles: "+sum);


			//get provider with most articles
			//gelöst anhand Unterlagen/Internetquellen wie zB:
			//https://mkyong.com/java8/java-8-collectors-groupingby-and-mapping-example/
			String providesMostArticles = getProviderWithMostArticles(articles);
			System.out.println("Provides most Articles: " + providesMostArticles);

			//get author with shortest name
			String shortestAuthorName = getShortestAuthorName(articles);
			System.out.println("Has shortest Name:" +shortestAuthorName);

			// d? - was ist das gewünschte Ergebnis

			List<Article> sortedList = getSortedList(articles);
			if (sortedList.get(0).getTitle().equals("no title")) {
				System.out.println("Sorted List not found");
			} else {
				System.out.println("Sorted list: ");
				sortedList.forEach(article ->
						System.out.println( article.getTitle())
				);
			}
		} else{
			System.out.println("No articles for your query found, please try again.");
		}
			// ADD-ON Originalartikel über HTTP URL in der Klasse Artikel herunterladen
		//-> wie drauf zugreifen, was genau runterladen wo programmieren?
		System.out.println("End process");
	}

	public String getProviderWithMostArticles(List<Article> articles) {
		Optional<Map.Entry<String, Long>> curArt = articles.stream()
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
				.max(Map.Entry.comparingByValue());
		return curArt.isPresent() ? curArt.get().toString() : "Provider not found";
	}

	public String getShortestAuthorName(List<Article> articles){
		if (articles.get(0).getAuthor()==null && articles.size() == 1 ){
				return "Shortest Author not found";
			}
		Optional<Article> author = articles.stream()
				.filter(e -> e.getAuthor() != null)
				.max(Comparator.comparing(Article::getAuthor));
		return author.isPresent() ? author.get().getAuthor() : "Shortest Author not found";
	}

	public List<Article> getSortedList(List<Article> articles){
		Comparator<Article> byTitleLength = (title1, title2) ->
				title1.getTitle().length() >= title2.getTitle().length() ? -1 : 1;
		List<Article> dummy = new ArrayList<>();
		dummy.add(new Article(new Source(), "no author", "no title", "no desc",
				"no url", "no urlToImg", "no publishedAt", "no content"));

		List<Article> sortedList = articles.stream()
				.filter(art -> art.getTitle() != null)
				.sorted(Comparator.comparing(Article::getTitle))
				.sorted(byTitleLength)
				.collect(Collectors.toList());
		return sortedList.isEmpty() ? dummy : sortedList;
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
