package de.danielhensel.palestrinizer;
import java.util.HashMap;
public class MajorMinorScale {
	
	public static HashMap<String,HashMap<String,Integer>> modi;
	public static HashMap<String,Integer> transposition;
	public static HashMap<String,String> names;
	
	
	private static void putLevels( HashMap<String,Integer> levels, int transpose, String named, String shortnumeric)
	{
		modi.put(named, levels);
		modi.put(shortnumeric, levels);
		transposition.put(named, transpose);
		transposition.put(shortnumeric, transpose);
		
		 
		names.put(named, shortnumeric);
		names.put(shortnumeric, shortnumeric);
	}
	
	
	static {
		modi = new HashMap<String,HashMap<String,Integer>>();
		transposition = new HashMap<String,Integer>();
		names = new HashMap<String,String>();

		HashMap<String,Integer> levels;
		
		 
		
		// C-Major
				levels = new HashMap<String,Integer>();		
				levels.put("c",1);
				levels.put("c#",1);
				levels.put("d",2);
				levels.put("d#",3);
				levels.put("e",3);
				levels.put("f",4);
				levels.put("f#",4);
				levels.put("g",5);
				levels.put("g#",5);
				levels.put("a",6);
				levels.put("a#",6);
				levels.put("h",7);
			 
				putLevels(levels,0,"c-major","ma1");
				
				
         //a-Minor
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
				levels.put("g",7);
				levels.put("g#",7);
				putLevels(levels,3,"a-minor","min0");
				
		// c#-Major
				levels = new HashMap<String,Integer>();		
				
				levels.put("c#",1);
				levels.put("d",2);
				levels.put("d#",2);
				levels.put("e",3);
				levels.put("f",3);
				levels.put("f#",4);
				levels.put("g",4);
				levels.put("g#",5);
				levels.put("a",6);
				levels.put("a#",6);
				levels.put("h",7);
				levels.put("c",7);
				
				putLevels(levels,11,"c#major","ma7#"); 
				
				
         //a#-Minor
				levels = new HashMap<String,Integer>();	
			
				levels.put("a#",1);
				levels.put("h",2);
				levels.put("c",2);
				levels.put("c#",3);
				levels.put("d",3);
				levels.put("d#",4);
				levels.put("e",4);
				levels.put("f",5);
				levels.put("f#",6);
				levels.put("g",6);
				levels.put("g#",7);
				levels.put("a",7);
				putLevels(levels,2,"ais#-minor","min7#"); 
				
				//D-Major
				
		
		
				levels = new HashMap<String,Integer>();		
			
				levels.put("d",1);
				levels.put("d#",1);
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
				putLevels(levels,10,"d-major","ma2"); 
				//h-Minor
				levels = new HashMap<String,Integer>();	
			
				
				levels.put("h",1);
				levels.put("c",2);
				levels.put("c#",2);
				levels.put("d",3);
				levels.put("d#",3);
				levels.put("e",4);
				levels.put("f",4);
				levels.put("f#",5);
				levels.put("g",6);
				levels.put("g#",6);
				levels.put("a",7);
				levels.put("a#",7);
				putLevels(levels,1,"h-minor","min2#"); 
				
				//D#-Major/Es-Major
				
				
				
				levels = new HashMap<String,Integer>();		
			
				
				levels.put("d#",1);
				levels.put("e",1);
				levels.put("f",2);
				levels.put("f#",3);
				levels.put("g",3);
				levels.put("g#",4);
				levels.put("a",4);
				levels.put("a#",5);
				levels.put("h",6);
				levels.put("c",6);
				levels.put("c#",7);
				levels.put("d",7);
				putLevels(levels,9,"d#-major","ma9#"); 
				
				//C-Minor
				levels = new HashMap<String,Integer>();		
				
				levels.put("c",1);
				levels.put("c#",1);
				levels.put("d",2);
				levels.put("d#",3);
				levels.put("e",3);
				levels.put("f",4);
				levels.put("f#",4);
				levels.put("g",5);
				levels.put("g#",5);
				levels.put("a",6);
				levels.put("a#",6);
				levels.put("h",7);
				putLevels(levels,0,"c-minor","min9#"); 
//e-Major
				
				
				
				levels = new HashMap<String,Integer>();		
			
				
				
				levels.put("e",1);
				levels.put("f",1);
				levels.put("f#",2);
				levels.put("g",3);
				levels.put("g#",3);
				levels.put("a",4);
				levels.put("a#",4);
				levels.put("h",5);
				levels.put("c",6);
				levels.put("c#",6);
				levels.put("d",7);
				levels.put("d#",7);
				putLevels(levels,8,"e-major","ma4#"); 
				
				//C#-Minor
				levels = new HashMap<String,Integer>();		
				
				levels.put("c#",1);
				levels.put("d",2);
				levels.put("d#",2);
				levels.put("e",3);
				levels.put("f",3);
				levels.put("f#",4);
				levels.put("g",3);
				levels.put("g#",5);
				levels.put("a",6);
				levels.put("a#",6);
				levels.put("h",7);
				levels.put("c",7);
				putLevels(levels,11,"c#-minor","min4#"); 
				
				//F-Major
                levels = new HashMap<String,Integer>();		
			
				
				levels.put("f",1);
				levels.put("f#",1);
				levels.put("g",2);
				levels.put("g#",3);
				levels.put("a",3);
				levels.put("a#",4);
				levels.put("h",4);
				levels.put("c",6);
				levels.put("c#",5);
				levels.put("d",6);
				levels.put("d#",7);
				levels.put("e",7);
				putLevels(levels,7,"f-major","ma11#"); 
				//d-Minor
				levels = new HashMap<String,Integer>();		
				
				
				levels.put("d",1);
				levels.put("d#",2);
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
				putLevels(levels,10,"d-minor","min11#"); 
				
				//C#-Minor
				levels = new HashMap<String,Integer>();		
				
				levels.put("c#",1);
				levels.put("d",2);
				levels.put("d#",2);
				levels.put("e",3);
				levels.put("f",3);
				levels.put("f#",4);
				levels.put("g",3);
				levels.put("g#",5);
				levels.put("a",6);
				levels.put("a#",6);
				levels.put("h",7);
				levels.put("c",7);
				putLevels(levels,11,"c#-minor","min4#"); 
				
				//Fis-Major
                levels = new HashMap<String,Integer>();		
			
				
				
				levels.put("f#",1);
				levels.put("g",1);
				levels.put("g#",2);
				levels.put("a",3);
				levels.put("a#",3);
				levels.put("h",4);
				levels.put("c",4);
				levels.put("c#",5);
				levels.put("d",6);
				levels.put("d#",6);
				levels.put("e",7);
				levels.put("f",7);
				putLevels(levels,6,"f#-major","ma6#"); 
				
				//dis-Minor
				levels = new HashMap<String,Integer>();		
				
				
				
				levels.put("d#",1);
				levels.put("e",2);
				levels.put("f",2);
				levels.put("f#",3);
				levels.put("g",3);
				levels.put("g#",4);
				levels.put("a",4);
				levels.put("a#",5);
				levels.put("h",6);
				levels.put("c",6);
				levels.put("c#",7);
				levels.put("d",7);
				putLevels(levels,9,"d#-minor","min6#"); 
				
				//G-Major
                levels = new HashMap<String,Integer>();		
			
				
				
				
				levels.put("g",1);
				levels.put("g#",2);
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
				putLevels(levels,5,"g-major","ma1#"); 
				
				//e-Minor
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
				levels.put("d",7);
				levels.put("d#",7);
				putLevels(levels,8,"e-minor","min1#"); 
				
				
				//G#-Major/Ab-Major
                levels = new HashMap<String,Integer>();		
			
				
				levels.put("g#",1);
				levels.put("a",2);
				levels.put("a#",2);
				levels.put("h",3);
				levels.put("c",3);
				levels.put("c#",4);
				levels.put("d",4);
				levels.put("d#",5);
				levels.put("e",6);
				levels.put("f",6);
				levels.put("f#",7);
				levels.put("g",7);
				putLevels(levels,4,"g#-major","ma8#"); 
				
				//f-Minor
				levels = new HashMap<String,Integer>();		
				
				
				levels.put("f",1);
				levels.put("f#",2);
				levels.put("g",2);
				levels.put("g#",3);
				levels.put("a",3);
				levels.put("a#",4);
				levels.put("h",4);
				levels.put("c",5);
				levels.put("c#",6);
				levels.put("d",6);
				levels.put("d#",7);
				levels.put("e",7);
				putLevels(levels,7,"f-minor","min8#"); 
				
				
				//A-Major 
                levels = new HashMap<String,Integer>();		

				
				levels.put("a",1);
				levels.put("a#",1);
				levels.put("h",2);
				levels.put("c",3);
				levels.put("c#",3);
				levels.put("d",4);
				levels.put("d#",4);
				levels.put("e",5);
				levels.put("f",6);
				levels.put("f#",6);
				levels.put("g",7);
				levels.put("g#",7);
				putLevels(levels,3,"a-major","ma3#"); 
				
				//f#-Minor
				levels = new HashMap<String,Integer>();		
				
				
				
				levels.put("f#",1);
				levels.put("g",2);
				levels.put("g#",2);
				levels.put("a",3);
				levels.put("a#",3);
				levels.put("h",4);
				levels.put("c",4);
				levels.put("c#",5);
				levels.put("d",6);
				levels.put("d#",6);
				levels.put("e",7);
				levels.put("f",7);
				putLevels(levels,6,"f#-minor","min3#"); 
				
				//A#-Major /B-Major
                levels = new HashMap<String,Integer>();		

				
				levels.put("a#",1);
				levels.put("h",1);
				levels.put("c",2);
				levels.put("c#",3);
				levels.put("d",3);
				levels.put("d#",4);
				levels.put("e",4);
				levels.put("f",5);
				levels.put("f#",6);
				levels.put("g",6);
				levels.put("g#",7);
				levels.put("a",7);
				putLevels(levels,2,"a#-major","ma10#"); 
				
				//g-Minor
				levels = new HashMap<String,Integer>();		
				
				
				
				
				levels.put("g",1);
				levels.put("g#",2);
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
				putLevels(levels,5,"g-minor","min10#"); 
				
				//B#-Major
                levels = new HashMap<String,Integer>();		

				
				
				levels.put("h",1);
				levels.put("c",1);
				levels.put("c#",2);
				levels.put("d",3);
				levels.put("d#",3);
				levels.put("e",4);
				levels.put("f",4);
				levels.put("f#",5);
				levels.put("g",6);
				levels.put("g#",6);
				levels.put("a",7);
				levels.put("a#",7);
				putLevels(levels,1,"b#-major","ma5#"); 
				
				//g#-Minor
				levels = new HashMap<String,Integer>();		
				
				
				
				
			
				levels.put("g#",1);
				levels.put("a",2);
				levels.put("a#",2);
				levels.put("h",3);
				levels.put("c",3);
				levels.put("c#",4);
				levels.put("d",4);
				levels.put("d#",5);
				levels.put("e",6);
				levels.put("f",6);
				levels.put("f#",7);
				levels.put("g",7);
				putLevels(levels,5,"g#-minor","min5#"); 

				//chromatic
				levels = new HashMap<String,Integer>();		
				levels.put("c",1);
				levels.put("c#",2);
				levels.put("d",3);
				levels.put("d#",4);
				levels.put("e",5);
				levels.put("f",6);
				levels.put("f#",7);
				levels.put("g",8);
				levels.put("g#",9);
				levels.put("a",10);
				levels.put("a#",11);
				levels.put("h",12);
				putLevels(levels,0,"chromatic","chrom"); 
				
				//Ganzton-1
				levels = new HashMap<String,Integer>();		
				levels.put("c",1);
				
				levels.put("d",2);
				
				levels.put("e",3);
				
				levels.put("f#",4);
				
				levels.put("g#",5);
				
				levels.put("a#",6);
			
				putLevels(levels,0,"whole","whole1"); 
				
				
				//Ganzton-2
				levels.put("c#",1);
				
				levels.put("d#",2);
				
				levels.put("f",3);
				
				levels.put("g",4);
				
				levels.put("a",5);
				
				levels.put("b",6);
			
				putLevels(levels,11,"whole","whole2");
				
				//accoustic
				levels = new HashMap<String,Integer>();		
				
				levels = new HashMap<String,Integer>();		
				levels.put("c",1);
				levels.put("d",2);
				levels.put("e",3);
				levels.put("f#",4);
				levels.put("g",5);		
				levels.put("a",6);
				levels.put("a#",7);
				
				putLevels(levels,27,"accoustic","acc"); 
				//Hier ist die Solmisation wichtiger als die Stufenzahl!
				//c-do-pentatonic
				levels = new HashMap<String,Integer>();		
				levels.put("c",1);
				levels.put("d",2);
				levels.put("e",3);
				levels.put("g",4);
				levels.put("a",5);
				putLevels(levels,28,"c-do-pent","c-do-pent");
				
				//c-La-Pent
				levels = new HashMap<String,Integer>();		
				levels.put("c",1);
				levels.put("d#",2);
				levels.put("f",3);
				levels.put("g",4);
				levels.put("a#",5);
				putLevels(levels,0,"c-la-pent","c-la-pent");
				
				//C-So-Pent
				
				levels = new HashMap<String,Integer>();		
				levels.put("c",1);
				levels.put("d",2);
				levels.put("f",3);
				levels.put("g",4);
				levels.put("a",5);
				putLevels(levels,0,"c-so-pent","c-so-pent");
				
//C-Mi-Pent
				
				levels = new HashMap<String,Integer>();		
				levels.put("c",1);
				levels.put("d#",2);
				levels.put("f",3);
				levels.put("g#",4);
				levels.put("a#",5);
				putLevels(levels,0,"c-mi-pent","c-mi-pent");
				
//C-Re-Pent
				
				levels = new HashMap<String,Integer>();		
				levels.put("c",1);
				levels.put("d",2);
				levels.put("f",3);
				levels.put("g",4);
				levels.put("a#",5);
				putLevels(levels,0,"c-re-pent","c-re-pent");
				
	}
				

}
