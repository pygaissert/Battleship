import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for playing the game Battleship
 * 
 * @author Philipp Gaissert
 *
 */
public class BattleshipGame {

	/* INSTANCE VARIABLES */

	// the ocean that contains the fleet of ships
	Ocean ocean;
	// used for getting inputs from the player
	Scanner scnr;
	// the lowest number of shots of any game played
	int lowestNumOfShots;

	/* METHODS */

	/**
	 * Runs the BattleshipGame program
	 */
	public void run() {
		/*
		 * Calling playOneGame() from within run() and letting it return
		 * after each game ends prevents multiple calls to playOneGame()
		 * from being pushed to the stack if the player plays multiple times
		 */
		
		// initialize lowest number of shots to 200 (arbitrarily high)
		lowestNumOfShots = 200;
		// initialize the Scanner to read inputs entered in the console
		scnr = new Scanner(System.in);
		// will hold inputs from the player
		String input;
		// play the first game of Battleship
		playOneGame();
		// after the first game, repeat this loop until the player quits
		while (true) {
			// ask the player if they want to play again
			System.out.printf("\nWould you like to play again (y/n)?: ");
			// get input from the player
			input = scnr.nextLine();
			// if the player enters "Y" or "y"...
			if (input.matches("[Yy]")) {
				// ...play another game
				playOneGame();
				// after the game is done, return to the start of the loop
				continue;
			}
			// if the player enters "N" or "n"...
			else if (input.matches("[Nn]")) {
				// ...wish the player goodbye
				System.out.println("Thank you for playing. Goodbye!");
				// and break the loop, ending the program
				break;
			}
			// if the player gave an invalid input...
			else {
				// tell the player, and return to start of the loop
				System.out.println("Invalid response.");
			}
		}
	}

	/**
	 * Play a single game of Battleship
	 */
	private void playOneGame() {
		// will indicate if the previous shot hit a ship
		// initialize to false, since no shots have been fired
		boolean hit = false;
		// will store the row and column of the previous shot
		int[] coords = new int[2];
		// create a new instance of Ocean (initializing the game variables)
		ocean = new Ocean();
		// randomly place the ships
		ocean.placeAllShipsRandomly();
		// repeat this loop until the game is over
		while (!ocean.isGameOver()) {
			// print the ocean display (updated before each shot)
			printOcean();
			// if the game has just started...
			if (ocean.getShotsFired() == 0) {
				// print this message
				System.out.println("You must eliminate the enemy fleet!");
			}
			// for every subsequent turn...
			else {
				// ...print the results of the previous shot
				printShotResults(coords[0], coords[1], hit);
			}
			// print the number of sunken ships and remaining ships
			printFleetStatus();
			// ask the player where they want to fire
			// and get the row and column (as an array)
			coords = askWhereToFire();
			// fire at the location given by the player
			// and get the result
			hit = playerShoot(coords[0], coords[1]);
		}
		// after the game is over
		// print the ocean one more time
		printOcean();
		// print the shot results one more time
		printShotResults(coords[0], coords[1], hit);
		// print the game over message
		printGameOver();
	}

	/**
	 * Asks where the player wants to fire next
	 * @return
	 */
	private int[] askWhereToFire() {
		String input;
		int[] coords;
		System.out.printf("Where will you fire next ([ROW],[COLUMN])?: ");
		while (true) {
			// get input from the player
			input = scnr.nextLine();
			try {
				// attempt to extract coordinates from the player's input
				coords = getCoordsFromInput(input);
				// break from the loop if successful
				break;
			} catch (InvalidInputException e) {
				// notify the player if an invalid input was given
				System.out.printf("Invalid coordinates. Please try again: ");
				// return to the start of the loop
			}
		}
		// return the coordinates extracted from the player's input
		return coords;
	}
	
