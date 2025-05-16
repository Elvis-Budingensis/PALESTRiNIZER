package de.danielhensel.palestrinizer;

import java.util.HashMap;
import java.io.OutputStream;
import java.io.IOException;

import de.danielhensel.palestrinizer.io.IMetricsFormatter;

public class MusicGrid
{
	public String modus = "";
	public int divider = 1;
	
	public int duration;
	public int numvoices;
    public int[][] notes;
    public int[][] intervals;
    public boolean[][] crossings;
    public Sound[] sounds;
    public float[] densities;
    
    public Staff[] staff;

    
	public HashMap<String,Integer> moduslevels = new HashMap<String,Integer>();
	public int transpose = 0;
	

	public boolean isaggregated = false;
	public MusicMetrics metrics = new MusicMetrics();
	
	public String filename = "";
	
    public MusicGrid(int aduration, int anumvoices)
    {
    	duration = aduration;
    	numvoices = anumvoices;

    	notes = new int[aduration][anumvoices];
    	intervals = new int[duration][numvoices];
    	crossings = new boolean[duration][numvoices];
    	sounds = new Sound[duration];
    	densities = new float[duration];
    	
    	staff = new Staff[anumvoices];
    	for (int i = 0; i < staff.length; i++)
    	{
    		staff[i] = Staff.UNDETERMINED;
    	}
    }
    
    
    
    
    private void buildIntervals()
    {
    	
    	for (int i = 0; i < duration; i++)
    	{
       		for (int j = 0; j < numvoices; j++)
       		{
       			intervals[i][j] = -1;
       			crossings[i][j] = false;
       		}
       		    		
       		int k = 0;
    		int last_key = -1;
 
    		for (int j = 0; j < numvoices; j++)
    		{
    			int key = notes[i][j];
    			if (key == -1) continue;
    			
    			if (last_key != -1) 
    			{
       				int interval;
       				
       				if (key < last_key)
    				{
       					interval = last_key - key;
       	       			crossings[i][j] = true;
    				} else {
    					interval = key - last_key;
    				}

    				intervals[i][k] = interval;
    				k++;
    			}
   			
    			last_key = key;
    		}
    	}
    	
    	
    }

    
    private void buildSounds()
    {
    	for (int i = 0; i < duration; i++)
    	{
			sounds[i] = Sound.createFromNotes(notes[i],i);
    	}
    	
    }

    
    private void buildDensities()
    {
    	for (int i = 0; i < duration; i++)
    	{
    		int minnote = 1000;
    		int maxnote = -1;
    		
    		int[] slice = notes[i];
    		
    		for (int j = 0; j < slice.length; j++)
    		{
    			int v = slice[j];
    			if (v < minnote) minnote = v;
    			if (v > maxnote) maxnote = v;
    			
    		}
    		
    		int ambitus = (maxnote-minnote);
    		int numnotes = sounds[i].numdistinctnotes - 1;
    		
    		if (numnotes <= 0)
    		{
    			densities[i] = 0; //-1;	
    		} else {
    			float d = ambitus / numnotes;
    			if (d != 0) d = (1/d) * 100;
    			densities[i] = d;
    		}
    		
  
    	}
    	
    }
    
    
    private void buildSoundCounters()
    {
    	
    	CounterList cl,cons,dis,sl;
		Sound c;

		// konsonanzen / dissonanzen
    	cl = new CounterList("cons-/dissonances");
    	cons = new CounterList("consonant sounds");
    	dis = new CounterList("dissonant sounds");
    	sl = new CounterList("all sounds");

    	for (int i = 0; i < duration; i++)
    	{
    		// anteiliger vergleich c/d
    		c = sounds[i];
    		if (c.relation == SoundRelation.DISSONANT)
    		{
    			cl.step("DISSONANT");
    		} else    		
    		if (c.relation == SoundRelation.CONSONANT)
    		{
    			cl.step("CONSONANT");
    		} else {
    			cl.step("");
    		}

    		// getrennte betrachtung der klänge nach c/d
    		String s = c.form.toString();
    		if ((c.form == SoundForm.THIRDLESSCHORD) || (c.form == SoundForm.FIFTHLESSCHORD))
    		{
    			s += " " + c.intervals[0] + "/" + c.intervals[1];
    		}
    		
    		if (c.inversion > 0) s += ", " + c.inversion + ". INVERSION";
    		
    		if (c.relation == SoundRelation.DISSONANT)
    		{
    			dis.step(s);
    		} else {
    			dis.step("");
    		}

    		if (c.relation == SoundRelation.CONSONANT)
       		{
    			cons.step(s);
    		} else {
    			cons.step("");
    		}

       		
       		// alle klänge
       		if (c.basstone != -1)
       		{
           		// das hier war wohl falsch: String histokey = s + " " + NoteUtils.pitchToString(c.keytone) + "/" + NoteUtils.pitchToSolmisation(c.keytone, transpose) + " step: " + NoteUtils.levelToString(getModusLevel(c.keytone));
       			
       			String histokey = s + " " + NoteUtils.pitchToString(c.basstone) + "/" + NoteUtils.pitchToSolmisation(c.basstone, transpose) + " step: " + NoteUtils.levelToString(getModusLevel(c.basstone));
           		sl.step(histokey);
       		} else {
       			sl.step("");
       		}
       		
    	}
    	cl.finish();
    	metrics.items.add(cl);

    	dis.finish();
    	metrics.items.add(dis);

    	cons.finish();
    	metrics.items.add(cons);
    	
    	sl.finish();
    	metrics.items.add(sl);
    
    } 
    
