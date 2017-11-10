package TowerDefence;

import java.util.*;

import Monsters.Monster;

public class EntityManager 
{
	private Vector<Entity> entityVector = new Vector<Entity>();
	
	public void addEntity(Entity e)
	{
		entityVector.add(e);
	}
	
	public Vector<Entity> getEntityVector()
	{
		return entityVector;
	}
	
	public Vector<Entity> getSortedEntityVector(int attribute)
	{
		Vector<Entity> returnVector = new Vector<Entity>();
		returnVector.addAll(entityVector);
		
		Collections.sort((List<Entity>)entityVector, new Entity.EntityComparator(attribute));
		
		return returnVector;
	}
	
	public boolean entityExists(int ID)
	{
		for(Entity e : entityVector)
		{
			if (e.entityID == ID && e.exists)
				return true;
		}
		return false;
	}
	
	public Entity getEntity(int ID)
	{
		Vector<Entity> sortedVector = getSortedEntityVector(1);
		
		int start = 0;
		int end = sortedVector.size() - 1;
		while(start <= end)
		{
			int mid = (start + end)/2;
			
			int midID = sortedVector.get(mid).entityID;
			if (midID == ID)
				return sortedVector.get(mid);
			else if (ID < midID)
				end = mid - 1;
			else
				start = mid + 1;
		}
		
		return null;
	}
	
	public Monster getMonsterAtPoint(int x, int y)
	{
		for(Entity e : entityVector)
		{
			if (e instanceof Monster)
			{
				if (Entity.getDistance(x, y, e.getX(), e.getY()) <= e.getR())
				{
					return (Monster)e;
				}
			}
		}
		return null;
	}
	
	public Vector<Monster> getMonstersInRange(int x, int y, int r)
	{
		Vector<Monster> mv = new Vector<Monster>();
		for(Entity e : entityVector)
		{
			if (e instanceof Monster)
			{
				Monster m = (Monster)e;
				if (Entity.getDistance(m.getX(), m.getY(), x, y) <= r)
					mv.add(m);
			}
		}
		
		return mv;
	}
	
	public Monster getRandomMonsterInRange(int x, int y, int r)
	{
		Random rng = new Random();
		Vector<Monster> mv = getMonstersInRange(x,y,r);
		if (mv.size() > 0)
			return (mv.get(rng.nextInt(mv.size())));
		return null;
	}
	
	public int getEntityIDAtPoint(int x, int y)
	{
		for(Entity e : entityVector)
		{
			if (Entity.getDistance(x, y, e.getX(), e.getY()) <= e.getR())
			{
				return e.entityID;
			}
			
		}
		return -1;
	}
	
	public Monster getMonsterNearest(int x, int y, int[] excludeIDs)
	{
		Monster m = null;
		
		for(Entity e : entityVector)
		{
			if (e instanceof Monster)
			{
				if (m == null)
					m = (Monster)e;
				else
				{
					boolean c = true;
					Monster mm = (Monster)e;
					for(int i : excludeIDs)
					{
						System.out.println("EX: " + i);
						if (mm.entityID == i)
						{
							c = false;
							break;
						}
					}
					if (c && Entity.getDistance(mm.getX(), mm.getY(), x, y) < Entity.getDistance(m.getX(), m.getY(), x, y))
					{
						m = mm;
					}
				}
			}
		}
		
		return m;
	}
	
	public void step()
	{
		for(int i = 0; i < entityVector.size(); i++)
		{
			Entity e = entityVector.get(i);
			e.step();
			if (!e.exists())
			{
				entityVector.remove(i--);
			}
		}
	}
}