	/**
	 * Returns the row and column captured from the player's input
	 * @param input
	 * @return [row, column]
	 * @throws InvalidInputException
	 */
	public int[] getCoordsFromInput(String input) throws InvalidInputException {
		int[] coords = new int[2];
		// regex for validating input from the player:
		// 1) "\\(?" - 1 optional left parenthesis
		// 2) "[0-9]{1}" - exactly 1 digit (row)
		// 3) ",?\s?" - 1 optional comma, 1 optional space
		// 4) "[0-9]{1}" - exactly 1 digit (column)
		// 5) "\\)?" - 1 optional right parenthesis
		// this allows for flexibility when entering coordinates:
		// - with or without parentheses
		// - with or without a comma AND/OR whitespace in between
		String inputRegex = "\\(?[0-9]{1},?\s?[0-9]{1}\\)?";
		// regex used by the Matcher to extract coordinates from a valid input:
		// 1) "[^[0-9]]*" - ignore any non-digit characters before Capture Group 1
		// 2) "([0-9])" - Capture Group 1: single digit (row)
		// 3) "[^[0-9]]*" - ignore any non-digit characters between the Capture Groups
		// 4) "([0-9])" = Capture Group 2: single digit (column)
		// 5) "[^[0-9]]*" - ignore any non-digit character after Capture Group 2
		Pattern pattern = Pattern.compile("[^[0-9]]*([0-9])[^[0-9]]*([0-9])[^[0-9]]*");
		// if the player gave a valid input...
        // (see description of inputRegex above)
		if (input.matches(inputRegex)) {
			// create a Matcher for extracting the row and column from the input string
			Matcher matcher = pattern.matcher(input);
			// find the Capture Groups
			matcher.find();
			// get Capture Group 1 (row)
			coords[0] = Integer.parseInt(matcher.group(1));
			// get Capture Group 2 (column)
			coords[1] = Integer.parseInt(matcher.group(2));
		} 
		// if the player gave an invalid input...
		else {
			// throw an exception
			throw new InvalidInputException("The player entered invalid coordinates");
		}
		return coords;
	}
	
	
	/**
	 * Prints the Ocean display
	 */
	private void printOcean() {
		// "clear" the console with newline characters
		for (int i = 0; i < 50; i++) {
			System.out.printf("\n");
		}
		// print the ocean
		ocean.print();
	}

	/**
	 * Prints the results of the previous shot
	 * 
	 * @param row
	 * @param column
	 * @param hit
	 */
	private void printShotResults(int row, int column, boolean hit) {
		// echo back where the player just shot at (for clarity)
		System.out.printf("You fired at (%d, %d). ", row, column);
		// if the previous shot hit a ship...
		if (hit) {
			// ...get the ship at was hit
			Ship ship = this.ocean.getShipArray()[row][column];
			// if that ship was just sunk...
			if (ship.isSunk()) {
				// ...tell the player the type of ship they sunk
				System.out.printf(" You sank %s!\n", ship.getShipType());
			}
			// if the ship is still afloat...
			else {
				// ...tell the player they hit a ship
				System.out.println("That's a hit!");
			}
		}
		// if the previous shot missed...
		else {
			// ... tell the player
			System.out.println("That's a miss!");
		}
	}

	/**
	 * Shoots at a given location and confirms whether the shot hit a ship
	 * 
	 * @param row
	 * @param column
	 * @return true if the shot hit a ship, false if it missed
	 */
	private boolean playerShoot(int row, int column) {
		// shoot at the given location
		// and return the result
		return ocean.shootAt(row, column);
	}

	/**
	 * Prints the numbers of remaining ships and sunken ships
	 */
	private void printFleetStatus() {
		// print the number of remaining ships
		System.out.printf("Ships remaining: %d\n", this.ocean.getShipsRemaining());
		// print the number of sunken ships
		System.out.printf("Ships sunk: %d\n", this.ocean.getShipsSunk());
	}

	/**
	 * Prints the results of the game after the game is over
	 */
	private void printGameOver() {
		// notify the player that the game is over
		System.out.printf("\n* You have eliminated the enemy fleet. Game over! *\n\n");
		// get the number of shots fired (this game)
		int shotsFired = ocean.getShotsFired();
		System.out.printf("Number of shots fired: %d ", shotsFired);
		// if the player broke the record for lowest number of shots...
		if (shotsFired < this.lowestNumOfShots) {
			// ...notify the player...
			System.out.printf("(NEW RECORD)\n");
			// ... and update the record
			this.lowestNumOfShots = shotsFired;
		}
		// if the player did not break the record...
		else {
			// ...display the current record
			System.out.printf("(current record: %d)\n", this.lowestNumOfShots);
		}
	}

	public static void main(String[] args) {
		// create an instance of BattleshipGame
		BattleshipGame bsg = new BattleshipGame();
		// run Battleship (play first game, then ask player if they want to play again)
		bsg.run();
	}
}
