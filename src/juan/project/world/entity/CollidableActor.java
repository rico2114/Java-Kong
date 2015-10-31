package juan.project.world.entity;

import java.awt.image.BufferedImage;

import juan.project.util.BoundaryUtil;
import juan.project.world.CollisionType;
import juan.project.world.Dimension;
import juan.project.world.Position;

/**
 * Created with eclipse 8/10/2015 20:14:07
 * 
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public abstract class CollidableActor extends ActorModel {

	public CollidableActor(final BufferedImage image, Position position, Dimension dimension) {
		super(image, position, dimension);
	}

	public abstract void doCollision(final ActorModel actor);

	public boolean collidesWith(final ActorModel actor) {
		return BoundaryUtil.inBounds(this, actor);
	}

	@Override
	public CollisionType getCollisionType() {
		return CollisionType.COLLIDABLE;
	}
}
