package npcs;

import gui.ContrastWindow;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.MediaTracker;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import projectiles.Shot;
import main.ArenaHandler;
import main.Contrast;
import main.Game;

public class Dude extends Rectangle{
	public int order;
	//max = 50
	//Should be 255/5 which equals 51, but
	//add one to beginning so you can exclude 255/0 error
	int strength = 1;
	int intellect = 1;
	int speed = 1;
	int damage = 10;
	int maxhealth = 100;
	int health = 100;
	int level = 1;
	Image arm = null;
	Image wings = null;
	MediaTracker tracker;
	int power = 10;
	int knockback = 0;
	int direction = 1;
	int range = 400;
	String name = "Unnamed";
	Classes type = Classes.WARRIOR; //default
	float hitTimer, deathTimer, shootingTimer;
	int shootingDelay = 100;
	int hit = 0;
	boolean dying = false;
	int yOrigin;
	float trueX;
	public Dude(int order, int direction)
	{
		this.order = order;
		this.direction = direction;
		this.width = 50;
		this.height = 50;
		this.y = ContrastWindow.getInstance().getHeight()-100;
		this.yOrigin = y;
		loadImages();
	}
	public Dude(Dude original)
	{
		this.order = original.order;
		this.direction = original.direction;
		this.width = original.width;
		this.height = original.height;
		this.y = original.y;
		this.yOrigin = original.yOrigin;
		this.strength = original.strength;
		this.intellect = original.intellect;
		this.speed = original.speed;
		this.type = original.type;
		loadImages();
	}
	public void loadImages()
	{
	    try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream("Crowbar.gif");
			arm = ImageIO.read(input);
	        final String path = "file:///Users/scagle/Desktop/Contrast/src/Wings.gif"; // Any URL would work here
	        wings = new ImageIcon("/Contrast/src/Wings.gif").getImage();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		try
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream("Crowbar.gif");
			arm = ImageIO.read(input);
		}
		catch(Exception e){e.printStackTrace();}
	}
	public String getName()
	{
		return name;
	}
	public void addAttribute(String att, String dir)
	{
		if (att.equals("Strength"))
		{
			if (dir.equals("+"))
				strength++;
			else
				strength--;
		}
		if (att.equals("Intellect"))
		{
			if (dir.equals("+"))
				intellect++;
			else
				intellect--;
		}
		if (att.equals("Speed"))
		{
			if (dir.equals("+"))
				speed++;
			else
				speed--;
		}
		if (strength <= 0)
			strength = 1;
		if (intellect <= 0)
			intellect = 1;
		if (speed <= 0)
			speed = 1;
		if (strength > 50)
			strength = 50;
		if (intellect > 50)
			intellect = 50;
		if (speed > 50)
			speed = 50;
	}
	public Classes getType()
	{
		return type;
	}
	public void setType(Classes type)
	{
		this.type = type;
		if (type == Classes.ARCHER)
		{
			this.maxhealth = 25;
		}
		if (type == Classes.WARRIOR)
		{
			this.maxhealth = 125;
		}
	}
	public boolean isKnockedBack()
	{
		if (knockback > 0)
			return true;
		return false;
	}
	public void setAttributes(int strength, int intellect, int speed)
	{
		if (strength == 0)
			strength = 1;
		if (intellect == 0)
			intellect = 1;
		if (speed == 0)
			speed = 1;
		this.strength = strength;
		this.intellect = intellect;
		this.speed = speed;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Color getColor()
	{
		if (direction == -1)
		{
			return new Color(150, 150, 150);
		}
		return new Color((int)5*strength, (int)5*intellect, (int)5*speed);
	}
	public int getOrder()
	{
		return order;
	}
	public void setOrder(int order)
	{
		this.order = order;
	}
	public void start(int x)
	{
		this.x = x;
		this.trueX = x;
	}
	public Dude checkFoes()
	{
		ArrayList<Dude> foes = ArenaHandler.getInstance().enemydudes;
		for (int i = 0; i < foes.size();i++)
		{
			if ((foes.get(i).x-this.x)*direction <= range)
			{
				return foes.get(i);
			}
		}
		return null;
	}
	public void shoot()
	{
		Shot shot = new Shot(this.x+width/2*direction, this.y+height/2, direction);
		ArenaHandler.getInstance().shots.add(shot);
	}
	public void tick()
	{
		if (!dying)
		{
			if (hitTimer > 0)
				hitTimer--;
			if (knockback > 0)
			{
				this.trueX -= knockback*direction;
				knockback--;
				if (this.trueX < 0 || this.trueX > Contrast.WIDTH)
				{
					knockback++;
					this.trueX +=knockback*direction;
					knockback = 0;
				}
			}
			else
			{
				float incrementX = (float) ((1 + speed/25 +.5)*direction);
				if (getType() == Classes.ARCHER)
				{
					Dude foe = checkFoes();
					if (foe != null)
					{
						if (shootingTimer > 0)
							shootingTimer--;
						else
						{
							shoot();
							shootingTimer = shootingDelay;
						}
					}
					else this.trueX += incrementX;
				}
				else this.trueX += incrementX;
			}
		}
		else
		{
			this.y--;
			if (deathTimer > 0)
				deathTimer--;
		}
		this.x = (int)(trueX+0.5);
	}
	public void render(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		if (dying)
		{
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, deathTimer/50));
		}
		int padding = 2;
		g.setColor(Color.BLACK);
		if (getType() == Classes.WARRIOR)
		{
			g.fillRect(x, y, width, height);
			g.setColor(getColor());
			g.fillRect(x+padding, y+padding, width-padding*2, height-padding*2);
			g.drawImage(wings, x, yOrigin, width, height, null);
		}
		if (getType() == Classes.ARCHER)
		{
			int[] xPos = new int[]{x-5, x+width/2, x+5+width};
			int[] yPos = new int[]{y+12, y,      y+12};
			Polygon p = new Polygon(xPos, yPos, 3);
			g.fillRect(x, y+10, width, height-10);
			g.fillPolygon(p);
			g.setColor(getColor());
			g.fillRect(x+padding, y+10+padding, width-padding*2, height-10-padding*2);
			xPos = new int[]{x-5+padding, x+width/2, x+5+width-padding};
			yPos = new int[]{y+10+padding, y+padding,      y+10+padding};
			p = new Polygon(xPos, yPos, 3);
			g.fillPolygon(p);
		}
		if (hitTimer > 0)
		{
			g.setFont(new Font("Courier New", Font.BOLD, 12));
		    g.setColor(Color.WHITE);
			Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, hitTimer/50);
		    g2d.setComposite(comp);
		    g.drawString(Integer.toString(hit), x+width/2-10, y-10-((int)(10-hitTimer/5)));
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
		//g.drawImage(arm, x, y-20, width+40, height+20, null);
	}
	public void hit(int damage)
	{
		this.health-=damage;
		this.hit = damage;
		this.hitTimer = 50;
		if (health <= 0)
		{
			dying = true;
			deathTimer = 50;
		}
	}
	public boolean isDying()
	{
		if (dying == true && deathTimer > 0)
		{
			return true;
		}
		return false;
	}
	public boolean isShowing() //If dude is dead, AND 100% GONE
	{
		if (health > 0 || deathTimer > 0)
			return true;
		return false;
	}
	public boolean isAlive() //If dude is dead, BUT NOT YET GONE yet (ghost)
	{
		if (health > 0)
			return true;
		return false;
	}
	public int getDamage()
	{
		return damage+strength;
	}
	public int getPower() {
		return power;
	}
	public void knockback(int enemypower) {
		this.knockback = enemypower;
	}
	public int getLevel()
	{
		return level;
	}
	public String toString()
	{
		String str="";
		if (direction == 1)
			str+="friend, ";
		else
			str+="enemy , ";
		str+="r:"+strength+", ";
		str+="g:"+intellect+", ";
		str+="b:"+speed+", ";
		str+="hp:"+health+", ";
		str+="X_Xtime:"+deathTimer;
		if (dying)
			str+="dying:true, ";
		else
			str+="dying:false, ";

		return str;
	}
}
