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
import juan.project.world.entity.event.impl.MoveEvent;
import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;

/**
 * Created with eclipse 12/10/2015 0:53:57
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Barrel extends CollidableActor {

	private final List<Stair> stairsCompleted = new ArrayList<>();
	
	private boolean goingRight = true;
	private boolean goingDown;
	private boolean goingDownEffective;
	
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
			System.out.println("GAME END,");
		}
	}
	
	@Override
	public void render(Graphics2D g2d) {
		int speed = 3; // add complexity
		
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
				
		if (getFloorId() == GameMap.getDivisions() - 2) {
			GameMap.deleteActor(this);
		}
		
		super.render(g2d);
	}
}
