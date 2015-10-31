package juan.project.world.entity.impl;

import java.awt.Color;
import java.awt.Graphics2D;

import juan.project.game.Constants;
import juan.project.world.Dimension;
import juan.project.world.Position;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.CollidableActor;

/**
 * Created with eclipse 8/10/2015 23:01:48
 * 
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Stair extends CollidableActor {
	private static final int MINIMUM = 80;
	private static final int MAXIMUM = 180;
	
	private int stairAlphaEffect = MAXIMUM;
	private boolean decreasing = true;
	
	public Stair(Position position, Dimension dimension) {
		super(null, position, dimension);
	}

	@Override
	public void doCollision(ActorModel actor) {
	}

	@Override
	public void render(Graphics2D g2d) {
		int endX = getPosition().getX() + getDimension().getWidth();

		g2d.setColor(new Color(0, 65, 87, stairAlphaEffect));
		g2d.fillRect(getPosition().getX(), getPosition().getY(), Constants.STAIR_LINE_WIDTH, getDimension().getHeight());
		g2d.fillRect(endX, getPosition().getY(), Constants.STAIR_LINE_WIDTH, getDimension().getHeight());
		
		int split = 5;
		int stairIncrement = getDimension().getHeight() / split;
		int increment = 0;
		
		for (int i = 0; i < split ; i++) {
			increment += stairIncrement;
			g2d.fillRect(getPosition().getX(), getPosition().getY() + getDimension().getHeight() - increment, (endX - getPosition().getX()) + Constants.STAIR_LINE_WIDTH, 3);
		}
		
		if (decreasing) {
			stairAlphaEffect --;
		} else {
			stairAlphaEffect ++;
		}
				
		if (stairAlphaEffect == MAXIMUM)
			decreasing = true;
		else if (stairAlphaEffect == MINIMUM)
			decreasing = false;
	}
	
}
