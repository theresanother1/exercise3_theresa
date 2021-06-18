package at.ac.fhcampuswien.newsanalyzer.ui;


import at.ac.fhcampuswien.newsanalyzer.ctrl.Controller;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;
import at.ac.fhcampuswien.newsapi.enums.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class UserInterface 
{
	private Controller ctrl = new Controller();

	public void getDataFromCtrl1(){
		System.out.println("ABC");
		NewsApi firstOption = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ("Corona")
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCountry(Country.at)
				.setSourceCategory(Category.science)
				.createNewsApi();
		ctrl.setData(firstOption);
		ctrl.process();
	}

	public void getDataFromCtrl2(){
		// TODO implement
		NewsApi secondOption = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ("IT")
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCountry(Country.at)
				.setSourceCategory(Category.technology)
				.createNewsApi();
		ctrl.setData(secondOption);
		ctrl.process();
	}

	public void getDataFromCtrl3(){
		// TODO implement me
		NewsApi thirdOption = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ("Business")
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCountry(Country.at)
				.setSourceCategory(Category.business)
				.createNewsApi();
		ctrl.setData(thirdOption);
		ctrl.process();

	}
	
	public void getDataForCustomInput() {
		Category curCat = getCategory();
		String userQuery = "";
		System.out.println("Please enter your Query: ");
		Scanner scanner = new Scanner(System.in);
		userQuery = scanner.next();

		NewsApi thirdOption = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ(userQuery)
				.setLanguage(Language.en)
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCountry(Country.au)
				.setSourceCategory(curCat)
				.createNewsApi();
		ctrl.setData(thirdOption);
		ctrl.process();

		// TODO implement me
	}


	//added to get Category from User
	public Category getCategory(){
		Category category[] = Category.values();
		System.out.println("Please choose one of the following Categories");
		int k = 1;
		for (Category value : category) {
			System.out.println(k + " " + value);
			k++;
		}
		Scanner custom1 = new Scanner(System.in);
		int next = custom1.nextInt();

		Category customCat = Category.entertainment;

		switch (next) {
			case 1: customCat = Category.business;
				break;
			case 2: customCat = Category.entertainment;
				break;
			case 3: customCat = Category.health;
				break;
			case 4: customCat = Category.science;
				break;
			case 5: customCat = Category.sports;
				break;
			case 6: customCat = Category.technology;
				break;
		}
		return customCat;
	}

	public void downloadLastSearch(){

	}

	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interface");
		menu.setTitle("Wählen Sie aus:");
		menu.insert("a", "Choice Corona", this::getDataFromCtrl1);
		menu.insert("b", "Choice IT", this::getDataFromCtrl2);
		menu.insert("c", "Choice Business", this::getDataFromCtrl3);
		menu.insert("d", "Choice User Input:",this::getDataForCustomInput);
		menu.insert("e", "Download Last Search:",this::downloadLastSearch);
		menu.insert("q", "Quit", null);
		Runnable choice;
		while ((choice = menu.exec()) != null) {
			 choice.run();
		}
		System.out.println("Program finished");
	}


    protected String readLine() {
		String value = "\0";
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = inReader.readLine();
        } catch (IOException ignored) {
		}
		return value.trim();
	}

	protected Double readDouble(int lowerlimit, int upperlimit) 	{
		Double number = null;
        while (number == null) {
			String str = this.readLine();
			try {
				number = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                number = null;
				System.out.println("Please enter a valid number:");
				continue;
			}
            if (number < lowerlimit) {
				System.out.println("Please enter a higher number:");
                number = null;
            } else if (number > upperlimit) {
				System.out.println("Please enter a lower number:");
                number = null;
			}
		}
		return number;
	}
}
