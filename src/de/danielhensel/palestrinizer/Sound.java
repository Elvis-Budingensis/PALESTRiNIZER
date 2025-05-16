package de.danielhensel.palestrinizer;


import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;


public class Sound 
{
	
	public int basstone = -1;
	public int keytone = -1;
	public int inversion = -1;
	public String empty_subtype = "";
	
	public int[] sourcenotes;
	public int[] cleannotes;
	public int[] ananotes;
	
	public int[] intervals;

	private int timestamp = 0;
	public boolean wide = false;
	public int numdistinctnotes = 0;
	
	public SoundForm form = SoundForm.UNDETERMINED;
	public SoundRelation relation = SoundRelation.UNDETERMINED;
	
	public double dgrade = 0;
	public double sgrade = 0;
	
	//private double FRQPOW = 1.0594630943592952645618252949463;
	private static HashMap<SoundForm,int[]> forms = new HashMap<SoundForm,int[]>();
	
	static {
		forms.put(SoundForm.MAJOR, new int[]{4,3,5} );
		forms.put(SoundForm.MINOR, new int[]{3,4,5} );
		forms.put(SoundForm.DIMINISHED, new int[]{3,3,6} );
		forms.put(SoundForm.AUGMENTED, new int[]{4,4,4} );
		forms.put(SoundForm.DOMINANTSEVENTH, new int[]{4,3,3,2} );
		forms.put(SoundForm.HALFDIMINISHED, new int[]{3,3,4,2} );
		forms.put(SoundForm.DOUBLEDIMINISHED, new int[]{3,3,3,3});
		forms.put(SoundForm.FOURTHCHORDS, new int[]{5, 5});
	}

	private static int[] dissonances =    {        1,       2,       5,        6,      10,      11};
	private static double[] partials =    {16.0/15.0, 9.0/8.0, 4.0/3.0, 11.0/8.0,14.0/8.0,15.0/8.0};	
	private static double[] subjgrades =  {        5,       3,       1,        4,       2,       6};	
	
	//---------------------------------------------------------------------------------------------
	// klang erkennen
	//
	// akzeptiert eine notenliste aus dem grid und gibt den dreiklang oder null zur�ck
	//---------------------------------------------------------------------------------------------
	public static Sound createFromNotes(int[] notes, int timestamp)
	{
		if (notes == null) throw new IllegalArgumentException("argument \"notes\" can not be null");

		/* zum testen: 
		notes[0] = -1;
		notes[1] = 60;
		notes[2] = 53;
		notes[3] = 65;
		notes[4] = 69;
		*/
		
		Sound r = new Sound();
		r.timestamp = timestamp;
		r.sourcenotes = notes;
		r.analyze();
		return r;
	}
	
	

