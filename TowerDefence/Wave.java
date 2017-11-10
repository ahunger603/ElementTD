package TowerDefence;
import java.util.ArrayList;

import Monsters.Monster;

public class Wave 
{
	private int index = 0;
	private final int researchPoints;
	private MainFrame main;
	ArrayList<Monster> wave = new ArrayList<Monster>();
	
	public Wave(MainFrame main, int rp)
	{
		this.main = main;
		this.researchPoints = rp;
	}
	
	public void addMonster(Monster m, double spawnTime)
	{
		m.setSpawnTime(spawnTime);
		wave.add(m);
		
	}
	
	public Monster next()
	{
		if (index < wave.size())
			return wave.get(index++);
		else
		{
			main.gainRp(researchPoints);
			return null;
		}
	}
}
