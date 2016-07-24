package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class CButton extends Rectangle
{
	boolean visible = true;
	String text = "";
	Font font = new Font("Courier New", Font.BOLD, 24);
	String command = "";
	Color color = Color.WHITE;
	GradientPaint gradient = null;
	int triangleDir = 0;
	int fontsize = 24;
	
	public CButton(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public void setBackground(Color color)
	{
		this.color = color;
	}
	public void setFontSize(int size)
	{
		font = new Font("Courier New", Font.BOLD, size);
	}
	public void setBackground(Color color1, Color color2)
	{
		this.gradient = new GradientPaint(x,y, color1, x+width/2, y+height/2, color2, true);
	}
	public void setCommand(String s)
	{
		this.command = s;
	}
	public String getCommand()
	{
		return command;
	}
	public void setFont(Font f)
	{
		this.font = f;
	}
	public void setTriangle(int dir)
	{
		this.triangleDir = dir;
	}
	public void setText(String s)
	{
		this.text = s;
	}
	public String getText()
	{
		return text;
	}
	public boolean isVisible()
	{
		return visible;
	}
	public void setVisible(boolean bool)
	{
		this.visible = bool;
	}
	public boolean checkButtonPress(int mouseX, int mouseY)
	{
		Point mouseClick = new Point(mouseX, mouseY);
		if (this.contains(mouseClick))
		{
			return true;
		}
		return false;
	}
	public void render(Graphics g)
	{
		if (triangleDir != 0)
		{
			g.setColor(color);
			if (triangleDir == 1)
			{
				int[] xPos = new int[] {x,x+width,x};
				int[] yPos = new int[] {y,y+height/2,y+height};
				g.fillPolygon(new Polygon(xPos, yPos, 3));
			}
			if (triangleDir == -1)
			{
				int[] xPos = new int[] {x+width,x,x+width};
				int[] yPos = new int[] {y,y+height/2,y+height};
				g.fillPolygon(new Polygon(xPos, yPos, 3));
			}
		}
		else
		{
			if (gradient != null)
			{
				Graphics2D g2d = (Graphics2D) g;
				g2d.setPaint(gradient);
				g.fillRect(x, y, width, height);
			}
			else
			{
				g.setColor(color);
				g.fillRect(x, y, width, height);
			}
		}
		g.setColor(Color.BLACK);
		g.setFont(font);
		FontMetrics fm   = g.getFontMetrics(g.getFont());
		String name = text;
		java.awt.geom.Rectangle2D bounds = fm.getStringBounds(name, g);
		g.drawString(name, (int)(x+width/2-bounds.getWidth()/2), y+height/2+6);
	}
	public void tick(){}
}