    private void buildNoteCounters()
    {

    	CounterList x = new CounterList("crossings");
    	
    	for (int j = 0; j < numvoices; j++)
    	{
    		Staff s = staff[j];
    		String ss = "voice " + j;
    		if (s != null)
    		{
    			ss = s.toString();
    		}
    		
    		CounterList n = new CounterList(ss);
    		CounterList nwo = new CounterList(ss + " pitches only");
    		
    		
        	for (int i = 0; i < duration; i++)
        	{
        		int key = notes[i][j];

        		if (key == -1) 
        		{
        			n.step("");
        			nwo.step("");
        		} else {
	        		n.step( NoteUtils.noteToString(key));
	        		nwo.step( NoteUtils.pitchToString(key));
        		}
        		
        		
    			if (crossings[i][j])
    			{
    				x.step( ss );
    			} else {
    				x.step( "" );
    			}
        		
        	}
        	
        	n.finish();
        	nwo.finish();
        	
        	
        	metrics.items.add(n);
        	metrics.items.add(nwo);
    		
    	}
    	
    	
    	x.finish();
    	metrics.items.add(x);
    	
    }
    

    
    public void analyze()
    {
    	metrics = new MusicMetrics();
    	
    	buildIntervals();
    	buildSounds();
    	buildDensities();
    	buildSoundCounters();
    	buildNoteCounters();
    }
    
    
    
    
	public int getModusLevel(int note)
	{
		
		if (note < 0) return -1;
		note = (note % 12);

		String n = NoteUtils.pitchToString(note);
		Integer v = moduslevels.get( n );

		if (v == null) return -1;
		return v.intValue();
	}
	

	
	public void setModus(String amodus) throws IllegalArgumentException
	{
		amodus = amodus.toLowerCase();
		if (!ModusUtil.modi.containsKey(amodus))
		{
			if (!MajorMinorScale.modi.containsKey(amodus))
			{
				throw new IllegalArgumentException("unknown modus '" + amodus + "'");
			}
			this.modus = MajorMinorScale.names.get(amodus);
			this.moduslevels = MajorMinorScale.modi.get(amodus);
			this.transpose = MajorMinorScale.transposition.get(amodus);
			return;
		}
		
		
		this.modus = ModusUtil.names.get(amodus);
		this.moduslevels = ModusUtil.modi.get(amodus);
		this.transpose = ModusUtil.transposition.get(amodus);
	}

	
	
	public void write(IMetricsFormatter formatter, OutputStream stream) throws IOException
	{
		formatter.write(this,stream);
	}
	
    
  	
	
}

