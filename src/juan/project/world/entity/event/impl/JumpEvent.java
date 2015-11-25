package juan.project.world.entity.event.impl;

import juan.project.world.entity.event.Event;

/**
 * Created with eclipse 8/10/2015 21:33:25
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class JumpEvent implements Event {

	/**
	 * Represents the altitude of the jump
	 */
	private final int altitude;
	
	/**
	 * Represents the jump mod
	 */
	private final float jumpMod;
	
	/**
	 * Constructs the jump event
	 * @param altitude	the altitude
	 * @param decreaseRate	the decrease rate
	 */
	public JumpEvent(final int altitude, final float decreaseRate) {
		this.altitude = altitude;
		this.jumpMod = decreaseRate;
	}
	
	/**
	 * Gets the decrease rate (same as the increase rate)
	 * @return	the decrease rate 
	 */
	public float getDecreaseRate() {
		return jumpMod;
	}
	
	/**
	 * Gets the altitude
	 * @return	the altitude
	 */
	public int getAltitude() {
		return altitude;
	}
}
