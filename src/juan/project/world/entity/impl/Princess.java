package juan.project.world.entity.impl;

import juan.project.graphics.Assets;
import juan.project.graphics.Game;
import juan.project.world.Dimension;
import juan.project.world.GameMap;
import juan.project.world.GameStage;
import juan.project.world.Position;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.CollidableActor;

/**
 * Created with eclipse 21/11/2015 13:22:27
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Princess extends CollidableActor {
	
	public Princess(Position position) {
		super(Assets.IMAGES[Assets.PRINCESS_LEFT], position, new Dimension(Assets.IMAGES[Assets.PRINCESS_LEFT].getWidth(), Assets.IMAGES[Assets.PRINCESS_LEFT].getHeight()));
	}

	@Override
	public void doCollision(ActorModel actor) {
		if (actor.getClass().equals(PlayerActor.class)) {
			GameMap.displayMessage(GameStage.NEW_LEVEL, "You have won the last level.", "");
			Game.setLevel(1);
		}
	}

}
