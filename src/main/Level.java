package main;
import java.util.ArrayList;

import npcs.Classes;
import npcs.Dude;
public class Level {
	ArrayList<Dude> dudes = new ArrayList<Dude>();
	
	public Level(String packet)
	{
		try
		{
			String[] ds = packet.split(",");
			for (int i = 0; i< ds.length;i++)
			{
				Dude dude = new Dude(i, -1);
				String[] by = ds[i].split("/");
				if (Integer.parseInt(by[1]) == 1)
				{
					dude.setType(Classes.ARCHER);
				}
				String[] att = by[0].split(" ");
				int st = Integer.parseInt(att[0]);
				int in = Integer.parseInt(att[1]);
				int sp = Integer.parseInt(att[2]);
				dude.setAttributes(st, in, sp);
				dudes.add(dude);
			}
		}
		catch(NumberFormatException e)
		{
			System.err.println("Idiot. You set the dude's attributes to zero. Can't divide by that");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Invalid Levels");
		}
	}
	public ArrayList<Dude> getDudes()
	{
		ArrayList<Dude> clone = new ArrayList<Dude>();
		for (int i = 0; i < dudes.size(); i++)		//gets clone array of dudes instead of passing on a reference (copy constructor)
		{
			clone.add(new Dude(dudes.get(i)));
		}
		return clone;
	}
}
