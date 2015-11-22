package juan.project.world.entity.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;

import juan.project.game.Constants;
import juan.project.graphics.Assets;
import juan.project.world.Dimension;
import juan.project.world.GameMap;
import juan.project.world.Position;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.CollidableActor;
import juan.project.world.entity.event.handler.impl.RefreshDirectionHandler;
import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;
import juan.project.world.entity.event.impl.RefreshDirectionEvent;

/**
 * Created with eclipse 8/10/2015 20:28:22
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class PlayerActor extends ActorModel {

	private static final int MAXIMUM_JUMP_MOD = 40;
	
	private boolean smashedBarrel;
	private long smashDelay;
	
	public void smashBarrel() {
		smashedBarrel = true;
		smashDelay = 11;
	}
	
	public boolean smashDelayPassed() {
		return smashDelay <= 0;
	}
	
	private long lastUpdate;
	private int jumpYOriginal = -1;
	private boolean isJumping;
	private int jumpMod;
	private boolean increasingJump = true;
	
	private boolean inStair;
	private int walkingImage;
	private long lastWalkingUpdate = System.currentTimeMillis();
	private MoveDirection lastDirection = MoveDirection.RIGHT;
	
	private boolean hasHammer = false;
	private long hammerInitTime;

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
				
				if (!increasingJump) {
					CollidableActor offsetCollition = GameMap.getActorCollition(this, 1, Constants.SCALED_FLOOR_HEIGHT * 2, Floor.class);
					
					if (Objects.nonNull(offsetCollition)) {
						completeJump();
					}
				}
			}
			lastUpdate = System.currentTimeMillis();
		}
		
		if (hasHammer) {
			if (System.currentTimeMillis() - hammerInitTime >= 10_000) { // 10 seconds
				hasHammer = false;
				RefreshDirectionHandler.REFRESH_DIRECTION_HANDLER.interact(this, RefreshDirectionEvent.REFRESH_DIRECTION_EVENT);
			}
			
		}
		
		if (smashedBarrel) {
			smashDelay --;
			
			if (smashDelay <= 0) {
				smashedBarrel = false;
				RefreshDirectionHandler.REFRESH_DIRECTION_HANDLER.interact(this, RefreshDirectionEvent.REFRESH_DIRECTION_EVENT);
			}
		}
		
		getPosition().setY(y);
		g2d.drawImage(getImage(), getPosition().getX(), y, null);
		g2d.setColor(Color.RED);
		g2d.drawString(GameMap.getUsername(), getPosition().getX(), y);

	}
	
	/**
	 * Called when the jumping is finished
	 */
	private void completeJump() {
		jumpYOriginal = -1;
		jumpMod = 0;
		isJumping = false;
		increasingJump = true;
		RefreshDirectionHandler.REFRESH_DIRECTION_HANDLER.interact(this, RefreshDirectionEvent.REFRESH_DIRECTION_EVENT);
	}

	public boolean hasHammer() {
		return hasHammer;
	}
	
	public void addHammer() {
		hasHammer = true;
		hammerInitTime = System.currentTimeMillis();
	}
	

}
