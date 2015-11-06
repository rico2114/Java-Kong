package juan.project.world.entity.event.handler;

import juan.project.world.entity.ActorModel;
import juan.project.world.entity.event.Event;

/**
 * Created with eclipse 8/10/2015 20:32:43
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public interface Handler<E extends Event> {

	/**
	 * The handler takes care of the interaction based on the implementation, the user pass an event and the handler intercepts it
	 * @param actor	the actor
	 * @param event	the event
	 */
	void interact(final ActorModel actor, final E event);
}
