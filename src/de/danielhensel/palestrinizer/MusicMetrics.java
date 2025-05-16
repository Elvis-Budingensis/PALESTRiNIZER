package de.danielhensel.palestrinizer;

import java.util.ArrayList;

public class MusicMetrics 
{
	
	public ArrayList<CounterList> items = new ArrayList<CounterList>();
	

	public MusicMetrics() 
	{
		
	}
	
	private CounterList getByTitle(String title)
	{
		String lct = title.toLowerCase();
		for (CounterList c: items)
		{
			String t = c.title.toLowerCase();
			if (t.equals(lct)) return c;
		}
		
		return null;
	}
	
	public void subtract(MusicMetrics a)
	{
		for (CounterList c: a.items)
		{
			CounterList d = getByTitle(c.title);
			if (d == null)
			{
				d = new CounterList(c.title);
				items.add(d);
			}
			
			d.subtract(c);
		}
	}

	public void add(MusicMetrics a)
	{
		for (CounterList c: a.items)
		{
			CounterList d = getByTitle(c.title);
			if (d == null)
			{
				d = new CounterList(c.title);
				items.add(d);
			}
			
			d.add(c);
		}
	}
	
	public void divide(int d)
	{
		for (CounterList c: items)
		{
			c.divide(d);
		}
	}

	public void dividePercentages(int d)
	{
		for (CounterList c: items)
		{
			c.dividePercentages(d);
		}
	}
	
	public void absolute()
	{
		for (CounterList c: items)
		{
			c.absolute();
		}
	}
	
	public void square()
	{
		for (CounterList c: items)
		{
			c.square();
		}
	}

	
	public void squareroot()
	{
		for (CounterList c: items)
		{
			c.squareroot();
		}
	}
	
}
