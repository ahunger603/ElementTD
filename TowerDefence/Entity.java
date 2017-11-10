package TowerDefence;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public abstract class Entity 
{
	private static int entityIndentifier = 0;
	public final int entityID;
	
	protected boolean exists = true;
	
	protected int depth = 0;
	protected double x, y, r;
	
	protected static MainFrame main;
	
	protected Entity(MainFrame main, int x, int y, int r)
	{
		if (Entity.main == null)
			Entity.main = main;
		
		this.x = x;
		this.y = y;
		this.r = r;
		
		entityID = ++entityIndentifier;
	}
	
	public static class EntityComparator implements Comparator<Entity>
	{
		private int attribute;
		
		public EntityComparator(int attribute)
		{
			this.attribute = attribute;
		}
		public int compare(Entity e1, Entity e2)
		{
			switch(attribute)
			{
			case 0:
				int d1 = e1.getDepth();
				int d2 = e2.getDepth();
				
				if (d1 == d2)
					return 0;
				else if (d1 > d2)
					return 1;
				else
					return -1;
			case 1:
				int id1 = e1.entityID;
				int id2 = e2.entityID;
				
				if (id1 == id2)
					return 0;
				else if (id1 > id2)
					return 1;
				else
					return -1;
			default:
				return 0;
			}
		}
	}
	
	public void init()
	{
		main.getManager().addEntity(this);
	}
	
	public int hashCode()
	{
		return entityID;
	}
	
	public boolean exists()
	{
		return exists;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getR()
	{
		return r;
	}
	
	public int getDepth()
	{
		return depth;
	}
	
	public void setPos(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public abstract void step();
	
	public abstract void render(Graphics2D g2d);
	
	protected static void drawImage(BufferedImage src, Graphics2D g2d, int x, int y, double ang)
	{
		BufferedImage currentImage = SpriteMap.rotate(src, Math.toRadians(ang));
		
		int cx_offset = currentImage.getWidth()/2;
		int cy_offset = currentImage.getHeight()/2;
		
		g2d.drawImage(currentImage, x - cx_offset, y - cy_offset, null);
	}
	
	protected static double getDirectionToward(double orgin_x, double orgin_y, double target_x, double target_y)
	{
		double dx = target_x - orgin_x;
		double dy = target_y - orgin_y;
		
		double ang = 90 - Math.toDegrees(Math.atan2(-dy, dx));
		
		if (ang < 0)
		{
			ang += 360;
		}
		
		ang %= 360;
		
		return ang;
	}
	
	protected static double getDirStepToDir(double ang1, double ang2, double maxTurnSpeed)
	{
		double dirInc = ((((ang2 - ang1) % 360) + 540) % 360) - 180;
		
		double direction = (ang1 + Math.min(Math.abs(dirInc), maxTurnSpeed) * Integer.signum((int)dirInc))%360;
		
		return (direction);
	}
	
	protected static double getDistance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(((x2 - x1)*(x2 - x1))+((y2 - y1)*(y2 - y1)));
	}
	
	protected static double getXVector(double ang, double len)
	{
		double theta = (ang - 90) * Math.PI / 180;
		return (len * Math.cos(theta));
	}
	
	protected static double getYVector(double ang, double len)
	{
		double theta = (ang - 90) * Math.PI / 180;
		return (len * Math.sin(theta));
	}
}
