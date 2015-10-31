package juan.project.world.entity.event.impl;

import juan.project.world.entity.event.Event;

/**
 * Created with eclipse 8/10/2015 21:33:25
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class JumpEvent implements Event {

	private final int altitude;
	private final float jumpMod;
	
	public JumpEvent(final int altitude, final float decreaseRate) {
		this.altitude = altitude;
		this.jumpMod = decreaseRate;
	}
	
	public float getDecreaseRate() {
		return jumpMod;
	}
	
	public int getAltitude() {
		return altitude;
	}
}
