/**
 * A class for a destroyer (ship of length 2) in the game Battleship
 * 
 * @author pgaissert
 *
 */
public class Destroyer extends Ship {

	/**
	 * Constructor for an instance of Destroyer
	 */
	public Destroyer() {
		// sets the length to 2
		this.length = 2;
	}

	@Override
	public String getShipType() {
		// to be concatenated after "You sunk "
		return "a destroyer";
	}

}
