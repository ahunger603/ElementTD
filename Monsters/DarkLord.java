package Monsters;
import java.awt.Graphics2D;
import java.awt.Point;

import TowerDefence.MainFrame;
import TowerDefence.SpriteMap;

public class DarkLord extends Monster
{
	private final static String name = "Dark Lord";
	private final static String spriteKey = "darklord";
	private final static int[] creatureTypes = {4};
	private final static int baseLevel = 5;
	private final static int baseGold = 20;
	private final static double baseHp = 65;
	private final static int baseArmor = 3;
	private final static double baseMoveSpeed = 1.3;
	private final static int baseRadius = 45;
	
	private int imageIndex = 0;
	
	public DarkLord(MainFrame main, Point[] path, int x, int y, int level)
	{
		super(main, path, baseRadius, x, y, name, spriteKey, creatureTypes, ((level < baseLevel) ? 1 : level - baseLevel), baseGold, baseHp, baseArmor, baseMoveSpeed);
	}
	
	public void step()
	{
		move();
		
		//Animation
		if (steps % 6 == 0)
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
