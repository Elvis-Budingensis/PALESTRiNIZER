package de.danielhensel.palestrinizer;

import java.util.*;

import java.io.*;

public class SequenceAnalyser 
{
	
	private HashMap<String,ArrayList<String>> sequences = new HashMap<String,ArrayList<String>>();
	private int window; 
	private int mode;
	
	public SequenceAnalyser(int awindow, int amode ) 
	{
		window = awindow;
		mode = amode;
		sequences.clear();
	}
	
	public void scan(MusicGrid mg)
	{
		switch (mode)
		{
		case 0:
			scanBasstones(mg);
			break;
		case 1:
			scanKeytoneProgression(mg);
			break;
		
		}
	}
	
	private void scanBasstones(MusicGrid mg)
	{
		//MusicGrid rg;// = condenseGrid(mg);
		
		/*
		IMetricsFormatter mf = new PlaintextMetricsFormatter();
		try {
			mf.write(mg, System.out);
		} catch (Exception e) {
			
		}
		*/
		
		MusicGrid rg = new MusicGrid(mg.duration,mg.numvoices);
		
		rg.divider = mg.divider;
		rg.isaggregated = false;
		rg.modus = mg.modus;
		rg.moduslevels = mg.moduslevels;
		rg.staff = mg.staff;
		rg.transpose = mg.transpose;

		
		String lastkey = "";
		String curkey;
		
		int o = 0;
		for (int i = 0; i < mg.duration; i++)
		{
			Integer v = mg.moduslevels.get( NoteUtils.pitchToString(mg.sounds[i].basstone) );
			curkey = v + " " + NoteUtils.pitchToSolmisation(mg.sounds[i].basstone, mg.transpose) + " "  + mg.sounds[i].toShortString();
			
			if (!curkey.equals(lastkey))
			{
				rg.crossings[o] = mg.crossings[i];
				rg.densities[o] = mg.densities[i];
				rg.intervals[o] = mg.intervals[i];
				rg.notes[o] = mg.notes[i];
				rg.sounds[o] = mg.sounds[i];
				
				lastkey = curkey;
				o++;
			}
		}
		rg.duration = o;
		//rg.analyze();
		
		StringBuffer key = new StringBuffer();
		for (int i = 0; i <= (rg.duration-window); i++)
		{
			key.setLength(0);
			
			boolean skipped = false;
			
			int j = i;
			int seqlen = 0;
			int gaplen = 0;
			while (j <= (rg.duration-window))
			{
				Sound s = rg.sounds[j];

				// no pitch here ( = silent), cancel sequence-detection
				if (s.basstone < 0)
				{
					skipped = true;
					break;
				}
				
				// skip up to x dissonant sounds
				if (s.relation == SoundRelation.DISSONANT)
				{
					// transition is too long OR sequence starts with dissonant sound: break
					if ((gaplen >= (mg.divider*4)) || (seqlen == 0))  // 4!? arguable...
					{
						skipped = true;
						break;
					}
					gaplen++;
					key.append("GAP\n");
						
				} else {
					String n = NoteUtils.pitchToString(s.basstone);
					Integer v = rg.moduslevels.get( n );
					
					key.append(v + " " + NoteUtils.pitchToSolmisation(s.basstone, rg.transpose) + " "  + s.toShortString() + "\n");
					
					seqlen++;
					//plen = 0;
					if (seqlen >= window)
					{
						// i've got x sounds in a row. exit
						break;
					}
				}
				
				
				j++;
			}
			
			/*
			for (int j = 0; j < window; j++)
			{
				Sound s = rg.sounds[i+j];
				
				if (s.basstone < 0)
				{
					skipped = true;
					break;
				} else {
					
					String n = NoteUtils.pitchToString(s.basstone);
					Integer v = rg.moduslevels.get( n );
					
					key.append(v + " " + NoteUtils.pitchToSolmisation(s.basstone, rg.transpose) + " "  + s.toShortString() + "\n");
				}
				key.append("");
				
			}
			*/
			
			if (!skipped)
			{
				String keystring = key.toString();
				ArrayList<String> l = null;
				if (!sequences.containsKey(keystring))
				{
					l = new ArrayList<String>();
					sequences.put(keystring, l);
				} else {
					l = sequences.get(keystring);
					//sequences.put(keystring, sequences.get(keystring) + 1 );
				}
				l.add(mg.filename + ":" + rg.sounds[i].getTimestamp());
			}
		}
	}
	
	
	public void sortAndTruncate(int limit,OutputStream outstream)
	{
        ValueComparator bvc =  new ValueComparator(sequences);
        TreeMap<String,ArrayList<String>> sorted_map = new TreeMap<String,ArrayList<String>>(bvc);
        sorted_map.putAll(sequences);
        
        
        
        //sorted_map.
        
        PrintStream p = new PrintStream(outstream);
        
        for (int i = 0; i < limit; i++)
        {
            Map.Entry<String,ArrayList<String>> entry = sorted_map.pollFirstEntry();
            
            if (entry == null) continue;
            
            if (entry.getValue().size() <= 1) break;
            
            p.println("-----------------------------------------------------");
            p.println(entry.getValue().size() + " occurences for:");
            p.println(entry.getKey());
            
            for (String file: entry.getValue())
            {
            	p.println(file);
            }
            
            p.println("");
        	
        	
            
            
        }
        
	}
	
	

	
	private void scanKeytoneProgression(MusicGrid mg)
	{
		MusicGrid rg = new MusicGrid(mg.duration,mg.numvoices);
		
		rg.divider = mg.divider;
		rg.isaggregated = false;
		rg.modus = mg.modus;
		rg.moduslevels = mg.moduslevels;
		rg.staff = mg.staff;
		rg.transpose = mg.transpose;
		
		String lastkey = "";
		String curkey;
		
		int o = 0;
		for (int i = 0; i < mg.duration; i++)
		{
			/*
			Integer v = mg.moduslevels.get( NoteUtils.pitchToString(mg.sounds[i].basstone) );
			curkey = v + " " + NoteUtils.pitchToSolmisation(mg.sounds[i].basstone, mg.transpose) + " "  + mg.sounds[i].toShortString();
			*/
			Integer v = mg.sounds[i].keytone;
			curkey = v + "";
			
			if (!curkey.equals(lastkey))
			{
				rg.crossings[o] = mg.crossings[i];
				rg.densities[o] = mg.densities[i];
				rg.intervals[o] = mg.intervals[i];
				rg.notes[o] = mg.notes[i];
				rg.sounds[o] = mg.sounds[i];
				
				lastkey = curkey;
				o++;
			}
		}
		rg.duration = o;
		
		StringBuffer key = new StringBuffer();
		for (int i = 0; i <= (rg.duration-window); i++)
		{
			key.setLength(0);
			
			boolean skipped = false;
			
			int j = i;
			int seqlen = 0;
			
			int prev_keytone = -1;
			
			while (j <= (rg.duration-window))
			{
				Sound s = rg.sounds[j];

				// no pitch here ( = silent), cancel sequence-detection
				if (s.keytone < 0)
				{
					skipped = true;
					break;
				}
				
				
				if (prev_keytone == -1)
				{

					prev_keytone = s.keytone;
					
				} else {
					
					int interval = ((s.keytone-prev_keytone) % 12);
					boolean ascending = true;
					if (interval < 0)
					{
						ascending = false;
						interval = Math.abs(interval);
						
					}

					prev_keytone = s.keytone;
					
					String keyname = "";
					if (ascending)
					{
						keyname = "Ascending " + NoteUtils.keytoneprogression[interval];
					} else {
						keyname = "Descending " + NoteUtils.keytoneprogression[interval];
					}
					if (keyname.equals("Ascending Prime"))
					{
						keyname = "-";
					}

					key.append( keyname + "\n");
					
					seqlen++;
					if (seqlen >= window)
					{
						// i've got x sounds in a row. exit
						break;
					}
				}
				
				j++;
			}
			
			if (!skipped)
			{
				String keystring = key.toString();
				
				ArrayList<String> l = null;
				if (!sequences.containsKey(keystring))
				{
					l = new ArrayList<String>();
					sequences.put(keystring, l);
				} else {
					l = sequences.get(keystring);
				}
				l.add(mg.filename + ":" + rg.sounds[i].getTimestamp());
			}
		}
		
		
		if (sequences.containsKey(""))
		{
			sequences.remove("");
		}
		
		
	}

	
}	


class ValueComparator implements Comparator<String> {

    Map<String, ArrayList<String>> base;
    public ValueComparator(Map<String, ArrayList<String>> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a).size() >= base.get(b).size()) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}