package TowerDefence;

import java.awt.*;
import java.awt.event.MouseEvent;

import Monsters.Monster;
import Towers.Tower;

public class GameMenu extends AbstractMenu
{
	private int selectionID = -1;
	private AbstractMenu selectionMenu = null;
	private AbstractMenu levelMenu = null;
	
	public GameMenu(MainFrame main, int x, int y)
	{
		super(main, x, y, (main.width - main.gameSpace_w), main.height);
		
		levelMenu = new LevelMenu(main, x + 13, y + 350);
		
		addComponent(new NextWave_bttn(this, (w/2), h - 70));
		addComponent(new TowerBuy_bttn(this, 0, (w/2), 305));
		
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				int type = 1 + j + (4*i);
				int xx = 23 + 60*j;
				int yy = 158 + 75*i;
				addComponent(new Element(this, type, xx, yy));
			}
		}
	}
	
	public void render(Graphics2D g2d)
	{
		g2d.setColor(Color.BLACK);
		g2d.drawLine(x, y, x, y + h);
		g2d.setColor(Color.GRAY);
		g2d.fillRect(x + 1, y, w, h);
		
		g2d.setFont(MainFrame.bigFont);
		FontMetrics fm = g2d.getFontMetrics();
		
		//Lives
		g2d.setColor(Color.WHITE);
		int heart_x = x + 30 - (SpriteMap.Heart.getWidth()/2);
		int heart_y = y + 30 - (SpriteMap.Heart.getHeight()/2);
		g2d.drawImage(SpriteMap.Heart, heart_x, heart_y, null);
		g2d.drawString("Lives: "+main.getLives(), heart_x + 30, heart_y + fm.getHeight()/2);
		
		//Gold
		int gold_x = x + 30 - (SpriteMap.Gold.getWidth()/2);
		int gold_y = y + 100 - (SpriteMap.Gold.getHeight()/2);
		g2d.drawImage(SpriteMap.Gold, gold_x, gold_y, null);
		g2d.drawString("Gold: "+main.getGold(), heart_x + 30, gold_y + fm.getHeight()/2 + 3);
		
		g2d.setFont(MainFrame.midFont);
		fm = g2d.getFontMetrics();
		
		//Divider
		g2d.setColor(Color.BLACK);
		g2d.drawLine(x + 23, y + 125, x + 227, y + 125);
		g2d.drawLine(x + 23, y + 270, x + 227, y + 270);
		
		//Elements
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				int type = 1 + j + (4*i);
				int xx = x + 23 + 60*j;
				int yy = y + 158 + 75*i;
				String num = "" + main.getElementLevels()[type - 1];
				g2d.drawString(num, xx + 8 + fm.stringWidth(num), yy + fm.getHeight()/2 - 4);
			}
		}
		
		//ResearchPoints
		if (main.getReserachPoints() > 0)
		{
			g2d.setColor(Color.WHITE);
			g2d.setFont(MainFrame.midFont);
			String s = ""+main.getReserachPoints();
			g2d.drawString(s, x + 35, y + 197);
			int width = g2d.getFontMetrics().stringWidth(s);
			g2d.drawString(" Research Points!", x + 35 + width, y + 197);
			
			g2d.setFont(MainFrame.font);
			
		}
		
		g2d.setFont(MainFrame.font);
		renderComponents(g2d);
		
		if (selectionMenu != null)
		{
			selectionMenu.render(g2d);
		}
		else
		{
			if (levelMenu == null)
				levelMenu = new LevelMenu(main, x + 13, y + 350);
			levelMenu.render(g2d);
		}
	}
	
	public void step()
	{
		stepComponents();
		
		//Sets selection menu
		if (selectionID != main.getSelection())
		{
			selectionMenu = null;
			selectionID = main.getSelection();
		}
		
		if (selectionMenu == null && selectionID != -1)
		{
			Entity selection = main.getManager().getEntity(selectionID);
			if (selection instanceof Monster)
			{
				levelMenu = null;
				selectionMenu = new MonsterMenu(main, (Monster)selection, x + 13, y + 275);
			}
			
			if (selection instanceof Tower)
			{
				levelMenu = null;
				selectionMenu = new TowerMenu(main, (Tower)selection, x + 13, y + 275);
			}
		}
		
		if (selectionMenu != null)
		{
			if (selectionMenu instanceof MonsterMenu)
			{
				if (!((MonsterMenu)selectionMenu).m.exists)
				{
					selectionMenu = null;
				}	
			}
			else if (selectionMenu instanceof TowerMenu)
			{
				if (!((TowerMenu)selectionMenu).t.exists)
				{
					selectionMenu = null;
				}
			}
			
			if (selectionMenu != null)
				selectionMenu.step();
		}
		else
		{
			if (levelMenu == null)
				levelMenu = new LevelMenu(main, x + 13, y + 350);
			
			levelMenu.step();
		}
	}
	
	private class MonsterMenu extends AbstractMenu
	{
		public final Monster m;
		
		public MonsterMenu(MainFrame main, Monster m, int x, int y)
		{
			super(main, x, y, 220, 345);
			
			this.m = m;
		}
		
		public void render(Graphics2D g2d)
		{
			g2d.setColor(Color.BLACK);
			g2d.fillRect(x, y, w, h);
			
			g2d.setColor(Color.WHITE);
			g2d.drawRect(x - 1, y - 1, w + 1, h + 1);
			
			g2d.setFont(MainFrame.bigFont);
			g2d.drawString(m.name, x + 20, y + 40);
			
			g2d.setFont(MainFrame.midFont);
			g2d.drawString("Level: " + m.level, x + 20, y + 65);
			g2d.drawString("Type: ", x + 20, y + 90);
			
			g2d.setFont(MainFrame.font);
			int[] types = m.creatureTypes;
			FontMetrics fm = g2d.getFontMetrics();
			int x_offset = 0;
			int y_offset = 0;
			for(int i = 0; i < types.length; i++)
			{
				String name = "";
				switch(types[i])
				{
				//Basic
				case 0:
					g2d.setColor(Color.LIGHT_GRAY);
					name += "BASIC";
					break;
				//Fire
				case 1:
					g2d.setColor(Color.RED);
					name += "FIRE";
					break;
				//Nature
				case 2:
					g2d.setColor(Color.GREEN);
					name += "NATURE";
					break;
				//Light
				case 3:
					g2d.setColor(Color.WHITE);
					name += "LIGHT";
					break;
				//Darkness
				case 4:
					g2d.setColor(Color.DARK_GRAY);
					name += "DARK";
					break;
				//Arcane
				case 5:
					g2d.setColor(Color.CYAN);
					name += "ARCANE";
					break;
				//Mechanical
				case 6:
					g2d.setColor(Color.GRAY);
					name += "MECHANICAL";
					break;
				//Electrical
				case 7:
					g2d.setColor(Color.YELLOW);
					name += "ELECTRICAL";
					break;
				//Water
				case 8:
					g2d.setColor(Color.BLUE);
					name += "WATER";
					break;
				}
				if (i < types.length - 1)
					name += ", ";
				
				int width = fm.stringWidth(name);
				if (x + 80 + x_offset + width > x + w)
				{
					x_offset = 0;
					y_offset += 19;
				}
				
				g2d.drawString(name, x + 80 + x_offset, y + 90 + y_offset);
				
				x_offset += width;
			}
			//Health
			g2d.setColor(Color.WHITE);
			g2d.setFont(MainFrame.midFont);
			g2d.drawString("Health: ", x + 20, y + 120 + y_offset);
			
			g2d.setFont(MainFrame.font);
			String hp = ""+(int)m.getHp()+ " / ";
			g2d.drawString(hp, x + 95, y + 120 + y_offset);
			g2d.drawString(""+(int)m.MAXHP, x + 95 + fm.stringWidth(hp), y + 120 + y_offset);
			
			//Armor
			g2d.setFont(MainFrame.midFont);
			g2d.drawString("Armor: ", x + 20, y + 160 + y_offset);
			g2d.setFont(MainFrame.font);
			g2d.drawString(""+m.getArmor(), x + 100, y + 160 + y_offset);
			
			g2d.setFont(MainFrame.font);
			renderComponents(g2d);
		}
		
		public void step(){}
	}
	
	private class TowerMenu extends AbstractMenu
	{
		public final Tower t;
		
		private MenuComponent[] upgradeBttns;
		private Damage_tp damageBox;
		
		public TowerMenu(MainFrame main, Tower t, int x, int y)
		{
			super(main, x, y, 220, 345);
			
			this.t = t;
			
			damageBox = new Damage_tp(this, t.getMinMax(), 1, 1);
			addComponent(damageBox);
			
			int[] upgrades = t.upgrades;
			
			int size = 0;
			for(int i = 0; i < upgrades.length; i++)
			{
				if (Tower.canBuild(upgrades[i]))
				{
					size++;
				}
			}
			
			int index = 0;
			upgradeBttns = new MenuComponent[size];
			if (size > 0)
			{
				for(int i = 0; i < upgrades.length; i++)
				{
					if (Tower.canBuild(upgrades[i]))
					{
						MenuComponent upgrade = new TowerUpgrade_bttn(this, upgrades[i], 1, 1);
						upgradeBttns[index] = upgrade;
						addComponent(upgrade);
						index++;
					}
				}
			}
		}
		
		public void render(Graphics2D g2d)
		{
			g2d.setColor(Color.BLACK);
			g2d.fillRect(x, y, w, h);
			
			g2d.setColor(Color.WHITE);
			g2d.drawRect(x - 1, y - 1, w + 1, h + 1);
			
			g2d.setFont(MainFrame.midFont);
			g2d.drawString(t.name, x + 20, y + 30);
			
			//Type
			g2d.drawString("Type: ", x + 20, y + 70);
			
			g2d.setFont(MainFrame.font);
			int[] types = t.types;
			FontMetrics fm = g2d.getFontMetrics();
			int x_offset = 0;
			int y_offset = 0;
			for(int i = 0; i < types.length; i++)
			{
				String name = "";
				switch(types[i])
				{
				//Basic
				case 0:
					g2d.setColor(Color.LIGHT_GRAY);
					name += "BASIC";
					break;
				//Fire
				case 1:
					g2d.setColor(Color.RED);
					name += "FIRE";
					break;
				//Nature
				case 2:
					g2d.setColor(Color.GREEN);
					name += "NATURE";
					break;
				//Light
				case 3:
					g2d.setColor(Color.WHITE);
					name += "LIGHT";
					break;
				//Darkness
				case 4:
					g2d.setColor(Color.DARK_GRAY);
					name += "DARK";
					break;
				//Arcane
				case 5:
					g2d.setColor(Color.CYAN);
					name += "ARCANE";
					break;
				//Mechanical
				case 6:
					g2d.setColor(Color.GRAY);
					name += "MECHANICAL";
					break;
				//Electrical
				case 7:
					g2d.setColor(Color.YELLOW);
					name += "ELECTRICAL";
					break;
				//Water
				case 8:
					g2d.setColor(Color.BLUE);
					name += "WATER";
					break;
				}
				if (i < types.length - 1)
					name += ", ";
				
				int width = fm.stringWidth(name);
				if (x + 80 + x_offset + width > x + w)
				{
					x_offset = 0;
					y_offset += 19;
				}
				
				g2d.drawString(name, x + 80 + x_offset, y + 70 + y_offset);			
				x_offset += width;
			}
			
			//Damage
			g2d.setColor(Color.WHITE);
			int min_dam = 0;
			int max_dam = 0;
			int[][] min_max = t.getMinMax();
			for(int i = 0; i < 9; i++)
			{
				int[] min_max_bit = min_max[i];
				if (min_max_bit != null)
				{
					min_dam += min_max_bit[0];
					max_dam += min_max_bit[1];
				}
			}
			g2d.setFont(MainFrame.midFont);
			g2d.drawString("Damage: ", x + 20, y + 100 + y_offset);
			
			g2d.setFont(MainFrame.font);
			String min = ""+min_dam+ " - ";
			g2d.drawString(min, x + 115, y + 100 + y_offset);
			g2d.drawString(""+max_dam, x + 115 + fm.stringWidth(min), y + 100 + y_offset);
			
			damageBox.setPos(65, 93 + y_offset);
			
			g2d.setFont(MainFrame.bigFont);
			fm = g2d.getFontMetrics();
			g2d.drawString("Upgrades", x + (w/2) - (fm.stringWidth("Upgrades")/2), y + 150 + y_offset);
			g2d.drawLine(x + 20, y + 175 + y_offset, x + w - 20, y + 175 + y_offset);
			
			if (upgradeBttns.length == 0)
			{
				g2d.drawString("None", x + (w/2) - (fm.stringWidth("None")/2), y + 205 + y_offset);
			}
			else
			{
				int row = 0;
				int i = 0;
				int left = upgradeBttns.length;
				while(left > 0)
				{
					int bttn_y = 205 + y_offset + (50*row);
					if (left >= 3)
					{
						upgradeBttns[i++].setPos(50, bttn_y);
						upgradeBttns[i++].setPos(w/2, bttn_y);
						upgradeBttns[i++].setPos(w - 50, bttn_y);
						row++;
						left -= 3;
					}
					else if (left >= 2)
					{
						upgradeBttns[i++].setPos(w/4 + 25, bttn_y);
						upgradeBttns[i++].setPos((w/4)*3 - 25, bttn_y);
						left -= 2;
						break;
					}
					else
					{
						upgradeBttns[i++].setPos(w/2, bttn_y);
						left -= 1;
						break;
					}
				}
			}
			
			g2d.setFont(MainFrame.font);
			renderComponents(g2d);
		}
		
		public void step()
		{
			stepComponents();
		}
		
		private class Damage_tp extends MenuComponent
		{
			private int[][] min_max;
			
			public Damage_tp(AbstractMenu menu, int[][] min_max, int rx, int ry)
			{
				super(menu, rx, ry, 95, 25);
				
				this.min_max = min_max;
				
				int len = 0;
				for(int i = 0; i < 9; i++)
				{
					if (min_max[i] != null)
						len++;
				}
				tooltip = new String[1 + len];
				tooltip[0] = "   Damage Breakdown   ";
				setToolTip();
			}
			
			public void setToolTip()
			{
				int index = 0;
				for(int i = 0; i < 9; i++)
				{
					String s = null;
					switch(i)
					{
					case 0:
						if (min_max[i] != null)
							s = "Basic: " + min_max[i][0] +" - "+min_max[i][1];
						break;
					case 1:
						if (min_max[i] != null)
							s = "Fire: " + min_max[i][0] +" - "+min_max[i][1];
						break;
					case 2:
						if (min_max[i] != null)
							s = "Nature" + min_max[i][0] +" - "+min_max[i][1];;
						break;
					case 3:
						if (min_max[i] != null)
							s = "Light" + min_max[i][0] +" - "+min_max[i][1];;
						break;
					case 4:
						if (min_max[i] != null)
							s = "Darkness"  + min_max[i][0] +" - "+min_max[i][1];;
						break;
					case 5:
						if (min_max[i] != null)
							s = "Arcane"  + min_max[i][0] +" - "+min_max[i][1];;
						break;
					case 6:
						if (min_max[i] != null)
							s = "Mechanical"  + min_max[i][0] +" - "+min_max[i][1];;
						break;
					case 7:
						if (min_max[i] != null)
							s = "Electricity"  + min_max[i][0] +" - "+min_max[i][1];;
						break;
					case 8:
						if (min_max[i] != null)
							s = "Water"  + min_max[i][0] +" - "+min_max[i][1];;
						break;
					default:
						s = "null";
					}
					if (s != null)
					{
						tooltip[index + 1] = s;
						index++;
					}
				}
			}
			
			public void step()
			{
				updateToolTip();
			}
			
			public void render(Graphics2D g2d){}
			public void mousePressed(MouseEvent ev){}
			public void mouseReleased(MouseEvent ev){}
			public void mouseDragged(MouseEvent ev){}
			public void mouseMoved(MouseEvent ev){}
		}
		
		private class TowerUpgrade_bttn extends MenuComponent
		{
			private boolean pressed = false;
			private int newTowerCode;
			
			public TowerUpgrade_bttn(AbstractMenu menu, int newTowerCode, int x, int y)
			{
				super(menu, x, y, 50, 50);
				this.newTowerCode = newTowerCode;
				
				tooltip = Tower.getTower(newTowerCode, AbstractMenu.main, -100, -100).getToolTip();
			}
			
			public void step()
			{
				updateToolTip();
			}
			
			public void render(Graphics2D g2d)
			{
				if (!pressed)
				{
					if (inComponent(main.getMouseX(), main.getMouseY()))
						g2d.drawImage(SpriteMap.Icons.get(Tower.getTowerSpriteKey(newTowerCode))[1], getX() - 25, getY() - 25, null);
					else
						g2d.drawImage(SpriteMap.Icons.get(Tower.getTowerSpriteKey(newTowerCode))[0], getX() - 25, getY() - 25, null);
				}
				else
				{
					g2d.drawImage(SpriteMap.Icons.get(Tower.getTowerSpriteKey(newTowerCode))[2], getX() - 25, getY() - 25, null);
				}
			}
			
			public void mousePressed(MouseEvent ev)
			{
				pressed = true;
			}
			
			public void mouseReleased(MouseEvent ev)
			{
				pressed = false;		
				t.upgrade(newTowerCode);
			}
			
			public void mouseDragged(MouseEvent ev)
			{
				pressed = false;
			}	
			
			public void mouseMoved(MouseEvent ev){}
		}
	}
	
	private class LevelMenu extends AbstractMenu
	{
		public LevelMenu(MainFrame main, int x, int y)
		{
			super(main, x, y, 220, 250);
		}
		
		public void step(){}
		
		public void render(Graphics2D g2d)
		{
			g2d.setColor(Color.BLACK);
			g2d.fillRect(x, y, w, h);
			
			g2d.setColor(Color.WHITE);
			g2d.drawRect(x - 1, y - 1, w + 1, h + 1);
			
			g2d.setFont(MainFrame.bigFont);
			g2d.drawString("Level: ", x + 20, y + 30);
			
			g2d.setFont(MainFrame.midFont);
			g2d.drawString(""+main.getLevel().getLevelNum(), x + 115, y + 30);
			
			g2d.drawString("Next Wave: ", x + 20, y + 70);
			
			g2d.drawString(""+main.getLevel().getWaveNum(), x + 145, y + 70);
			
			g2d.setFont(MainFrame.font);
		}
	}
	
	private class Element extends MenuComponent
	{
		int Type;
		public Element(AbstractMenu menu, int Type, int rx, int ry)
		{
			super(menu, rx, ry, 32, 64);
			
			this.Type = Type;
			
			String name;
			tooltip = new String[8];
			switch(Type)
			{
			case 1:
				name = "Fire";
				tooltip[4] = "Best Against: Nature (200%)";
				tooltip[5] = "Good Against: Darkness (150%)";
				tooltip[6] = "Bad Against: Mechanical (75%)";
				tooltip[7] = "Worst Against: Water (50%)";
				break;
			case 2:
				name = "Nature";
				tooltip[4] = "Best Against: Light (200%)";
				tooltip[5] = "Good Against: Water (150%)";
				tooltip[6] = "Bad Against: Arcane (75%)";
				tooltip[7] = "Worst Against: Fire (50%)";
				break;
			case 3:
				name = "Light";
				tooltip[4] = "Best Against: Darkness (200%)";
				tooltip[5] = "Good Against: Arcane (150%)";
				tooltip[6] = "Bad Against: Electrical (75%)";
				tooltip[7] = "Worst Against: Nature (50%)";
				break;
			case 4:
				name = "Darkness";
				tooltip[4] = "Best Against: Arcane (200%)";
				tooltip[5] = "Good Against: Electrical (150%)";
				tooltip[6] = "Bad Against: Fire (75%)";
				tooltip[7] = "Worst Against: Light (50%)";
				break;
			case 5:
				name = "Arcane";
				tooltip[4] = "Best Against: Mechanical (200%)";
				tooltip[5] = "Good Against: Nature (150%)";
				tooltip[6] = "Bad Against: Light (75%)";
				tooltip[7] = "Worst Against: Darkness (50%)";
				break;
			case 6:
				name = "Mechanical";
				tooltip[4] = "Best Against: Electrical (200%)";
				tooltip[5] = "Good Against: Fire (150%)";
				tooltip[6] = "Bad Against: Water (75%)";
				tooltip[7] = "Worst Against: Arcane (50%)";
				break;
			case 7:
				name = "Electricity";
				tooltip[4] = "Best Against: Water (200%)";
				tooltip[5] = "Good Against: Light (150%)";
				tooltip[6] = "Bad Against: Darkness (75%)";
				tooltip[7] = "Worst Against: Mechanical (50%)";
				break;
			case 8:
				name = "Water";
				tooltip[4] = "Best Against: Fire (200%)";
				tooltip[5] = "Good Against: Mechancial (150%)";
				tooltip[6] = "Bad Against: Nature (75%)";
				tooltip[7] = "Worst Against: Electrical (50%)";
				break;
			default:
				name = "null";
			}
			tooltip[0] = " "+name+" ";
			tooltip[1] = "  Researching "+name+" allows";
			tooltip[2] = "the user to build towers";
			tooltip[3] = "combining with "+name+".";
		}
		
		public void step()
		{
			updateToolTip();
		}
		
		public void render(Graphics2D g2d)
		{
			if (main.getReserachPoints() > 0)
				g2d.drawImage(SpriteMap.Up, getX() - 10, getY() - 33, null);
			SpriteMap.drawElement(g2d, Type, getX(), getY());
		}
		
		public void mouseReleased(MouseEvent ev)
		{
			if (main.getReserachPoints() > 0)
				if (ev.getX() > getX() - 10 && ev.getX() < getX() + 10 && ev.getY() > (getY() - 40) && ev.getY() < (getY() - 10))
					main.Research(Type);
		}
		
		public void mousePressed(MouseEvent ev){}
		public void mouseDragged(MouseEvent ev){}
		public void mouseMoved(MouseEvent ev){}
	}
	
	private class NextWave_bttn extends MenuComponent
	{
		private boolean pressed = false;
		
		public NextWave_bttn(AbstractMenu menu, int x, int y)
		{
			super(menu, x, y, 50, 50);
			
			tooltip = new String[1];
			tooltip[0] = " NEXT WAVE ";
		}
		
		public void step()
		{
			updateToolTip();
		}
		
		public void render(Graphics2D g2d)
		{
			if (!pressed)
			{
				g2d.setColor(Color.GRAY);
				g2d.fillRect(getX() - 25, getY() - 25, w, h);
			}
			else
			{
				g2d.setColor(Color.WHITE);
				g2d.fillRect(getX() - 25, getY() - 25, w, h);
			}
			g2d.drawImage(SpriteMap.Next, getX() - 25, getY() - 25, null);
		}
		
		public void mousePressed(MouseEvent ev)
		{
			pressed = true;
		}
		
		public void mouseReleased(MouseEvent ev)
		{
			pressed = false;
			main.getLevel().setState(true);
		}
		
		public void mouseDragged(MouseEvent ev)
		{
			pressed = false;
		}
		
		public void mouseMoved(MouseEvent ev){}
	}
	
	private class TowerBuy_bttn extends MenuComponent
	{
		private boolean pressed = false;
		private Tower t;
		
		public TowerBuy_bttn(AbstractMenu menu, int towerCode, int x, int y)
		{
			super(menu, x, y, 50, 50);
			
			t = Tower.getTower(towerCode, main, x, y);
			
			tooltip = t.getToolTip();
		}
		
		public void step()
		{
			if (selectionMenu == null)
				updateToolTip();
		}
		
		public void render(Graphics2D g2d)
		{
			if (selectionMenu == null)
			{
				if (!pressed)
				{
					if (inComponent(main.getMouseX(), main.getMouseY()))
						g2d.drawImage(SpriteMap.Icons.get(t.spriteKey)[1], getX() - 25, getY() - 25, null);
					else
						g2d.drawImage(SpriteMap.Icons.get(t.spriteKey)[0], getX() - 25, getY() - 25, null);
				}
				else
				{
					g2d.drawImage(SpriteMap.Icons.get(t.spriteKey)[2], getX() - 25, getY() - 25, null);
				}
			}
		}
		
		public void mousePressed(MouseEvent ev)
		{
			if (selectionMenu == null)
				pressed = true;
		}
		
		public void mouseReleased(MouseEvent ev)
		{
			if (selectionMenu == null)
			{
				pressed = false;
				main.setBuilding(0);
			}
		}
		
		public void mouseDragged(MouseEvent ev)
		{
			if (selectionMenu == null)
				pressed = false;
		}	
		
		public void mouseMoved(MouseEvent ev){}
	}
	
	public void mousePressed(MouseEvent ev)
	{
		if (!(selectionMenu != null && selectionMenu.inMenu(ev.getX(), ev.getY())))
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
		
		if (selectionMenu != null)
		{
			selectionMenu.mousePressed(ev);
		}
	}
	
	public void mouseReleased(MouseEvent ev)
	{
		if (!(selectionMenu != null && selectionMenu.inMenu(ev.getX(), ev.getY())))
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
		
		if (selectionMenu != null)
		{
			selectionMenu.mouseReleased(ev);
		}
	}
	
	public void mouseMoved(MouseEvent ev)
	{
		if (!(selectionMenu != null && selectionMenu.inMenu(ev.getX(), ev.getY())))
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
			
		if (selectionMenu != null)
		{
			selectionMenu.mouseMoved(ev);
		}
	}
	
	public void mouseDragged(MouseEvent ev)
	{
		if (!(selectionMenu != null && selectionMenu.inMenu(ev.getX(), ev.getY())))
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
		
		if (selectionMenu != null)
		{
			selectionMenu.mouseDragged(ev);
		}
	}

}
