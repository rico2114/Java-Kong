package juan.project.world.entity.impl;

import java.awt.Graphics2D;

import juan.project.graphics.Assets;
import juan.project.world.Dimension;
import juan.project.world.GameLogic;
import juan.project.world.Position;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.CollidableActor;

/**
 * Created with eclipse 18/11/2015 22:58:54
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Hammer extends CollidableActor {

	/**
	 * Should we remove the hammer
	 */
	private boolean removeRequest = false;
	
	/**
	 * Construct a hammer at a given position
	 * @param position	the position
	 */
	public Hammer(Position position) {
		super(Assets.IMAGES[Assets.MAP_HAMMER], position, new Dimension(Assets.IMAGES[Assets.MAP_HAMMER].getWidth(), Assets.IMAGES[Assets.MAP_HAMMER].getHeight()));
	}

	@Override
	public void doCollision(ActorModel actor) {
		if (actor.getClass().equals(PlayerActor.class)) {
			removeRequest = true;
			((PlayerActor) actor).addHammer();
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		super.render(g2d);
		
		// To avoid synchronization on the actors list :-), nice trick (logic is done first, rendering later))
		if (removeRequest)
			GameLogic.deleteActor(this);
	}
}
