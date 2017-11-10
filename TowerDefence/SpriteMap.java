package TowerDefence;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.*;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class SpriteMap
{
	//Sprite map
	public static HashMap<String, BufferedImage[][]> MonsterSprites = new HashMap<String, BufferedImage[][]>();
	public static HashMap<String, BufferedImage[]> TowerSprites = new HashMap<String, BufferedImage[]>();
	public static HashMap<String, BufferedImage[]> EffectSprites = new HashMap<String, BufferedImage[]>();
	public static HashMap<String, BufferedImage[]> Icons = new HashMap<String, BufferedImage[]>();
	
	//Levels
	public static BufferedImage level_1bg;
	
	//Images
	public static BufferedImage[] Elements;
	public static BufferedImage Heart;
	public static BufferedImage Gold;
	public static BufferedImage Next;
	public static BufferedImage Up;
	public static BufferedImage Down;
	
	//Load
	public synchronized void loadAll() throws IOException
	{
		//LEVELS
		//level_1
		level_1bg = ImageIO.read(SpriteMap.class.getResource("/Resources/Level_1.png"));
		
		
		//MONSTSERS
		//troll
		BufferedImage trollmove_src = ImageIO.read(SpriteMap.class.getResource("/Resources/Troll_Move.png"));
		
		BufferedImage[] trollMove = new BufferedImage[8];
		for(int i = 0; i < 8; i++)
			trollMove[i] = clip(trollmove_src, (64*i), 0, 64, 64);
		
		BufferedImage[][] troll = new BufferedImage[1][8];
		troll[0] = trollMove;
		MonsterSprites.put("troll", troll);
		
		//Demon
		BufferedImage demonmove_src = ImageIO.read(SpriteMap.class.getResource("/Resources/demon.png"));
		
		BufferedImage[] demonMove = new BufferedImage[8];
		for(int i = 0; i < 8; i++)
			demonMove[i] = clip(demonmove_src, (64 * i), 126, 64, 64);
		
		BufferedImage[][] demon = new BufferedImage[1][8];
		demon[0] = demonMove;
		MonsterSprites.put("demon", demon);
		
		//SeaWeed Monster
		BufferedImage seaweedmove_src = ImageIO.read(SpriteMap.class.getResource("/Resources/seaweedmove.png"));
		
		BufferedImage[] seaweedmove = new BufferedImage[16];
		int index = 0;
		for(int i = 0; i < 4; i++)
			for(int k = 0; k < 4; k++)
				seaweedmove[index++] = clip(seaweedmove_src, (64 * k), (64 * i), 64, 64);
		
		BufferedImage[][] seaweed = new BufferedImage[1][16];
		seaweed[0] = seaweedmove;
		MonsterSprites.put("seaweed", seaweed);
		
		//DarkLord
		BufferedImage darklord_src = ImageIO.read(SpriteMap.class.getResource("/Resources/gargant-lord-movedark.png"));
		
		BufferedImage[] darklordMove = new BufferedImage[8];
		for(int i = 0; i < 8; i++)
			darklordMove[i] = clip(darklord_src, (64*i), 0, 64, 64);
		
		BufferedImage[][] darklord = new BufferedImage[1][8];
		darklord[0] = darklordMove;
		MonsterSprites.put("darklord", darklord);
		
		//Charged Shaman
		BufferedImage chargedshaman_src = ImageIO.read(SpriteMap.class.getResource("/Resources/ogre-move.png"));
		
		BufferedImage[] chargedshamanMove = new BufferedImage[8];
		for(int i = 0; i < 8; i++)
			chargedshamanMove[i] = clip(chargedshaman_src, (64*i), 0, 64, 64);
		
		BufferedImage[][] chargedshaman = new BufferedImage[1][8];
		chargedshaman[0] = chargedshamanMove;
		MonsterSprites.put("chargedshaman", chargedshaman);

		//TOWERS
		//cannon
		BufferedImage[] cannonTower = new BufferedImage[2];
		cannonTower[0] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Towers/cannonBase.png")), 0, 0, 64, 64);
		cannonTower[1] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Towers/cannonTop.png")), 0, 0, 58, 58);
		
		TowerSprites.put("cannonTower", cannonTower);
		Iconize("cannonTower", cannonTower);
		
		//fire
		BufferedImage[] fireTower = new BufferedImage[2];
		fireTower[0] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Towers/fireTowerBase.png")), 0, 0, 64, 64);
		fireTower[1] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Towers/fireTowerTop.png")), 0, 0, 58, 58);
		
		TowerSprites.put("fireTower", fireTower);
		Iconize("fireTower", fireTower);
		
		//nature
		BufferedImage[] natureTower = new BufferedImage[2];
		natureTower[0] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Towers/natureTowerBase.png")), 0, 0, 64, 64);
		natureTower[1] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Towers/natureTowerTop.png")), 0, 0, 58, 58);
		
		TowerSprites.put("natureTower", natureTower);
		Iconize("natureTower", natureTower);
		
		//electric
		BufferedImage[] electricTower = new BufferedImage[2];
		electricTower[0] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Towers/electricTowerBase.png")), 0, 0, 64, 64);
		electricTower[1] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Towers/electricTowerTop.png")), 0, 0, 58, 58);
		
		TowerSprites.put("electricTower", electricTower);
		Iconize("electricTower", electricTower);
		
		//EFFECTS
		//fire ball
		BufferedImage fireBallprojectile_src = ImageIO.read(SpriteMap.class.getResource("/Resources/FireBall_project.png"));
		BufferedImage fireBallhit_src = ImageIO.read(SpriteMap.class.getResource("/Resources/FireBall_aoe.png"));
		
		BufferedImage[] fireBallprojectile = new BufferedImage[6];
		for(int i = 0; i < 6; i++)
			fireBallprojectile[i] = clip(fireBallprojectile_src, (48 * i), 0, 48, 18);
		
		BufferedImage[] fireBallhit = new BufferedImage[10];
		for(int i = 0; i < 10; i++)
			fireBallhit[i] = clip(fireBallhit_src, (int)(47.5 * i), 0, 47, 50);
		
		EffectSprites.put("fireBall_projectile", fireBallprojectile);
		EffectSprites.put("fireBall_hit", fireBallhit);
		
		//Cannon ball
		BufferedImage[] cannonBallprojectile = {clip(ImageIO.read(SpriteMap.class.getResource("/Resources/cannonBall_project.png")), 0, 0, 24, 24)};
		EffectSprites.put("cannonBall_projectile", cannonBallprojectile);
		
		//Root
		BufferedImage rootDirect_src = ImageIO.read(SpriteMap.class.getResource("/Resources/Root_direct.png"));
		
		BufferedImage[] rootDirect = new BufferedImage[9];
		for(int i = 0; i < 9; i++)
			rootDirect[i] = clip(rootDirect_src, 0, (54*i), 51, 54);
		
		EffectSprites.put("root_direct", rootDirect);	
		
		//Lightning
		BufferedImage[] lightning = {clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Lightning.png")), 0, 0, 28, 203)};
		EffectSprites.put("lightning", lightning);
		
		//ICONS
		//Elements
		Elements = new BufferedImage[9];
		Elements[0] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Elements/Basic.png")), 0, 0, 32, 32);
		Elements[1] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Elements/Fire.png")), 0, 0, 32, 32);
		Elements[2] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Elements/Nature.png")), 0, 0, 32, 32);
		Elements[3] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Elements/Light.png")), 0, 0, 32, 32);
		Elements[4] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Elements/Dark.png")), 0, 0, 32, 32);
		Elements[5] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Elements/Arcane.png")), 0, 0, 32, 32);
		Elements[6] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Elements/Mechanical.png")), 0, 0, 32, 32);
		Elements[7] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Elements/Electricity.png")), 0, 0, 32, 32);
		Elements[8] = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Elements/Water.png")), 0, 0, 32, 32);
		
		//Lives
		Heart = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Heart.png")), 0, 0, 27, 23);
		
		//Gold
		Gold = clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Gold.png")), 0, 0, 30, 30);
		
		//Play
		Next = scale(clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Play.png")), 0, 0, 128, 128), 46, 46);
		
		//Up Arrow
		Up = scale(clip(ImageIO.read(SpriteMap.class.getResource("/Resources/Up2.png")), 0, 0, 24, 24), 20, 20);
	}
	
	public BufferedImage clip(BufferedImage src, int x, int y, int w, int h)
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		BufferedImage newImage = null;
		
		try
		{
			GraphicsDevice screen = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = screen.getDefaultConfiguration();
			newImage = gc.createCompatibleImage(w, h, Transparency.BITMASK);
		} catch (Exception ex) {}
		
		if (newImage == null)
		{
			newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		}
		
		int[] pixels = new int[w*h];
		src.getRGB(x, y, w, h, pixels, 0, w);
		newImage.setRGB(0, 0, w, h, pixels, 0, w);
		
		return newImage;
	}
	
	public static BufferedImage rotate(BufferedImage image, double angle) {
		
	    double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
	    int w = image.getWidth(), h = image.getHeight();
	    int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
	    
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice screen = ge.getDefaultScreenDevice();
	    GraphicsConfiguration gc = screen.getDefaultConfiguration();
	    BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
	    Graphics2D g = result.createGraphics();
	    
	    g.translate((neww-w)/2, (newh-h)/2);
	    g.rotate(angle, w/2, h/2);
	    g.drawRenderedImage(image, null);
	    g.dispose();
	    
	    return result;
	}
	
	public static BufferedImage scale(BufferedImage image, double target_w, double target_h)
	{
		BufferedImage after = new BufferedImage((int)target_w, (int)target_h, BufferedImage.TYPE_4BYTE_ABGR_PRE);
		
		Graphics2D g2d = after.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(image, 0, 0, (int)target_w, (int)target_h, null);
		g2d.dispose();
		
		return after;
	}
	
	private static void Iconize(String key, BufferedImage[] images)
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice screen = ge.getDefaultScreenDevice();
	    GraphicsConfiguration gc = screen.getDefaultConfiguration();
	    
	    BufferedImage[] icon = new BufferedImage[3];
	    for(int i = 0; i < 3; i++)
	    {
		    BufferedImage result = gc.createCompatibleImage(50, 50, Transparency.TRANSLUCENT);
		    Graphics2D g2d = result.createGraphics();
		    Color c;
		    switch(i)
		    {
		    case 0:
		    	c = Color.BLACK;
		    	break;
		    case 1:
		    	c = Color.DARK_GRAY;
		    	break;
		    case 2:
		    	c = Color.WHITE;
		    	break;
		    default:
		    	c = Color.BLACK;
		    }
		    
		    g2d.setColor(c);
		    g2d.fillRect(0, 0, 50, 50);
		    for(int j = 0; j < images.length; j++)
		    {
		    	g2d.drawImage(scale(images[j], 46, 46), 2, 2, null);
		    }
		    
		    icon[i] = result;
	    }
	    
	    Icons.put(key, icon);
	}
	
	public static void drawMonster(Graphics2D g2d, String key, int animationID, int imageIndex, int x, int y, double ang)
	{
		try
		{
			BufferedImage image = rotate(MonsterSprites.get(key)[animationID][imageIndex % MonsterSprites.get(key)[animationID].length], Math.toRadians(ang));
			
			g2d.drawImage(image, x - (image.getWidth()/2), y - (image.getHeight()/2), null);
			
		}catch (Exception ex) {ex.printStackTrace();}
	}
	
	public static void drawTower(Graphics2D g2d, String key, boolean selected, int x, int y, int r, double ang)
	{
		try
		{
			BufferedImage baseImage = TowerSprites.get(key)[0];
			
			if (selected)
			{
				g2d.setColor(Color.BLUE);
				g2d.drawOval(x - r, y - r, r*2, r*2);
				
				int w = baseImage.getWidth() + 10;
				int h = baseImage.getHeight() + 10;
				g2d.fillRect(x - (w/2), y - (h/2), w, h);
			}
			g2d.drawImage(baseImage, x - (baseImage.getWidth()/2), y - (baseImage.getHeight()/2), null);
			
			BufferedImage topImage = rotate(TowerSprites.get(key)[1], Math.toRadians(ang));
			g2d.drawImage(topImage, x - (topImage.getWidth()/2), y - (topImage.getHeight()/2), null);
			
		}catch (Exception ex) {ex.printStackTrace();}
	}
	
	public static void drawEffect(Graphics2D g2d, String key, int imageIndex, int x, int y, double ang)
	{
		try
		{
			BufferedImage image;
			if (ang != 0)
				image = rotate(EffectSprites.get(key)[imageIndex % EffectSprites.get(key).length], Math.toRadians(ang));
			else
				image = EffectSprites.get(key)[imageIndex % EffectSprites.get(key).length];
			
			g2d.drawImage(image, x - (image.getWidth()/2), y - (image.getHeight()/2), null);
			
		}catch (Exception ex) {ex.printStackTrace();}
	}
	
	public static void drawEffect(Graphics2D g2d, String key, int imageIndex, int x1, int y1, int x2, int y2)
	{
		double ang = Entity.getDirectionToward(x1, y1, x2, y2);
		double dist = Entity.getDistance(x1, y1, x2, y2);
		
		try
		{
			BufferedImage image = scale(EffectSprites.get(key)[imageIndex % EffectSprites.get(key).length], EffectSprites.get(key)[imageIndex % EffectSprites.get(key).length].getWidth(), dist );
			if (ang != 0)
				image = rotate(image, Math.toRadians((ang + 180)%360));
			
			g2d.drawImage(image, (x1 + x2)/2 - image.getWidth()/2, (y1 + y2)/2 - image.getHeight()/2, null);
			
		}catch (Exception ex) {ex.printStackTrace();}
	}
	
	public static void drawElement(Graphics2D g2d, int type, int x, int y)
	{
		try
		{
			assert (type >= 0 && type < 9);
			
			g2d.drawImage(Elements[type], x - 16, y - 16, null);
			
		}catch (Exception ex) {ex.printStackTrace();}
	}
}
