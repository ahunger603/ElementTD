package Monsters;
import java.awt.*;

import TowerDefence.MainFrame;
import TowerDefence.SpriteMap;

public class SeaWeedMonster extends Monster
{
	
	private final static String name = "Sea Weed Monster";
	private final static String spriteKey = "seaweed";
	private final static int[] creatureTypes = {8};
	private final static int baseLevel = 5;
	private final static int baseGold = 18;
	private final static double baseHp = 40;
	private final static int baseArmor = 1;
	private final static double baseMoveSpeed = 2.1;
	private final static int baseRadius = 45;
	
	private int imageIndex = 0;
	
	public SeaWeedMonster(MainFrame main, Point[] path, int x, int y, int level)
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
