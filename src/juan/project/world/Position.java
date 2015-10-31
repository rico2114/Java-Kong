package juan.project.world;

/**
 * Created with eclipse 8/10/2015 20:08:09
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Position {

	private int x;
	private int y;
	
	public Position(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(final int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(final int y) {
		this.y = y;
	}
	
	public int getY() {
		return y;
	}
}
