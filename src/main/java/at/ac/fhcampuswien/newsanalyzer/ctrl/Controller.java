package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsanalyzer.downloader.ParallelDownloader;
import at.ac.fhcampuswien.newsanalyzer.downloader.SequentialDownloader;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiException;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.beans.Source;


import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Controller {

    public static final String APIKEY = "03f88a13272b471a805932b58b86a49c";  //TODO add your api key

    //added NewsApi to get current values
    private NewsApi current = null;

    public void process() {
        System.out.println("Start process");

        //TODO implement Error handling


        NewsResponse newsResponse = null;
        List<Article> articles = null;
        NewsApi newOne = null;

        // first try get a not NULL NewsAPI template
        try {
            newOne = getNewsAPIData();
        } catch (NullPointerException nullPointerException) {
            System.out.println("NewsAPI cannot be empty.");
            nullPointerException.printStackTrace();
        }

        //next try to get a not NULL NewsResponse based on NewsAPI
        try {
            newsResponse = newOne.getNews();
        } catch (NewsApiException e) {
            System.out.println("NewsResponse cannot be empty.");
            e.printStackTrace();
        } catch (NullPointerException n) {
            System.out.println("NewsResponse cannot be NULL");
            n.printStackTrace();
        }

        //next try to get the Articles from NewsResponse saved in a List
        try {
            articles = newsResponse.getArticles();
        } catch (NewsApiException newsApiException) {
            System.out.println("Probably NewsResponse was NULL, so no articles could be fetched.");
            newsApiException.printStackTrace();
        } catch (NullPointerException n) {
            System.out.println("NewsResponse cannot be NULL");
            n.printStackTrace();
        }


        //assert articles != null;
        //if (articles.size() != 0) {
			/*articles.stream()
					.forEach(article -> System.out.println(article.toString()));*/

        if (articles != null && !articles.isEmpty()) {
            List<String> urls = getListofURLs(articles);
            System.out.println(urls);
            //ca 3 secs für 20 artikel
          /*  ParallelDownloader parallelDownloader = new ParallelDownloader();
            parallelDownloader.process(urls);*/

            //ca 13 secs für 20 Artikel
            SequentialDownloader sequentialDownloader = new SequentialDownloader();
            sequentialDownloader.process(urls);
        }


        System.out.println("Articles: ");
        articles.stream()
                .forEach(article -> {
                    System.out.println(article.getSource().getName());
                    System.out.println(article.getTitle());
						/*try {
							System.out.println(article.getContent(article.getUrl()));
						} catch (IOException ioException){
							System.out.println("Couldn't read URL.");
							ioException.printStackTrace();
						}*/
                    System.out.println(article.getContent());

                    System.out.println();
                });



		/*	if (!articles.isEmpty()) {
				try {
					Article writeArticle;
					writeArticle = articles.get(0);
					//articleURL = writeArticle.getUrl();
					writeArticle.getContent(writeArticle.getUrl(), "NewsHTMLFile3");
				} catch (IOException ioException) {
					ioException.printStackTrace();
				} catch (NullPointerException nullPointerException) {
					System.out.println("Articles cannot be NULL.");
					nullPointerException.printStackTrace();
				} catch (Exception e) {
					System.out.println("Something went wrong.");
					e.printStackTrace();
				}
			}*/

			//TODO implement methods for analysis

			//count number of articles
			//sum = articles.size();
			long sum = articles.stream()
					.count();
			System.out.println("Number of articles: "+sum);

/*
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
		/*} else{
			System.out.println("No articles for your query found, please try again.");
		}*/
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

    public String getShortestAuthorName(List<Article> articles) {
        if (articles != null && articles.isEmpty()) {
            return "Shortest Author not found.";
        }
        if (articles != null && articles.get(0).getAuthor() == null && articles.size() == 1) {
            return "Shortest Author not found";
        }
        Optional<Article> author = articles.stream()
                .filter(e -> e.getAuthor() != null)
                .max(Comparator.comparing(Article::getAuthor));
        return author.isPresent() ? author.get().getAuthor() : "Shortest Author not found";
    }

    public List<Article> getSortedList(List<Article> articles) {
        Comparator<Article> byTitleLexical = (title1, title2) ->
                title1.getTitle().length() >= title2.getTitle().length() ? -1 : 1;
        List<Article> dummy = new ArrayList<>();
        dummy.add(new Article(new Source(), "no author", "no title", "no desc",
                "no url", "no urlToImg", "no publishedAt", "no content"));

        List<Article> sortedList = articles.stream()
                .filter(art -> art.getTitle() != null)
                .sorted(Comparator.comparing(Article::getTitle).reversed())
                .sorted(byTitleLexical)
                .collect(Collectors.toList());
        return sortedList.isEmpty() ? dummy : sortedList;
    }

    public List<String> getListofURLs(List<Article> articles) {
        return articles.stream()
                .filter(art -> art.getUrl() != null)
                .filter(art -> art.getUrlToImage() != null)
                .map(Article::getUrl)
                .collect(Collectors.toList());
    }

    //added to set NewsAPI to get current response from API
    public void setData(NewsApi myNewsAPI) {
        this.current = myNewsAPI;
    }

    //returns current NewsAPI
    public NewsApi getNewsAPIData() throws NullPointerException {
        return current;
    }

    public Object getData() {
        return null;
    }
}
