package juan.project.world.entity.impl;

import java.awt.Graphics2D;
import java.util.Objects;

import juan.project.graphics.Assets;
import juan.project.util.RandomUtils;
import juan.project.world.Dimension;
import juan.project.world.Game;
import juan.project.world.GameLogic;
import juan.project.world.GameStage;
import juan.project.world.Position;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.CollidableActor;

/**
 * Created with eclipse 17/10/2015 0:46:51
 * 
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Monkey extends CollidableActor {

	/**
	 * Represents the monkey last move
	 */	
	private long lastMove;
	
	/**
	 * Represents the last barrel thrown in
	 */
	private long lastBarrelMod;
	
	/**
	 * Represents the animation id of the monkey
	 */
	private int currentImage = Assets.MONKEY_LEFT_HAND;
	
	/**
	 * Represents the next barrel in time
	 */
	private int nextBarrelIn;

	/**
	 * Represents the initial floor of the monkey
	 */
	private Floor startingFloor;

	/**
	 * Constructs the monkey
	 * @param position	the position of themonkey
	 */
	public Monkey(Position position) {
		super(Assets.IMAGES[Assets.MONKEY_LEFT_HAND], position, new Dimension(Assets.IMAGES[Assets.MONKEY_LEFT_HAND].getWidth(), Assets.IMAGES[Assets.MONKEY_LEFT_HAND].getHeight()));
	}
	
	/**
	 * Gets the starting floor of the monkey
	 * @return	the starting floor
	 */
	public Floor getStartingFloor() {
		return startingFloor;
	}

	/**
	 * Sets the starting floor of the monkey
	 * @param floor	the floor
	 */
	public void setStartingFloor(final Floor floor) {
		this.startingFloor = floor;
	}

	@Override
	public void render(Graphics2D g2d) {
		int complexity = Math.abs(getPosition().getY() - Game.getPlayer().getPosition().getY()) / (GameLogic.getDivisions() / Game.getLevel().getBarrelComplexity());
		
		if (complexity <= 45) {
			complexity += RandomUtils.RANDOM.nextInt(15);
		}
		
		int barrelModTime = complexity; // change based on complexity;
		int barrelTopTicks = 25; // change based on complexity;

		if (System.currentTimeMillis() - lastMove >= 145) {
			int next = -1;
			if (GameLogic.getGameStage().equals(GameStage.PLAYING))
				next = currentImage == Assets.MONKEY_LEFT_HAND ? Assets.MONKEY_RIGHT_HAND : Assets.MONKEY_LEFT_HAND;
			else 
				next = currentImage == Assets.MONKEY_INTRO ? Assets.MONKEY_INTRO_2 : Assets.MONKEY_INTRO;
				
			setImage(Assets.IMAGES[next]);
			currentImage = next;

			lastMove = System.currentTimeMillis();
		}

		if (Objects.nonNull(startingFloor)) {
			if (System.currentTimeMillis() - lastBarrelMod >= barrelModTime) {
				if (nextBarrelIn-- <= 0) {
					nextBarrelIn = RandomUtils.RANDOM.nextInt(barrelTopTicks) + (barrelTopTicks / 2);

					final Barrel barrel = new Barrel(new Position(getPosition().getX(), startingFloor.getPosition().getY()));
					barrel.getPosition().setY(barrel.getPosition().getY() - barrel.getDimension().getHeight());
					barrel.setGoingRight(RandomUtils.RANDOM.nextBoolean());

					GameLogic.registerActor(barrel);
				}
				lastBarrelMod = System.currentTimeMillis();
			}
		}

		super.render(g2d);
	}

	@Override
	public void doCollision(ActorModel actor) {
	}
}
