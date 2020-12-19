/**
 * A class for a submarine (ship of length 1) in the game Battleship
 * 
 * @author pgaissert
 *
 */
public class Submarine extends Ship {

	/**
	 * Constructor for an instance of Submarine
	 */
	public Submarine() {
		// set the length to 1
		this.length = 1;
	}

	@Override
	public String getShipType() {
		// to be concatenated after "You sunk "
		return "a submarine";
	}

}
