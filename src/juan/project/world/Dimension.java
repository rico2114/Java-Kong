package juan.project.world;

/**
 * Created with eclipse 8/10/2015 20:12:29
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Dimension {

	/**
	 * Represents the width of a given object
	 */
	private int width;
	
	/**
	 * Represents the height of a given object
	 */
	private int height;
	
	/**
	 * Constructs the dimension of an object
	 * @param width	the width
	 * @param height	the height
	 */
	public Dimension(final int width, final int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Sets the width of an object
	 * @param width	the new width
	 * @return	this instance to use chaining
	 */
	public Dimension setWidth(final int width) {
		this.width = width;
		return this;
	}
	
	/**
	 * Gets the width of the object
	 * @return	the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Sets the height of the object
	 * @param height	the height
	 * @return	this instance for chaininig
	 */
	public Dimension setHeight(final int height) {
		this.height = height;
		return this;
	}
	
	/**
	 * Gets the height of the object
	 * @return	the height
	 */
	public int getHeight() {
		return height;
	}
}
