package juan.project.world;

import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;

/**
 * Created with eclipse 22/11/2015 12:05:11
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class LevelConfiguration {

	/**
	 * Represents some special level configurations as the movement direction
	 */
	private final MoveDirection constructDirection;
	
	/**
	 * Represents the barrel complexity (time)
	 */
	private final int barrelComplexity;
	
	/**
	 * Constructs the level configuration
	 * @param constructDirection	the level configuration
	 * @param barrelComplexity	the barrel complexity
	 */
	public LevelConfiguration(final MoveDirection constructDirection, final int barrelComplexity) {
		this.constructDirection = constructDirection;
		this.barrelComplexity = barrelComplexity;
	}
	
	/**
	 * Gets the construction direction of the level
	 * @return	the construct direction
	 */
	public MoveDirection getConstructDirection() {
		return constructDirection;
	}
	
	/**
	 * Gets the barrel complexity
	 * @return	the barrel complexity
	 */
	public int getBarrelComplexity() {
		return barrelComplexity;
	}
}
