package juan.project.world.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import juan.project.graphics.Renderable;
import juan.project.world.CollisionType;
import juan.project.world.Dimension;
import juan.project.world.Position;

/**
 * Created with eclipse 8/10/2015 20:07:55
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class ActorModel implements Renderable {
	
	private BufferedImage image;
	private final Position position;
	private final Dimension dimension;
	
	public ActorModel(final BufferedImage image, final Position position, final Dimension dimension) {
		this.image = image;
		this.position = position;
		this.dimension = dimension;
	}
		
	public void setImage(final BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	
	public CollisionType getCollisionType() {
		return CollisionType.NOT_COLLIDABLE;
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.drawImage(image, position.getX(), position.getY(), null);
	}
}
