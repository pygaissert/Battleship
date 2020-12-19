import java.util.Random;

/**
 * A class for the ocean in the game Battleship
 * 
 * @author Philipp Gaissert
 *
 */
public class Ocean {

	/* INSTANCE VARIABLES */

	// used to quickly determine which ship is in any given location
	private Ship[][] ships;
	// the total number of shots fired by the user
	private int shotsFired;
	// the number of times a shot hit a ship
	// if the user shoots the same part of a ship more than once,
	// every hit is counted, even though the additional "hits" don't do any good
	private int hitCount;
	// the number of ships sunk (10 ships in all)
	private int shipsSunk;
	// the number of remaining ships
	private int shipsRemaining;

	/* METHODS */

	/**
	 * Constructor for an instance of Ocean
	 */
	public Ocean() {
		// create a new 10x10 ships array
		ships = new Ship[10][10];
		// populate the ships array with EmptySeas
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				// place an EmptySea at the current square
				ships[i][j] = new EmptySea();
				// set the EmptySea's bowRow
				ships[i][j].setBowRow(i);
				// set the EmptySea's bowColumn
				ships[i][j].setBowColumn(j);
			}
		}
		// initialize game variables as 0 (except for shipsRemaining)
		shotsFired = 0;
		hitCount = 0;
		shipsSunk = 0;
		shipsRemaining = 10;
	}

	/**
	 * Places all ten ships randomly on the empty ocean
	 */
	public void placeAllShipsRandomly() {
		// will hold each new ship
		Ship ship;
		// will be used to generate random values
		Random rand = new Random();
		// will hold randomly generated values for bowRow
		int bowRow;
		// will hold randomly generated values for bowColumn
		int bowColumn;
		// will hold randomly generated values for horizontal
		boolean horizontal;
		// create and place each ship in the fleet in descending order of length
		for (int i = 0; i < 10; i++) {
			if (i == 0) {
				// 1x battleship (length 4)
				ship = new Battleship();
			} else if (i == 1 || i == 2) {
				// 2x cruisers (length 3)
				ship = new Cruiser();
			} else if (3 <= i && i <= 5) {
				// 3x destroyers (length 2)
				ship = new Destroyer();
			} else {
				// 4x submarines (length 1)
				ship = new Submarine();
			}
			// keep generating random values the new ship
			// until it is okay to place it
			while (true) {
				// generate a random int for bowRow
				bowRow = rand.nextInt(10);
				// generate a random int for bowColumn
				bowColumn = rand.nextInt(10);
				// generate a random boolean for horizontal
				horizontal = rand.nextBoolean();
				// check if it is okay to place the ship with the random values
				if (ship.okToPlaceShipAt(bowRow, bowColumn, horizontal, this)) {
					// if it is okay, then place the ship
					ship.placeShipAt(bowRow, bowColumn, horizontal, this);
					// break from the while loop and proceed to next ship
					break;
				}
			}
		}
	}

	/**
	 * Return true if the given location contains a ship
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean isOccupied(int row, int column) {
		// return false if the location contains an EmptySea
		if (ships[row][column].getShipType().equals("empty")) {
			return false;
		}
		// return false if the location contains a sunken ship
		if (ships[row][column].isSunk()) {
			return false;
		}
		// otherwise return true
		return true;
	}

	/**
	 * Shoots at the given location and returns true if the location contains a
	 * "real" ship (still afloat)
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	boolean shootAt(int row, int column) {
		// get the ship at the given location
		Ship ship = this.ships[row][column];
		// increment the hit count if the location contains a "real" ship
		if (isOccupied(row, column)) {
			this.hitCount++;
		}
		// shoot at the given location
		boolean hit = ship.shootAt(row, column);
		// if a ship was just hit AND sunk...
		if (hit && ship.isSunk()) {
			// ...increment the # of ships sunk
			shipsSunk++;
			// and decrement the # of ships remaining
			shipsRemaining--;
		}
		// increment the # of shots fired by 1
		this.shotsFired++;
		return hit;
	}

	/**
	 * Returns the number of shots fired
	 * 
	 * @return
	 */
	public int getShotsFired() {
		return this.shotsFired;
	}

	/**
	 * Returns the number of hits recorded (in this game)
	 * 
	 * @return
	 */
	public int getHitCount() {
		// all hits are counted, not just the first time a given square is hit
		return this.hitCount;
	}

	/**
	 * Returns the number of ships sunk (in this game)
	 * 
	 * @return
	 */
	public int getShipsSunk() {
		return this.shipsSunk;
	}

	/**
	 * Returns the number of remaining ships (in this game)
	 * 
	 * @return
	 */
	public int getShipsRemaining() {
		return this.shipsRemaining;
	}

	/**
	 * Returns true if all ships have been sunk
	 * 
	 * @return
	 */
	public boolean isGameOver() {
		return this.shipsSunk == 10;
	}

	/**
	 * Returns the 10x10 array of ships
	 * 
	 * @return
	 */
	public Ship[][] getShipArray() {
		/*
		 * Returns the 10x10 array of ships Methods in the Ship class that take an Ocean
		 * parameter really need to be able to look at the contents of this array
		 * placeShipAt() even needs to modify it While it is undesirable to allow
		 * methods in one class to directly access instance variables in another class,
		 * sometimes there is just no good alternative
		 */
		return this.ships;
	}

	/**
	 * Prints the ocean grid
	 */
	public void print() {
		/*
		 * Prints the ocean Row numbers should be displayed along the left edge of the
		 * array, and column numbers should displayed along the top Numbers should be 0
		 * to 9 Use 'S' to indicate a location that you have fired upon and hit a "real"
		 * ship Use '-' to indicate a location that you have fired upon and found
		 * nothing Use 'x' to indicate a location containing a sunken ship Use '.' to
		 * indicate a location that you have never fired upon The only method in Ocean
		 * that does any input/output NEVER called from within the Ocean class, only
		 * from BattleShipGame
		 */
		System.out.println("    |  0  |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |  9  |");
		System.out.println("----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+");
		for (int i = 0; i < 10; i++) {
			System.out.printf("  %d |", i);
			for (int j = 0; j < 10; j++) {
				System.out.printf("  %s  |", this.ships[i][j].isHit(i, j) ? this.ships[i][j] : ".");
			}
			System.out.println("");
			System.out.println("----+-----+-----+-----+-----+-----+-----+-----+-----+-----+-----+");
		}
	}
}
