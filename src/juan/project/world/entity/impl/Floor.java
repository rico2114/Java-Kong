package juan.project.world.entity.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import juan.project.util.BoundaryUtil;
import juan.project.world.Dimension;
import juan.project.world.Position;
import juan.project.world.entity.ActorModel;
import juan.project.world.entity.CollidableActor;
import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;

/**
 * Created with eclipse 8/10/2015 20:26:18
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Floor extends CollidableActor {
	private final List<MoveDirection> continuityIn = new ArrayList<>();
	public boolean specialFloor;
	
	public Floor(Position position, Dimension dimension) {
		super(null, position, dimension);
	}
	
	public Floor addContinuity(final MoveDirection direction) {
		continuityIn.add(direction);
		return this;
	}
	
	public List<MoveDirection> getContinuity() {
		return continuityIn;
	}

	@Override
	public void doCollision(ActorModel actor) {
	
	}
	
	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.decode("#87081D"));
		g2d.fillRect(getPosition().getX(), getPosition().getY(), getDimension().getWidth(), getDimension().getHeight());
		//g2d.fillRect(getPosition().getX(), getPosition().getY(), getDimension().getWidth(), yFinal/*getDimension().getHeight()*/);
		//int divisions = 10;
		//int split = getDimension().getWidth() / divisions;
		//int x1 = getPosition().getX();
		
		/*for (int i = 0; i < divisions; i++) {
			g2d.setColor(Color.decode("#BD0826"));
			g2d.drawLine(x1, getPosition().getY(), x1 + split, getPosition().getY() + getDimension().getHeight() - 1);
			g2d.drawLine(x1 + split, getPosition().getY(), x1, getPosition().getY() + getDimension().getHeight() - 1);

			x1 += split;
		}*/
	}
	
	@Override
	public boolean collidesWith(ActorModel actor) {
		return BoundaryUtil.inBounds(actor, this);
	}

}
