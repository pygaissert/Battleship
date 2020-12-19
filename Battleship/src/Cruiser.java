/**
 * A class for a cruiser (ship of length 3) in the game Battleship
 * 
 * @author pgaissert
 *
 */
public class Cruiser extends Ship {

	/**
	 * Constructor for an instance of Cruiser
	 */
	public Cruiser() {
		// set the length of the ship to 3
		this.length = 3;
	}

	@Override
	public String getShipType() {
		// to be concatenated after "You sunk "
		return "a cruiser";
	}

}
