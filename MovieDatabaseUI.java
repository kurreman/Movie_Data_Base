package moviedatabase; // Måste skriva "javac -d . MovieDatabaseUI.java " i terminalen i samma directory för att skapa package först

import java.util.*;
import java.nio.file.*;
import java.io.IOException;
import java.lang.*;

/**
 * A command line user interface for a movie database.
 */
public class MovieDatabaseUI {
	private Scanner _scanner;
	private List<String> _movieDB = new ArrayList<String>();
	private Path _path = Paths.get("MoviesFull.txt");
	private int _propertiesPerMovie = 2; //Number of properties provided per movie. Title, score, duration etc..

	/**
	 * Construct a MovieDatabaseUI.
	 * This runs on init.
	 */
	public MovieDatabaseUI() throws IOException{
		
		if(!Files.exists(this._path)) {
			Files.createFile(this._path);
		}
		this._movieDB = Files.readAllLines(this._path); // each line is a separate element in _movieDB
		
		
	}

	/**
	 * Start the movie database UI.
	 */
	public void startUI() throws IOException{
		_scanner = new Scanner(System.in);
		int input;		
		boolean quit = false;
		

		System.out.println("** FILMDATABAS **");

		while(!quit) {			
			input = getNumberInput(_scanner, 1, 4, getMainMenu());

			switch(input) {  // Really cool built-in method in java
			case 1: searchTitle(); break;  // break jumps out of switch-statement or for/while-loop
			case 2: searchReviewScore(); break;
			case 3: addMovie(); break;
			case 4: quit = true; 			
			}
		}
		//Close scanner to free resources
		_scanner.close();
	}
	/**
	 * Get input and translate it to a valid number.
	 * 
	 * @param scanner the Scanner we use to get input 
	 * @param min the lowest correct number
	 * @param max the highest correct number
	 * @param message message to user
	 * @return the chosen menu number 
	 */
	private int getNumberInput(Scanner scanner, int min, int max, String message) {
		int input = -1;

		while(input < 0) {
			System.out.println(message);
			try {
				input = Integer.parseInt(scanner.nextLine().trim());		
			} 
			catch(NumberFormatException nfe) {
				input = -1;
			}
			if(input < min || input > max) {
				System.out.println("Ogiltigt värde.");
				input = -1;
			}			
		}			
		return input;
	}
	/**
	 * Get search string from user, search title in the movie 
	 * database and present the search result.
	 */
	private void searchTitle() {
		System.out.print("Ange sökord: ");
		String wantedTitle = _scanner.nextLine().trim().toLowerCase();

		int counter = 0;
		int n;
		int props = this._propertiesPerMovie;  // currently = 2
		//See comment regarding indexes and n under method addMovie()
		for (n = 0; n <this._movieDB.size()/props;n++){
			String title = this._movieDB.get(props*n);
			String titleLow = title.toLowerCase();
			String reviewScore = this._movieDB.get(props*n+1);
			if (titleLow.contains(wantedTitle)){
				System.out.print("\nTitel: " + title + " Betyg: " + reviewScore + "/5");
				counter += 1;
			}

		}

		if(n==0){
			System.out.println("Inga titlar existerar i databasen än.");
		}

		if (counter == 0){
			System.out.println("Inga filmer med önskad titel existerar i databasen.");
		}
		
	}
	/**
	 * Get search string from user, search review score in the movie 
	 * database and present the search result.
	 */
	private void searchReviewScore() {		
		int review = getNumberInput(_scanner, 1, 5, "Ange minimibetyg (1 - 5): ");

		int n;
		int props = this._propertiesPerMovie;
		//See comment regarding indexes and n under method addMovie()
		for (n = 0; n <this._movieDB.size()/props;n++){
			String title = this._movieDB.get(props*n);
			String reviewScore = this._movieDB.get(props*n+1);
			if (Integer.parseInt(reviewScore) >= review){
				System.out.print("\nTitel: " + title + " Betyg: " + reviewScore + "/5");
			}

		}

		if(n==0){
			System.out.print("Inga titlar existerar i databasen än.");
		}
		
	}	
	/**
	 * Get information from user on the new movie and add
	 * it to the database.
	 */
	private void addMovie() throws IOException{
		System.out.print("Titel: ");
		String title = _scanner.nextLine().trim();
		int reviewScore = getNumberInput(_scanner, 1, 5, "Betyg (1 - 5): ");

		//index = 2*n = title, index = 2*n+1 = reviewScore | for all n belonging to the natural numbers (n=0,1,2,3,....) 
		// | n represents one movie | easy to add other properties such as duration by adding 2*n+2 etc.. 
		// I wanted to use a json file, but it seemed very tricky in java? (very open to advice) 
		
		this._movieDB.add(title);
		this._movieDB.add(Integer.toString(reviewScore));
		// If any other properties are added in future remember to increase _propertiesPerMovie
		Files.write(this._path, this._movieDB);
		
	}	
	/**
	 * Return the main menu text.
	 * 
	 * @return the main menu text
	 */
	private String getMainMenu() {
		return  "\n-------------------\n" +
				"1. Sök på titel\n" +
				"2. Sök på betyg\n" +	
				"3. Lägg till film\n" +
				"-------------------\n" + 
				"4. Avsluta";
	}	
}