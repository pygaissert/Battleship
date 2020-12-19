/**
 * A class for a square that doesn't contain a ship in the game Battleship
 * @author Philipp Gaissert
 *
 */
public class EmptySea extends Ship {

	/**
	 * Constructor for an instance of EmptySea
	 */
	public EmptySea() {
		// set the length to 1
		this.length = 1;
	}
	
	@Override
	/**
	 * Always returns false to indicate no ship was hit
	 */
	public boolean shootAt(int row, int column) {
		this.hit[0] = true;
		return false;
	}
	
	@Override
	/**
	 * Always returns false to indicate that no ship was sunk
	 */
	public boolean isSunk() {
		return false;
	}
	
	@Override
	/**
	 * Returns "-" for use in Ocean.print()
	 */
	public String toString() {
		return "-";
	}
	
	@Override
	/**
	 * Returns "empty"
	 */
	public String getShipType() {
		return "empty";
	}

}
