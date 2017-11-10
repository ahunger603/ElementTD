package Towers;

import java.awt.*;

import TowerDefence.DamagePacket;
import TowerDefence.MainFrame;
import TowerDefence.Projectile;
import TowerDefence.SpriteMap;

public class CannonTower extends Tower
{
	public final static String name = "Cannon Tower";
	
	public final static int tier = 0;
	public final static int[] types = {0};
	public final static int cost = 50;
	public final static int radius = 32;
	public final static String spriteKey = "cannonTower";
	
	protected final static int[] upgrades = {1,2,3};
	protected final static int baseRange = 250;
	protected final static double baseCooldown = 1;
	
	protected final static int[][] min_max = {{5, 10}, null, null, null, null, null, null, null, null};
	
	public CannonTower(MainFrame main, int x, int y)
	{
		super(main, x, y, radius, name, spriteKey, tier, types, cost, upgrades, min_max, baseRange, baseCooldown);
		
		setToolTip();
	}
	
	public void step()
	{
		defaultStep();
		
		target = setTarget();
		if(target != null)
		{
			double targetdirection = getDirectionToward(x, y, target.getX(), target.getY());
			
			direction = getDirStepToDir(direction, targetdirection, 3);
			
			if	(System.nanoTime() / 1000000000 - lastShot >= cooldown)
			{
				lastShot = System.nanoTime() / 1000000000;
				attack();
			}
		}
	}
	
	public void render(Graphics2D g2d)
	{
		boolean selected = false;
		if (main.getSelection() == entityID)
			selected = true;
		
		SpriteMap.drawTower(g2d, spriteKey, selected, (int)x, (int)y, (int)range, direction);
	}
	
	public void attack()
	{
		assert target != null;
		
		new Projectile(main, (int)(x + getXVector(direction, r)), (int)(y + getYVector(direction, r)), 10, "cannonBall_projectile", new DamagePacket(min_max), direction, false, -1, 6, null, 0, null);
	}
}
