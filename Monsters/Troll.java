package Monsters;
import java.awt.*;

import TowerDefence.MainFrame;
import TowerDefence.SpriteMap;

public class Troll extends Monster
{
	
	private final static String name = "Troll";
	private final static String spriteKey = "troll";
	private final static int[] creatureTypes = {2};
	private final static int baseLevel = 5;
	private final static int baseGold = 10;
	private final static double baseHp = 20;
	private final static int baseArmor = 2;
	private final static double baseMoveSpeed = 1;
	private final static int baseRadius = 45;
	
	private int imageIndex = 0;
	
	public Troll(MainFrame main, Point[] path, int x, int y, int level)
	{
		super(main, path, baseRadius, x, y, name, spriteKey, creatureTypes, ((level < baseLevel) ? 1 : level - baseLevel), baseGold, baseHp, baseArmor, baseMoveSpeed);
	}
	
	public void step()
	{
		move();
		
		//Animation
		if (steps % 12 == 0)
		{
			imageIndex++;
		}
				
		steps++;
	}
	
	public void render(Graphics2D g2d)
	{
		SpriteMap.drawMonster(g2d, spriteKey, 0, imageIndex, (int)x, (int)y, direction);
	}
}
