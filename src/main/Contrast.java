package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import gui.*;

public class Contrast extends Canvas implements Runnable{
	
	boolean running = false;
	ContrastWindow window;
	Game game;
	public static int WIDTH;
	public static int HEIGHT;
	public Contrast()
	{
		window = new ContrastWindow("Contrast");
		window.add(this);
		this.setSize(new Dimension(window.getContentPane().getWidth(), window.getContentPane().getHeight()));
		game = new Game(this, window);
		game.load(Locations.MAINMENU);
		Thread t = new Thread(this);
		running = true;
		t.start();
		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e) 
			{
				int x = e.getX();
				int y = e.getY();
				game.checkPress(x, y);
			}
		});
	}
	public static void main(String[] args)
	{
		new Contrast();
	}
	public void run() 
	{
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("FPS: "+frames+" TICKS: "+updates);
				frames = 0;
				updates = 0;
			}
		}
	}
	public void render()
	{
		WIDTH = getWidth();
		HEIGHT = getHeight();
		BufferStrategy bs = getBufferStrategy();
		if (bs == null)
		{
			createBufferStrategy(2);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		game.render(g);
		g.dispose();
		bs.show();
	}
	public void tick()
	{
		game.tick();
	}
}
