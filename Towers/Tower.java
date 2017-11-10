package Towers;
import java.util.*;

import Monsters.Monster;
import TowerDefence.Entity;
import TowerDefence.MainFrame;

public abstract class Tower extends Entity
{
	protected static Random rng = new Random();
	
	public final String name;
	protected String[] tooltip;
	
	public final String spriteKey;
	
	public final int tier;
	public final int[] types;
	public final int[] upgrades;
	public final int cost;
	
	protected int priority = 3;
	protected Monster target;
	protected double direction = 0;
	protected int range;
	protected double cooldown;
	protected int[][] min_max;
	protected double lastShot = System.nanoTime() / 1000000000;
	
	public Tower(MainFrame main, int x, int y, int r, String name, String spriteKey, int tier, int[] types, int cost, int[] upgrades, int[][] min_max, int range, double cooldown)
	{
		super(main, x, y, r);
		
		depth = 5;
		this.name = name;
		
		this.spriteKey = spriteKey;
		
		this.tier = tier;
		this.types = types;
		this.upgrades = upgrades;
		this.cost = cost;
		
		this.min_max = min_max;
		this.range = range;
		this.cooldown = cooldown;
	}
	
	public static String getTowerSpriteKey(int towerCode)
	{
		String spriteKey = "";
		switch(towerCode)
		{
		case 0:
			spriteKey = CannonTower.spriteKey;
			break;
		case 1:
			spriteKey = FireTower.spriteKey;
			break;
		case 2:
			spriteKey = NatureTower.spriteKey;
			break;
		case 3:
			spriteKey = ElectricTower.spriteKey;
			break;
		default:
			System.out.println("Invalid build code");
		}
		
		return spriteKey;
	}
	
	public static int getTowerRadius(int towerCode)
	{
		int r = -1;
		switch(towerCode)
		{
		case 0:
			r = CannonTower.radius;
			break;
		case 1:
			r = FireTower.radius;
			break;
		case 2:
			r = NatureTower.radius;
			break;
		case 3:
			r = ElectricTower.radius;
			break;
		default:
			System.out.println("Invalid build code");
		}
		
		return r;
	}
	
	public static int getTowerCost(int towerCode)
	{
		int c = 0;
		switch(towerCode)
		{
		case 0:
			c = CannonTower.cost;
			break;
		case 1:
			c = FireTower.cost;
			break;
		case 2:
			c = NatureTower.cost;
			break;
		case 3:
			c = ElectricTower.cost;
			break;
		default:
			System.out.println("Invalid build code");
		}
		
		return c;
	}
	
	public static int getTowerTier(int towerCode)
	{
		int t = 0;
		switch(towerCode)
		{
		case 0:
			t = CannonTower.tier;
			break;
		case 1:
			t = FireTower.tier;
			break;
		case 2:
			t = NatureTower.tier;
			break;
		case 3:
			t = ElectricTower.tier;
			break;
		default:
			System.out.println("Invalid build code");
		}
		
		return t;
	}
	
	public static int[] getTowerTypes(int towerCode)
	{
		int[] t = {};
		switch(towerCode)
		{
		case 0:
			t = CannonTower.types;
			break;
		case 1:
			t = FireTower.types;
			break;
		case 2:
			t = NatureTower.types;
			break;
		case 3:
			t = ElectricTower.types;
			break;
		default:
			System.out.println("Invalid build code");
		}
		
		return t;
	}
	
	public static Tower getTower(int towerCode, MainFrame main, int x, int y)
	{
		Tower t = null;
		switch(towerCode)
		{
		case 0:
			t = new CannonTower(main, x, y);
			break;
		case 1:
			t = new FireTower(main, x, y);
			break;
		case 2:
			t = new NatureTower(main, x, y);
			break;
		case 3:
			t = new ElectricTower(main, x, y);
			break;
		default:
			System.out.println("Invalid build code");
		}
		
		return t;
	}
	
	public void defaultStep(){}
	
	public boolean inRange(double x, double y)
	{
		return (getDistance(this.x, this.y, x, y) <= range);
	}
	
	public static boolean canBuild(int towerCode)
	{
		int[] elementLevels = main.getElementLevels();
		int[] towerTypes = getTowerTypes(towerCode);
		int towerTier = getTowerTier(towerCode);
		
		for(int type : towerTypes)
		{
			if (elementLevels[type - 1] < towerTier)
				return false;
		}
		
		return true;
	}
	
