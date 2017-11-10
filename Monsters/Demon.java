package Monsters;
import java.awt.*;

import TowerDefence.MainFrame;
import TowerDefence.SpriteMap;

public class Demon extends Monster
{
	
	private final static String name = "Demon";
	private final static String spriteKey = "demon";
	private final static int[] creatureTypes = {1};
	private final static int baseLevel = 5;
	private final static int baseGold = 15;
	private final static double baseHp = 33;
	private final static int baseArmor = 1;
	private final static double baseMoveSpeed = 1.4;
	private final static int baseRadius = 45;
	
	private int imageIndex = 0;
	
	public Demon(MainFrame main, Point[] path, int x, int y, int level)
	{
		super(main, path, baseRadius, x, y, name, spriteKey, creatureTypes, ((level < baseLevel) ? 1 : level - baseLevel), baseGold, baseHp, baseArmor, baseMoveSpeed);
	}
	
	public void step()
	{
		move();
		
		//Animation
		if (steps % 11 == 0)
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
