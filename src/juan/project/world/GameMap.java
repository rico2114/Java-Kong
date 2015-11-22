package juan.project.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import juan.project.game.Constants;
import juan.project.graphics.Assets;
import juan.project.graphics.Game;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.CollidableActor;
import juan.project.world.entity.impl.Barrel;
import juan.project.world.entity.impl.Floor;
import juan.project.world.entity.impl.Monkey;
import juan.project.world.entity.impl.PlayerActor;
import juan.project.world.entity.impl.Stair;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * Created with eclipse 8/10/2015 20:26:04
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class GameMap {

	private static final Color LOGIN_BOX_COLOR = new Color(192, 192, 192, 125);
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
	
	private static String lostMessage[] = new String[] {
		"", ""
	};
	
	private static StringBuilder username = new StringBuilder();
	private static int accountGraphic;
	private static String renderHelp = "|";
	
	private static GameStage gameStage = GameStage.USERNAME_SELECT;
	
	public static void appendToUsername(final String append) {
		if (username.length() >= 20)
			return;
		
		username.append(append);
	}
	
	public static void deleteChar() {
		int length = username.length();
		int newLength = length <= 1 ? 0 : length -1;
		username.setLength(newLength);
	}
	
	public static void login() {
		if (username.length() <= 0)
			return;
		
		gameStage = GameStage.INTRO;
	}
	
	private static final List<ActorModel> introActors = new ArrayList<>();
	
	static {
		int width = 640;
		int height = 480;
		int n = 6;
		int hFactor = height / n;
		int y = hFactor;
		
		int mX = (width / 2) - (Assets.IMAGES[Assets.MONKEY_LEFT_HAND].getWidth() / 2);
		for (int i = 0; i < n; i++) {
			introActors.add(MapGenerator.doFloor(0, y, width, false));
			introActors.add(MapGenerator.doStair(mX - Constants.STAIR_LINE_WIDTH, y, hFactor));
			y += hFactor;
		}
		
		
		introActors.add(new Monkey(new Position(mX, height - hFactor)));
	}
		
	/**
	 * Renders the map
	 * @param g2d	the graphics instance
	 */
	public static void render(final Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, (int) Constants.DIMENSION.getWidth(), (int) Constants.DIMENSION.getHeight());
		int widhtMiddle = (int) Constants.DIMENSION.getWidth() / 2;
		int heightMiddle = (int) Constants.DIMENSION.getHeight() / 2;
		int widhtOffset = 50;
		switch (gameStage) {
		case USERNAME_SELECT:
			g2d.drawImage(Assets.IMAGES[Assets.LOGIN_BACKGROUND], 0, 0, null);

			int x = widhtMiddle - (widhtOffset << 1);
			int y = heightMiddle - widhtOffset;
			
			int xEnd = widhtOffset + widhtOffset << 1;
			int yEnd = widhtOffset >> 1;
			
			final AffineTransform old = g2d.getTransform();
			
			g2d.setColor(LOGIN_BOX_COLOR);
			g2d.fillRect(x, y, xEnd, yEnd);
			
			g2d.setTransform(old);
			int textX = x + (widhtOffset >> 1);
			int textY = y + ((yEnd / 2) + 4);
			g2d.setColor(Color.BLACK);
			
			if (accountGraphic % 40 == 0) {
				renderHelp = "";
			} else if (accountGraphic % 81 == 0) {
				renderHelp = "|";
			}
			accountGraphic ++;

			if (username.length() <= 0) {
				g2d.drawString(renderHelp, textX, textY);
			}
			
			g2d.drawString(username.toString() + renderHelp, textX, textY);
			
			g2d.scale(2, 2);
			g2d.setColor(Color.BLACK);
			g2d.drawString("Username:", 50, 105);
			break;
			
		case INTRO:
			doIntro(g2d);
			break;
			
		case DEAD:
			renderDisplayMessages(g2d);
			break;
			
		case PLAYING:
			final Collection<Entry<CollisionType, ActorModel>> collection = actors.entries();
			final List<Entry<CollisionType, ActorModel>> list = new ArrayList<>();
			
			list.addAll(collection);
			Collections.sort(list, RenderingComparable.SINGLETON);
			for (Entry<CollisionType, ActorModel> entry : list) {
				entry.getValue().render(g2d);
			}
			
			g2d.setColor(Color.BLACK);
			byte second = (byte) (maximum_time & 0x3F);
			g2d.drawString("Level: " + Game.getLevelId() + " ; Remaining Time: " + ((maximum_time >> 6) & 3) + ":" + (second < 10 ? "0" : "") + (second), (int) Constants.DIMENSION.getWidth() / 2, 10);
			handleTimer();
			break;
			
		case NEW_LEVEL:
			renderDisplayMessages(g2d);
			break;
		}
		
		
		//renderCollision(g2d);
	}
	
	private static long lastClimbUp;
	private static int loopCutscene;
	private static int cutX = 0, cutY = 0, cutWidth = (int) (Constants.DIMENSION.getWidth()), cutHeight = (int) (Constants.DIMENSION.getHeight());
	
	private static void doIntro(final Graphics2D g2d) {
		int height = (int) Constants.DIMENSION.getHeight();
		int width = (int) Constants.DIMENSION.getWidth();
		int cutSpeed = 2;
		if (loopCutscene % 3 == 0) {
			cutX += 3;
			cutY += cutSpeed / 2;
			
			cutWidth -= 6;
			cutHeight -= 5;
			
		}
		
		if (cutX >= width / 2) {
			setGameStage(GameStage.PLAYING);
		}
		
		g2d.setColor(Color.WHITE);
		g2d.setClip(new Ellipse2D.Double(cutX, cutY, cutWidth, cutHeight));
		g2d.fillRect(0, 0, 640, height);
		
		for (ActorModel actor : introActors) {
			actor.render(g2d);
		}
				
		final Monkey monkey = (Monkey) introActors.get(introActors.size() - 1);
		
		int n = 6;
		int hFactor = height / n;
		int mY = monkey.getPosition().getY();

		if (mY > hFactor) {
			if (System.currentTimeMillis() - lastClimbUp >= 55) {
				int speed = 7;
				monkey.getPosition().setY(mY - speed);
				lastClimbUp = System.currentTimeMillis();
			}
		}
		
		loopCutscene ++;
	}
		
	
	private static void handleTimer() { 
		// 1000 millisecs -> 1 sec
		if (System.currentTimeMillis() - lastTimeCycle >= 1000) {
			byte minute = (byte) ((maximum_time >> 6) & 3);
			byte sec = (byte) ((maximum_time & 0x3F) - 1);
			
			if (minute <= 0 && sec <= 0) {
				displayMessage(GameStage.DEAD, "You have lost", "The time has finished.");
			}
			
			if (sec <= 0) {
				minute -= 1;
				sec = 60;
			}
			
			maximum_time = (byte) ((minute << 6) | ((sec) & 0x3F));			
			lastTimeCycle = System.currentTimeMillis();
		}
	}
	
	private static void renderDisplayMessages(final Graphics2D g2d) {
		int mX = (int) (Constants.DIMENSION.getWidth() / 2) - 50;
		int mY = (int) (Constants.DIMENSION.getHeight() / 2) - 50;
		g2d.setColor(Color.BLACK);
		g2d.drawString(lostMessage[0], mX, mY);
		g2d.drawString(lostMessage[1], mX - 20, mY + 25);
		
		g2d.drawString("Press any key to continue...", mX - 20, mY + 50);
	}
	
	public static void displayMessage(final GameStage stage, final String lm0, final String lm) {
		setGameStage(stage);
		lostMessage[0] = lm0;
		lostMessage[1] = lm;
		Game.getMonkey().setStartingFloor(null);
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
	
	public static String getUsername() {
		return username.toString();
	}
	
	public static GameStage getGameStage() {
		return gameStage;
	}
	
	public static void setGameStage(final GameStage stage) { 
		gameStage = stage;
	}

	public static void setMaximumTime(final byte maxTime) {
		maximum_time = maxTime;
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
