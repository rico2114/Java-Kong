package juan.project.world.entity.event.handler.impl;

import juan.project.graphics.Assets;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.event.handler.Handler;
import juan.project.world.entity.event.impl.JumpEvent;
import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;
import juan.project.world.entity.impl.PlayerActor;

/**
 * Created with eclipse 8/10/2015 21:34:01
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class JumpHandler implements Handler<JumpEvent> {

	public static final JumpHandler JUMP_HANDLER = new JumpHandler();
	
	@Override
	public void interact(ActorModel actor, JumpEvent event) {
		if (!(actor instanceof PlayerActor))
			return;
		
		final PlayerActor playerActor = (PlayerActor) actor;
		
		if (playerActor.isJumping() || playerActor.isInStair())
			return;
		

		playerActor.setJumping(true);
		playerActor.setJumpingMod(3);
		
		int image = playerActor.getLastDirection().equals(MoveDirection.RIGHT) ? Assets.PLAYER_JUMP_RIGHT : Assets.PLAYER_JUMP_LEFT;
		playerActor.setImage(Assets.IMAGES[image]);
	}

}
