package juan.project.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import juan.project.game.Constants;
import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;
import juan.project.world.entity.impl.Floor;
import juan.project.world.entity.impl.Hammer;
import juan.project.world.entity.impl.Monkey;
import juan.project.world.entity.impl.PlayerActor;
import juan.project.world.entity.impl.Princess;
import juan.project.world.entity.impl.Stair;

/**
 * Created with eclipse 12/10/2015 13:45:26
 * 
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class MapGenerator {

	/**
	 * Represents the maximum floor amount
	 */
	private static final int MAXIMUM_FLOORS = 7;
	
	/**
	 * Represents the slope floor amount
	 */
	private static final int SLOPE_FLOOR_AMOUNT = 20;
	
	/**
	 * Generates a procedural map (sort of :p)
	 * @param player	the player
	 * @param monkey	the monkey
	 */
	public static void generateMap(final PlayerActor player, final Monkey monkey) {	
		int yDivisions = (int) (Constants.DIMENSION.getHeight() / MAXIMUM_FLOORS);
		
		int bigFloorWidth = (int) (Constants.DIMENSION.getWidth() / 2);
		int smallFloorWidth = bigFloorWidth / 2;
		int initialY = monkey.getPosition().getY() + monkey.getDimension().getHeight();		
				
		MoveDirection constructDirection = Game.getLevel().getConstructDirection();
		MoveDirection opositeDirection = constructDirection.equals(MoveDirection.RIGHT) ? MoveDirection.LEFT : MoveDirection.RIGHT;
		
		Princess princess = null;
		boolean placedHammer = false;
		
		Floor floorNode = doFloor(constructDirection.equals(MoveDirection.RIGHT) ? 0 : (int) (Constants.DIMENSION.getWidth() - 170), initialY, 0);
		for (int i = 0; i < yDivisions; i++) {
			boolean cr = constructDirection.equals(MoveDirection.RIGHT);
			Floor x = null;
			GameLogic.registerActor(x = doFloor(floorNode.getPosition().getX(), floorNode.getPosition().getY(), smallFloorWidth).addContinuity(constructDirection));
			
			if (Objects.isNull(monkey.getStartingFloor())) {
				monkey.setStartingFloor(x);
				monkey.getPosition().setX(x.getPosition().getX() + ((x.getDimension().getWidth() / 2) - (monkey.getDimension().getWidth() / 2)));
				monkey.getPosition().setY(x.getPosition().getY() - monkey.getDimension().getHeight());
				
				GameLogic.registerActor(monkey);
			}
			
			if (!placedHammer && i > 0 && Math.random() > 0.5D) {
				GameLogic.registerActor(new Hammer(new Position(x.getPosition().getX() / 2, x.getPosition().getY() - Constants.FLOOR_HEIGHT * 3)));
				placedHammer = true;
			}
			
			List<Floor> floors = doScaledFloors(floorNode.getPosition().getX() + (cr ? smallFloorWidth : 0), floorNode.getPosition().getY(), bigFloorWidth, SLOPE_FLOOR_AMOUNT, constructDirection);
			floorNode = floors.get(floors.size() - 1);
			for (Floor f : floors) {
				GameLogic.registerActor(f);
				
			}
			
			x = doFloor(floorNode.getPosition().getX() + (cr ? 0 : -smallFloorWidth), floorNode.getPosition().getY(), smallFloorWidth).addContinuity(opositeDirection);
			GameLogic.registerActor(x);
			
			if (Objects.isNull(princess)) {
				int xx = floorNode.getPosition().getX() + (cr ? 0 : -smallFloorWidth);
				princess = new Princess(new Position(xx, 0));
				int yy = floorNode.getPosition().getY() - princess.getDimension().getHeight();
				princess.getPosition().setY(yy);
				GameLogic.registerActor(princess);
			}
			
			if (!cr) {
				floorNode = x;
			}
			
			Stair stair = null;
			int half = (smallFloorWidth / 2) - (Constants.STAIR_DEFAULT_WIDTH / 2);
			GameLogic.registerActor(stair = doStair(floorNode.getPosition().getX() + half, floorNode.getPosition().getY(), yDivisions));
			
			// for the stair modifications :)
			floorNode = doFloor(floorNode.getPosition().getX(), floorNode.getPosition().getY() + stair.getDimension().getHeight(), smallFloorWidth);
			int stairEndY = stair.getPosition().getY() + stair.getDimension().getHeight();
			int floorEndY = floorNode.getPosition().getY() + floorNode.getDimension().getHeight();
			boolean deleted = false;
			
			if (stairEndY >= Constants.DIMENSION.getHeight()) {
				GameLogic.deleteActor(stair);
				deleted = true;
			}
			if (floorEndY >= Constants.DIMENSION.getHeight()) {
				GameLogic.deleteActor(floorNode);
				deleted = true;
			}
			
			if (deleted) {
				if (Objects.nonNull(x)) {
					player.getPosition().setX(x.getPosition().getX());
					player.getPosition().setY(x.getPosition().getY() - player.getDimension().getHeight());
					GameLogic.registerActor(player);
				}
				break;
			}

			MoveDirection opDir = constructDirection;
			constructDirection = opositeDirection;
			opositeDirection = opDir;
		}
		
		GameLogic.setDivisions(MAXIMUM_FLOORS - 2);
	}
	
	/**
	 * Takes care of the scaled floors 
	 * @param x	the x coordinate
	 * @param y	the y coordinate
	 * @param width	the width of the floor
	 * @param pieces	the n pieces in which we are going to split the floor
	 * @param direction	the direction of construction
	 * @return	the floors returned as a list
	 */
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
	
	/**
	 * Does a normal floor
	 * @param x	the x coordinate
	 * @param y	the y coordinate
	 * @param width	the width of the floor
	 * @return	the floor instance
	 */
	public static Floor doFloor(final int x, final int y, final int width) {
		return doFloor(x, y, width, false);
	}

	/**
	 * Does a floor with some desired parameter
	 * @param x	the x coordinate
	 * @param y	the y coordinate
	 * @param width	the width of the floor
	 * @param specialFloor	is the floor an special floor
	 * @return	the floor instance
	 */
	public static Floor doFloor(final int x, final int y, final int width, final boolean specialFloor) {
		int height = Constants.FLOOR_HEIGHT;
		final Floor floor = new Floor(new Position(x, y), new Dimension(width, height));
		floor.setSpecialFloor(specialFloor);
		return floor;
	}

	/**
	 * Does a stair 
	 * @param x	the x coordinate
	 * @param y	the y coordinate
	 * @param yDivisions	the y divisions  of the stair (graphical improvement)
	 * @return	the stair instance
	 */
	public static Stair doStair(final int x, final int y, int yDivisions) {
		return new Stair(new Position(x, y), new Dimension(Constants.STAIR_DEFAULT_WIDTH, yDivisions));
	}

}
