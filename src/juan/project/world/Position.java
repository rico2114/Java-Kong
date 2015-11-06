package juan.project.world;

/**
 * Created with eclipse 8/10/2015 20:08:09
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Position {

	/**
	 * Represents the x coordinate
	 */
	private int x;
	
	/**
	 * Represents the y coordinate
	 */
	private int y;
	
	/**
	 * Constructs a position based on the x and y
	 * @param x	the x coordinate
	 * @param y	the y coordinate
	 */
	public Position(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets the x coordinate of the possition
	 * @param x	the x coordinate
	 */
	public void setX(final int x) {
		this.x = x;
	}
	
	/**
	 * Gets the x coordinate
	 * @return	the x coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Sets the y coordinate for the position
	 * @param y	the new y coordinate
	 */
	public void setY(final int y) {
		this.y = y;
	}
	
	/**
	 * Gets the y coordinate
	 * @return	the y coordinate
	 */
	public int getY() {
		return y;
	}
}
