package juan.project.game;

import javax.swing.JFrame;

import juan.project.world.Game;

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
		//printLines();
	}
	
	/*public static void printLines() {
		final List<File> ff = filesIn(new ArrayList<>(), "./src/");
		long lines = 0;
		for (File f : ff) {
			try {
				final BufferedReader reader = new BufferedReader(new FileReader(f));
				lines += reader.lines().count();
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		
		System.out.println("Total lineas de codigo: " + lines);
	}
	
	public static List<File> filesIn(final List<File> result, final String dir) {
		final File [] files = new File(dir).listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				filesIn(result, f.getPath());
			} else {
				result.add(f);
			}
		}
		return result;
	}*/
}
