package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiException;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;

import java.util.List;

public class Controller {

	public static final String APIKEY = "03f88a13272b471a805932b58b86a49c";  //TODO add your api key

	//added NewsApi to get current values
	private NewsApi current = null;

//String option, String Category übergeben möglich
	public void process() {
		System.out.println("Start process");

		//TODO implement Error handling

		NewsResponse newsResponse;
		try {
			newsResponse = current.getNews();
			List<Article> articles = newsResponse.getArticles();
			articles.stream().forEach(article -> System.out.println(article.toString()));
		} catch (NewsApiException e) {
			System.out.println("NewsResponse cannot be empty.");
			e.printStackTrace();
		} catch (NullPointerException n){
			System.out.println("NewsResponse cannot be NULL");
			n.printStackTrace();
		}
		//TODO load the news based on the parameters

		//TODO implement methods for analysis

		System.out.println("End process");
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
