package Towers;

import java.util.ArrayList;

import java.awt.Graphics2D;
import java.awt.Point;

import TowerDefence.DamagePacket;
import TowerDefence.MainFrame;
import TowerDefence.SpriteMap;
import TowerDefence.Entity;
import Monsters.Monster;

public class ElectricTower extends Tower
{
	public final static String name = "Electric Tower";
	
	public final static int tier = 1;
	public final static int[] types = {7};
	public final static int cost = 100;
	public final static int radius = 32;
	public final static String spriteKey = "electricTower";
	
	protected final static int[] upgrades = {};
	protected final static int baseRange = 325;
	protected final static double baseCooldown = 0.8;
	protected final static int targets = 3;
	protected final static int[][] min_max = {null, null, null, null, null, null, null, {2, 15}, null};
	
	protected ArrayList<Point> electric_points = new ArrayList<Point>();
	
	public ElectricTower(MainFrame main, int x, int y)
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
			direction += 3;
			
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
		
		Point last = new Point((int)x, (int)y);
		for(Point p : electric_points)
		{
			SpriteMap.drawEffect(g2d, "lightning", 0, last.x, last.y, p.x, p.y);
			last = p;
		}
		electric_points.clear();
	}
	
	public void attack()
	{
		assert target != null;
		
		int[] exclude = new int[targets];
		System.out.println(target.entityID);
		electric_points.add(new Point((int)target.getX(), (int)target.getY()));
		target.takeDamage(new DamagePacket(min_max));
		exclude[0] = target.entityID;
		
		targets: for(int i = 0; i < targets-1; i++)
		{
			Entity e = main.getManager().getEntity(target.entityID+1);
			while(true)
			{
				e = main.getManager().getEntity(target.entityID+1);
				if (e == null)
					break targets;
				else
				{
					if (e instanceof Monster)
					{
						target = (Monster)e;
						break;
					}
				}
			}
			
			System.out.println(target.entityID);
			for(int k : exclude)
				System.out.println("exlcude: " + k);
			if (target != null)
			{
				electric_points.add(new Point((int)target.getX(), (int)target.getY()));
				target.takeDamage(new DamagePacket(min_max));
				exclude[i+1] = target.entityID;
			}
			else
				break;
		}
	}
}