	public void upgrade(int towerCode)
	{
		Tower t = null;
		
		t = getTower(towerCode, main, (int)x, (int)y);
		
		if (t.cost <= main.getGold())
		{
			exists = false;
			t.init();
			
			main.setSelection(t.entityID);
			main.spendGold(t.cost);
		}
	}
	
	protected Monster setTarget()
	{
		/* 0 - First (Default)
		 * 1 - Last
		 * 2 - Strongest
		 * 3 - Weakest
		 */
		Vector<Entity> entityVector = main.getManager().getEntityVector();
		switch(priority)
		{
		//First
		case 0:
			Monster firstMonster = null;
			for(Entity e : entityVector)
			{
				if (e instanceof Monster)
				{
					Monster m = (Monster)e;
					if ((firstMonster == null || m.getSpawnOrder() < firstMonster.getSpawnOrder()) && inRange(m.getX(), m.getY()))
						firstMonster = m;
				}
			}
			return firstMonster;
			
		//Last
		case 1:
			Monster lastMonster = null;
			for(Entity e : entityVector)
			{
				if (e instanceof Monster)
				{
					Monster m = (Monster)e;
					if ((lastMonster == null || m.getSpawnOrder() > lastMonster.getSpawnOrder()) && inRange(m.getX(), m.getY()))
							lastMonster = m;
				}
			}
			return lastMonster;
		
		//Strongest
		case 2:
			Monster strongestMonster = null;
			for(Entity e : entityVector)
			{
				if (e instanceof Monster)
				{
					Monster m = (Monster)e;
					if ((strongestMonster == null || m.getHp() > strongestMonster.getHp()) && inRange(m.getX(), m.getY()))
						strongestMonster = m;
				}
			}
			return strongestMonster;
			
		//Weakest
		case 3:
			Monster weakestMonster = null;
			for(Entity e : entityVector)
			{
				if (e instanceof Monster)
				{
					Monster m = (Monster)e;
					if ((weakestMonster == null || m.getHp() < weakestMonster.getHp()) && inRange(m.getX(), m.getY()))
						weakestMonster = m;
				}
			}
			return weakestMonster;
			
		default:
			System.out.println("INVALID PRIORITY");
			break;
		}
		
		return null;
	}
	
	public void setToolTip()
	{
		int size = 8 + (types.length);
		tooltip = new String[size];
		
		int index = 0;
		tooltip[index++] = "   "+name+"   ";
		tooltip[index++] = "Cost: " + cost + " gold";
		tooltip[index++] = "Tier: " + tier;
		tooltip[index++] = "Types: ";
		for(int k = 0; k < types.length; k++)
		{
			int i = types[k];
			String s = null;
			switch(i)
			{
			case 0:
				if (min_max[i] != null)
					s = "Basic";
				break;
			case 1:
				if (min_max[i] != null)
					s = "Fire";
				break;
			case 2:
				if (min_max[i] != null)
					s = "Nature";
				break;
			case 3:
				if (min_max[i] != null)
					s = "Light";
				break;
			case 4:
				if (min_max[i] != null)
					s = "Darkness";
				break;
			case 5:
				if (min_max[i] != null)
					s = "Arcane";
				break;
			case 6:
				if (min_max[i] != null)
					s = "Mechanical";
				break;
			case 7:
				if (min_max[i] != null)
					s = "Electricity";
				break;
			case 8:
				if (min_max[i] != null)
					s = "Water";
				break;
			default:
				s = "null";
			}
			
			tooltip[index++] = s;
		}
		
		tooltip[index++] = "";
		tooltip[index++] = "Damage: " + getMinSum() + " - " + getMaxSum();
		tooltip[index++] = "Range: " + range;
		tooltip[index++] = "Cooldown: " + cooldown;
	}
	
	public String[] getToolTip()
	{
		setToolTip();
		
		return tooltip;
	}
	
	public int[][] getMinMax()
	{
		return min_max;
	}
	
	public int getMinSum()
	{
		int sum = 0;
		for(int i = 0; i < 9; i++)
		{
			int[] min_max_bit = min_max[i];
			if (min_max_bit != null)
				sum += min_max_bit[0];
		}
		
		return sum;
	}
	
	public int getMaxSum()
	{
		int sum = 0;
		for(int i = 0; i < 9; i++)
		{
			int[] min_max_bit = min_max[i];
			if (min_max_bit != null)
				sum += min_max_bit[1];
		}
		
		return sum;
	}
	
	
	
	protected abstract void attack();
}
