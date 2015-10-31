package juan.project.world;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import juan.project.game.Constants;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.impl.Floor;
import juan.project.world.entity.impl.Monkey;
import juan.project.world.entity.impl.PlayerActor;
import juan.project.world.entity.impl.Stair;

/**
 * Created with eclipse 12/10/2015 13:45:26
 * 
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class MapGenerator {

	private static final int MAXIMUM_FLOORS = 7;
	
	// Procedural generation maps
	public static void generateMap(final PlayerActor player, final Monkey monkey) {
		StairLocation stairLocation = StairLocation.values()[ThreadLocalRandom.current().nextInt(0, StairLocation.values().length)];
		ActorModel glue = player;
		
		int divisions = MAXIMUM_FLOORS;
		int yDivisions = (int) (Constants.DIMENSION.getHeight() / divisions);
		
		Floor firstFloor = null;
		
		for (int i = 0; i < divisions; i++) {
			GameMap.registerActor(glue = doFloor(glue, player));
			
			if (Objects.isNull(firstFloor)) {
				firstFloor = (Floor) glue;
			}
			
			if (i + 1 == divisions - 1) {
				player.getPosition().setX(glue.getPosition().getX());
				player.getPosition().setY(glue.getPosition().getY() - player.getDimension().getHeight());
				
				monkey.getPosition().setY(firstFloor.getPosition().getY() - (monkey.getDimension().getHeight() - 5));
				monkey.getPosition().setX((firstFloor.getDimension().getWidth() / 2) - (monkey.getDimension().getWidth() / 2));
				monkey.setStartingFloor(firstFloor);
				break;
			}
			
			GameMap.registerActor(glue = doStair(yDivisions, glue.getPosition().getY(), stairLocation));
			stairLocation = StairLocation.next(stairLocation);
			
		}
		GameMap.setDivisions(divisions);
	}

	public static Floor doFloor(final ActorModel glue, final ActorModel player) {
		int x = 0;
		int width = (int) (Constants.DIMENSION.getWidth());

		int y = glue.getPosition().getY() + glue.getDimension().getHeight();
		int height = Constants.FLOOR_HEIGHT;

		final Floor floor = new Floor(new Position(x, y), new Dimension(width, height));
		return floor;
	}

	public static Stair doStair(final int yDivisions, final int glueY, final StairLocation location) {
		int x = 0;
		int y = glueY;
		
		int screenWidth = (int) Constants.DIMENSION.getWidth();
		int middleScreen = screenWidth / 2;
		int half = middleScreen / 2;
		int stairExtra = (Constants.STAIR_DEFAULT_WIDTH + Constants.STAIR_LINE_WIDTH);
		switch (location) {
		case CENTER:
			x = ThreadLocalRandom.current().nextInt(middleScreen - half, middleScreen + half);
			break;
		case LEFT:
			x = ThreadLocalRandom.current().nextInt(0, half - stairExtra);
			break;
		case RIGHT:
			x = ThreadLocalRandom.current().nextInt(middleScreen + half + stairExtra, screenWidth - stairExtra);
			break;
		}
		
		return new Stair(new Position(x, y), new Dimension(Constants.STAIR_DEFAULT_WIDTH, yDivisions));
	}
	
	private enum StairLocation {
		CENTER,
		LEFT,
		RIGHT;
		
		public static StairLocation next(final StairLocation current) {
			int next = current.ordinal() + 1;
			if (next >= StairLocation.values().length) {
				next = 0;
			}
			return StairLocation.values()[next];
		}
	}
	
}
