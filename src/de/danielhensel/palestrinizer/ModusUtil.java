package de.danielhensel.palestrinizer;

import java.util.HashMap;

public class ModusUtil 
{
	public static HashMap<String,HashMap<String,Integer>> modi;
	public static HashMap<String,Integer> transposition;
	public static HashMap<String,String> names;
	
	
	private static void putLevels( HashMap<String,Integer> levels, int transpose, String named, String shortnumeric, String numeric)
	{
		modi.put(named, levels);
		modi.put(shortnumeric, levels);
		transposition.put(named, transpose);
		transposition.put(shortnumeric, transpose);
		
		names.put(named, numeric);
		names.put(shortnumeric, numeric);
	}
	
	
	static {
		modi = new HashMap<String,HashMap<String,Integer>>();
		transposition = new HashMap<String,Integer>();
		names = new HashMap<String,String>();

		HashMap<String,Integer> levels;
		
		// ddorian
		levels = new HashMap<String,Integer>();		
		levels.put("d",1);
		levels.put("e",2);
		levels.put("f",3);
		levels.put("f#",3);
		levels.put("g",4);
		levels.put("g#",4);
		levels.put("a",5);
		levels.put("a#",6);
		levels.put("h",6);
		levels.put("c",7);
		levels.put("c#",7);

		putLevels(levels,0,"ddorian","m1","1. Modus");
		
		
		// ahypodorian
		levels = new HashMap<String,Integer>();		
		levels.put("a",1);
		levels.put("a#",2);
		levels.put("h",2);
		levels.put("c",3);
		levels.put("c#",3);
		levels.put("d",4);
		levels.put("e",5);
		levels.put("f",6);
		levels.put("f#",6);
		levels.put("g",7);
		levels.put("g#",7);

		putLevels(levels,0,"ahypodorian","m2","2. Modus");
		
		
		// ephrygian
		levels = new HashMap<String,Integer>();		
		levels.put("e",1);
		levels.put("f",2);
		levels.put("f#",2);
		levels.put("g",3);
		levels.put("g#",3);
		levels.put("a",4);
		levels.put("a#",4);
		levels.put("h",5);
		levels.put("c",6);
		levels.put("c#",6);
		levels.put("d", 7);

		putLevels(levels,0,"ephrygian","m3","3. Modus");

		// hhypophrygian
		levels = new HashMap<String,Integer>();		
		levels.put("h",1);
		levels.put("c",2);
		levels.put("c#",2);
		levels.put("d",3);
		levels.put("e",4);
		levels.put("f",5);
		levels.put("f#",5);
		levels.put("g",6);
		levels.put("g#",6);
		levels.put("a",7);
		levels.put("a#", 7);

		putLevels(levels,0,"hhypophrygian","m4","4. Modus");

	
		// flydian
		levels = new HashMap<String,Integer>();		
		levels.put("f",1);
		levels.put("f#",1);
		levels.put("g",2);
		levels.put("g#",2);
		levels.put("a",3);
		levels.put("a#", 4);
		levels.put("h",4);
		levels.put("c",5);
		levels.put("c#",5);
		levels.put("d",6);
		levels.put("d#",-7);
		levels.put("e",7);

		putLevels(levels,0,"flydian","m5","5. Modus");

	
		// chypolydian
		levels = new HashMap<String,Integer>();		
		levels.put("c",1);
		levels.put("c#",1);
		levels.put("d",2);
		levels.put("d#",-3);
		levels.put("e",3);
		levels.put("f",4);
		levels.put("f#",4);
		levels.put("g",5);
		levels.put("g#",5);
		levels.put("a",6);
		levels.put("a#", 7);
		levels.put("h",7);

		putLevels(levels,0,"chypolydian","m6","6. Modus");

	
		// gmixolydian
		levels = new HashMap<String,Integer>();		
		levels.put("g",1);
		levels.put("g#",1);
		levels.put("a",2);
		levels.put("a#",3);
		levels.put("h",3);
		levels.put("c", 4);
		levels.put("c#",4);
		levels.put("d",5);
		levels.put("e",6);
		levels.put("f",7);
		levels.put("f#",7);

		putLevels(levels,0,"gmixolydian","m7","7. Modus");
	
		// dhypomixolydian
		levels = new HashMap<String,Integer>();		
		levels.put("d",1);
		levels.put("e",2);
		levels.put("f",3);
		levels.put("f#",3);
		levels.put("g",4);
		levels.put("g#",4);
		levels.put("a",5);
		levels.put("a#",6);
		levels.put("h",6);
		levels.put("c", 7);
		levels.put("c#",7);

		putLevels(levels,0,"dhypomixolydian","m8","8. Modus");
	
		// gdorian
		levels = new HashMap<String,Integer>();		
		levels.put("g",1);
		levels.put("a",2);
		levels.put("a#",3);
		levels.put("h",3);
		levels.put("c",4);
		levels.put("c#",4);
		levels.put("d",5);
		levels.put("d#",6);
		levels.put("e",6);
		levels.put("f",7);
		levels.put("f#",7);

		putLevels(levels,7,"gdorian","m1t","1. Modus transpositus");

		
		
		// dhypodorian
		levels = new HashMap<String,Integer>();		
		levels.put("d",1);
		levels.put("d#",2);
		levels.put("e",2);
		levels.put("f",3);
		levels.put("f#",3);
		levels.put("g",4);
		levels.put("a",5);
		levels.put("a#",6);
		levels.put("h",6);
		levels.put("c",7);
		levels.put("c#",7);

		putLevels(levels,7,"dhypodorian","m2t","2. Modus transpositus");
	
		// aphrygian
		levels = new HashMap<String,Integer>();		
		levels.put("a",1);
		levels.put("a#",2);
		levels.put("h",2);
		levels.put("c",3);
		levels.put("c#",3);
		levels.put("d",4);
		levels.put("d#",4);
		levels.put("e",5);
		levels.put("f",6);
		levels.put("f#",6);
		levels.put("g", 7);

		putLevels(levels,7,"aphrygian","m3t","3. Modus transpositus");
	
		// ehypophrygian
		levels = new HashMap<String,Integer>();		
		levels.put("e",1);
		levels.put("f",2);
		levels.put("f#",2);
		levels.put("g",3);
		levels.put("a",4);
		levels.put("a#",5);
		levels.put("h",5);
		levels.put("c",6);
		levels.put("c#",6);
		levels.put("d",7);
		levels.put("d#", 7);

		putLevels(levels,7,"ehypophrygian","m4t","4. Modus transpositus");

	
		// blydian
		levels = new HashMap<String,Integer>();		
		levels.put("a#",1);
		levels.put("h",1);
		levels.put("c",2);
		levels.put("c#",2);
		levels.put("d",3);
		levels.put("d#", 4);
		levels.put("e",4);
		levels.put("f",5);
		levels.put("f#",5);
		levels.put("g",6);
		levels.put("a",7);

		putLevels(levels,7,"blydian","m5t","5. Modus transpositus");

	
		// fhypolydian
		levels = new HashMap<String,Integer>();		
		levels.put("f",1);
		levels.put("f#",1);
		levels.put("g",2);
		levels.put("a",3);
		levels.put("a#",4);
		levels.put("h",4);
		levels.put("c",5);
		levels.put("c#",5);
		levels.put("d",6);
		levels.put("d#", 7);
		levels.put("e",7);

		putLevels(levels,7,"fhypolydian","m6t","6. Modus transpositus");
		
		
	
		//ghypolydian
		levels = new HashMap<String,Integer>();		
		levels.put("g",1);
		levels.put("g#",1);		
		levels.put("a",2);
		levels.put("a#", 3);
		levels.put("h",3);
		levels.put("c",4);
		levels.put("c#",4);
		levels.put("d",5);
		levels.put("d#",5);
		levels.put("e",6);
		levels.put("f",7);
		levels.put("f#",7);
		putLevels(levels,5,"ghypolydian","m6t5","6. Modus transpositus qu");
		
	    
		// cmixolydian
		levels = new HashMap<String,Integer>();		
		levels.put("c",1);
		levels.put("c#",1);		
		levels.put("d",2);
		levels.put("d#", 3);
		levels.put("e",3);
		levels.put("f",4);
		levels.put("f#",4);
		levels.put("g",5);
		levels.put("a",6);
		levels.put("a#",7);
		levels.put("h",7);
		putLevels(levels,7,"cmixolydian","m7t","7. Modus transpositus");
	
		// ghypomixolydian
		levels = new HashMap<String,Integer>();		
		levels.put("g",1);
		levels.put("a",2);
		levels.put("a#",3);
		levels.put("h",3);
		levels.put("c",4);
		levels.put("c#",4);
		levels.put("d",5);
		levels.put("d#", 6);
		levels.put("e",6);
		levels.put("f",7);
		levels.put("f#",7);

		putLevels(levels,7,"ghypomixolydian","m8t","8. Modus transpositus");
	
	}
	
	 
	
	
	
}
