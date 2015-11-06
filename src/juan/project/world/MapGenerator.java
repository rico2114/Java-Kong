package juan.project.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import juan.project.game.Constants;
import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;
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
	private static final int SLOPE_FLOOR_AMOUNT = 20;
	
	// Procedural generation maps
	public static void generateMap(final PlayerActor player, final Monkey monkey) {	
		int yDivisions = (int) (Constants.DIMENSION.getHeight() / MAXIMUM_FLOORS);
		
		int bigFloorWidth = (int) (Constants.DIMENSION.getWidth() / 2);
		int smallFloorWidth = bigFloorWidth / 2;
		int initialY = monkey.getPosition().getY() + monkey.getDimension().getHeight();		
				
		MoveDirection constructDirection = MoveDirection.RIGHT;
		MoveDirection opositeDirection = MoveDirection.LEFT;
		
		Floor marioPlace = null;
		Floor floorNode = doFloor(0, initialY, 0);
		for (int i = 0; i < yDivisions; i++) {
			boolean cr = constructDirection.equals(MoveDirection.RIGHT);
			Floor x = null;
			GameMap.registerActor(x = doFloor(floorNode.getPosition().getX(), floorNode.getPosition().getY(), smallFloorWidth).addContinuity(constructDirection));
			
			if (Objects.isNull(monkey.getStartingFloor())) {
				monkey.setStartingFloor(x);
			}
			
			List<Floor> floors = doScaledFloors(floorNode.getPosition().getX() + (cr ? smallFloorWidth : 0), floorNode.getPosition().getY(), bigFloorWidth, SLOPE_FLOOR_AMOUNT, constructDirection);
			floorNode = floors.get(floors.size() - 1);
			for (Floor f : floors) {
				GameMap.registerActor(f);
			}
			
			x = doFloor(floorNode.getPosition().getX() + (cr ? 0 : -smallFloorWidth), floorNode.getPosition().getY(), smallFloorWidth).addContinuity(opositeDirection);
			GameMap.registerActor(x);
			
			if (!cr) {
				floorNode = x;
			}
			Stair stair = null;
			int half = (smallFloorWidth / 2) - (Constants.STAIR_DEFAULT_WIDTH / 2);
			GameMap.registerActor(stair = doStair(floorNode.getPosition().getX() + half, floorNode.getPosition().getY(), yDivisions));
			floorNode = doFloor(floorNode.getPosition().getX(), floorNode.getPosition().getY() + stair.getDimension().getHeight(), smallFloorWidth);

			int stairEndY = stair.getPosition().getY() + stair.getDimension().getHeight();
			int floorEndY = floorNode.getPosition().getY() + floorNode.getDimension().getHeight();
			boolean deleted = false;
			
			if (stairEndY >= Constants.DIMENSION.getHeight()) {
				GameMap.deleteActor(stair);
				deleted = true;
			}
			if (floorEndY >= Constants.DIMENSION.getHeight()) {
				GameMap.deleteActor(floorNode);
				deleted = true;
			} else {
				marioPlace = floorNode;
			}
			
			if (deleted) {
				if (Objects.nonNull(marioPlace)) {
					player.getPosition().setY(marioPlace.getPosition().getY() - player.getDimension().getHeight());
				}
				break;
			}

			MoveDirection opDir = constructDirection;

			constructDirection = opositeDirection;
			opositeDirection = opDir;
		}
		
		GameMap.setDivisions(MAXIMUM_FLOORS - 2);
	}
	
	public static List<Floor> doScaledFloors(int x, int y, final int width, final int pieces, MoveDirection direction) {
		final List<Floor> floor = new ArrayList<>();
		int size = width / pieces;
		for (int i = 0; i < pieces; i ++) {
			floor.add(doFloor(x, y, size, true).addContinuity(direction).addContinuity(direction.equals(MoveDirection.RIGHT) ? MoveDirection.LEFT : MoveDirection.RIGHT));
			x += direction.equals(MoveDirection.RIGHT) ? size : -size;
			y += Constants.SCALED_FLOOR_HEIGHT;
		}
		return floor;
	}
	
	public static Floor doFloor(final int x, final int y, final int width) {
		return doFloor(x, y, width, false);
	}


	public static Floor doFloor(final int x, final int y, final int width, final boolean specialFloor) {
		int height = Constants.FLOOR_HEIGHT;
		final Floor floor = new Floor(new Position(x, y), new Dimension(width, height));
		floor.specialFloor = specialFloor;
		return floor;
	}

	public static Stair doStair(final int x, final int y, int yDivisions) {
		return new Stair(new Position(x, y), new Dimension(Constants.STAIR_DEFAULT_WIDTH, yDivisions));
	}

}
