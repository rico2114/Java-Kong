package juan.project.world.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

import juan.project.graphics.Renderable;
import juan.project.world.CollisionType;
import juan.project.world.Dimension;
import juan.project.world.Position;

/**
 * Created with eclipse 8/10/2015 20:07:55
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class ActorModel implements Renderable {
		
	/**
	 * Represents the {@link BufferedImage image} for this model
	 */
	private BufferedImage image;
	
	/**
	 * Represents the {@link Position position} for this model
	 */
	private final Position position;
	
	/**
	 * Represents the {@link Dimension dimension} for this model
	 */
	private final Dimension dimension;
	
	/**
	 * Constructs the actor model based on the parameters
	 * @param image	the image for the model (can be null if we are overriding the render method)
	 * @param position	the position for the model
	 * @param dimension	the dimension of the model
	 */
	public ActorModel(final BufferedImage image, final Position position, final Dimension dimension) {
		this.image = image;
		this.position = position;
		this.dimension = dimension;
	}
			
	/**
	 * Sets the current {@link BufferedImage image} into a new image
	 * @param image	the new image 
	 */
	public void setImage(final BufferedImage image) {
		this.image = image;
	}
	
	/**
	 * Gets the {@link BufferedImage image}
	 * @return	the image
	 */
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * Gets the {@link Position position} of the model	
	 * @return	the position
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Gets the {@link Dimension dimension} of the model
	 * @return	the dimension
	 */
	public Dimension getDimension() {
		return dimension;
	}
	
	/**
	 * Represents the model {@link CollisionType collision type}
	 * @return	the collision type
	 */
	public CollisionType getCollisionType() {
		return CollisionType.NOT_COLLIDABLE;
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.drawImage(image, position.getX(), position.getY(), null);
	}
}
