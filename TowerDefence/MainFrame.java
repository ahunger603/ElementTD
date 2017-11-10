package TowerDefence;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.*;

import Monsters.Monster;
import Towers.Tower;

public class MainFrame extends Canvas implements Runnable, MouseListener, MouseMotionListener
{
	//DEBUG VAR
	private boolean drawPaths = true;
	
	//VARAIBLE INIT
	private static final long serialVersionUID = 1L;
	
	public static final int MILLISECOND_PER_STEP = 10;
	private static final int MILLISECOND_PER_FPS_REFRESH = 250;

	public static final Font font =  new Font("Sans-Serif", Font.PLAIN, 12);
	public static final Font midFont = new Font("Sans-Serif", Font.PLAIN, 22);
	public static final Font bigFont = new Font("Sans-Serif", Font.PLAIN, 32);
	
	private boolean running = false;
	private boolean paused = false;
	private int fps = 0;
	
	private int lives = 10;
	private int gold = 350;
	private int researchPoints = 1;
	private int[] elementLevels = {0,0,0,0,0,0,0,0};
	private Level level;
	
	private boolean titleScreen = false;
	private boolean levelSelect = false;
	private boolean gameOver = false;
	
	public final int width, height;
	public final int gameSpace_w, gameSpace_h;
	private VolatileImage image;
	
	private SpriteMap spriteMap = new SpriteMap();
	private EntityManager entityManager;
	private GameMenu menu;
	
	private Thread mainThread;
	
	private int mouse_x = 0, mouse_y = 0;
	
	private int selection = -1;
	private int building = -1;
	//
	//CONSTRUCTOR
	//
	
