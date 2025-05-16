package de.danielhensel.palestrinizer.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.midi.*;

import de.danielhensel.palestrinizer.MusicGrid;
import de.danielhensel.palestrinizer.Staff;

public class MidiFileReader  
{
	public MidiFileReader()
	{
	}
	
	public MusicGrid readFromFile(String filename,int divider,String modus,Staff staff[]) throws InvalidMidiDataException,IllegalArgumentException,IOException 
	{
		File file = new File( filename );
		
		Sequence sequence;
		sequence = MidiSystem.getSequence( file );
		
        // we only accept midi-files that have a PPQ-timing   
		if (sequence.getDivisionType() != Sequence.PPQ)
		{
			throw new InvalidMidiDataException("input-midifile has no ppq-timing");
		}


        // print some basic information about the inputfile		
		int ticksperquarter = sequence.getResolution();
		int slices = (int) sequence.getTickLength()/ticksperquarter;

		ticksperquarter = (ticksperquarter*4) / divider; 
		slices = (int) sequence.getTickLength()/ticksperquarter;
		
		
		Track tracks[] = sequence.getTracks();
		ArrayList<Track> musictracks = new ArrayList<Track>();

		// find tracks that contain music
		long lasttick = 0;
		for (int i = 0; i < tracks.length; i++)
		{
			boolean trackadded = false;
			Track t = tracks[i];
			
			for (int j = 0; j < t.size(); j++)
			{
				MidiEvent me = t.get(j);
				MidiMessage mm = me.getMessage();
				
				if (!(mm instanceof ShortMessage)) continue;

				ShortMessage sm = (ShortMessage) mm;
				
				if ((sm.getCommand() == ShortMessage.NOTE_ON) && (!trackadded))
				{
					musictracks.add(t);
					trackadded = true;
				}
				
				if (me.getTick() > lasttick)
				{
					lasttick = me.getTick();
				}
			}
			
			
		}
		
		
		

		
		MusicGrid grid = new MusicGrid(slices,musictracks.size());
		grid.divider = divider;
		grid.setModus(modus);
		for (int i = 0; i < staff.length; i++)
		{
			if (i >= grid.staff.length) break;
			grid.staff[i] = staff[i];
		}
		
		// build musicgrid
		for (int i = 0; i < musictracks.size(); i++)
		{
			
			Track t = musictracks.get(i);
			
			int current_key = -1;
			int read_cursor = 0;
			int write_cursor = 0;

			for (int j = 0; j < t.size(); j++)
			{
				MidiEvent me = t.get(j);
				MidiMessage mm = me.getMessage();	

				if (!(mm instanceof ShortMessage)) continue;
				ShortMessage sm = (ShortMessage) mm;
				
				read_cursor = (int) Math.floor( me.getTick() / ticksperquarter );

				if (read_cursor > write_cursor)
				{
					for (int k = write_cursor; k < read_cursor; k++)
					{
						grid.notes[k][i] = current_key;
					}
				}
				write_cursor = read_cursor;
				
				
				current_key = -1;
				if (sm.getCommand() == ShortMessage.NOTE_ON)
				{
					if (sm.getData2() != 0)
					{
						current_key = sm.getData1();
					} 	
				}

			}
			
			for (int k = write_cursor; k < read_cursor; k++)
			{
				grid.notes[k][i] = current_key;
			}
		}
		
		
		for (int i = 0; i < grid.notes.length; i ++)
		{
			int[] notes  = grid.notes[i];
			for (int j = 0; j < notes.length / 2; j++)
			{
				int temp = notes[j];
			    notes[j] = notes[notes.length - j - 1];
			    notes[notes.length - j - 1] = temp;
			}
		}

		grid.filename = filename;
		grid.analyze();
		return grid;
	}
	
	public MusicGrid readFromSelfDescribingFile(String filename,int divider) throws IOException, InvalidMidiDataException
	{
		String parts[] = filename.split("_");
		if (parts.length < 3) throw new IllegalArgumentException("filename is not self describing");
		String modus = parts[parts.length-2];
		
		String staffcode = parts[parts.length-1].toUpperCase();
		staffcode = staffcode.substring(0, staffcode.indexOf('.'));
		Staff staff[] = new Staff[staffcode.length()];
	 
		Staff B = Staff.BASSUS;
		Staff T = Staff.TENOR;
		Staff A = Staff.ALTUS;
		Staff S = Staff.SOPRAN;
	 
		
		for (int i = staff.length-1; i >= 0; i--)
		{
			
			staff[i] = Staff.UNDETERMINED;
			
			
			char c = staffcode.charAt(i);
			switch (c)
			{
				case 'B':
					staff[i] = B;
					B = Staff.BASSUS2;
					break;
					
				
				case 'T':
					staff[i] = T;
					T = Staff.TENOR2;
					break;
					
				case 'A':
					staff[i] = A;
					A = Staff.ALTUS2;
					break;
					
				case 'S':
					staff[i] = S;
					S = Staff.SOPRAN2;
					break;
			}
		}
		
		return readFromFile(filename,divider,modus,staff);
	}

}
