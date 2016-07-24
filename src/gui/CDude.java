package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;

import main.Game;
import npcs.Classes;
import npcs.Dude;

public class CDude extends Rectangle
{
	boolean visible = true;
	String text = "Start Game";
	Font font = new Font("Courier New", Font.BOLD, 12);
	Color color = new Color(200, 200, 200);
	GradientPaint gradient = null;
	Dude dude = null;
	CButton erase, special;
	
	public CDude(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		erase = new CButton(x+width-20, y, 20, 20);
		erase.setText("X");
		erase.setBackground(Color.WHITE);
		Game.getInstance().buttons.add(erase);
		special = new CButton(x+30, y+height/2-10, width-60, 20);
		special.setText("Specialize");
		special.setFontSize(12);
		special.setBackground(Color.WHITE);
		Game.getInstance().buttons.add(special);
	}
	public void setBackground(Color color)
	{
		this.color = color;
	}
	public void setDude(Dude dude)
	{
		this.dude = dude;
		if (dude != null)
		{
			erase.setCommand("Delete="+dude.order);
			special.setCommand("Specialize="+dude.order);
		}
	}
	public void setBackground(Color color1, Color color2)
	{
		this.gradient = new GradientPaint(x,y, color1, x+width/2, y+height/2, color2, true);
	}
	public void setFont(Font f)
	{
		this.font = f;
	}
	public void setText(String s)
	{
		this.text = s;
	}
	public boolean isVisible()
	{
		return visible;
	}
	public void setVisible(boolean bool)
	{
		this.visible = bool;
	}
	public Polygon getPolygon()
	{
		Polygon p = null;
		int[] xPos;
		int[] yPos;
		if (dude != null)
		{
			if (dude.getType() == Classes.WARRIOR)
			{
				xPos = new int[]{x+20, x+width-20, x+width-20, x+20};
				yPos = new int[]{y+20, y+20, y+height-30, y+height-30};
				p = new Polygon(xPos, yPos, 4);
			}
			if (dude.getType() == Classes.ARCHER)
			{
				xPos = new int[]{x+20, x+width/2, x+width-20, x+width-20 , x+20};
				yPos = new int[]{y+40, y+20,      y+40,       y+height-30, y+height-30};
				p = new Polygon(xPos, yPos, 5);
			}
			if (dude.getType() == Classes.SPECIALIST)
			{
				xPos = new int[]{x+20, x+width-20, x+width-20, x+20};
				yPos = new int[]{y+20, y+20, y+height-30, y+height-30};
				p = new Polygon(xPos, yPos, 4);
			}
		}
		return p;
	}
	public void updateDude(Dude dude)
	{
		setDude(dude);
		
	}
	public void render(Graphics g)
	{
		if (gradient != null)
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.setPaint(gradient);
			g.fillRect(x, y, width, height);
			int[] xs = new int[]{x, x+40, x+50, x};
			int[] ys = new int[]{y-10, y-10, y, y};
			Polygon p = new Polygon(xs, ys, 4);
			g.fillPolygon(p);
		}
		else
		{
			int[] xs = new int[]{x, x+40, x+50, x};
			int[] ys = new int[]{y-9, y-9, y, y};
			Polygon p = new Polygon(xs, ys, 4);
			g.setColor(color);
			g.fillRect(x, y, width, height);
			g.fillPolygon(p);
		}
		if (dude == null)
		{
			erase.setVisible(false);
			special.setVisible(false);
		}
		else
		{
			erase.setVisible(true);
			special.setVisible(true);
			g.setColor(dude.getColor());
			g.fillPolygon(getPolygon());
			g.setColor(Color.BLACK);
			g.setFont(new Font("Courier New", Font.BOLD, 20));
			FontMetrics fm   = g.getFontMetrics(g.getFont());
			String name = "";
			if (dude != null && dude.getType() != null && dude.getType().getName() != null)
				//Errored here when trying to delete archer from squad
			{
				name = dude.getType().getName();
			}
			java.awt.geom.Rectangle2D bounds = fm.getStringBounds(name, g);
			g.drawString(name, (int)(x + width/2-bounds.getWidth()/2), y+height-5);
			name = dude.getName();
			bounds = fm.getStringBounds(name, g);
			g.drawString(name, (int)(x + width/2-bounds.getWidth()/2), y+15);
			String experiencelevel = "Level "+dude.getLevel();
			bounds = fm.getStringBounds(experiencelevel, g);
			g.drawString(experiencelevel, (int)(x + width/2-bounds.getWidth()/2), y+50);
		}
	}
	public void tick(){}
}