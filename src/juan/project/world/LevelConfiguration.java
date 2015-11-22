package juan.project.world;

import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;

/**
 * Created with eclipse 22/11/2015 12:05:11
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class LevelConfiguration {

	private final MoveDirection constructDirection;
	private final int barrelComplexity;
	
	public LevelConfiguration(final MoveDirection constructDirection, final int barrelComplexity) {
		this.constructDirection = constructDirection;
		this.barrelComplexity = barrelComplexity;
	}
	
	public MoveDirection getConstructDirection() {
		return constructDirection;
	}
	
	public int getBarrelComplexity() {
		return barrelComplexity;
	}
}
