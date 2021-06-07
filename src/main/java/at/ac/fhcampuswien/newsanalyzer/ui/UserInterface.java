package at.ac.fhcampuswien.newsanalyzer.ui;


import at.ac.fhcampuswien.newsanalyzer.ctrl.Controller;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
		// TODO implement me
	}


	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interface");
		menu.setTitle("Wählen Sie aus:");
		menu.insert("a", "Choice Corona", this::getDataFromCtrl1);
		menu.insert("b", "Choice IT", this::getDataFromCtrl2);
		menu.insert("c", "Choice Business", this::getDataFromCtrl3);
		menu.insert("d", "Choice User Input:",this::getDataForCustomInput);
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
