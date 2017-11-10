package Monsters;

import java.awt.Graphics2D;
import java.awt.Point;

import TowerDefence.MainFrame;
import TowerDefence.SpriteMap;

public class ChargedShaman extends Monster
{
	private final static String name = "Charged Shaman";
	private final static String spriteKey = "chargedshaman";
	private final static int[] creatureTypes = {7};
	private final static int baseLevel = 5;
	private final static int baseGold = 23;
	private final static double baseHp = 60;
	private final static int baseArmor = 0;
	private final static double baseMoveSpeed = 4.5;
	private final static int baseRadius = 45;
	
	private int imageIndex = 0;
	
	public ChargedShaman(MainFrame main, Point[] path, int x, int y, int level)
	{
		super(main, path, baseRadius, x, y, name, spriteKey, creatureTypes, ((level <= baseLevel) ? 1 : level - baseLevel), baseGold, baseHp, baseArmor, baseMoveSpeed);
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