	private void analyze()
	{
		numdistinctnotes = numNotes();

		cleannotes = Sound.stripSilence(sourcenotes);
		Sound.sortNotes(cleannotes);
		ananotes = Sound.cleanUpNotes(cleannotes);
		intervals = Sound.extractIntervals(ananotes);

		// erster fall: gar keine noten an dieser stelle
		if (cleannotes.length == 0)
		{
			form = SoundForm.NOTHING;
			return;
		}
		

		// zweiter fall: eine einzelne note an dieser stelle
		if (cleannotes.length == 1)
		{
			form = SoundForm.SINGLENOTE;
			basstone = cleannotes[0];
			keytone = cleannotes[0];
			return;
		}
		
		// dritter fall: zwei noten
		if (cleannotes.length == 2)
		{
			form = SoundForm.SINGLEINTERVALL;
			basstone = cleannotes[0];
			keytone = cleannotes[0];
			intervals = new int[1];
			intervals[0] = cleannotes[1]-cleannotes[0];
			if (intervals[0] < 0) intervals[0] += 12;
			decideRelation();
			return;
		} 
		
		
		
		// vierter fall: leer klang!?
		if (decideEmptyChord())
		{
			return;
		} 
		
		// sechster fall: drei oder vierklang, evtl. in umkehrung
		for (SoundForm form: forms.keySet())
		{
			if (decideForm(form)) break;
		}
		decideRelation();
		return;
	}
	
	
	public boolean decideEmptyChord()
	{
		if (isEmptyChord(0)) return true;
		if (isEmptyChord(3)) return true;
		if (isEmptyChord(4)) return true;
		if (isEmptyChord(5)) return true;
		if (isEmptyChord(7)) return true;
		if (isEmptyChord(8)) return true;
		if (isEmptyChord(9)) return true;
		return false;
	}
	
	
	public boolean isEmptyChord(int lowerinterval)
	{
		int allowstep = lowerinterval;
		int firststep = -1;
		int secondstep = -1;
		for (int i = 0; i < cleannotes.length-1; i++)
		{
			int step = cleannotes[i+1]-cleannotes[i];
			
			if (i == 0) firststep = (step % 12);
			if (i == 1) secondstep = (step % 12);
			
			if ((step % 12) == 0) continue;
			if (step == allowstep) 
			{
				allowstep = 12-allowstep;
				continue;
			}
			
			return false;
		}
		
		if ((lowerinterval == 0) || (lowerinterval == 5) || (lowerinterval == 7))
		{
			form = SoundForm.THIRDLESSCHORD;
		} else {
			form = SoundForm.FIFTHLESSCHORD;
		}
		
		switch (lowerinterval)
		{
		case 3:
			relation = SoundRelation.CONSONANT;
			break;
		case 4:
			relation = SoundRelation.CONSONANT;
			break;
		case 5:
			relation = SoundRelation.DISSONANT;
			break;
		case 7:
			relation = SoundRelation.CONSONANT;
			break;
		case 8:
			relation = SoundRelation.CONSONANT;
			break;
		case 9:
			relation = SoundRelation.CONSONANT;
			break;
		default:
			relation = SoundRelation.CONSONANT;
		}

		intervals = new int[2];
		intervals[0] = (firststep + 10*12) % 12;
		if (intervals[0] == 0) intervals[0] = 12;
		intervals[1] = (secondstep + 10*12) % 12;
		if (intervals[1] == 0) intervals[1] = 12;
		basstone = cleannotes[0];
		keytone = cleannotes[0];
		return true;
	}
	

	
	
	
	
	public void decideRelation()
	{
		dgrade = 0;
		
		if (cleannotes.length <= 1) return;

		if ((form == SoundForm.MAJOR) && (inversion == 2)) 
		{
			relation = SoundRelation.DISSONANT;
			return;
		}
		if ((form == SoundForm.MINOR) && (inversion == 2)) 
		{
			relation = SoundRelation.DISSONANT; 
			return;
		}
		if (form == SoundForm.AUGMENTED) 
		{
			relation = SoundRelation.DISSONANT; 
			return;
		}
		if (form == SoundForm.DIMINISHED) 
		{
			relation = SoundRelation.DISSONANT; 
			return;
		}
		if (form == SoundForm.FOURTHCHORDS) 
		{
			relation = SoundRelation.DISSONANT; 
			return;
		}
		
		
		double sum_dgrades = 0.0;
		double sum_sgrades = 0.0;
		int num_dgrades = 0;
		
		// es werden alle intervallschritte von unten nach oben betrachtet
		for (int i = 0; i < cleannotes.length-1; i++)
		{
			for (int j = i+1; j < cleannotes.length; j++)
			{
				int delta = (cleannotes[j] - cleannotes[i]) % 12;
				
				for (int k = 0; k < dissonances.length; k++)
				{
					if (delta == dissonances[k])
					{
						// sonderfall: quarte ( 5hts ) gilt nur als dissonant
						// sofern sie vom untersten ton ausgeht
			
						if (delta == 5)
						{
							if (i == 0)
							{
								relation = SoundRelation.DISSONANT;
								
								sum_dgrades += partials[k];
								sum_sgrades += subjgrades[k];
								num_dgrades++;
								break;
							}
							
						} else {
							relation = SoundRelation.DISSONANT;
							sum_dgrades += partials[k];
							sum_sgrades += subjgrades[k];
							num_dgrades++;
							break;
						}
						
					}
				}
				
				if (relation == SoundRelation.DISSONANT) break; 
			}
			if (relation == SoundRelation.DISSONANT) break; 
		}
		
		if (num_dgrades > 0)
		{
			sum_dgrades /= num_dgrades;
			sum_sgrades /= num_dgrades;
		} else {
			sum_dgrades = 0;
			sum_sgrades = 0;
		}
		this.dgrade = sum_dgrades; //sum_dgrades;
		this.sgrade = sum_sgrades; //sum_dgrades;
		
		if (relation != SoundRelation.DISSONANT)
			relation = SoundRelation.CONSONANT;
	}
	
	
	//---------------------------------------------------------------------------------------------
	
	
	
