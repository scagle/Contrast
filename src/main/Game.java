package main;

import gui.CButton;
import gui.CDude;
import gui.ContrastWindow;
import npcs.Classes;
import npcs.Dude;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game{
	public ArrayList<CButton> buttons = new ArrayList<CButton>();
	public ArrayList<Dude> dudes = new ArrayList<Dude>();
	public ArrayList<CDude> cdudes = new ArrayList<CDude>();
	public ArrayList<Dude> recruitdudes = new ArrayList<Dude>();
	String[] names = new String[]
	{"Abergavenny", "Steven", "Juan", "Eitan", "Evan", "Mr. Cooper",
	 "Adam", "Tiberius", "Adriana", "Agamemnon", "Sir Andrew",
	 "Ajax", "Alexander"};
	private static Contrast canvas;
	private static ContrastWindow window;
	ArenaHandler arenaHandler;
	Locations location = Locations.MAINMENU;
	Image menu, arena, recruit;
	public int width;
	public int height;
	boolean battling = false;
	String winText = "";
	int currentDude = 0;
	public static Game game;
	
	public Game(Contrast canvas, ContrastWindow window)
	{
		loadImages();
		new Levels(); //Initializes the levels arraylist. Necessary
		Game.canvas = canvas;
		Game.window = window;
		this.width = canvas.getWidth();
		this.height = canvas.getHeight();
		game = this;
	}
	public void loadImages()
	{
		try
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream("Contrast.jpg");
			menu = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Arena.jpg");
			arena = ImageIO.read(input);
			input = classLoader.getResourceAsStream("Recruit.bmp");
			recruit = ImageIO.read(input);
			
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void initializeEnemyTeam()
	{
		ArrayList<Dude> enemys = Levels.getLevel(Levels.getCurrentLevel());
		if (enemys != null)
		{
			for (int i = 0;i<enemys.size();i++)
			{
				arenaHandler.addEnemy(enemys.get(i));
			}
		}
	}
	public void checkPress(int x, int y)
	{
		for (int i = 0; i < buttons.size(); i++)
		{
			if (buttons.get(i).checkButtonPress(x, y))
			{
				if (buttons.get(i).getCommand().equals("Start"))
				{
					load(Locations.LOBBY);
					break;
				}
				if (buttons.get(i).getCommand().equals("Back"))
				{
					winText = "";
					arenaHandler = null;
					load(Locations.LOBBY);
					break;
				}
				if (buttons.get(i).getCommand().equals("Battle"))
				{
					System.out.println("To Battle!");
					load(Locations.ARENA);
					break;
				}
				if (buttons.get(i).getCommand().equals("Settings"))
				{
					System.out.println("Settings");
					load(Locations.SETTINGS);
					break;
				}
				if (buttons.get(i).getCommand().equals("Recruit"))
				{
					System.out.println("Recruiting");
					load(Locations.RECRUIT);
					break;
				}
				if (buttons.get(i).getCommand().equals("Quit"))
				{
					System.out.println("Quiting!");
					System.exit(1);
					break;
				}
				String[] erasedata = buttons.get(i).getCommand().split("=");
				if (erasedata[0].equals("Delete"))
				{
					if (Integer.parseInt(erasedata[1])<=dudes.size()-1)
					{
							dudes.remove(Integer.parseInt(erasedata[1])-1);
						for (int j = 0; j < dudes.size();j++)
						{
							dudes.get(j).setOrder(j+1);
						}
						for (int j = 0; j < cdudes.size();j++)
						{
							if (dudes.size()-1 >= j)
								cdudes.get(j).updateDude(dudes.get(j));
							else
								cdudes.get(j).updateDude(null);
						}
						break;
					}
				}
				else if (erasedata[0].equals("Specialize"))
				{
					currentDude = Integer.parseInt(erasedata[1])-1;
					load(Locations.SPECIALIZE);
					break;
				}
				if (buttons.get(i).getCommand().equals("ArrowLeft"))
				{
					if (currentDude <= 0)
					{
						if (location == Locations.RECRUIT)
							currentDude = recruitdudes.size()-1;
					}
					else
					{
						currentDude--;
					}
					break;
				}
				else if (erasedata[0].equals("Level"))
				{
					int level = Integer.parseInt(erasedata[1]);
					Levels.setCurrentLevel(level);
					System.out.println("Level " + level);
					load(Locations.ARENA);
					break;
				}
				if (buttons.get(i).getCommand().equals("ArrowRight"))
				{
					System.out.println("Right");
					currentDude++;
					switch(location)
					{
						case RECRUIT:
							if (currentDude >= recruitdudes.size())
								currentDude = 0;
							break;
					}
				}
				if (buttons.get(i).getCommand().equals("RecruitDude"))
				{
					if (dudes.size() < 4)
					{
						dudes.add(recruitdudes.get(currentDude));
						recruitdudes.remove(currentDude);
						if (currentDude >= recruitdudes.size()-1)
						{
							currentDude = 0;
						}
						else
						{
							currentDude--;
							if (currentDude < 0)
							{
								currentDude++;
							}
						}
					}
					break;
				}
				String[] data = buttons.get(i).getCommand().split(",");
				if (data.length == 2)
				{
					if (data[0].equals("r"))
						dudes.get(currentDude).addAttribute("Strength", data[1]);
					if (data[0].equals("g"))
						dudes.get(currentDude).addAttribute("Intellect", data[1]);
					if (data[0].equals("b"))
						dudes.get(currentDude).addAttribute("Speed", data[1]);
				}
			}
		}
	}
	public void createDefaultButtons()
	{
		CButton button = new CButton(width-85, 5, 80, 20);
		button.setText("Back");
		button.setCommand("Back");
		buttons.add(button);
	}
	public void load(Locations l)
	{
		buttons.clear();
		cdudes.clear();
		arenaHandler = null;
		battling = false;
		this.location = l;
		if (location == Locations.MAINMENU)
		{
			buttons.add(new CButton(100, 150, canvas.getWidth()-200, 50));
			buttons.get(0).setText("Start Game");
			buttons.get(0).setCommand("Start");
		}
		if (location == Locations.LOBBY)
		{
			buttons.add(new CButton(width-width/3+3, 5, width/3-10, 30));
			buttons.get(0).setText("Quit");
			buttons.get(0).setCommand("Quit");
			buttons.add(new CButton((width-width/3 * 2)+ 3, 5, width/3-10, 30));
			buttons.get(1).setText("Settings");
			buttons.get(1).setCommand("Settings");
			buttons.add(new CButton(width/2-100 ,height/3-125, 200, 75));
			buttons.get(2).setText("Random Battle");
			buttons.get(2).setCommand("Battle");
			buttons.get(2).setBackground(new Color(255, 216, 57), new Color(255, 255, 125));
			buttons.add(new CButton(8, 5, width/3-15, 30));
			buttons.get(3).setText("Recruit");
			buttons.get(3).setCommand("Recruit");
			for (int i = 0; i < 4; i++)
			{
				CDude cd = new CDude(i*width/4+5, height/3*2+10, width/4-10, height/3-20);
				if (dudes.size() >= i+1)
				{
					cd.setDude(dudes.get(i));
				}
				cdudes.add(cd);
			}
			int level = 1;
			for (int x = 1; x <= 5; x++)
			{
				for (int y = 1; y <= 5; y++)
				{
					if (level <= Levels.getProgression() && level <= Levels.getSize())
					{
						CButton button = new CButton(2*x + width/5*(x-1), 2*y + height/3-20 + height/14*(y-1), width/5-10, height/14-10);
						button.setText("Level "+level);
						button.setBackground(Color.cyan);
						button.setCommand("Level="+level);
						buttons.add(button);
					}
					level++;
				}
			}
		}
		if (location == Locations.SETTINGS)
		{
			createDefaultButtons();
		}
		if (location == Locations.SPECIALIZE)
		{
			CButton but = new CButton(30, height-height/4+15, (width-50)/3-10, height/8-35);
			but.setText("+");
			but.setCommand("r,+");
			buttons.add(but);
			but = new CButton(30 + (width-50)/3, height-height/4+15, (width-50)/3-10, height/8-35);
			but.setText("+");
			but.setCommand("g,+");
			buttons.add(but);
			but = new CButton(width - 30 - (width-50)/3, height-height/4+15, (width-50)/3-10, height/8-35);
			but.setText("+");
			but.setCommand("b,+");
			buttons.add(but);
			but = new CButton(30, height-height/8+5, (width-50)/3-10, height/8-35);
			but.setText("-");
			but.setCommand("r,-");
			buttons.add(but);
			but = new CButton(35 + (width-50)/3, height-height/8+5, (width-50)/3-10, height/8-35);
			but.setText("-");
			but.setCommand("g,-");
			buttons.add(but);
			but = new CButton(width - 40 - (width-50)/3, height-height/8+5, (width-50)/3-10, height/8-35);
			but.setText("-");
			but.setCommand("b,-");
			buttons.add(but);
			
			createDefaultButtons();
		}
		if (location == Locations.ARENA)
		{
			if (dudes.size() > 0)
			{
				
				arenaHandler = new ArenaHandler(this, cloneDudes());
				initializeEnemyTeam();
				battling = true;
				createDefaultButtons();
			}
			else
			{
				load(Locations.LOBBY);
			}
		}
		if (location == Locations.RECRUIT)
		{
			recruitdudes.clear();
			currentDude = 0;
			for (int i = 1; i <= 5; i++)
			{
				int randomStr = (int)(Math.random()*50);
				int randomInt = (int)(Math.random()*50);
				int randomSpe = (int)(Math.random()*50);
				int randomName = (int)(Math.random()*names.length);
				Dude tempdude = new Dude(i, 1);
				int randomArcher = (int)(Math.random()*2);
				if (randomArcher == 1)
					tempdude.setType(Classes.ARCHER);
				tempdude.setName(names[randomName]);
				tempdude.setAttributes(randomStr, randomInt, randomSpe);
				recruitdudes.add(tempdude);
			}
			buttons.add(new CButton(5, height/2-25, 50, 50));
			buttons.get(0).setTriangle(-1);
			buttons.get(0).setText("");
			buttons.get(0).setBackground(Color.BLACK);
			buttons.get(0).setCommand("ArrowLeft");
			buttons.add(new CButton(width-55, height/2-25, 50, 50));
			buttons.get(1).setTriangle(1);
			buttons.get(1).setText("");
			buttons.get(1).setBackground(Color.BLACK);
			buttons.get(1).setCommand("ArrowRight");
			buttons.add(new CButton(width/2-200, height-height/4+50, 400, 50));
			buttons.get(2).setText("Recruit");
			buttons.get(2).setCommand("RecruitDude");
			createDefaultButtons();
		}
	}
	public ArrayList<Dude> cloneDudes()
	{
		ArrayList<Dude> temp = new ArrayList<Dude>();
		for (int i = 0;i<dudes.size();i++)
			temp.add(new Dude(dudes.get(i))); //makes identical clone
		return temp;
	}
	public void renderMenu(Graphics g)
	{
		g.drawImage(menu, 0, 0, window.getContentPane().getWidth(), window.getContentPane().getHeight(), null);
	}
	public void renderLobby(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, canvas.getWidth()-1, canvas.getHeight()/3-100);
		g.fillRect(0, canvas.getHeight()/3-100, canvas.getWidth()-1, canvas.getHeight()/3+100);
		g.drawRect(0, canvas.getHeight()/3*2, canvas.getWidth()-1, canvas.getHeight()/3+1);
	}
	public void renderSettings(Graphics g)
	{
		
	}
	public void renderArena(Graphics g)
	{
		g.drawImage(arena, 0, 0, window.getContentPane().getWidth(), window.getContentPane().getHeight(), null);
		if (!winText.equals(""))
		{
			g.setFont(new Font("Courier New", Font.BOLD, 24));
			FontMetrics fm   = g.getFontMetrics(g.getFont());
			java.awt.geom.Rectangle2D bounds = fm.getStringBounds(winText, g);
			g.drawString(winText, (int)(width/2-bounds.getWidth()/2), 405);

		}
	}
	public void renderStore(Graphics g)
	{
		
	}
	public void renderSpecialize(Graphics g)
	{
		if (dudes.size() > 0)
		{
			g.setColor(new Color(200,200,255));
			int[] xPos = new int[] {width/2-200, width/2, width/2+200};
			int[] yPos = new int[] {height-height/3, -100, height-height/3};
			g.fillPolygon(new Polygon(xPos, yPos, 3));
			g.fillOval(width/2-199, height-height/3-50, 398, 100);
			g.setColor(new Color(150,150,200));
			g.drawOval(width/2-199, height-height/3-50, 398, 100);
			g.setColor(dudes.get(currentDude).getColor());
			g.fillRect(width/2-100, height/2-100, 200, 200);
			g.setColor(new Color(200,50,50));
			g.fillRect(25, height-height/4+10, (width-50)/3, height/4-35);
			g.setColor(new Color(50,200,50));
			g.fillRect(25 + (width-50)/3, height-height/4+10, (width-50)/3, height/4-35);
			g.setColor(new Color(50,50,200));
			g.fillRect(width - 25 - (width-50)/3, height-height/4+10, (width-50)/3, height/4-35);
			g.setFont(new Font("Courier New", Font.BOLD, 24));
			FontMetrics fm   = g.getFontMetrics(g.getFont());
			String name = dudes.get(currentDude).getName();
			java.awt.geom.Rectangle2D bounds = fm.getStringBounds(name, g);
			g.drawString(name, (int)(width/2-bounds.getWidth()/2), 405);
			name = "{"+dudes.get(currentDude).getColor().getRed()+", "
					+dudes.get(currentDude).getColor().getGreen()+", "
					+dudes.get(currentDude).getColor().getBlue() +"}";
			bounds = fm.getStringBounds(name, g);
			g.setColor(Color.WHITE);
			g.drawString(name, (int)(width/2-bounds.getWidth()/2), 430);
		}
		else
		{
			g.setFont(new Font("Courier New", Font.BOLD, 24));
			FontMetrics fm   = g.getFontMetrics(g.getFont());
			String name = "You need some Dudes first!";
			java.awt.geom.Rectangle2D bounds = fm.getStringBounds(name, g);
			g.setColor(Color.WHITE);
			g.drawString(name, (int)(width/2-bounds.getWidth()/2), height/2-25);
		}
	}
	public void renderRecruit(Graphics g)
	{
		g.drawImage(recruit, 0, 0, width, height-height/4, null);
		g.setColor(Color.BLACK);
		g.fillRect(0, height-height/4, width, height/4);
		if (recruitdudes.size() > 0)
		{
			g.setColor(new Color(50, 50 ,50));
			Classes type = recruitdudes.get(currentDude).getType();
			if (type == Classes.ARCHER)
			{
				g.fillRect(width/2-100, height/2-70, 200, 170);
				int[] xPos = new int[]{width/2-130, width/2, width/2+130};
				int[] yPos = new int[]{height/2-67, height/2-105, height/2-67};
				Polygon p = new Polygon(xPos, yPos, 3);
				g.fillPolygon(p);
				g.setColor(recruitdudes.get(currentDude).getColor());
				g.fillRect(width/2-95, height/2-70, 190, 170);
				xPos = new int[]{width/2-120, width/2, width/2+120};
				yPos = new int[]{height/2-70, height/2-100, height/2-70};
				p = new Polygon(xPos, yPos, 3);
				g.fillPolygon(p);
			}
			if (type == Classes.WARRIOR)
			{
				g.fillRect(width/2-100, height/2-100, 200, 200);
				g.setColor(recruitdudes.get(currentDude).getColor());
				g.fillRect(width/2-95, height/2-95, 190, 190);
			}
			g.setColor(Color.black);
			g.fillOval(width/2-75, height/2-25, 5, 5);
			g.fillOval(width/2+70, height/2-25, 5, 5);
			g.drawLine(width/2-85, height/2+10, width/2+85, height/2+10);
			g.setFont(new Font("Courier New", Font.BOLD, 24));
			FontMetrics fm   = g.getFontMetrics(g.getFont());
			Dude dude = recruitdudes.get(currentDude);
			String name = "{"+dude.getColor().getRed()+", "+dude.getColor().getGreen()+", "+dude.getColor().getBlue()+"}";
			java.awt.geom.Rectangle2D bounds = fm.getStringBounds(name, g);
			g.drawString(name, (int)(width/2-bounds.getWidth()/2), 150);
			name = dude.getName();
			bounds = fm.getStringBounds(name, g);
			g.drawString(name, (int)(width/2-bounds.getWidth()/2), 405);
			name = (currentDude+1)+" / "+recruitdudes.size();
			bounds = fm.getStringBounds(name, g);
			g.drawString(name, (int)(width/2-bounds.getWidth()/2), 120);
		}
	}
	public void renderButtons(Graphics g)
	{
		for (int i = 0; i < buttons.size(); i++)
		{
			if (buttons.get(i).isVisible())
				buttons.get(i).render(g);
		}
	}
	public void renderCDudes(Graphics g)
	{
		for (int i = 0; i < cdudes.size(); i++)
		{
			cdudes.get(i).render(g);
		}
	}
	public void render(Graphics g)
	{
		if (location == Locations.MAINMENU)
			renderMenu(g);
		if (location == Locations.LOBBY)
			renderLobby(g);
		if (location == Locations.SETTINGS)
			renderSettings(g);
		if (location == Locations.SPECIALIZE)
			renderSpecialize(g);
		if (location == Locations.RECRUIT)
			renderRecruit(g);
		if (location == Locations.ARENA)
		{
			renderArena(g);
			if (arenaHandler != null)
				arenaHandler.render(g);
		}
		if (location == Locations.STORE)
			renderStore(g);
		renderCDudes(g);
		renderButtons(g);
	}
	public void tick()
	{
		if (battling)
		{
			arenaHandler.tick();
		}
	}
	public void endBattle(int result)
	{
		battling = false;
		if (result == 1)
		{
			winText = "VICTORY";
		}
		else
		{
			winText = "DEFEAT";
		}
	}
	public static Game getInstance()
	{
		return game;
	}
	public static Contrast getCanvas()
	{
		return canvas;
	}
	public static JFrame getWindow()
	{
		return window;
	}
}
