package at.ac.fhcampuswien.newsapi;

public class NewsApiException extends Exception{
    NewsApiException(String userMessage){
        super(userMessage);
    }
}
