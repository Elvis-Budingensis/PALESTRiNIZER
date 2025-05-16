package de.danielhensel.palestrinizer.io;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import de.danielhensel.palestrinizer.Counter;
import de.danielhensel.palestrinizer.CounterList;
import de.danielhensel.palestrinizer.MusicGrid;
import de.danielhensel.palestrinizer.*;

public class PlaintextMetricsFormatter implements IMetricsFormatter 
{

	public void write(MusicGrid musicgrid,OutputStream stream) throws IOException
	{
		StringBuffer result = new StringBuffer();
		
		if (!musicgrid.isaggregated)
		{
			// dump the timeline
			for (int i = 0; i < musicgrid.duration; i++)
			{
				result.append(i + ": " );
				for (int j = 0; j < musicgrid.numvoices; j++)
				{
					int key = musicgrid.notes[i][j];
					
					if (key != -1)
					{
						if (musicgrid.crossings[i][j])
						{
							result.append(" " + NoteUtils.noteToString(key) + "*");
							
						} else {
							result.append(" " + NoteUtils.noteToString(key));
							
						}
						
					} else {
						result.append(" ---");
						
					}
				}
	
				result.append("| ");
				if (musicgrid.densities[i] >= 0)
					result.append( String.format("d: %3.2f ",musicgrid.densities[i]) );
	
				if (musicgrid.sounds[i].dgrade > 0)
				{
					result.append( String.format("dg: %3.2f ",musicgrid.sounds[i].dgrade) );
				}
				
				if (musicgrid.sounds[i].sgrade > 0)
				{
					result.append( String.format("sg: %3.2f ",musicgrid.sounds[i].sgrade) );
				}
				
				result.append(" | ");
				
				for (int j = 0; j < musicgrid.numvoices-1; j++)
				{
					int interval = musicgrid.intervals[i][j];
					
					if (interval != -1) 
					{
						result.append(" " + NoteUtils.deltaToInterval(interval) + " (" + interval + ")");
					}				
					
				}
	
				
				
				
				Sound d = musicgrid.sounds[i];
				if (d != null)
				{
					result.append("| " + d.toString() );
			
					if (d.basstone >= 0)
					{
						result.append(" step: " + NoteUtils.pitchToSolmisation(d.basstone,musicgrid.transpose) + " " + musicgrid.getModusLevel(d.basstone) );
					}
				}
				result.append("\n");
	
			}
		}
		
		
		for (int i = 0; i < musicgrid.metrics.items.size(); i++)
		{
			exportCounterList(result,musicgrid.metrics.items.get(i));
		}

		OutputStreamWriter osw = new OutputStreamWriter(stream,"UTF-8");
		osw.write(result.toString());
		osw.flush();
	}
	
    private void exportCounterList(StringBuffer b, CounterList cl)
    {
		b.append("\n");

		b.append(String.format("%-45s ", cl.title));
		b.append(String.format("%7s ", "evts"));
		b.append(String.format("%7s ", "sd"));
		b.append(String.format("%7s ", "%evt"));
		b.append(String.format("%7s ", "sd"));
		b.append(String.format("%7s ", "%ttl"));
		b.append(String.format("%7s ", "sd"));

		b.append(String.format("| %7s ", "blks"));
		b.append(String.format("%7s ", "sd"));
		b.append(String.format("%7s ", "mindur"));
		b.append(String.format("%7s ", "maxdur"));
		b.append(String.format("%7s ", "avgdur"));


		b.append(String.format("%7s ", "avgdur_sd"));
		
		b.append(String.format("\n-----------------------------------------------------------------------------------------------------------------------------------------------\n"));
		
		ArrayList<Counter> items = cl.getItems();
    	for (int i = 0; i < items.size(); i++)
    	{
    		Counter c = items.get(i);
    		
    		b.append(String.format("%-45s ", c.key));
    		b.append(String.format("%7.2f ", c.events));
    		b.append(String.format("%7.2f ", c.events_sd));
    		b.append(String.format("%7.2f ", c.perc_events_total));
    		b.append(String.format("%7.2f ", c.perc_events_total_sd));
    		b.append(String.format("%7.2f ", c.perc_events_duration));
    		b.append(String.format("%7.2f ", c.perc_events_duration_sd));

    		b.append(String.format("| %7.2f ",c.blocks));
    		b.append(String.format("%7.2f ",c.blocks_sd));
    		b.append(String.format("%7.2f ", c.min_duration));
    		b.append(String.format("%7.2f ", c.max_duration));
    		b.append(String.format("%7.2f ", c.avg_duration));
    		
    		b.append(String.format("%7.2f ", c.avg_duration_sd));
    		
    		b.append("\n");
    	}
    	
    }
	

}
