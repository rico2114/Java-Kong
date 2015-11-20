package juan.project.world.entity.event.handler.impl;

import java.util.List;
import java.util.Objects;

import juan.project.game.Constants;
import juan.project.graphics.Assets;
import juan.project.util.RandomUtils;
import juan.project.world.Dimension;
import juan.project.world.GameMap;
import juan.project.world.Position;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.CollidableActor;
import juan.project.world.entity.event.handler.Handler;
import juan.project.world.entity.event.impl.MoveEvent;
import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;
import juan.project.world.entity.impl.Barrel;
import juan.project.world.entity.impl.Floor;
import juan.project.world.entity.impl.PlayerActor;
import juan.project.world.entity.impl.Stair;

/**
 * Created with eclipse 8/10/2015 20:32:32
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class MovementHandler implements Handler<MoveEvent> {

	private static final ActorModel SCREEN_COLLISION = new ActorModel(null, new Position(0, 0), new Dimension((int) Constants.DIMENSION.getWidth(), (int) Constants.DIMENSION.getHeight()));
	public static final MovementHandler MOVEMENT_HANDLER = new MovementHandler();
	
	/**
	 * XXX: Clean this uggly class!!! :C
	 */
	
	@Override
	public void interact(ActorModel actor, MoveEvent event) {
		final List<CollidableActor> actorsColliding = GameMap.getActorCollition(actor);
		
		CollidableActor floorCollition = GameMap.actorCollidesWith(actorsColliding, Floor.class);
		CollidableActor stairCollition = GameMap.actorCollidesWith(actorsColliding, Stair.class);

		boolean isPlayer = actor instanceof PlayerActor;
		boolean isBarrel = actor instanceof Barrel;

		PlayerActor playerActor = null;
		Barrel barrel = null;
		
		if (isPlayer) {
			playerActor = (PlayerActor) actor;
		}
		
		if (isBarrel) {
			barrel = (Barrel) actor;
		}
		
		int newY = 0;
		int newX = 0;
		
		if (isPlayer) {
			if (playerActor.isJumping() && !playerActor.getLastDirection().equals(event.getDirection())) {
				System.out.println("Can't jump and face another side!");
				return;
			}
		}
		
		switch (event.getDirection()) {
		case UP:
		case DOWN:
			if (Objects.isNull(stairCollition))
				return;
			
			int stairMinX = stairCollition.getPosition().getX() + Constants.STAIR_LINE_WIDTH;
			int stairMaxX = stairCollition.getPosition().getX() + (stairCollition.getDimension().getWidth() - actor.getDimension().getWidth()) ;
			
			if (actor.getPosition().getX() < stairMinX || actor.getPosition().getX() > stairMaxX) {
				return;
			}
						
			int distance = (stairCollition.getPosition().getY() + stairCollition.getDimension().getHeight());

			if (event.getDirection().equals(MoveDirection.UP)) {
				newY = -(stairCollition.getDimension().getHeight() / actor.getDimension().getHeight());
				if (isPlayer) {
					if (actor.getPosition().getY() + newY <= stairCollition.getPosition().getY() - actor.getDimension().getHeight()) {
						if (isPlayer)
							playerActor.setInStair(false);
						return;
					}
				}
			} else {
				newY = stairCollition.getDimension().getHeight() / actor.getDimension().getHeight();
				if (isPlayer || isBarrel) {
					if (actor.getPosition().getY() >= (distance - actor.getDimension().getHeight())) {
						if (isPlayer)
							playerActor.setInStair(false);
						else {
							Stair stair = (Stair) stairCollition;
							if (!barrel.hasCompletedStair(stair)) {
								barrel.setGoingDown(false);
								barrel.setGoingDownEffective(false);
								barrel.setAnimationId(Assets.BARREL_FIRST);
								barrel.setGoingRight(RandomUtils.RANDOM.nextBoolean());
								barrel.increaseFloor();
								barrel.addStair(stair);
							}
						}
						return;
					}
				} 
			}

			if (isPlayer) {
				playerActor.setInStair(true);
			} 
			
			if (isBarrel) {
				barrel.setGoingDownEffective(true);
				barrel.setAnimationId(Assets.BARREL_THIRD);
			}
			break;
			
		case LEFT:
		case RIGHT:
			if (isPlayer) {
				if (playerActor.isInStair())
					return;
			}
						
			int magnitude = event.getMagnitude();
			if (event.getDirection().equals(MoveDirection.LEFT)) {
				magnitude *=-1;
			}
			newX = magnitude;
			break;

		}
		
		ActorModel mainCollitionCause = floorCollition;
		if (Objects.isNull(mainCollitionCause)) {
			mainCollitionCause = stairCollition;
		}
		
		CollidableActor offsetCollition = GameMap.getActorCollition(actor, 1, Constants.SCALED_FLOOR_HEIGHT * 2, Floor.class);

		if (Objects.isNull(mainCollitionCause)) {
			boolean pass = false;
			if (isPlayer) {
				if (playerActor.isJumping())
					pass = true;
			}
			
			if (!pass) {
				mainCollitionCause = offsetCollition;
				if (Objects.nonNull(mainCollitionCause))
					pass = true;
			}
			
			if (!pass) {
				System.out.println("Im standing on nothing!");
				return;
			}
		}
		
		if (isPlayer) {
			if (playerActor.isJumping()) {
				newX /= 1.5;
			}
		}
				
		int setX = actor.getPosition().getX() + newX;
		int setY = actor.getPosition().getY() + newY;
				
		if (isPlayer && playerActor.isJumping() && Objects.isNull(mainCollitionCause)) {
			mainCollitionCause = SCREEN_COLLISION;
		}
		
		int collX = mainCollitionCause.getPosition().getX();
		int colWidth = mainCollitionCause.getDimension().getWidth();
		boolean isFloor = mainCollitionCause instanceof Floor;
		Floor floorCollision = isFloor ? (Floor) mainCollitionCause : null;
		
		if (isFloor) {
			boolean pass = true;
			if (isBarrel) {
				if (barrel.isInStair()) {
					pass = false;
				}
			}
			
			if (isPlayer) {
				if (playerActor.isInStair() || playerActor.isJumping())
					pass = false;
			}
						
			if (pass) {
				if (setX + actor.getDimension().getWidth() >= collX + colWidth) {
					if (!floorCollision.getContinuity().contains(MoveDirection.RIGHT)) {
						if (isBarrel) {
							switchBarrelDirection(barrel);
						}
						System.out.println("Out of map bro FOR RIGHT.");
						return;
					}
				}
				
				if (setX <= collX) {
					if (!floorCollision.getContinuity().contains(MoveDirection.LEFT)) {
						if (isBarrel) {
							switchBarrelDirection(barrel);
						}
						System.out.println("Out of map bro FOR LEFT.");
						return;
					}
				}

				if (Objects.nonNull(offsetCollition) && ((Floor) offsetCollition).specialFloor) {
					setY = offsetCollition.getPosition().getY() - actor.getDimension().getHeight();
				}
			}
		}

		actor.getPosition().setX(setX);
		actor.getPosition().setY(setY);
		
		if (isPlayer) {
			handlePlayerAnimations(event, playerActor);
		}
		if (isBarrel) {
			handleBarrelAnimations(barrel);
		}
	}
	
	public static void switchBarrelDirection(final Barrel barrel) {
		barrel.setGoingDown(true);
		barrel.setGoingRight(!barrel.isGoingRight());
	}
	
	private static void handleBarrelAnimations(final Barrel barrel) {
		if (System.currentTimeMillis() - barrel.getLastAnimationChange() >= 57) {
			int id = barrel.getAnimationId();
			int next = id == Assets.BARREL_FIRST ? Assets.BARREL_SECOND : id == Assets.BARREL_SECOND ? Assets.BARREL_FIRST : -1;
			if (next == -1) 
				next = id == Assets.BARREL_THIRD ? Assets.BARREL_FOURTH : Assets.BARREL_THIRD;
			
			barrel.setAnimationId(next);
			barrel.setImage(Assets.IMAGES[next]);
			barrel.setLastAnimationChange(System.currentTimeMillis());
		}
	}

	private static void handlePlayerAnimations(final MoveEvent event, final PlayerActor playerActor) {
		int next = -1;
		if (event.getDirection().equals(MoveDirection.RIGHT)) {
			next = playerActor.getWalkingImage() == Assets.PLAYER_WALK_RIGHT ? Assets.PLAYER_WALK_RIGHT_2 : Assets.PLAYER_WALK_RIGHT;
		} else if (event.getDirection().equals(MoveDirection.LEFT)) {
			next = playerActor.getWalkingImage() == Assets.PLAYER_WALK_LEFT ? Assets.PLAYER_WALK_LEFT_2 : Assets.PLAYER_WALK_LEFT;
		}

		if (playerActor.hasHammer()) {
			if (event.getDirection().equals(MoveDirection.RIGHT)) {
				if (playerActor.smashDelayPassed())
					next = playerActor.getWalkingImage() == Assets.PLAYER_HAMMER_RIGHT ? Assets.PLAYER_HAMMER_RIGHT_2 : Assets.PLAYER_HAMMER_RIGHT;
				else
					next = Assets.PLAYER_HAMMER_SMASH_RIGHT;
			} else {
				if (playerActor.smashDelayPassed())
					next = playerActor.getWalkingImage() == Assets.PLAYER_HAMMER_LEFT ? Assets.PLAYER_HAMMER_LEFT_2 : Assets.PLAYER_HAMMER_LEFT;
				else
					next = Assets.PLAYER_HAMMER_SMASH_LEFT;
			}
		}

		
		if (playerActor.isInStair()) {
			next = playerActor.getWalkingImage() == Assets.PLAYER_STAIR_LEFT ? Assets.PLAYER_STAIR_RIGHT : Assets.PLAYER_STAIR_LEFT;			
		}
		
		
			
		if (!playerActor.isJumping() && next != -1 && (System.currentTimeMillis() - playerActor.getWalkingUpdate()) > 75) {
			playerActor.setImage(Assets.IMAGES[next]);
			playerActor.setWalkingImage(next);
			playerActor.setLastDirection(event.getDirection());
			playerActor.setWalkingUpdate(System.currentTimeMillis());
		}
	}
}
