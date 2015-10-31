package juan.project.util;

import juan.project.world.entity.ActorModel;

/**
 * Created with eclipse 11/10/2015 22:00:12
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class BoundaryUtil {

	public static boolean inBounds(final ActorModel actor, final ActorModel boundary) {
		int minX = actor.getPosition().getX();
		int minY = actor.getPosition().getY();
		int maxX = minX + actor.getDimension().getWidth();
		int maxY = minY + actor.getDimension().getHeight();
		
		return maxX >= boundary.getPosition().getX() && minX <= boundary.getPosition().getX() + boundary.getDimension().getWidth() && maxY >= boundary.getPosition().getY() && minY <= boundary.getPosition().getY() + boundary.getDimension().getHeight();
	}
}
