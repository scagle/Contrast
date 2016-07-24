package main;

import gui.ContrastWindow;

import java.awt.Graphics;
import java.util.ArrayList;

import projectiles.Shot;
import npcs.Classes;
import npcs.Dude;

public class ArenaHandler {
	Game game;
	public ArrayList<Dude> dudes;
	public ArrayList<Dude> enemydudes = new ArrayList<Dude>();
	public ArrayList<Shot> shots = new ArrayList<Shot>();
	static ArenaHandler arenaHandler;
	public ArenaHandler(Game game, ArrayList<Dude> dudes)
	{
		arenaHandler = this;
		this.game = game;
		this.dudes = dudes;
		this.enemydudes = new ArrayList<Dude>();
		this.shots = new ArrayList<Shot>();
		init();
	}
	public void addEnemy(Dude enemy)
	{
		enemydudes.add(enemy);
		// LEFT OFF HERE!
		for (int i = 0; i < enemydudes.size(); i++)
		{
			enemydudes.get(i).start(game.width-(5*i + 50*i));
		}
	}
	public static ArenaHandler getInstance()
	{
		return arenaHandler;
	}
	public void init()
	{
		for (int i = 0; i < dudes.size(); i++)
		{
			dudes.get(i).start(5*i + 50*i);
		}
		System.out.println("Initialized "+dudes.size()+ " dudes.");
	}
	public Dude checkCollision(Dude friendly)
	{
		for (int i = 0; i < enemydudes.size(); i++)
		{
			if (friendly.intersects(enemydudes.get(i)) && !friendly.isDying() && !enemydudes.get(i).isDying())
			{
				
				return enemydudes.get(i);
			}
		}
		
		return null;
	}
	public Dude checkShotCollision(Shot shot)
	{
		if (shot.getDirection() == 1)
		{
			for (int i = 0; i < enemydudes.size(); i++)
			{
				if (shot.intersects(enemydudes.get(i)))
				{
					return enemydudes.get(i);
				}
			}
		}
		else
		{
			for (int i = 0; i < dudes.size(); i++)
			{
				if (shot.intersects(dudes.get(i)))
				{
					return dudes.get(i);
				}
			}
		}
		return null;
	}
	public void tick()
	{
		int friendlyAlive = 0;
		int enemyAlive = 0;
		for (int i = 0; i < dudes.size(); i++)
		{
			if (dudes.get(i).isAlive())
			{
				Dude friendly = dudes.get(i);
				if (friendly.isShowing())
				{
					Dude foe = null;
					if (!friendly.isKnockedBack())
						foe = checkCollision(friendly);
					if (foe != null)
					{
						if (foe.isShowing())    //Added this to get rid of ghost collisions (6/28/2016)
						{
							if (friendly.getType() != Classes.ARCHER)
							{
								foe.knockback(friendly.getPower());
								foe.hit(friendly.getDamage());
							}
							friendly.knockback(foe.getPower());
							friendly.hit(foe.getDamage());
						}
					}
				}
				friendly.tick();
				friendlyAlive++;
			}
		}
		for (int i = 0; i < enemydudes.size(); i++)
		{
			if (enemydudes.get(i).isShowing())
			{
				enemydudes.get(i).tick();
				enemyAlive++;
			}
		}
		for (int i = 0; i < shots.size(); i++)
		{
			Dude foe = checkShotCollision(shots.get(i));
			if (foe != null && foe.isShowing())
			{
				foe.hit(shots.get(i).getDamage());
				foe.knockback(10);
				shots.remove(i);
			}
			else
			{
				shots.get(i).tick();
			}
		}
		if (enemydudes != null && enemydudes.get(0) != null)
		{
			if (friendlyAlive == 0)
			{
				game.endBattle(0);
			}
			if (enemyAlive == 0)
			{
				Levels.advance();
				game.endBattle(1);
			}
		}
	}
	public void render(Graphics g)
	{
		for (int i = 0; i < shots.size(); i++)
		{
			shots.get(i).render(g);
		}
		for (int i = 0; i < dudes.size(); i++)
		{
			if (dudes.get(i).isShowing())
				dudes.get(i).render(g);
		}
		for (int i = 0; i < enemydudes.size(); i++)
		{
			if (enemydudes.get(i).isShowing())
			{
				enemydudes.get(i).render(g);
			}
		}
	}
}
