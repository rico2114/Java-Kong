package juan.project.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import juan.project.game.Constants;
import juan.project.world.GameMap;
import juan.project.world.MapGenerator;
import juan.project.world.Position;
import juan.project.world.entity.event.handler.impl.JumpHandler;
import juan.project.world.entity.event.handler.impl.MovementHandler;
import juan.project.world.entity.event.handler.impl.RefreshDirectionHandler;
import juan.project.world.entity.event.impl.JumpEvent;
import juan.project.world.entity.event.impl.MoveEvent;
import juan.project.world.entity.event.impl.MoveEvent.MoveDirection;
import juan.project.world.entity.event.impl.RefreshDirectionEvent;
import juan.project.world.entity.impl.Monkey;
import juan.project.world.entity.impl.PlayerActor;

/**
 * Created with eclipse 8/10/2015 21:40:44
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Game extends JPanel implements ActionListener, KeyListener {

	/**
	 * Represents the serial of this panel
	 */
	private static final long serialVersionUID = -8165883146338627431L;
	
	/**
	 * Represents a map of all the keys 
	 * XXX: We can use a map for this to save at least 180 unused keys...
	 */
	private static final boolean [] keys = new boolean[200];
	
	/**
	 * Represents the player
	 */
	private static final PlayerActor PLAYER = new PlayerActor(new Position(0, 15));
	
	/**
	 * Represents the monkey
	 */
	private static final Monkey MONKEY = new Monkey(new Position(0, 15));
	
	/**
	 * Represents the game tick
	 */
	private final Timer timer = new Timer(10, this);

	/**
	 * Static calls
	 */
	static {		
		GameMap.registerActor(PLAYER);
		GameMap.registerActor(MONKEY);

		MapGenerator.generateMap(PLAYER, MONKEY);
	}
	
	/**
	 * Construct the game
	 */
	public Game() {
		timer.start();
	}
	
	/**
	 * Updates the logic of the map
	 */
	public void updateLogic() {
		GameMap.updateLogic();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		final Graphics2D g2d = (Graphics2D) g;
		GameMap.render(g2d);
		
		update();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		updateLogic();
		
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		
		if (!keys[68] && !keys[83] && !keys[65] && !keys[87])
			RefreshDirectionHandler.REFRESH_DIRECTION_HANDLER.interact(PLAYER, RefreshDirectionEvent.REFRESH_DIRECTION_EVENT);
	}
	
	public void update() {
		if (keys[68] == true) {
			MovementHandler.MOVEMENT_HANDLER.interact(PLAYER, new MoveEvent(MoveDirection.RIGHT, Constants.PLAYER_SPEED));
		} else if (keys[65] == true) {
			MovementHandler.MOVEMENT_HANDLER.interact(PLAYER, new MoveEvent(MoveDirection.LEFT, Constants.PLAYER_SPEED));
		}
		
		if (keys[83] == true) {
			MovementHandler.MOVEMENT_HANDLER.interact(PLAYER, new MoveEvent(MoveDirection.DOWN, Constants.PLAYER_SPEED));
		} else if (keys[87] == true) {
			MovementHandler.MOVEMENT_HANDLER.interact(PLAYER, new MoveEvent(MoveDirection.UP, Constants.PLAYER_SPEED));
		}
		
		if (keys[32] == true) {
			JumpHandler.JUMP_HANDLER.interact(PLAYER, new JumpEvent(Constants.JUMP_ALTITUDE, 1.8F));
		}
	}
}
