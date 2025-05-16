package de.danielhensel.palestrinizer;

public class NoteUtils 
{
	public static String[] intervalnames = {
		" 1p",
		" 2-",
		" 2+",
		" 3-",
		" 3+",
		" 4p",
		" 4a",
		" 5p",
		" 6-",
		" 6+",
		" 7-",
		" 7+",
		" 8p",
		" 9-",
		" 9+",
		"10-",
		"10+",
		"11p",
		"11a",
		"12p"
	};
	
	public static String[] keytoneprogression = {
		"Prime",
		"Second",
		"Second",
		"Third",
		"Third",
		"Fourth",
		"Fourth",
		"Fifth",
		"Sixth",
		"Sixth",
		"Seventh",
		"Seventh",
		"Octave",
		"Ninth",
		"Ninth",
		"Tenth",
		"Tenth",
		"Eleventh",
		"Eleventh",
		"Twelvth"
	};
	
	

	public static String deltaToInterval(int delta)
	{
		if (delta < 0) return "???";
		
		if (delta >= intervalnames.length) return ("+" + delta);
		
		return intervalnames[delta];
	}
	
	
	public static String[] notenames = {"c","c#","d","d#","e","f","f#","g","g#","a", "a#","h"};
	public static String[] solmisation = {"do","di","re","mib","mi","fa","fi","so","si","la","ta","ti"};
	
	public static String pitchToString(int note)
	{
		if (note < 0) return "";
		return notenames[note % 12];
	}
	
	public static String pitchToSolmisation(int note, int transpose)
	{
		if (note < 0) return "";
		return solmisation[(note + transpose) % 12];
		
	}
	
	public static String noteToString(int note)
	{
		if (note < 0) return "";
		return notenames[note % 12] + ((int) Math.floor(note / 12) -2);
	}

	public static String levelToString(int level)
	{
		String res = Integer.toString(Math.abs(level));
		if (level < 0)
		{
			res += "b";
		}
		return res;
	}
	

	
	 public static long determineLCM(long  a, long b) 
	 {
		 long num1, num2;
         if (a > b) {
                 num1 = a;
                 num2 = b;
         } else {
                 num1 = b;
                 num2 = a;
         }
         for (long i = 1; i <= num2; i++) {
                 if ((num1 * i) % num2 == 0) {
                         return i * num1;
                 }
         }
         throw new Error("Error");
	 }

	 
	 	 
	 
	 
	
}
