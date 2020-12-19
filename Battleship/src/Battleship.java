/**
 * A class for a battleship (ship of length 4) in the game Battleship
 * 
 * @author pgaissert
 *
 */
public class Battleship extends Ship {

	/**
	 * Constructor for an instance of Battleship
	 */
	public Battleship() {
		// set the length to 4
		this.length = 4;
	}

	@Override
	public String getShipType() {
		// to be concatenated after "You sunk "
		return "the battleship";
	}

}
