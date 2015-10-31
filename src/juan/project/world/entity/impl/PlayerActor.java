package juan.project.world.entity.impl;

import java.awt.Graphics2D;

import juan.project.graphics.Assets;
import juan.project.world.Dimension;
import juan.project.world.Position;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.event.handler.impl.RefreshDirectionHandler;
import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;
import juan.project.world.entity.event.impl.RefreshDirectionEvent;

/**
 * Created with eclipse 8/10/2015 20:28:22
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class PlayerActor extends ActorModel {

	private static final int MAXIMUM_JUMP_MOD = 40;
	
	private long lastUpdate;
	private int jumpYOriginal = -1;
	private boolean isJumping;
	private int jumpMod;
	private boolean increasingJump = true;
	
	private boolean inStair;
	private int walkingImage;
	private long lastWalkingUpdate = System.currentTimeMillis();
	private MoveDirection lastDirection = MoveDirection.RIGHT;

	public PlayerActor(Position position) {
		super(Assets.IMAGES[Assets.PLAYER_DEFAULT_RIGHT], position, new Dimension(Assets.IMAGES[Assets.PLAYER_DEFAULT_RIGHT].getWidth(), Assets.IMAGES[Assets.PLAYER_DEFAULT_RIGHT].getHeight()));
	}
	
	public void setInStair(final boolean state) {
		this.inStair = state;
	}
	
	public boolean isInStair() {
		return inStair;
	}
	
	public void setWalkingUpdate(final long update) {
		this.lastWalkingUpdate = update;
	}
	
	public long getWalkingUpdate() {
		return lastWalkingUpdate;
	}
	
	public void setLastDirection(final MoveDirection direction) {
		this.lastDirection = direction;
	}
	
	public MoveDirection getLastDirection() {
		return lastDirection;
	}
	
	public void setWalkingImage(final int walkingImage) {
		this.walkingImage = walkingImage;
	}
	
	public int getWalkingImage() {
		return walkingImage;
	}
	
	public void setJumping(final boolean state) {
		isJumping = state;
	}
	
	public boolean isJumping() {
		return isJumping;
	}
	
	public void setJumpingMod(final int mod) {
		this.jumpMod = mod;
	}
	
	public int getJumpingMod() {
		return jumpMod;
	}
	
	@Override
	public void render(Graphics2D g2d) {
		int y = getPosition().getY();
		if (System.currentTimeMillis() - lastUpdate >= 15) {
			if (isJumping) {
				if (jumpYOriginal == -1) {
					jumpYOriginal = y;
				}

				if (increasingJump) {
					y -= jumpMod;
				} else {
					y += jumpMod;
				}

				if (Math.abs(y - jumpYOriginal) >= MAXIMUM_JUMP_MOD) {
					increasingJump = false;
				}

				if (y == jumpYOriginal) {
					jumpYOriginal = -1;
					jumpMod = 0;
					isJumping = false;
					increasingJump = true;
					RefreshDirectionHandler.REFRESH_DIRECTION_HANDLER.interact(this, RefreshDirectionEvent.REFRESH_DIRECTION_EVENT);
				}
			}
			lastUpdate = System.currentTimeMillis();
		}
		
		getPosition().setY(y);
		/*if (jumpingAltitude > 0) {
			if (increasingJump) {
				y -= jumpingModifier;
				jumpingModifier += jumpDecreaseRate;
			} else {
				y += jumpingModifier;
				jumpingModifier -= jumpDecreaseRate;
			}
						
			if (jumpingModifier >= jumpingAltitude) {
				increasingJump = false;
			}
			
			if (!increasingJump && jumpingModifier <= 0) {
				jumpingAltitude = 0;
				jumpingModifier = 0;
				jumpDecreaseRate = 0;
				
				RefreshDirectionHandler.REFRESH_DIRECTION_HANDLER.interact(this, RefreshDirectionEvent.REFRESH_DIRECTION_EVENT);
			}
		}*/
		
		///getPosition().setY(y); FIX THIS! TO COLIDE ON JUMP
		
		g2d.drawImage(getImage(), getPosition().getX(), y/*getPosition().getY()*/, null);
	}

}
