package juan.project.world.entity.impl;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import juan.project.graphics.Assets;
import juan.project.world.Dimension;
import juan.project.world.GameMap;
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

	private List<CollidableActor> processedCollisions = new ArrayList<>();
	private final List<Stair> stairsCompleted = new ArrayList<>();
	
	private boolean deregister = false;
	private boolean goingRight = true;
	private boolean goingDown;
	private boolean goingDownEffective;
	private boolean inStair;
	
	private int floorId = 0;
	private int animationId = Assets.BARREL_FIRST;
	
	private long lastAnimationChange;

	public Barrel(Position position) {
		super(Assets.IMAGES[Assets.BARREL_FIRST], position, new Dimension(Assets.IMAGES[Assets.BARREL_FIRST].getWidth(), Assets.IMAGES[Assets.BARREL_FIRST].getHeight()));
	}
	
	public void setLastAnimationChange(final long time) {
		this.lastAnimationChange = time;
	}
	
	public long getLastAnimationChange() {
		return lastAnimationChange;
	}
	
	public int getAnimationId() {
		return animationId;
	}
	
	public void setAnimationId(final int id) {
		this.animationId = id;
	}
	
	public void setGoingRight(final boolean state) {
		this.goingRight = state;
	}
	
	public boolean isGoingRight() {
		return goingRight;
	}
	
	public void setGoingDown(final boolean state) {
		this.goingDown = state;
	}
	
	public boolean isInStair() {
		return inStair;
	}
	
	public void setGoingDownEffective(final boolean state) {
		this.goingDownEffective = state;
	}
	
	public void increaseFloor() {
		floorId ++;
	}
	
	public int getFloorId() {
		return floorId;
	}
	
	public void addStair(final Stair stair) {
		stairsCompleted.add(stair);
	}
	
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
				
				if (deregister) {
					player.smashBarrel();
					RefreshDirectionHandler.REFRESH_DIRECTION_HANDLER.interact(((ActorModel) actor), RefreshDirectionEvent.REFRESH_DIRECTION_EVENT);
				}
			}
			
			if (!deregister)
				System.out.println("GAME END,");
		}
	}
	
	@Override
	public void render(Graphics2D g2d) {
		int speed = 3; //XXX: add complexity
		
		// XXX: I KNOW THIS IS UGLY, BUT I NEED TO CLEAN UP MOVEMENT FIRST YUK!
		processedCollisions = GameMap.getActorCollition(this);
		inStair = GameMap.actorCollidesWith(processedCollisions, Stair.class) != null;
		
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
				
		if ((getFloorId() == GameMap.getDivisions() - 2) || deregister) {
			GameMap.deleteActor(this);
		}
		
		super.render(g2d);
	}
}
