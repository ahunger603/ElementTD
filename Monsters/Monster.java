package Monsters;
import java.awt.*;

import TowerDefence.DamagePacket;
import TowerDefence.Entity;
import TowerDefence.MainFrame;

public abstract class Monster extends Entity
{
	protected double spawnTime = 1;
	protected int spawnOrder = 0;
	
	protected Point[] path;
	protected int pathIndex = 0;
	
	protected double direction = 0;
	protected int steps = 0;
	
	protected String spriteKey;
	
	public final String name;
	public final int[] creatureTypes;
	public final int level;
	public final double MAXHP;
	protected double hp;
	protected int armor;
	protected double moveSpeed;
	protected int gold;
	
	public Monster(MainFrame main, Point[] path, int r, int x, int y, String name, String spriteKey, int[] creatureTypes, int level, int gold, double hp, int armor, double moveSpeed)
	{
		super(main, x, y, r);
		depth = 5; 
		
		this.path = path;
		
		this.name = name;
		this.spriteKey = spriteKey;
		this.creatureTypes = creatureTypes;
		this.level = level;
		this.MAXHP = hp*level;
		this.hp = hp*level;
		this.armor = armor*level;
		this.moveSpeed = moveSpeed;
		this.gold = gold*level;
	}
	
	public void renderHealthBar(Graphics2D g2d, int Height)
	{
		
		if (hp < MAXHP && hp > 0 || main.getSelection() == entityID)
		{
			if (main.getSelection() == entityID)
			{
				int width = 47;
				g2d.setColor(Color.BLUE);
				g2d.fillRect((int)x - width/2 - 1, (int)y - Height - 1, width, 6);
			}
			int width = 40;
			int midWidth = (int)(width * (hp/MAXHP));
			g2d.setColor(Color.RED);
			g2d.fillRect((int)x - width/2, (int)y - Height, midWidth, 4);
			g2d.setColor(Color.BLACK);
			g2d.fillRect((int)x - width/2 + midWidth, (int)y - Height, width - midWidth, 4);
		}
	}
	
	protected void move()
	{
		if (getDistance(x, y, path[pathIndex].x, path[pathIndex].y) < r / 2)
		{
			pathIndex++;
			if (pathIndex >= path.length)
			{
				exists = false;
				main.lifeLost();
				return;
			}
		}
		Point target = path[pathIndex];
		double targetDirection = getDirectionToward(x, y, target.x, target.y);
		
		direction = getDirStepToDir(direction, targetDirection, 2 + (int)(moveSpeed*1.4));
		
		x += getXVector(direction, moveSpeed);
		y += getYVector(direction, moveSpeed);
	}
	
	public void takeDamage(DamagePacket dp)
	{
		double damage = 0;
		for(int i : creatureTypes)
		{
			switch(i)
			{
			//BASIC
			case 0:
				damage += dp.basicDam;
				damage += dp.fireDam;
				damage += dp.natureDam;
				damage += dp.lightDam;
				damage += dp.darknessDam;
				damage += dp.arcaneDam;
				damage += dp.mechanicalDam;
				damage += dp.electricityDam;
				damage += dp.waterDam;
				break;
			//FIRE
			case 1:
				damage += dp.basicDam * 0.80;
				damage += dp.fireDam;
				damage += dp.natureDam * 0.5;
				damage += dp.lightDam;
				damage += dp.darknessDam * 0.75;
				damage += dp.arcaneDam;
				damage += dp.mechanicalDam * 1.5;
				damage += dp.electricityDam;
				damage += dp.waterDam * 2;
				break;
				
			//NATURE
			case 2:
				damage += dp.basicDam * 0.80;
				damage += dp.fireDam * 2;
				damage += dp.natureDam;
				damage += dp.lightDam * 0.5;
				damage += dp.darknessDam;
				damage += dp.arcaneDam * 1.5;
				damage += dp.mechanicalDam;
				damage += dp.electricityDam;
				damage += dp.waterDam * 0.75;
				break;
				
			//LIGHT
			case 3:
				damage += dp.basicDam * 0.80;
				damage += dp.fireDam;
				damage += dp.natureDam * 2;
				damage += dp.lightDam;
				damage += dp.darknessDam * 0.5;
				damage += dp.arcaneDam * 0.75;
				damage += dp.mechanicalDam;
				damage += dp.electricityDam * 1.5;
				damage += dp.waterDam;	
				break;
				
			//DARKNESS
			case 4:
				damage += dp.basicDam * 0.80;
				damage += dp.fireDam * 1.5;
				damage += dp.natureDam;
				damage += dp.lightDam * 2;
				damage += dp.darknessDam;
				damage += dp.arcaneDam * 0.5;
				damage += dp.mechanicalDam;
				damage += dp.electricityDam * 0.75;
				damage += dp.waterDam;	
				break;
				
			//ARCANE
			case 5:
				damage += dp.basicDam * 0.80;
				damage += dp.fireDam;
				damage += dp.natureDam * 0.75;
				damage += dp.lightDam * 1.5;
				damage += dp.darknessDam * 2;
				damage += dp.arcaneDam;
				damage += dp.mechanicalDam * 0.5;
				damage += dp.electricityDam;
				damage += dp.waterDam;	
				break;
				
			//MECHANICAL
			case 6:
				damage += dp.basicDam * 0.80;
				damage += dp.fireDam * 0.75;
				damage += dp.natureDam;
				damage += dp.lightDam;
				damage += dp.darknessDam;
				damage += dp.arcaneDam * 2;
				damage += dp.mechanicalDam;
				damage += dp.electricityDam * 0.5;
				damage += dp.waterDam * 1.5;	
				break;
				
			//ELECTRICITY
			case 7:
				damage += dp.basicDam * 0.80;
				damage += dp.fireDam;
				damage += dp.natureDam;
				damage += dp.lightDam * 0.75;
				damage += dp.darknessDam * 1.5;
				damage += dp.arcaneDam;
				damage += dp.mechanicalDam * 2;
				damage += dp.electricityDam;
				damage += dp.waterDam * 0.5;	
				break;
				
			//WATER
			case 8:
				damage += dp.basicDam * 0.80;
				damage += dp.fireDam * 0.5;
				damage += dp.natureDam * 1.5;
				damage += dp.lightDam;
				damage += dp.darknessDam;
				damage += dp.arcaneDam;
				damage += dp.mechanicalDam * 0.75;
				damage += dp.electricityDam * 2;
				damage += dp.waterDam;	
				break;
			default:
				System.out.println("INVALID CREATURE TYPE");
			}
		}
		damage = (damage/creatureTypes.length) - armor;
		if (damage <= 0)
			damage = 1;
		
		hp -= damage;
		if (hp <= 0)
			death();
	}
	
	protected void death()
	{
		main.gainGold(gold);
		exists = false;
	}
	
	//
	//SET
	//
	
	public void setSpawnTime(double time)
	{
		spawnTime = time;
	}
	
	public void setSpawnOrder(int order)
	{
		spawnOrder = order;
	}
	
	public void setDirection(double dir)
	{
		direction = dir;
	}
	
	//
	//GET
	//
	
	public double getSpawnTime()
	{
		return spawnTime;
	}
	
	public int getSpawnOrder()
	{
		return spawnOrder;
	}
	
	public double getHp()
	{
		return hp;
	}
	
	public int getArmor()
	{
		return armor;
	}
	
	public Point getPoint(int index)
	{
		if (index < path.length)
		{
			return path[index];
		}
		
		return null;
	}
}
