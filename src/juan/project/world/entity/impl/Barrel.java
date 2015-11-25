package juan.project.world.entity.impl;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import juan.project.graphics.Assets;
import juan.project.world.Dimension;
import juan.project.world.GameLogic;
import juan.project.world.GameStage;
import juan.project.world.Position;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.CollidableActor;
import juan.project.world.entity.event.handler.impl.MovementHandler;
import juan.project.world.entity.event.handler.impl.RefreshDirectionHandler;
import juan.project.world.entity.event.impl.MoveEvent;
import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;
import juan.project.world.entity.event.impl.RefreshDirectionEvent;

/**
 * Created with eclipse 12/10/2015 0:53:57
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Barrel extends CollidableActor {

	/**
	 * Represents all the processed collisions (cached)
	 */
	private List<CollidableActor> processedCollisions = new ArrayList<>();
	
	/**
	 * Represents all the stairs completed to avoid some pesky issues
	 */
	private final List<Stair> stairsCompleted = new ArrayList<>();
	
	/**
	 * Should we deregister this actor
	 */
	private boolean deregister = false;
	
	/**
	 * Is our barrel going right
	 */
	private boolean goingRight = true;
	
	/**
	 * Is our going down
	 */
	private boolean goingDown;
	
	/**
	 * Are we going down and it has been possible
	 */
	private boolean goingDownEffective;
	
	/**
	 * Is our barrel in the stair
	 */
	private boolean inStair;
	
	/**
	 * Represents the floor id of the barrel
	 */
	private int floorId = 0;
	
	/**
	 * Represents the default animation id of the barrel
	 */
	private int animationId = Assets.BARREL_FIRST;
	
	/**
	 * Represents the last animation change
	 */
	private long lastAnimationChange;

	/**
	 * Constructs the barrel based on the position
	 * @param position	the position of the barrel
	 */
	public Barrel(Position position) {
		super(Assets.IMAGES[Assets.BARREL_FIRST], position, new Dimension(Assets.IMAGES[Assets.BARREL_FIRST].getWidth(), Assets.IMAGES[Assets.BARREL_FIRST].getHeight()));
	}
	
	/**
	 * Sets the last animation change
	 * @param time	the time
	 */
	public void setLastAnimationChange(final long time) {
		this.lastAnimationChange = time;
	}
	
	/**
	 * Gets the last animation change
	 * @return	the last animation change
	 */
	public long getLastAnimationChange() {
		return lastAnimationChange;
	}
	
	/**
	 * Gets the animation id
	 * @return	the animation id
	 */
	public int getAnimationId() {
		return animationId;
	}
	
	/**
	 * Sets the animation id to a desire id
	 * @param id	the id
	 */
	public void setAnimationId(final int id) {
		this.animationId = id;
	}
	
	/**
	 * Sets the going right variable state to a desired state
	 * @param state	the state
	 */
	public void setGoingRight(final boolean state) {
		this.goingRight = state;
	}
	
	/**
	 * Is our barrel going right or not
	 * @return	true if it is
	 */
	public boolean isGoingRight() {
		return goingRight;
	}
	
	/**
	 * Sets the going down to a desired state
	 * @param state	the state
	 */
	public void setGoingDown(final boolean state) {
		this.goingDown = state;
	}
	
	/**
	 * Is our barrel in the stair
	 * @return	true if it is
	 */
	public boolean isInStair() {
		return inStair;
	}
	
	/**
	 * Sets the going down effective to a desired state
	 * @param state	the state
	 */
	public void setGoingDownEffective(final boolean state) {
		this.goingDownEffective = state;
	}
	
	/**
	 * Increases the floor 
	 */
	public void increaseFloor() {
		floorId ++;
	}
	
	/**
	 * Gets the floor id
	 * @return	the floor id
	 */
	public int getFloorId() {
		return floorId;
	}
	
	/**
	 * Adds an stair into the stiar completed list
	 * @param stair	the stair
	 */
	public void addStair(final Stair stair) {
		stairsCompleted.add(stair);
	}
	
	/**
	 * Has our barrel completed the stair
	 * @param stair	the stair
	 * @return	true if it has
	 */
	public boolean hasCompletedStair(final Stair stair) {
		return stairsCompleted.contains(stair);
	}
	
	@Override
	public void doCollision(ActorModel actor) {
		if (actor.getClass().equals(PlayerActor.class)) {
			final PlayerActor player = (PlayerActor) actor;
			if (player.hasHammer()) {
				
				
				if (goingRight && player.getLastDirection().equals(MoveDirection.LEFT)) {
					player.setWalkingImage(Assets.PLAYER_HAMMER_SMASH_LEFT);
					deregister = true;
				}
				
				if (!goingRight && player.getLastDirection().equals(MoveDirection.RIGHT)) {
					player.setWalkingImage(Assets.PLAYER_HAMMER_SMASH_RIGHT);
					deregister = true;
				}
				
				if (goingDown && !player.isInStair()) {
					player.setWalkingImage(Assets.PLAYER_HAMMER_SMASH_RIGHT);
					deregister = true;
				}
				
				if (player.isInStair()) {
					deregister = false;
				}
				
				if (deregister) {
					player.smashBarrel();
					RefreshDirectionHandler.REFRESH_DIRECTION_HANDLER.interact(((ActorModel) actor), RefreshDirectionEvent.REFRESH_DIRECTION_EVENT);
				}
			}
			
			if (!deregister) {
				GameLogic.displayMessage(GameStage.DEAD, "You have lost", "You were struck by barrel.");
			}
				
		}
	}
	
	@Override
	public void render(Graphics2D g2d) {
		int speed = 3;
		
		processedCollisions = GameLogic.getActorCollition(this);
		inStair = GameLogic.actorCollidesWith(processedCollisions, Stair.class) != null;
		
		if (!goingDownEffective) {
			if (goingRight) {
				MovementHandler.MOVEMENT_HANDLER.interact(this, new MoveEvent(MoveDirection.RIGHT, speed));
			} else {
				MovementHandler.MOVEMENT_HANDLER.interact(this, new MoveEvent(MoveDirection.LEFT, speed));
			}
		}
		
		if (goingDown) {					
			MovementHandler.MOVEMENT_HANDLER.interact(this, new MoveEvent(MoveDirection.DOWN, speed));
		}
				
		if ((getFloorId() == GameLogic.getDivisions() - 2) || deregister) {
			GameLogic.deleteActor(this);
		}
		
		super.render(g2d);
	}
}
