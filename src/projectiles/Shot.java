package projectiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Shot extends Rectangle{
	int direction;
	int damage = 25;
	Color color = Color.BLACK;
	public Shot(int x, int y, int direction)
	{
		this.direction = direction;
		this.x = x;
		this.y = y;
		this.width = 20;
		this.height = 2;
	}
	public void setColor(Color c)
	{
		this.color = c;
	}
	public int getDirection()
	{
		return direction;
	}
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
	public int getDamage()
	{
		return damage;
	}
	public void tick()
	{
		this.x += 4;
	}
	public void render(Graphics g)
	{
		g.setColor(color);
		g.fillRect(x-width/2, y-height/2, width, height);
	}
}
