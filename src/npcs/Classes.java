package npcs;

public enum Classes {
	ARCHER("Archer"),
	SPECIALIST("Specialist"),
	WARRIOR("Warrior");
	
	private final String name;
	Classes(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
}
