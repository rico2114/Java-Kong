package juan.project.world.entity.impl;

import java.awt.Graphics2D;

import juan.project.graphics.Assets;
import juan.project.graphics.Game;
import juan.project.util.RandomUtils;
import juan.project.world.Dimension;
import juan.project.world.GameMap;
import juan.project.world.Position;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.CollidableActor;

/**
 * Created with eclipse 17/10/2015 0:46:51
 * 
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Monkey extends CollidableActor {

	private long lastMove;
	private long lastBarrelMod;
	private int currentImage = Assets.MONKEY_LEFT_HAND;
	private int nextBarrelIn;

	private Floor startingFloor;

	public Monkey(Position position) {
		super(Assets.IMAGES[Assets.MONKEY_LEFT_HAND], position, new Dimension(Assets.IMAGES[Assets.MONKEY_LEFT_HAND].getWidth(), Assets.IMAGES[Assets.MONKEY_LEFT_HAND].getHeight()));
	}
	
	public Floor getStartingFloor() {
		return startingFloor;
	}

	public void setStartingFloor(final Floor floor) {
		this.startingFloor = floor;
	}

	@Override
	public void render(Graphics2D g2d) {
		int difference = Math.abs(getPosition().getY() - Game.getPlayer().getPosition().getY());
		System.out.println("difference: " + difference);
		int barrelModTime = 188 /*difference*/; // change based on complexity;
		int barrelTopTicks = 25; // change based on complexity;

		if (System.currentTimeMillis() - lastMove >= 145) {
			int next = currentImage == Assets.MONKEY_LEFT_HAND ? Assets.MONKEY_RIGHT_HAND : Assets.MONKEY_LEFT_HAND;
			setImage(Assets.IMAGES[next]);
			currentImage = next;

			lastMove = System.currentTimeMillis();
		}

		if (System.currentTimeMillis() - lastBarrelMod >= barrelModTime) {
			if (nextBarrelIn-- <= 0) {
				nextBarrelIn = RandomUtils.RANDOM.nextInt(barrelTopTicks) + (barrelTopTicks / 2);

				final Barrel barrel = new Barrel(new Position(getPosition().getX(), startingFloor.getPosition().getY()));
				barrel.getPosition().setY(barrel.getPosition().getY() - barrel.getDimension().getHeight());
				barrel.setGoingRight(RandomUtils.RANDOM.nextBoolean());

				GameMap.registerActor(barrel);
			}
			lastBarrelMod = System.currentTimeMillis();
		}

		super.render(g2d);
	}

	@Override
	public void doCollision(ActorModel actor) {
		if (actor.getClass().equals(PlayerActor.class)) {
			// / you won!
			//System.out.println("I WON");
		}
	}
}
