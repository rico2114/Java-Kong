package juan.project.world.entity.event.impl;

import juan.project.world.entity.event.Event;

/**
 * Created with eclipse 8/10/2015 20:29:42
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class MoveEvent implements Event {

	/**
	 * Represents the movement direction
	 */
	private final MoveDirection direction;
	
	/**
	 * Represents the magnitude aka the speed
	 */
	private final int magnitude;
	
	/**
	 * Constructs the movement event
	 * @param direction	the direction
	 * @param magnitude	the speed
	 */
	public MoveEvent(final MoveDirection direction, final int magnitude) {
		this.direction = direction;
		this.magnitude = magnitude;
	}
		
	/**
	 * Gets the magnitude of the direction
	 * @return	the magnitude
	 */
	public int getMagnitude() {
		return magnitude;
	}
	
	/**
	 * Gets the movement direction
	 * @return	the movement direction
	 */
	public MoveDirection getDirection() {
		return direction;
	}
	
	/**
	 * @author Sebastían
	 * 
	 * Represent the movement directions
	 */
	public enum MoveDirection {
		UP,
		RIGHT,
		DOWN,
		LEFT;
	}
}
