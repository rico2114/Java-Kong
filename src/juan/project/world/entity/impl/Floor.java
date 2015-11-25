package juan.project.world.entity.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import juan.project.util.BoundaryUtil;
import juan.project.world.Dimension;
import juan.project.world.Position;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.CollidableActor;
import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;

/**
 * Created with eclipse 8/10/2015 20:26:18
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Floor extends CollidableActor {
	
	/**
	 * Represent the floor continuity in direction
	 */
	private final List<MoveDirection> continuityIn = new ArrayList<>();
	
	/**
	 * Represents if whether or not is the floor an special floor
	 */
	private boolean specialFloor;
	
	/**
	 * Construct the floor
	 * @param position	the position of the floor
	 * @param dimension	the dimension of the floor
	 */
	public Floor(Position position, Dimension dimension) {
		super(null, position, dimension);
	}
	
	/**
	 * Adds continuity in the floor direction
	 * @param direction	the direction of the continuity
	 * @return	this instance for chaining
	 */
	public Floor addContinuity(final MoveDirection direction) {
		continuityIn.add(direction);
		return this;
	}
	
	/**
	 * Gets the continuity in direction
	 * @return	the list
	 */
	public List<MoveDirection> getContinuity() {
		return continuityIn;
	}

	@Override
	public void doCollision(ActorModel actor) {
	}
	
	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.decode("#87081D"));
		g2d.fillRect(getPosition().getX(), getPosition().getY(), getDimension().getWidth(), getDimension().getHeight());
	}
	
	@Override
	public boolean collidesWith(ActorModel actor) {
		return BoundaryUtil.inBounds(actor, this);
	}
	
	/**
	 * Sets this floor to the desired special floor
	 * @param state	the new state
	 */
	public void setSpecialFloor(final boolean state) {
		this.specialFloor = state;
	}
	
	/**
	 * Is our floor an special floor
	 * @return	true if so
	 */
	public boolean isSpecial() {
		return specialFloor;
	}

}
