package juan.project.graphics;

import java.awt.Graphics2D;

/**
 * Created with eclipse 8/10/2015 21:09:49
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public interface Renderable {

	/**
	 * Renders its proper implementation 
	 * @param g2d	the graphics instance
	 */
	void render(final Graphics2D g2d);
}
