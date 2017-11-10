package TowerDefence;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class MenuComponent 
{
	protected AbstractMenu menu;
	
	protected int rx;
	protected int ry;
	protected int w;
	protected int h;
	
	protected boolean showtooltip = false;
	protected String[] tooltip;
	
	protected float lastMove = System.nanoTime() / 1000000000;
	protected int mouse_x = 0, mouse_y = 0;
	protected int hmouse_x, hmouse_y;
	
	public MenuComponent(AbstractMenu menu, int rx, int ry, int w, int h)
	{
		this.menu = menu;
		
		this.rx = rx;
		this.ry = ry;
		this.w = w;
		this.h = h;
	}
	
	public boolean inComponent(int x, int y)
	{
		return (x >= getX() - w/2 && y >= getY() - h/2 && x <= (getX() + w/2) && y <= (getY() + h/2));
	}
	
	public void drawToolTip(Graphics2D g2d, int x, int y)
	{
		if (showtooltip && tooltip != null && tooltip.length > 0)
		{
			FontMetrics fm = g2d.getFontMetrics();
			 
			int charHeight = (fm.getHeight()+2); 
			int longWidth = 0;
			for(String s : tooltip)
			{
				int widthtemp = fm.stringWidth(s);
				if (longWidth < widthtemp)
				{
					longWidth = widthtemp;
				}
			}
			int widthAdj = 10;
			int heightAdj = 10;
			
			int fullWidth = longWidth + widthAdj + 1;
			
			int xx = x;
			int yy = y;
			
			if (x + fullWidth > AbstractMenu.main.width)
				xx -= ((x + fullWidth*1.1) - (AbstractMenu.main.width));
			
			g2d.setColor(Color.BLACK);
			g2d.drawRect(xx - 1, yy - 1, fullWidth, (charHeight * tooltip.length) + heightAdj + 1);
			g2d.setColor(Color.WHITE);
			g2d.fillRect(xx, yy, fullWidth - 1, (charHeight * tooltip.length) + heightAdj);
			g2d.setColor(Color.BLACK);
			for(int i = 0; i < tooltip.length; i++)
			{
				String s = tooltip[i];
				int width = fm.stringWidth(s);
				
				int x_offset = (fullWidth - 1 - width)/2 ;
				g2d.drawString(s, xx + x_offset, yy + (charHeight * i) + charHeight);
			}
		}
	}
	
	public void updateToolTip()
	{
		mouse_x = AbstractMenu.main.getMouseX();
		mouse_y = AbstractMenu.main.getMouseY();
		if (inComponent(mouse_x, mouse_y))
		{
			float now =  System.nanoTime() / 1000000000;
			if (!(hmouse_x == mouse_x && hmouse_y == mouse_y))
			{
				hmouse_x = mouse_x;
				hmouse_y = mouse_y;
				lastMove = now;
				showtooltip = false;
			}
			
			if (now - lastMove >= 1)
			{
				showtooltip = true;
				lastMove = now;
			}
		}
		else
		{
			showtooltip = false;
		}
	}
	
	public void setPos(int rx, int ry)
	{
		this.rx = rx;
		this.ry = ry;
	}
	
	public int getX()
	{
		return rx + menu.getX();
	}
	
	public int getY()
	{
		return ry + menu.getY();
	}
	
	public boolean showToolTip()
	{
		return showtooltip;
	}
	
	public abstract void mousePressed(MouseEvent ev);
	
	public abstract void mouseReleased(MouseEvent ev);
	
	public abstract void mouseMoved(MouseEvent ev);
	
	public abstract void mouseDragged(MouseEvent ev);
	
	public abstract void render(Graphics2D g2d);
	
	public abstract void step();
}
