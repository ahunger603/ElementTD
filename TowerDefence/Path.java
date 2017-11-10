package TowerDefence;
import java.awt.Point;
import java.awt.Polygon;


public class Path 
{
	int pathWidth;
	Point[] path;
	Polygon[] pathArea;
	
	public Path(Point[] path, int pathWidth)
	{
		this.path = path;
		this.pathWidth = pathWidth;
		
		pathArea = new Polygon[path.length - 1];
		for(int i = 0; i < path.length - 1; i++)
		{
			int[] xpoints = new int[4];
			int[] ypoints = new int[4];
			
			Point p1 = path[i];
			Point p2 = path[i+1];
			
			double dir = Entity.getDirectionToward(p1.x, p1.y, p2.x, p2.y);
			double invDir = (dir + 180) % 360;
			
			Point cap1 = new Point(p1.x + (int)Entity.getXVector(invDir, pathWidth/2), p1.y + (int)Entity.getYVector(invDir, pathWidth/2));
			Point cap2 = new Point(p2.x + (int)Entity.getXVector(dir, pathWidth/2), p2.y + (int)Entity.getYVector(dir, pathWidth/2));
			
			xpoints[0] = cap1.x + (int)Entity.getXVector((dir + 90)%360, pathWidth/2);
			ypoints[0] = cap1.y + (int)Entity.getYVector((dir + 90)%360, pathWidth/2);
			
			xpoints[1] = cap1.x + (int)Entity.getXVector((dir + 270)%360, pathWidth/2);
			ypoints[1] = cap1.y + (int)Entity.getYVector((dir + 270)%360, pathWidth/2);
			
			xpoints[2] = cap2.x + (int)Entity.getXVector((dir + 270)%360, pathWidth/2);
			ypoints[2] = cap2.y + (int)Entity.getYVector((dir + 270)%360, pathWidth/2);
			
			xpoints[3] = cap2.x + (int)Entity.getXVector((dir + 90)%360, pathWidth/2);
			ypoints[3] = cap2.y + (int)Entity.getYVector((dir + 90)%360, pathWidth/2);
			
			pathArea[i] = new Polygon(xpoints, ypoints, 4);
		}
	}
	
	public Point[] getPath()
	{
		return path;
	}
	
	public Polygon[] getPathArea()
	{
		return pathArea;
	}
	
	public boolean inPath(int x, int y, int r)
	{
		for(Polygon p : pathArea)
		{
			if (p.contains(new Point(x, y)))
				return true;
			else if (p.intersects(x - r, y - r, r*2, r*2))
					return true;
		}
		
		return false;
	}
}
