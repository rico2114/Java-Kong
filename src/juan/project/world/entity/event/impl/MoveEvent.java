package juan.project.world.entity.event.impl;

import juan.project.world.entity.event.Event;

/**
 * Created with eclipse 8/10/2015 20:29:42
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class MoveEvent implements Event {

	private final MoveDirection direction;
	private final int magnitude;
	
	public MoveEvent(final MoveDirection direction, final int magnitude) {
		this.direction = direction;
		this.magnitude = magnitude;
	}
		
	public int getMagnitude() {
		return magnitude;
	}
	
	public MoveDirection getDirection() {
		return direction;
	}
	
	public enum MoveDirection {
		UP,
		RIGHT,
		DOWN,
		LEFT;
	}
}