	//---------------------------------------------------------------------------------------------
	// strip silent voices from the list of notes
	//---------------------------------------------------------------------------------------------
	private static int[] stripSilence(int[] notes)
	{
		int count = notes.length;
		int[] result = new int[count];
		
		int counter = 0;
		for (int i = 0; i < count; i++)
		{
			// keine note in dieser stimme? überspringen!
			if (notes[i] == -1) continue;
			result[counter] = notes[i];
			counter++;
		}
		
		if (result.length == counter) return result;
		return Arrays.copyOf(result, counter);
	}


	private static void sortNotes(int[] notes)
	{
		int count = notes.length;
		boolean hadtosort;

		do {
			hadtosort = false;
			for (int i = 0; i < count-1; i++)
			{
				if (notes[i] > notes[i+1])
				{
					int tmp = notes[i+1];
					notes[i+1] = notes[i];
					notes[i] = tmp;
					
					hadtosort = true;
				}
			}
			
		} while (hadtosort); 
		
	}
	
	//---------------------------------------------------------------------------------------------
	// notenliste normalisieren
	//
	// die methode erwartet eine vorsortierte liste mit noten (=> eine spalte aus dem grid)
	// hier können noten fehlen (-1), die mössen entfernt werden
	// obendrein nehmen wir oktaven und gleiche töne heraus, die stören bei der klangbetrachtung nur
	//---------------------------------------------------------------------------------------------
	private static int[] cleanUpNotes(int[] notes)
	{
		int count = notes.length;
		int[] result = new int[count];
		
		int counter = 0;
		int last = -1;
		for (int i = 0; i < count; i++)
		{
			int curnote = notes[i];
			// keine note in dieser stimme? überspringen!
			if (curnote == -1) continue;

			// wir betrachten gleich die intervalle, das geht erst ab der zweiten note
			if (last != -1)
			{
				// stimmkreuzungen stören bei der betrachtung. aufheben
				while (curnote < last) curnote += 12;
				// oktave oder gleicher ton? �berspringen!
				if (((curnote-last) % 12) == 0) continue;
			}
			last = curnote;
			result[counter] = curnote;
			counter++;
		}
		
		if (result.length == counter) return result;
		return Arrays.copyOf(result, counter);
		
	}
	//---------------------------------------------------------------------------------------------

	
	
	
	//---------------------------------------------------------------------------------------------
	// intervalle ermitteln
	//
	// aus der normalisierten notenliste werden die intervalle erstellt 
	//---------------------------------------------------------------------------------------------
	private static int[] extractIntervals(int[] notes)
	{
		if (notes.length < 2) return new int[0];
		
		int[] intervals = new int[notes.length-1];

		for (int i = 1; i < notes.length; i++)
		{
			int intervall = (notes[i]-notes[i-1]) % 12;
			if (intervall < 0) intervall += 12;
			intervals[i-1] = intervall;
		}
		
		return intervals;
	}
	//---------------------------------------------------------------------------------------------

	
	
	

	
	
