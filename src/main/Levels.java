package main;

import java.util.ArrayList;

import npcs.Dude;

public class Levels {
	private static ArrayList<Level> levels = new ArrayList<Level>();
	private static int progression = 1;
	private static int currentLevel = 1;
	public Levels()
	{
		levels.add(new Level("1 1 1/0"));
		levels.add(new Level("1 1 100/0"));
		levels.add(new Level("25 1 100/0"));
		levels.add(new Level("50 1 100/0"));
	}
	public static ArrayList<Dude> getLevel(int level)
	{
		ArrayList<Dude> dudes = null;
		if (levels.size() >= level)
		{
			dudes = levels.get(level-1).getDudes();
		}
		else
		{
			System.out.println("No more levels");
		}
		return dudes;
	}
	public static int getSize()
	{
		return levels.size();
	}
	public static int getProgression()
	{
		return progression;
	}
	public static int getCurrentLevel()
	{
		return currentLevel;
	}
	public static void setCurrentLevel(int level)
	{
		currentLevel = level;
	}
	public static void advance()
	{
		progression++;
	}
}
