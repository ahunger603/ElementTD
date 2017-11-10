package TowerDefence;
import java.util.*;
import java.awt.*;
import java.awt.image.*;

import Monsters.*;
import Towers.Tower;

public class Level 
{
	private MainFrame main;
	private boolean won = false;
	private boolean running = false;
	
	public final int level;
	private Wave[] waves;
	private Wave wave;
	private int waveInd = 0;
	private Monster nextMonster;
	private float lastSpawn = System.nanoTime() / 1000000000;
	
	private Path path;
	
	private BufferedImage background;
	
	public Level(MainFrame main, int level, Path path, Wave[] waves, BufferedImage background)
	{
		this.main = main;
		this.level = level;
		this.path = path;
		this.waves = waves;
		this.background = background;
	}
	
	public static Level levelSelect(MainFrame main, int level)
	{
		switch(level)
		{
		case 1:
			return level_1(main);
		default:
			return null;
		}
	}
	
	public static Level level_1(MainFrame main)
	{
		Point[] points = {new Point(145, -50), new Point(147, 299), new Point(431, 299), new Point(434, 229), new Point(297, 211), 
				new Point(259, 173), new Point(286, 124), new Point(472, 92), new Point(513, 60), new Point(570, 67), new Point(619, 98), 
				new Point(773, 142), new Point(804, 179), new Point(761, 212), new Point(633, 232), new Point(619, 273), new Point(648, 293),
				new Point(809, 296), new Point(921, 409), new Point(917, 516), new Point(803, 588), new Point(672, 559), new Point(655, 489),
				new Point(224, 483), new Point(137, 496), new Point(142, 770)}; 
		
		Path path = new Path(points, 50);
		
		Wave[] waves = new Wave[5];
		waves[0] = new Wave(main, 1);
		for(int j = 0; j < 5; j++)
			waves[0].addMonster(new Troll(main, points, points[0].x, points[0].y, 1), 1);
		
		waves[1] = new Wave(main, 0);
		for(int j = 1; j < 6; j++)
			waves[1].addMonster(new Demon(main, points, points[0].x, points[0].y, 2), 1);
		
		waves[2] = new Wave(main, 0);
		for(int j = 1; j < 7; j++)
			waves[2].addMonster(new SeaWeedMonster(main, points, points[0].x, points[0].y, 3), 2);
		
		waves[3] = new Wave(main, 0);
		for(int j = 1; j < 8; j++)
			waves[3].addMonster(new DarkLord(main, points, points[0].x, points[0].y, 4), 1);
		
		waves[4] = new Wave(main, 1);
		for(int j = 1; j < 8; j++)
			waves[4].addMonster(new ChargedShaman(main, points, points[0].x, points[0].y, 5), 1.5);
		
		
		BufferedImage background = null;
		while(background == null)
			background = SpriteMap.level_1bg;
		
		return (new Level(main, 1, path, waves, background));
	}
	
	public void step()
	{
		int spawnCount = 0;
		if (running && !won)
		{
			float now = System.nanoTime() / 1000000000;
			
			if (wave == null)
			{
				wave = waves[waveInd];
				spawnCount = 0;
			}
			
			if (nextMonster == null)
			{
				nextMonster = wave.next();
				lastSpawn = System.nanoTime() / 1000000000;
				spawnCount++;
			}
			
			if (nextMonster != null)
			{
				if (now - lastSpawn >= nextMonster.getSpawnTime())
				{
					nextMonster.init();
					nextMonster.setSpawnOrder(spawnCount); 
					nextMonster.setDirection(Entity.getDirectionToward(nextMonster.getX(), nextMonster.getY(), nextMonster.getPoint(1).getX(), nextMonster.getPoint(1).getY()));
					nextMonster = null;
				}
			}
			else
			{
				//Progress waves unless end
				wave = null;
				waveInd++;
				if (waveInd >= waves.length)
					won = true;
				
				running = false;
			}
			
		}
	}
	
	public boolean buildable(int x, int y, int r)
	{
		if (x < background.getWidth())
		{
			boolean open = true;
			Vector<Entity> entities = main.getManager().getEntityVector();
			for(Entity e : entities)
			{
				if (e instanceof Tower)
				{
					if (Entity.getDistance(x, y, e.getX(), e.getY()) <= (r + e.getR()))
					{
						open = false;
						break;
					}
				}
			}
			
			return (!path.inPath(x, y, r) && open);
		}
		
		return false;
	}
	
	public void render(Graphics2D g2d)
	{
		g2d.drawImage(background, 0, 0, null);
	}
	
	//
	//GET
	//
	public int getLevelNum()
	{
		return level;
	}
	
	public int getWaveNum()
	{
		return waveInd+1;
	}
	
	public Point[] getPath()
	{
		return path.getPath();
	}
	
	public Polygon[] getPathArea()
	{
		return path.getPathArea();
	}
	
	public boolean getState()
	{
		return running;
	}
	
	public boolean isCompleted()
	{
		return won;
	}
	
	//
	//SET
	//
	
	public void setState(boolean state)
	{
		running = state;
	}
}
