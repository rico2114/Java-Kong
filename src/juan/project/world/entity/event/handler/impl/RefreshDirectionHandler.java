package juan.project.world.entity.event.handler.impl;

import juan.project.graphics.Assets;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.event.handler.Handler;
import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;
import juan.project.world.entity.event.impl.RefreshDirectionEvent;
import juan.project.world.entity.impl.PlayerActor;

/**
 * Created with eclipse 8/10/2015 22:06:41
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class RefreshDirectionHandler implements Handler<RefreshDirectionEvent> {

	public static final RefreshDirectionHandler REFRESH_DIRECTION_HANDLER = new RefreshDirectionHandler();
	
	@Override
	public void interact(ActorModel actor, RefreshDirectionEvent event) {
		if (!(actor instanceof PlayerActor)) {
			return;
		}
		
		final PlayerActor playerActor = (PlayerActor) actor;
		
		if (playerActor.isInStair() || playerActor.isJumping())
			return;
		
		int image = playerActor.getLastDirection().equals(MoveDirection.RIGHT) ? Assets.PLAYER_DEFAULT_RIGHT : Assets.PLAYER_DEFAULT_LEFT;
		
		if (playerActor.hasHammer()) {
			if (image == Assets.PLAYER_DEFAULT_RIGHT) {
				if (playerActor.smashDelayPassed())
					image = Assets.PLAYER_DEFAULT_HAMMER_RIGHT;
				else
					image = Assets.PLAYER_HAMMER_SMASH_RIGHT;
			} else {
				if (playerActor.smashDelayPassed())
					image = Assets.PLAYER_DEFAULT_HAMMER_LEFT;
				else
					image = Assets.PLAYER_HAMMER_SMASH_LEFT;
			}

		}
		actor.setImage(Assets.IMAGES[image]);
	}

}