	//---------------------------------------------------------------------------------------------
	// prüft eine intervallfolge gegen eine bestimmte klangform 
	//---------------------------------------------------------------------------------------------
	private boolean decideForm(SoundForm formtotest)
	{
		if (ananotes.length < 3) return false; //wenigstens drei noten ben�tigen wir f�r die betrachtung  
		
		int[] f = forms.get(formtotest);
	
		
		if (f.length != numdistinctnotes) return false;
		
		int[] counter = new int[f.length];
		for (int i = 0; i < f.length; i++) counter[i] = 0;
		
		int first_interval = intervals[0];
		
		// einen startpunkt in meiner form finden. 
		// den letzten lasse ich aus, der f�llt nur bis zur n�chsten oktave auf
		int starting_point = -1;
		int _inversion = 0;
		boolean _wide = false;
		
		for (int i = 0; i < f.length; i++)
		{
			if (i != (f.length-1))
			{
				if (f[i] == first_interval)
				{
					_inversion = i;
					starting_point = i;
					_wide = false;
					break;
				}
			}

			
			int carry = f[i];
			
			// auslassungen erlauben:
			for (int j = 0; j < f.length; j++)
			{
				int k = ((i+1+j) % f.length);
				carry += f[k];
				if (carry == first_interval)
				{
					_inversion = i;
					starting_point = k;
					_wide = false; //true;
					break;
				}
			}
			
		}
		
		if (starting_point == -1) return false; // passt nicht

		boolean compare_ok = false;
		int checkpoint = starting_point;
		counter[starting_point] = 1;
		for (int i = 1; i < intervals.length; i++)
		{
			int interval = intervals[i];

			// gleicher ton oder nur oktavunterschied: nicht mitpr�fen
			if ((interval % 12)== 0) continue;

			
			compare_ok = false; // steuervariable, wird die nach der n�chsten schleife false, passt der name nicht.
			int compare = 0;
			
			// mit der dreiklangform vergleichen, auslassungen erlauben 
			for (int j = 1; j < 10; j++)
			{
				int fi = (checkpoint+j) % f.length;
				compare += f[fi];
				
				if (compare == interval)
				{
					// passt, weiter
					compare_ok = true;
					checkpoint += j;
					counter[fi] = 1;
					break;
				}
				
				if (j == 1)
				{
					// der erste intervallschritt muss auf alle f�lle da sein (z.b. "terz im bass")
					//break;
				}
				
				if (interval < compare)
				{
					// aus dem pr�fbereich heraus, abbruch
					break;
				}
			}
			
			if (!compare_ok) break; 
		}
		

		

		if (compare_ok)
		{

			form = formtotest;
			inversion = _inversion;
			basstone = ananotes[0];
			keytone = basstone;
			wide = _wide;
			
			// grundton bestimmen (muss nicht teil der notenfolge sein)
			for (int i = inversion; i < f.length; i++)
			{
				int x = (i) % f.length;
				keytone += f[x];
			}
			
			return true;
		}

		return false;
		
	}
	//---------------------------------------------------------------------------------------------

	
	
	
	public String toString()
	{
		String s = "";
		
		
		switch (form)
		{
		case UNDETERMINED:
			for (int i = 0; i < intervals.length; i++)
			{
				if (!s.equals("")) s+= " ";
				s += NoteUtils.deltaToInterval(intervals[i]);
			}
			s += " " + relation.toString();
			break;
		case NOTHING:
			s = "-";
			break;
		case SINGLENOTE:
			s += form.toString();
			break;
		case SINGLEINTERVALL:
			s += "singleinterval " + NoteUtils.deltaToInterval(intervals[0]) + " " + relation.toString();
			break;
		case THIRDLESSCHORD:
		case FIFTHLESSCHORD:
			s += form.toString();
			s += " " + intervals[0] + "/" + intervals[1] + " " + relation.toString();
			break;
		default:
			s += form.toString() + " " + relation.toString();
			if (inversion > 0) s += " (" + inversion + ". inversion)";
			if (wide) s += " (weite lage)";
			s += " basstone: " + NoteUtils.pitchToString(basstone);
			s += " keytone: " + NoteUtils.pitchToString(keytone);
			break;
		}
		
		
		return s;
	}
	
	
	private int numNotes()
	{
		
		HashSet<Integer> notemap = new HashSet<Integer>();
		
		for (int i = 0; i < sourcenotes.length; i++)
		{
			int v = sourcenotes[i];
			if (v == -1) continue;
		
			v = v % 12;
			if (!notemap.contains(v)) notemap.add(v);
		}
		
		return (notemap.size());
	
	}

	
	
	public String toShortString()
	{
		String s = "";
		
		
		switch (form)
		{
		case UNDETERMINED:
			for (int i = 0; i < intervals.length; i++)
			{
				if (!s.equals("")) s+= " ";
				s += NoteUtils.deltaToInterval(intervals[i]);
			}
			s += " " + relation.toString();
			break;
		case NOTHING:
			s = "-";
			break;
		case SINGLENOTE:
			s += form.toString();
			break;
		case SINGLEINTERVALL:
			s += NoteUtils.deltaToInterval(intervals[0]) + " " + relation.toString();
			break;
		case THIRDLESSCHORD:
		case FIFTHLESSCHORD:
			s += form.toString();
			s += " " + intervals[0] + "/" + intervals[1] + " " + relation.toString();
			break;
		default:
			s += form.toString() + " " + relation.toString();
			if (inversion > 0) s += " (" + inversion + ". inversion)";
			if (wide) s += " (weite lage)";
			break;
		}
		return s;
	}
	
	
	public int getTimestamp()
	{
		return timestamp;
	}
	
}
