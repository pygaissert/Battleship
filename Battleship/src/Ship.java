/**
 * Abstract class for all ships used in the game Battleship
 * 
 * @author Philipp Gaissert
 *
 */
public abstract class Ship {

	/* INSTANCE VARIABLES */

	// the row (0 to 9) which contains the bow/front of the ship
	protected int bowRow;
	// the column (0 to 9) which contains the bow/front of the ship
	protected int bowColumn;
	// the number of squares occupied by the ship
	protected int length;
	// true if the ship occupies a single row, false otherwise
	protected boolean horizontal;
	// array of booleans telling whether that part of the ship has been hit
	// BATTLESHIP - uses all 4 locations
	// CRUISER - uses the first 3
	// DESTROYER - uses the first 2
	// SUBMARINE - uses the first 1
	// EMPTYSEA - uses the first 1 or none
	protected boolean[] hit = new boolean[4];

	/* METHODS */

	/**
	 * Returns true if it is okay to put a ship of this length with its bow in this
	 * location with the given orientation
	 * 
	 * @param row
	 * @param column
	 * @param horizontal
	 * @param ocean
	 * @return
	 */
	public boolean okToPlaceShipAt(int row, int column, boolean horizontal, Ocean ocean) {
		/*
		 * must not overlap another ship, must not touch another ship (vertically,
		 * horizontally, diagonally), and must not stick out beyond the array
		 */
		// if the ship is horizontal...
		if (horizontal) {
			// check if any part of the ship will stick out beyond the array
			if ((column + this.length) > 10)
				return false;
			for (int i = 0; i < this.length; i++) {
				// check if the ship's potential spots are occupied
				if (ocean.isOccupied(row, column + i))
					return false;
				// check if there is already a ship in the row above
				if ((row - 1 >= 0) && (ocean.isOccupied(row - 1, column + i)))
					return false;
				// check if there is already a ship in the row below
				if ((row + 1 <= 9) && (ocean.isOccupied(row + 1, column + i)))
					return false;
			}
			for (int j = -1; j < 2; j++) {
				// check if there is already a ship in the column to the left
				if ((column - 1 >= 0) && (0 <= row + j) && (row + j <= 9)) {
					if (ocean.isOccupied(row + j, column - 1))
						return false;
				}
				// check if there is already a ship in the column to the right
				if ((column + this.length - 1 < 9) && (0 <= row + j) && (row + j <= 9)) {
					if (ocean.isOccupied(row + j, column + this.length))
						return false;
				}
			}
		}
		// if the ship is vertical...
		else {
			// check if any part of the ship will stick out beyond the array
			if ((row + this.length) > 10)
				return false;
			for (int k = 0; k < this.length; k++) {
				// check if the ship's potential spots are occupied
				if (ocean.isOccupied(row + k, column))
					return false;
				// check if there is already a ship in the column to the left
				if ((column - 1 >= 0) && (ocean.isOccupied(row + k, column - 1)))
					return false;
				// check if there is already a ship in the column to the right
				if ((column + 1 <= 9) && (ocean.isOccupied(row + k, column + 1)))
					return false;
			}
			for (int l = -1; l < 2; l++) {
				// check if there is already a ship in the row above
				if ((row - 1 >= 0) && (0 <= column + l) && (column + l <= 9)) {
					if (ocean.isOccupied(row - 1, column + l))
						return false;
				}
				// check if there is already a ship in the row below
				if ((row + this.length - 1 < 9) && (0 <= column + l) && (column + l <= 9)) {
					if (ocean.isOccupied(row + this.length, column + l))
						return false;
				}
			}
		}
		// if all the checks have been passed, it is okay to place the ship
		return true;
	}

	/**
	 * Places the ship in the ocean with the given location and orientation
	 * 
	 * @param row
	 * @param column
	 * @param horizontal
	 * @param ocean
	 */
	public void placeShipAt(int row, int column, boolean horizontal, Ocean ocean) {
		// get the ocean's ship array
		Ship[][] ships = ocean.getShipArray();
		// if the ship is horizontal...
		if (horizontal) {
			// place identical references to the ship in the appropriate spots
			// from left to right
			for (int i = 0; i < this.length; i++) {
				ships[row][column + i] = this;
			}
		}
		// if the ship is vertical...
		else {
			// place identical references to the ship in the appropriate spots
			// from top to bottom
			for (int j = 0; j < this.length; j++) {
				ships[row + j][column] = this;
			}
		}
		// give the values to the ship's instance variables
		this.bowRow = row;
		this.bowColumn = column;
		this.horizontal = horizontal;
	}

	/**
	 * Marks a part of the ship as hit
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean shootAt(int row, int column) {
		// if the ship has not been sunk...
		if (!isSunk()) {
			// ...update the "hit" array
			// if the ship is horizontal...
			if (horizontal) {
				// ...calculate the index of the hit array
				// and set that element to true
				this.hit[column - this.bowColumn] = true;
			}
			// if the ship is vertical...
			else {
				// ...calculate the index of the hit array
				// and set that element to true
				this.hit[row - this.bowRow] = true;
			}
			// return true to indicate that the ship was hit
			return true;
		}
		// return false if the ship has already been sunk
		return false;
	}

	/**
	 * Return true if all parts of the ship have been hit
	 * 
	 * @return
	 */
	public boolean isSunk() {
		// check if all locations used by the ship have been hit
		for (int i = 0; i < this.length; i++) {
			// return false if any location has not been hit
			if (!this.hit[i])
				return false;
		}
		// return true if all locations have been hit
		return true;
	}

	/**
	 * Returns true if the ship has been hit at the given location
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean isHit(int row, int column) {
		// if the ship is horizontal...
		if (horizontal) {
			// ...calculate the index of the hit array
			// and return the value at that index
			return this.hit[column - this.bowColumn];
		}
		// if the ship is vertical...
		else {
			// ...calculate the index of the hit array
			// and return the value at that index
			return this.hit[row - this.bowRow];
		}
	}

	@Override
	/**
	 * Returns a string that indicates whether or not the ship has been sunk, for
	 * use in Ocean.print()
	 */
	public String toString() {
		/*
		 * Returns "x" if the ship has been sunk Returns "S" if it has not been sunk
		 */
		if (isSunk()) {
			// return "x" if the ship has been sunk
			return "x";
		}
		// return "S" if the ship has not been sunk
		return "S";
	}

	/**
	 * Returns the length of the ship
	 * 
	 * @return length
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * Returns the row that contains the ship's bow (front)
	 * 
	 * @return bow row
	 */
	public int getBowRow() {
		return this.bowRow;
	}

	/**
	 * Returns the column that contains the ship's bow (front)
	 * 
	 * @return bow column
	 */
	public int getBowColumn() {
		return this.bowColumn;
	}

	/**
	 * Indicates whether or not the ship is horizontal
	 * 
	 * @return true if horizontal (facing left), false if vertical (facing up)
	 */
	public boolean isHorizontal() {
		return this.horizontal;
	}

	/**
	 * Sets the row that contains the ship's bow
	 * 
	 * @param bowRow
	 */
	public void setBowRow(int bowRow) {
		this.bowRow = bowRow;
	}

	/**
	 * Sets the column that contains the ship's bow
	 * 
	 * @param bowColumn
	 */
	public void setBowColumn(int bowColumn) {
		this.bowColumn = bowColumn;
	}

	/**
	 * Sets whether or not the ship is horizontal
	 * 
	 * @param horizontal
	 */
	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	/**
	 * Abstract method for returning the type of ship
	 * 
	 * @return
	 */
	public abstract String getShipType();

}