	public MainFrame(int w, int h)
	{
		width = w;
		height = h;
		
		gameSpace_w = (int) (w * (4.0/5.0));
		gameSpace_h = h;
		
		setSize(w, h);
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	
	//
	//MAIN FUNCTIONS
	//
	public void start()
	{
		mainThread = new Thread(this);
		mainThread.start();
	}
	
	public void stop()
	{
		running = false;
		try
		{
			if (mainThread != null)
				mainThread.join();
			
		}catch(InterruptedException ex){}
	}
	
	public void run()
	{
		init();
		int frames = 0;
		double lastRender = System.nanoTime() / 1000000;
		double lastFPSRefresh = System.nanoTime() / 1000000;
		running = true;
		
		while(running)
		{
			synchronized (this)
			{
				double now = System.nanoTime() / 1000000;				
				if (now - lastRender >= MILLISECOND_PER_STEP)
					{
						double timediff = now - lastRender;
						for(int i = 0; i < (int)(timediff / MILLISECOND_PER_STEP); i++)
							step();
						render();
						lastRender = System.nanoTime() / 1000000;
					}
				
				if (now - lastFPSRefresh >= MILLISECOND_PER_FPS_REFRESH)
				{
					lastFPSRefresh = now;
					fps = frames*(1000/MILLISECOND_PER_FPS_REFRESH);
					frames = 0;
				}
				frames++;
			}
		}
	}
	
	public void init()
	{	
		try
		{
			spriteMap.loadAll();
		
		}catch (IOException ex) {ex.printStackTrace();}
		
		entityManager = new EntityManager();
		menu = new GameMenu(this, gameSpace_w, 0);
		level = Level.levelSelect(this, 1);
	}
	
	public void pause()
	{
		paused = true;
	}
	
	public void unpause()
	{
		if (mainThread == null)
		{
			start();
		}
		paused = true;
	}
	
	public void step()
	{
		if (!titleScreen)
		{
			level.step();
			entityManager.step();
			menu.step();
		}
	}
	
	//
	//GRAPHIC FUNCTIONS
	//
	private void render()
	{
		BufferStrategy bs = getBufferStrategy();
		if (bs == null)
		{
			createBufferStrategy(2);
			bs = getBufferStrategy();
		}
		
		if (image == null)
			image = createVolatileImage(width, height);
		
		if (bs != null)
		{
			Graphics2D g2d = image.createGraphics();
			g2d.setFont(font);
			renderGame(g2d);
			
			g2d.dispose();
			Graphics gg = bs.getDrawGraphics();
			gg.drawImage(image, 0, 0, width, height, 0, 0, width, height, null);
			gg.dispose();
			bs.show();
		}
	}
	
	private void renderGame(Graphics2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
		if (!titleScreen)
		{
			//Draw Level
			level.render(g2d);
			
			//Draw Entities
			Vector<Entity> depthSortedEntities = entityManager.getSortedEntityVector(0);
			for(Entity e : depthSortedEntities)
			{
				e.render(g2d);
			}
			
			//Health Bars
			for(Entity e : depthSortedEntities)
			{
				if (e instanceof Monster)
				{
					((Monster)e).renderHealthBar(g2d, 40);
				}
			}
			
			//Draw UI
			menu.render(g2d);
			
			if(drawPaths)
			{
				Polygon[] poly = level.getPathArea();
				for(Polygon p : poly)
				{
					g2d.setColor(Color.RED);
					g2d.drawPolygon(p);
				}
			}
			
			//Building Circle
			if (building != -1)
			{
				double r = Tower.getTowerRadius(building);
				if (level.buildable(mouse_x, mouse_y, (int)r))
					g2d.setColor(Color.GREEN);
				else
					g2d.setColor(Color.RED);
				g2d.drawOval(mouse_x - (int)r, mouse_y - (int)r, (int)r*2, (int)r*2);
			}
			
			//FPS
			g2d.setColor(Color.RED);
			g2d.drawString(("fps: " + fps), 10, 20);
		}
	}
	
	//
	//GAME FUNCTIONS
	//
	
	public void lifeLost()
	{
		lives--;
		System.out.println("Lives: "+lives);
		if (lives <= 0)
		{
			gameOver = true;
		}
	}
	
	public void spendGold(int gold)
	{
		this.gold -= gold;
	}
	
	public void gainGold(int gold)
	{
		this.gold += gold;
	}
	
	public void gainRp(int rp)
	{
		researchPoints += rp;
	}
	
	public void Research(int type)
	{
		if (researchPoints > 0)
		{
			elementLevels[type-1]++;
			researchPoints--;
		}
	}
	
	public void build(int towerCode, int x, int y)
	{
		int r = Tower.getTowerRadius(towerCode);
		
		if (level.buildable(x, y, r))
		{
			Tower t = Tower.getTower(towerCode, this, x, y);
			if (t.cost <= gold)
			{
				gold -= t.cost;
				t.init();
			}
			else
				t = null;
		}
	}
	
	//
	//MOUSE FUNCTIONS
	//
	
	public void mouseClicked(MouseEvent ev)
	{
		System.out.println("("+ev.getX()+", "+ev.getY()+")");
	}
	
	public void mousePressed(MouseEvent ev)
	{
		//Menu
		if (ev.getX() > gameSpace_w)
		{
			menu.mousePressed(ev);
			building = -1;
		}
		else{}
	}
	
	public void mouseReleased(MouseEvent ev)
	{
		//LEFT CLICK
		if (ev.getButton() == MouseEvent.BUTTON1)
		{
			//Menu
			if (ev.getX() > gameSpace_w)
			{
				menu.mouseReleased(ev);
				selection = -1;
			}
			else
			{
				//Building
				if (building != -1)
				{
					if (level.buildable(ev.getX(), ev.getY(), (int)Tower.getTowerRadius(building)))
						build(building, ev.getX(), ev.getY());
					building = -1;
				}
				else
				{
					//Selection
					selection = entityManager.getEntityIDAtPoint(ev.getX(), ev.getY());
				}
			}
		}
		//RIGHT CLICK
		else if (ev.getButton() == MouseEvent.BUTTON3)
		{
			building = -1;
			selection = -1;
		}
	}
	
	public void mouseEntered(MouseEvent ev){}
	
	public void mouseExited(MouseEvent ev){}
	
	public void mouseMoved(MouseEvent ev)
	{
		mouse_x = ev.getX();
		mouse_y = ev.getY();
	}
	
	public void mouseDragged(MouseEvent ev)
	{
		//Menu
		if (ev.getX() > gameSpace_w)
		{
			menu.mouseDragged(ev);
		}
		else{}
	}
	
	//
	//GET
	//
	
	public EntityManager getManager()
	{
		return entityManager;
	}
	
	public SpriteMap getMap()
	{
		return spriteMap;
	}
	
	public int[] getElementLevels()
	{
		return elementLevels;
	}
	
	public int getReserachPoints()
	{
		return researchPoints;
	}
	
	public Level getLevel()
	{
		return level;
	}
	
	public int getBuilding()
	{
		return building;
	}
	
	public int getSelection()
	{
		return selection;
	}
	
	public int getMouseX()
	{
		return mouse_x;
	}
	
	public int getMouseY()
	{
		return mouse_y;
	}
	
	public int getLives()
	{
		return lives;
	}
	
	public int getGold()
	{
		return gold;
	}
	
	//
	//SET
	//
	
	public void setSelection(int entityID)
	{
		if (entityManager.entityExists(entityID))
			selection = entityID;
	}
	
	public void setBuilding(int building)
	{
		this.building = building;
	}
	
	//MAIN
	
	public static void main(String[] args)
	{
		final MainFrame main = new MainFrame(1280, 720);
		
		Frame frame = new Frame("Tower Defence");
		frame.add(main);
		frame.setSize(new Dimension(1280, 720));
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent we)
			{
				main.stop();
				
				System.exit(0);
			}
		});
		
		frame.setVisible(true);
		frame.setResizable(true);
		
		main.start();
	}
	
	//Unused
	public void paint(Graphics g){};
	public void update(Graphics g){};
}
