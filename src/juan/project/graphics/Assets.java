package juan.project.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Created with eclipse 8/10/2015 21:15:35
 * 
 * @Author Juan Sebastian Quiceno <Juan.2114@hotmail.com>
 */
public class Assets {

	/**
	 * XXX: Yuk! clean this please...
	 */
	
	public static final int PLAYER_WALK_RIGHT = 0;
	public static final int PLAYER_WALK_LEFT = 1;
	public static final int PLAYER_DEFAULT_RIGHT = 3;
	public static final int PLAYER_DEFAULT_LEFT = 4;
	public static final int PLAYER_WALK_RIGHT_2 = 5;
	public static final int PLAYER_WALK_LEFT_2 = 6;
	public static final int PLAYER_STAIR_LEFT = 7;
	public static final int PLAYER_STAIR_RIGHT = 8;
	public static final int PLAYER_JUMP_RIGHT = 9;
	public static final int PLAYER_JUMP_LEFT = 10;
	public static final int BARREL_FIRST = 11;
	public static final int BARREL_SECOND = 12;
	public static final int BARREL_THIRD = 13;
	public static final int BARREL_FOURTH = 14;
	public static final int MONKEY_LEFT_HAND = 15;
	public static final int MONKEY_RIGHT_HAND = 16;
	public static final int MONKEY_NORMAL = 17;
	public static final int MONKEY_DROP_BALL = 18;
	public static final int MAP_HAMMER = 19;
	public static final int PLAYER_DEFAULT_HAMMER_LEFT = 20;
	public static final int PLAYER_DEFAULT_HAMMER_RIGHT = 21;

	public static final int PLAYER_HAMMER_LEFT = 22;
	public static final int PLAYER_HAMMER_RIGHT = 23;
	public static final int PLAYER_HAMMER_LEFT_2 = 24;
	public static final int PLAYER_HAMMER_RIGHT_2 = 25;

	public static final int PLAYER_HAMMER_SMASH_LEFT = 26;
	public static final int PLAYER_HAMMER_SMASH_RIGHT = 27;
	
	public static final int PRINCESS_LEFT = 28;
	public static final int PRINCESS_RIGHT = 29;
	
	public static final int LOGIN_BACKGROUND = 30;

	public static final int MONKEY_INTRO = 31;
	public static final int MONKEY_INTRO_2 = 32;

	public static BufferedImage[] IMAGES = new BufferedImage[33];

	static {
		String path = "./assets/";
		try {
			IMAGES[PLAYER_WALK_RIGHT] = ImageIO.read(new File(path + PLAYER_WALK_RIGHT + ".png"));
			IMAGES[PLAYER_WALK_LEFT] = ImageIO.read(new File(path + PLAYER_WALK_LEFT + ".png"));
			
			IMAGES[PLAYER_DEFAULT_RIGHT] = ImageIO.read(new File(path + PLAYER_DEFAULT_RIGHT + ".png"));
			IMAGES[PLAYER_DEFAULT_LEFT] = ImageIO.read(new File(path + PLAYER_DEFAULT_LEFT + ".png"));
			IMAGES[PLAYER_WALK_RIGHT_2] = ImageIO.read(new File(path + PLAYER_WALK_RIGHT_2 + ".png"));
			IMAGES[PLAYER_WALK_LEFT_2] = ImageIO.read(new File(path + PLAYER_WALK_LEFT_2 + ".png"));
			IMAGES[PLAYER_STAIR_LEFT] = ImageIO.read(new File(path + PLAYER_STAIR_LEFT + ".png"));
			IMAGES[PLAYER_STAIR_RIGHT] = ImageIO.read(new File(path + PLAYER_STAIR_RIGHT + ".png"));
			
			IMAGES[PLAYER_JUMP_RIGHT] = ImageIO.read(new File(path + PLAYER_JUMP_RIGHT + ".png"));
			IMAGES[PLAYER_JUMP_LEFT] = ImageIO.read(new File(path + PLAYER_JUMP_LEFT + ".png"));

			IMAGES[BARREL_FIRST] = ImageIO.read(new File(path + BARREL_FIRST + ".png"));
			IMAGES[BARREL_SECOND] = ImageIO.read(new File(path + BARREL_SECOND + ".png"));
			IMAGES[BARREL_THIRD] = ImageIO.read(new File(path + BARREL_THIRD + ".png"));
			IMAGES[BARREL_FOURTH] = ImageIO.read(new File(path + BARREL_FOURTH + ".png"));
			
			IMAGES[MONKEY_LEFT_HAND] = ImageIO.read(new File(path + MONKEY_LEFT_HAND + ".png"));
			IMAGES[MONKEY_RIGHT_HAND] = ImageIO.read(new File(path + MONKEY_RIGHT_HAND + ".png"));
			IMAGES[MONKEY_NORMAL] = ImageIO.read(new File(path + MONKEY_NORMAL + ".png"));
			IMAGES[MONKEY_DROP_BALL] = ImageIO.read(new File(path + MONKEY_DROP_BALL + ".png"));
			
			IMAGES[MAP_HAMMER] = ImageIO.read(new File(path + MAP_HAMMER + ".png"));

			IMAGES[PLAYER_DEFAULT_HAMMER_LEFT] = ImageIO.read(new File(path + PLAYER_DEFAULT_HAMMER_LEFT + ".png"));
			IMAGES[PLAYER_DEFAULT_HAMMER_RIGHT] = ImageIO.read(new File(path + PLAYER_DEFAULT_HAMMER_RIGHT + ".png"));

			IMAGES[PLAYER_HAMMER_LEFT] = ImageIO.read(new File(path + PLAYER_HAMMER_LEFT + ".png"));
			IMAGES[PLAYER_HAMMER_RIGHT] = ImageIO.read(new File(path + PLAYER_HAMMER_RIGHT + ".png"));
			IMAGES[PLAYER_HAMMER_LEFT_2] = ImageIO.read(new File(path + PLAYER_HAMMER_LEFT_2 + ".png"));
			IMAGES[PLAYER_HAMMER_RIGHT_2] = ImageIO.read(new File(path + PLAYER_HAMMER_RIGHT_2 + ".png"));
			
			IMAGES[PLAYER_HAMMER_SMASH_LEFT] = ImageIO.read(new File(path + PLAYER_HAMMER_SMASH_LEFT + ".png"));
			IMAGES[PLAYER_HAMMER_SMASH_RIGHT] = ImageIO.read(new File(path + PLAYER_HAMMER_SMASH_RIGHT + ".png"));
			
			IMAGES[PRINCESS_LEFT] = ImageIO.read(new File(path + PRINCESS_LEFT + ".png"));
			IMAGES[PRINCESS_RIGHT] = ImageIO.read(new File(path + PRINCESS_RIGHT + ".png"));

			IMAGES[LOGIN_BACKGROUND] = ImageIO.read(new File(path + LOGIN_BACKGROUND + ".jpg"));

			IMAGES[MONKEY_INTRO] = ImageIO.read(new File(path + MONKEY_INTRO + ".png"));
			IMAGES[MONKEY_INTRO_2] = ImageIO.read(new File(path + MONKEY_INTRO_2 + ".png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
