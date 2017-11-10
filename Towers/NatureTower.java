package Towers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

import TowerDefence.DamagePacket;
import TowerDefence.MainFrame;
import TowerDefence.SpriteMap;

public class NatureTower extends Tower
{
public final static String name = "Nature Tower";
	
	public final static int tier = 1;
	public final static int[] types = {2};
	public final static int cost = 100;
	public final static int radius = 32;
	public final static String spriteKey = "natureTower";
	
	protected final static int[] upgrades = {};
	protected final static int baseRange = 200;
	protected final static double baseCooldown = 1.4;
	
	protected final static int[][] min_max = {null, null, {15, 35}, null, null, null, null, null, null};
	
	private ArrayList<LingeringEffect> lfList = new ArrayList<LingeringEffect>();
	
	public NatureTower(MainFrame main, int x, int y)
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
		
		int index = 0;
		for(int i = 0; i < lfList.size(); i++)
		{
			LingeringEffect lf = lfList.get(index);
			
			lf.step();
			
			if (lf.isEnd())
				lfList.remove(index--);
			
			index++;
		}
	}
	
	public void render(Graphics2D g2d)
	{
		boolean selected = false;
		if (main.getSelection() == entityID)
			selected = true;
		
		SpriteMap.drawTower(g2d, spriteKey, selected, (int)x, (int)y, (int)range, direction);
		
		for(LingeringEffect lf : lfList)
		{
			lf.render(g2d);
		}
	}
	
	public void attack()
	{
		assert target != null;
		
		lfList.add(new LingeringEffect((int)target.getX(), (int)target.getY(), "root_direct"));
		target.takeDamage(new DamagePacket(min_max));
	}
	
	public class LingeringEffect
	{
		private final int x, y;
		
		private int step = 0;
		private int imageIndex = 0;
		private boolean isEnd = false;
		private final String effectSpriteKey;
		
		public LingeringEffect(int x, int y, String effectSpriteKey)
		{
			this.x = x;
			this.y = y;
			
			this.effectSpriteKey = effectSpriteKey;
		}
		
		public void step()
		{
			if (step % 4 == 0)
			{
				imageIndex++;
			}
			step++;
			
			if (imageIndex != 0 && (imageIndex % SpriteMap.EffectSprites.get(effectSpriteKey).length == 0))
				isEnd = true;
		}
		
		public void render(Graphics2D g2d)
		{
			if (!isEnd)
			{
				BufferedImage[] images = SpriteMap.EffectSprites.get(effectSpriteKey);
				BufferedImage image = images[imageIndex % images.length];
				g2d.drawImage(image, x - image.getWidth()/2, y - image.getHeight()/2, null);
			}
			
		}
		
		public boolean isEnd()
		{
			return isEnd;
		}
	}
}
