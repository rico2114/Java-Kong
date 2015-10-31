package juan.project.game;

import javax.swing.JFrame;

import juan.project.graphics.Game;

/**
 * Created with eclipse 8/10/2015 21:40:54
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Launcher {

	
	public static void main(String[] args) {
		final JFrame frame = new JFrame("Donkey Kong.");
		frame.setSize(Constants.DIMENSION);
		frame.setMaximumSize(Constants.DIMENSION);
		frame.setResizable(false);
		frame.setMinimumSize(Constants.DIMENSION);
		frame.setPreferredSize(Constants.DIMENSION);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final Game game = new Game();
		frame.addKeyListener(game);
		frame.add(game);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
