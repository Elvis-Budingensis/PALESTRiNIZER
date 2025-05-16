package de.danielhensel.palestrinizer;

import java.lang.Comparable;

public class Counter implements Comparable<Counter>
{
	public String key = "";
	public double events = 0;			// single occurences
	public double blocks = 0;			//number of coherent blocks
	public double min_duration = 0;	// length of the shortest coherent block
	public double max_duration = 0;	// length of the longest coherent block

	public double avg_duration = 0;	// average duration of blocks 
	
	
	public double perc_events_duration = 0; //percentage of total events in music piece  
	public double perc_events_total = 0; //percentage of total captured events

	
	// standard deviation
	public double events_sd = 0;
	public double blocks_sd = 0;
	public double avg_duration_sd = 0;	
	public double perc_events_duration_sd = 0; 
	public double perc_events_total_sd = 0; 
	
	
	public int compareTo(Counter a)
	{
		double d = (a.events - this.events);
		if (d == 0) return 0;
		if (d < 0) return -1;
		return 1;
		
	}
	
	
	public void subtract(Counter a)
	{
		if (a == null) return;
		events -= a.events;
		blocks -= a.blocks;
		min_duration -= a.min_duration;
		max_duration -= a.max_duration;
		avg_duration -= a.avg_duration;
		perc_events_duration -= a.perc_events_duration; 
		perc_events_total -= a.perc_events_total; 
	}

	
	public void add(Counter a)
	{
		if (a == null) return;
		events += a.events;
		blocks += a.blocks;
		min_duration += a.min_duration;
		max_duration += a.max_duration;
		avg_duration += a.avg_duration;
		perc_events_duration += a.perc_events_duration; 
		perc_events_total += a.perc_events_total; 
	}
	
	public void divide(int d)
	{
		if (d == 0) return;
		events /= d;
		blocks /= d;
		min_duration /= d;
		max_duration /= d;
		avg_duration /= d;
		perc_events_duration /= d; 
		perc_events_total /= d; 
	}

	public void dividePercentages(int d)
	{
		if (d == 0) return;
		perc_events_duration /= d; 
		perc_events_total /= d; 
	}
	
	
	public void absolute()
	{
		events = Math.abs(events);
		blocks = Math.abs(blocks);
		min_duration = Math.abs(min_duration);
		max_duration = Math.abs(max_duration);
		avg_duration = Math.abs(avg_duration);
		perc_events_duration = Math.abs(perc_events_duration); 
		perc_events_total = Math.abs(perc_events_total); 
	}

	
	public void square()
	{
		events *= events;
		blocks *= blocks;
		min_duration *= min_duration;
		max_duration *= max_duration;
		avg_duration *= avg_duration;
		perc_events_duration *= perc_events_duration; 
		perc_events_total *= perc_events_total; 
	}

	
	public void squareroot()
	{
		events = Math.sqrt(events);
		blocks = Math.sqrt(blocks);
		min_duration = Math.sqrt(min_duration);
		max_duration = Math.sqrt(max_duration);
		avg_duration = Math.sqrt(avg_duration);
		perc_events_duration = Math.sqrt(perc_events_duration); 
		perc_events_total = Math.sqrt(perc_events_total); 
	}

}
