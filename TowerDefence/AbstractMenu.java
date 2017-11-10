package TowerDefence;

import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class AbstractMenu 
{
	protected int x, y;
	protected int w, h;
	
	protected static MainFrame main;
	
	protected ArrayList<MenuComponent> components = new ArrayList<MenuComponent>();
	
	public AbstractMenu(MainFrame main, int x, int y, int w, int h)
	{
		if (AbstractMenu.main == null)
			AbstractMenu.main = main;
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	protected void renderComponents(Graphics2D g2d)
	{
		for(MenuComponent c : components)
		{
			c.render(g2d);
		}
		
		for(MenuComponent c : components)
		{
			if (c.showToolTip())
				c.drawToolTip(g2d, main.getMouseX(), main.getMouseY());
		}
	}
	
	protected void stepComponents()
	{
		for(MenuComponent c : components)
		{
			c.step();
		}
	}
	
	protected boolean inMenu(int x, int y)
	{
		return (x >= this.x && y >= this.y && x <= this.x + w && y <= this.y + h);
	}
	
	protected void addComponent(MenuComponent c)
	{
		if (inMenu(c.getX(), c.getY()))
		{
			components.add(c);
			return;
		}
		System.out.println("COULDN'T ADD COMPONENT");
	}
	
	public void mousePressed(MouseEvent ev)
	{
		for(MenuComponent c : components)
		{
			if (c.inComponent(ev.getX(), ev.getY()))
			{
				c.mousePressed(ev);
				return;
			}
		}
	}
	
	public void mouseReleased(MouseEvent ev)
	{
		for(MenuComponent c : components)
		{
			if (c.inComponent(ev.getX(), ev.getY()))
			{
				c.mouseReleased(ev);
				return;
			}
		}
	}
	
	public void mouseMoved(MouseEvent ev)
	{
		for(MenuComponent c : components)
		{
			if (c.inComponent(ev.getX(), ev.getY()))
			{
				c.mouseMoved(ev);
				return;
			}
		}
	}
	
	public void mouseDragged(MouseEvent ev)
	{
		for(MenuComponent c : components)
		{
			if (c.inComponent(ev.getX(), ev.getY()))
			{
				c.mouseDragged(ev);
				return;
			}
		}
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public abstract void render(Graphics2D g2d);
	
	public abstract void step();
}
