package at.ac.fhcampuswien.newsapi.beans;


import at.ac.fhcampuswien.newsapi.NewsApiException;
import com.fasterxml.jackson.annotation.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "source",
        "author",
        "title",
        "description",
        "url",
        "urlToImage",
        "publishedAt",
        "content"
})
public class Article {

    @JsonProperty("source")
    private Source source;
    @JsonProperty("author")
    private String author;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("url")
    private String url;
    @JsonProperty("urlToImage")
    private String urlToImage;
    @JsonProperty("publishedAt")
    private String publishedAt;
    @JsonProperty("content")
    private String content;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Article() {
    }

    /**
     *
     * @param publishedAt
     * @param author
     * @param urlToImage
     * @param description
     * @param source
     * @param title
     * @param url
     * @param content
     */
    public Article(Source source, String author, String title, String description, String url, String urlToImage, String publishedAt, String content) {
        super();
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    @JsonProperty("source")
    public Source getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(Source source) {
        this.source = source;
    }

    public Article withSource(Source source) {
        this.source = source;
        return this;
    }

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    public Article withAuthor(String author) {
        this.author = author;
        return this;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Article withTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public Article withDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    public Article withUrl(String url) {
        this.url = url;
        return this;
    }

    @JsonProperty("urlToImage")
    public String getUrlToImage() {
        return urlToImage;
    }

    @JsonProperty("urlToImage")
    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public Article withUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
        return this;
    }

    @JsonProperty("publishedAt")
    public String getPublishedAt() {
        return publishedAt;
    }

    @JsonProperty("publishedAt")
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Article withPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

/*    @JsonProperty("content")
    public String getContent() {
        return content;
    }*/

    /**
     * Lädt kompletten HTML code der URL
     */
    @JsonProperty("content")
    public String getContent(String contentURL, String filename) throws IOException {
            URL url;
            // get URL content
            url = new URL(contentURL);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            Path myPath = Paths.get(".././exercise3_theresa/src/main/resources/"+filename+".html");
            File newHTMLFile = new File(filename+".html");
            FileWriter creatingFile = new FileWriter(newHTMLFile.getPath());
            BufferedWriter bufferedWriter = null;
            try {
                FileOutputStream fileStream = new FileOutputStream(newHTMLFile);
                OutputStreamWriter streamWriter = new OutputStreamWriter(fileStream);
                bufferedWriter = new BufferedWriter(streamWriter);
            } catch (IOException ioException){
                System.out.println("Something went wrong");
                ioException.printStackTrace();
            }

            while ((inputLine = br.readLine()) != null) {
                assert bufferedWriter != null;
                bufferedWriter.write(inputLine);
                //System.out.println(inputLine);
            }

            br.close();

            System.out.println("Done");







/*
        HttpURLConnection con;
        StringBuilder response = new StringBuilder();
        try {
            con = (HttpURLConnection) obj.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            // TODO improve ErrorHandling
            throw new NewsApiException("Could not read Input Stream.");
            //System.out.println("Error "+e.getMessage());
        }
        NewsResponse newsReponse = null;
        String jsonResponse;

        try {
            jsonResponse= requestData();
        } catch (NullPointerException n){
            throw new NewsApiException("JsonResponse could not be initialized & is not allowed to be NULL");
        }
        if(!jsonResponse.isEmpty()){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                newsReponse = objectMapper.readValue(jsonResponse, NewsResponse.class);
                if(!"ok".equals(newsReponse.getStatus())){
                    System.out.println("Error: "+newsReponse.getStatus());
                }
            }

            catch (JsonProcessingException e) {
                throw new NewsApiException("The JSON File could not be processed properly.");
                // System.out.println("Error: "+e.getMessage());
            }
        }

*/
        return content;
    }
    @JsonProperty("content")
    public String getContent(){
        return content;
    }



    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    public Article withContent(String content) {
        this.content = content;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Article withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return "Article{" +
                "source=" + source +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", content='" + content + '\'' +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}