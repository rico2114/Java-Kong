package juan.project.world;

/**
 * Created with eclipse 8/10/2015 20:12:29
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Dimension {

	private int width;
	private int height;
	
	public Dimension(final int width, final int height) {
		this.width = width;
		this.height = height;
	}
	
	public Dimension setWidth(final int width) {
		this.width = width;
		return this;
	}
	
	public int getWidth() {
		return width;
	}
	
	public Dimension setHeight(final int height) {
		this.height = height;
		return this;
	}
	
	public int getHeight() {
		return height;
	}
}
