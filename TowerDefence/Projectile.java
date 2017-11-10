package TowerDefence;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import Monsters.Monster;

public class Projectile extends Entity
{
	private double direction = 0;
	private boolean homing;
	private int speed;
	private DamagePacket p_dp;
	
	private int AOE_r;
	private DamagePacket a_dp;
	
	private String mainSpriteKey;
	private String aoeSpriteKey;
	private int steps = 0;
	private int imageIndex = 0;
	
	private int targetID;
	private boolean collide = false;
	
	public Projectile(MainFrame main, int x, int y, int r, String mainSpriteKey, DamagePacket p_dp, double direction, boolean homing, int targetID, int speed, String aoeSpriteKey, int AOE_r, DamagePacket a_dp)
	{
		super(main, x, y, r);
		
		depth = 6;
		this.direction = direction;
		this.homing = homing;
		this.speed = speed;
		this.p_dp = p_dp;
		
		this.AOE_r = AOE_r;
		this.a_dp = a_dp;
		
		this.mainSpriteKey = mainSpriteKey;
		this.aoeSpriteKey = aoeSpriteKey;
		
		this.targetID = targetID;
		main.getManager().addEntity(this);
	}
	
	public void collisionCheck()
	{
		if (!collide)
		{
			Vector<Entity> entityVector = main.getManager().getEntityVector();
			for(Entity e : entityVector)
			{
				if (e instanceof Monster)
				{
					Monster m = (Monster)e;
					if (getDistance(x, y, m.getX(), m.getY()) - m.getR() <= r)
					{
						//Collision
						
						//DIRECT
						m.takeDamage(p_dp);
						
						//AOE
						if (AOE_r > 0 && a_dp != null)
						{
							Vector<Monster> inRangeVector = main.getManager().getMonstersInRange((int)x, (int)y, AOE_r);
							for(Monster mm : inRangeVector)
							{
								mm.takeDamage(a_dp);
							}
							collide = true;
							steps = 0;
							imageIndex = 0;
						}
						else
						{
							exists = false;
						}
						return;
					}
				}
			}
		}
	}
	
	
	public void step()
	{
		if (x > main.width + 50 || x < -50 || y > main.height + 50 || y < -50)
		{
			exists = false;
		}
		if (!collide)
		{			
			if (homing && targetID != -1)
			{
				Monster target = null;
				try
				{
					target = (Monster)main.getManager().getEntity(targetID);
					
				} catch (Exception ex) {ex.printStackTrace();}
				
				if (target != null)
				{
					double targetDirection = getDirectionToward(x, y, target.getX(), target.getY());
					
					direction = getDirStepToDir(direction, targetDirection, 3);
				}
			}
			
			x += getXVector(direction, speed);
			y += getYVector(direction, speed);
			
			//Animation
			if (steps % 4 == 0)
			{
				try
				{
					imageIndex++;
				
				}catch(Exception ex){};
			}
			
			collisionCheck();		
			steps++;
		}
		else
		{
			if (steps % 4 == 0)
			{
				try
				{
					imageIndex++;
				
				}catch(Exception ex){};
			}
			if (aoeSpriteKey != null && imageIndex != 0 && (imageIndex % SpriteMap.EffectSprites.get(aoeSpriteKey).length == 0))
			{
				exists = false;
			}
			steps++;
		}
	}
	
	public void render(Graphics2D g2d)
	{
		if (!collide)
			SpriteMap.drawEffect(g2d, mainSpriteKey, imageIndex, (int)x, (int)y, (direction + 90)%360);
		else if (aoeSpriteKey != null)
		{
			BufferedImage[] images = SpriteMap.EffectSprites.get(aoeSpriteKey);
			BufferedImage image = SpriteMap.scale(images[imageIndex % images.length], AOE_r, AOE_r);
			g2d.drawImage(image, (int)x - image.getWidth()/2, (int)y - image.getHeight()/2, null);
		}
	}
}
