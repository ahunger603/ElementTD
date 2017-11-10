package TowerDefence;

import java.util.Random;

public class DamagePacket 
{
	public static Random rng = new Random();
	
	public int basicDam;
	public int fireDam;
	public int natureDam;
	public int lightDam;
	public int darknessDam;
	public int arcaneDam;
	public int mechanicalDam;
	public int electricityDam;
	public int waterDam;
	
	public DamagePacket(int basicDam, int fireDam, int natureDam, int lightDam, int darknessDam, int arcaneDam, int mechanicalDam, int electricityDam, int waterDam)
	{
		this.basicDam = basicDam;
		this.fireDam = fireDam;
		this.natureDam = natureDam;
		this.lightDam = lightDam;
		this.darknessDam = darknessDam;
		this.arcaneDam = arcaneDam;
		this.mechanicalDam = mechanicalDam;
		this.electricityDam = electricityDam;
		this.waterDam = waterDam;
	}
	
	public DamagePacket(int[][] min_max)
	{
		if (min_max.length == 9)
		{
			for(int i = 0; i < 9; i++)
			{
				int[] min_max_bit = min_max[i];
				switch(i)
				{
				case 0:
					if (min_max_bit == null)
						basicDam = 0;
					else
						basicDam = rng.nextInt(min_max_bit[1] - min_max_bit[0]) + min_max_bit[0];
					break;
				case 1:
					if (min_max_bit == null)
						fireDam = 0;
					else
						fireDam = rng.nextInt(min_max_bit[1] - min_max_bit[0]) + min_max_bit[0];
					break;
				case 2:
					if (min_max_bit == null)
						natureDam = 0;
					else
						natureDam = rng.nextInt(min_max_bit[1] - min_max_bit[0]) + min_max_bit[0];
					break;
				case 3:
					if (min_max_bit == null)
						lightDam = 0;
					else
						lightDam = rng.nextInt(min_max_bit[1] - min_max_bit[0]) + min_max_bit[0];
					break;
				case 4:
					if (min_max_bit == null)
						darknessDam = 0;
					else
						darknessDam = rng.nextInt(min_max_bit[1] - min_max_bit[0]) + min_max_bit[0];
					break;
				case 5:
					if (min_max_bit == null)
						arcaneDam = 0;
					else
						arcaneDam = rng.nextInt(min_max_bit[1] - min_max_bit[0]) + min_max_bit[0];
					break;
				case 6:
					if (min_max_bit == null)
						mechanicalDam = 0;
					else
						mechanicalDam = rng.nextInt(min_max_bit[1] - min_max_bit[0]) + min_max_bit[0];
					break;
				case 7:
					if (min_max_bit == null)
						electricityDam = 0;
					else
						electricityDam = rng.nextInt(min_max_bit[1] - min_max_bit[0]) + min_max_bit[0];
					break;
				case 8:
					if (min_max_bit == null)
						waterDam = 0;
					else
						waterDam = rng.nextInt(min_max_bit[1] - min_max_bit[0]) + min_max_bit[0];
					break;
				}
			}
		}
		else
		{
			this.basicDam = 0;
			this.fireDam = 0;
			this.natureDam = 0;
			this.lightDam = 0;
			this.darknessDam = 0;
			this.arcaneDam = 0;
			this.mechanicalDam = 0;
			this.electricityDam = 0;
			this.waterDam = 0;
			System.out.println("INVALID MIN_MAX");
		}
	}
}
