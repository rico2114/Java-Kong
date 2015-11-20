package juan.project.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
import juan.project.world.entity.impl.Barrel;
import juan.project.world.entity.impl.Floor;
import juan.project.world.entity.impl.PlayerActor;
import juan.project.world.entity.impl.Stair;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * Created with eclipse 8/10/2015 20:26:04
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class GameMap {

	/**
	 * Represents the {@link ListMultimap map} used for the two kind of {@link ActorModel models} {@link CollisionType#COLLIDABLE collidable} or {@link CollisionType#NOT_COLLIDABLE not collidable}
	 */
	private static final ListMultimap<CollisionType, ActorModel> actors = ArrayListMultimap.create();
	
	/**
	 * Represents the game {@link Floor floor} divisions (number of floors)
	 */
	private static int divisions = 0;
	
	private static long lastTimeCycle;
	
	private static byte maximum_time;
	private static int level;
	
	private static boolean lost;
	private static String lostMessage = "";
	
	/**
	 * Renders the map
	 * @param g2d	the graphics instance
	 */
	public static void render(final Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, (int) Constants.DIMENSION.getWidth(), (int) Constants.DIMENSION.getHeight());
		
		if (lost) {
			renderLost(g2d);
			return;
		}
		
		
		final Collection<Entry<CollisionType, ActorModel>> collection = actors.entries();
		final List<Entry<CollisionType, ActorModel>> list = new ArrayList<>();
		
		list.addAll(collection);
		
		Collections.sort(list, RenderingComparable.SINGLETON);
		
		for (Entry<CollisionType, ActorModel> entry : list) {
			entry.getValue().render(g2d);
		}
		
		g2d.setColor(Color.BLACK);
		
		// Por ende el maximo minuto es 3 :)...
		byte second = (byte) (maximum_time & 0x3F);
		g2d.drawString("Level: " + level + " ; Remaining Time: " + ((maximum_time >> 6) & 3) + ":" + (second < 10 ? "0" : "") + (second), (int) Constants.DIMENSION.getWidth() / 2, 10);
		
		if (System.currentTimeMillis() - lastTimeCycle >= 1000) {
			byte minute = (byte) ((maximum_time >> 6) & 3);
			byte sec = (byte) ((maximum_time & 0x3F) - 1);
			
			if (minute <= 0 && sec <= 0) {
				gameOver("The time has finished.");
			}
			
			if (sec <= 0) {
				minute -= 1;
				sec = 60;
			}
			
			
			maximum_time = (byte) ((minute << 6) | ((sec) & 0x3F));			
			lastTimeCycle = System.currentTimeMillis();
		}
		//renderCollision(g2d);
	}
	
	public static void renderLost(final Graphics2D g2d) {
		int mX = (int) (Constants.DIMENSION.getWidth() / 2) - 50;
		int mY = (int) (Constants.DIMENSION.getHeight() / 2) - 50;
		g2d.setColor(Color.BLACK);
		g2d.drawString("You have lost", mX, mY);
		g2d.drawString(lostMessage, mX - 20, mY + 25);
		
		g2d.drawString("Press any key to restart...", mX - 20, mY + 50);
	}
	
	public static void gameOver(final String lm) {
		lost = true;
		lostMessage = lm;
	}
	
	/**
	 * This method is merely used for debugging
	 * This takes care of the clipping box
	 * @param g2d	the graphics instance
	 */
	public static void renderCollision(final Graphics2D g2d) {
		int times = 0;
		for (Map.Entry<CollisionType, ActorModel> actor : actors.entries()) {
			ActorModel model = actor.getValue();
			int x = model.getPosition().getX();
			int y = model.getPosition().getY();
			
			g2d.setColor(Color.CYAN);

			g2d.drawRect(x, y, model.getDimension().getWidth(), model.getDimension().getHeight());
			
			g2d.setColor(new Color((int)(Math.random() * 0x1000000)));

			//if (model.getClass().equals(PlayerActor.class)) {
			if (times ++ < 3) {
				int myX = model.getPosition().getX()/* + model.getDimension().getWidth()*/;
				int myY = model.getPosition().getY()/* + model.getDimension().getHeight()*/;
				g2d.drawString("MY X: " + myX + " MY Y: " + myY, myX, myY);
				times= 0;
			}
			//}
		}
	}
	
	/**
	 * Process all the logic (collisions)
	 */
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
	
	/**
	 * Registers an {@link ActorModel actor} into the {@link ListMultimap map}
	 * @param actor	the actor to register
	 */
	public static void registerActor(final ActorModel actor) {
		actors.put(actor.getCollisionType(), actor);
	}
	
	/**
	 * Deletes an {@link ActorModel actor} from the {@link ListMultimap map}
	 * @param actor	the actor
	 * @return	true if we could remove the actor
	 */
	public static boolean deleteActor(final ActorModel actor) {
		return actors.remove(actor.getCollisionType(), actor);
	}
	
	/**
	 * Clears all the {@link ActorModel actors} from the {@link ListMultimap}
	 */
	public static void clearActors() {
		actors.clear();
	}
	
	/**
	 * Gets all the {@link ActorModel actor} collisions
	 * @param actor	the actor that we want to match collisions
	 * @return	the list with all the collisions based on that actor
	 */
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
	
	/**
	 * Returns the first occurrence based on the given {@link ActorModel actor} as a test and some offsets 
	 * @param actor	the actor
	 * @param xOFfset	the offset in x
	 * @param yOFfset	the offset in y
	 * @param clasz	the class that we want to check if actor collides with
	 * @return	the {@link CollidableActor collision} representation
	 */
	public static CollidableActor getActorCollition(final ActorModel actor, final int xOFfset, final int yOFfset, final Class<?> clasz) {
		int w = actor.getDimension().getWidth();
		int h = actor.getDimension().getHeight();
		
		int x = actor.getPosition().getX();
		int y = actor.getPosition().getY();
		
		if (actor instanceof PlayerActor || actor instanceof Barrel) {
			x += w;
			y += h;
		}

		for (Map.Entry<CollisionType, ActorModel> possible : actors.entries()) {
			if (possible.getKey().equals(CollisionType.NOT_COLLIDABLE)) 
				continue;
			
			final CollidableActor collidable = (CollidableActor) possible.getValue();
			int cx = collidable.getPosition().getX();
			int cy = collidable.getPosition().getY();
			int cw = collidable.getDimension().getWidth();
			int ch = collidable.getDimension().getHeight();
			
			boolean same = collidable.getClass().getSimpleName().equals(clasz.getSimpleName());
			if (same) {
				final Rectangle actorR = new Rectangle(x, y, xOFfset, yOFfset);
				final Rectangle collidableR = new Rectangle(cx, cy, cw, ch);

				if (same && actorR.intersects(collidableR)) {
					return collidable;
				}
			}
		}
		return null;
	}
	
	/**
	 * Does the list collides with the given class
	 * @param list	the list of collidables 
	 * @param clasz	the class that we want to match
	 * @return	true if collides
	 */
	public static CollidableActor actorCollidesWith(final List<CollidableActor> list, final Class<?> clasz) {
		for (CollidableActor collidable : list) {
			if (collidable.getClass().getSimpleName().equals(clasz.getSimpleName())) {
				return collidable;
			}
		}
		return null;
	}

	public static void setMaximumTime(final byte maxTime) {
		maximum_time = maxTime;
	}
	
	public static void increaseLevel() {
		level += 1;
	}
	
	/**
	 * Does the actor collides with anything
	 * @param actor	the actor
	 * @return	true if collides
	 */
	public static boolean actorCollides(final ActorModel actor) {
		return !getActorCollition(actor).isEmpty();
	}
	
	/**
	 * Sets the divisions of the map
	 * @param val	the new divisions N
	 */
	public static void setDivisions(final int val) {
		divisions = val;
	}
	
	/**
	 * Gets the divisions of the floors
	 * @return	the divisions
	 */
	public static int getDivisions() {
		return divisions;
	}
	
	/**
	 * @author Sebastían
	 * 
	 * This class takes care of the rendering hierarchy
	 */
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
