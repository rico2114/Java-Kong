package juan.project.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import juan.project.game.Constants;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.CollidableActor;
import juan.project.world.entity.impl.PlayerActor;
import juan.project.world.entity.impl.Stair;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * Created with eclipse 8/10/2015 20:26:04
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class GameMap {

	private static final ListMultimap<CollisionType, ActorModel> actors = ArrayListMultimap.create();
	
	private static int divisions = 0;
	
	public static void render(final Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, (int) Constants.DIMENSION.getWidth(), (int) Constants.DIMENSION.getHeight());
		
		final Collection<Entry<CollisionType, ActorModel>> collection = actors.entries();
		final List<Entry<CollisionType, ActorModel>> list = new ArrayList<>();
		
		list.addAll(collection);
		
		Collections.sort(list, RenderingComparable.SINGLETON);
		
		for (Entry<CollisionType, ActorModel> entry : list) {
			entry.getValue().render(g2d);
		}
		
		//renderCollision(g2d);
	}
	
	public static void renderCollision(final Graphics2D g2d) {
		g2d.setColor(Color.CYAN);
		for (Map.Entry<CollisionType, ActorModel> actor : actors.entries()) {
			ActorModel model = actor.getValue();
			int x = model.getPosition().getX();
			int y = model.getPosition().getY();

			g2d.drawRect(x, y, model.getDimension().getWidth(), model.getDimension().getHeight());
		}
	}
	
	public static void updateLogic() {
		final List<ActorModel> collidable = actors.get(CollisionType.COLLIDABLE); 
		final List<ActorModel> notCollidable = actors.get(CollisionType.NOT_COLLIDABLE); 

		for (ActorModel collisionModel : collidable) {
			final CollidableActor collision = (CollidableActor) collisionModel;
			for (ActorModel normal : notCollidable) {
				if (collision.collidesWith(normal)) {
					collision.doCollision(normal);
				}
			}
		}
	}
	
	public static void registerActor(final ActorModel actor) {
		actors.put(actor.getCollisionType(), actor);
	}
	
	public static void clearActors() {
		actors.clear();
	}
	
	public static List<CollidableActor> getActorCollition(final ActorModel actor) {
		final List<CollidableActor> list = new ArrayList<>();
		for (Map.Entry<CollisionType, ActorModel> possible : actors.entries()) {
			if (possible.getKey().equals(CollisionType.NOT_COLLIDABLE)) 
				continue;
			
			final CollidableActor collidable = (CollidableActor) possible.getValue();
			if (collidable.collidesWith(actor)) {
				list.add(collidable);
			}
		}
		return list;
	}
	
	public static CollidableActor actorCollidesWith(final List<CollidableActor> list, final Class<?> clasz, final ActorModel actor) {
		for (CollidableActor collidable : list) {
			if (collidable.getClass().getSimpleName().equals(clasz.getSimpleName())) {
				return collidable;
			}
		}
		return null;
	}
	
	public static boolean actorCollides(final ActorModel actor) {
		return !getActorCollition(actor).isEmpty();
	}
	
	public static boolean deleteActor(final ActorModel actor) {
		return actors.remove(actor.getCollisionType(), actor);
	}
	
	public static void setDivisions(final int val) {
		divisions = val;
	}
	
	public static int getDivisions() {
		return divisions;
	}
	
	public static final class RenderingComparable implements Comparator<Entry<CollisionType, ActorModel>> {

		public static final RenderingComparable SINGLETON = new RenderingComparable();
		
		@Override
		public int compare(Entry<CollisionType, ActorModel> o1, Entry<CollisionType, ActorModel> o2) {
			if (o1.getValue().getClass().equals(PlayerActor.class))
					return 1;
				
			if (o2.getValue().getClass().equals(PlayerActor.class))
					return -1;
			
			if (o1.getValue().getClass().equals(Stair.class))
					return 1;
				
			if (o2.getValue().getClass().equals(Stair.class))
					return -1;
			
			return 0;
		}
		
	}
}
