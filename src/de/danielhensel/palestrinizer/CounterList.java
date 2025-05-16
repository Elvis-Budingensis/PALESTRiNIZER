package de.danielhensel.palestrinizer;

import java.util.ArrayList;
import java.util.Collections;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class CounterList 
{

    private ArrayList<Counter> _items;
    
    
    private String curkey = null;
	private Counter current = null;
	private int cursteps = 0;
	
	private int numevents = 0;
	private int totalsteps = 0;

	public String title = "Counter";
	
    public CounterList(String atitle)
    {
    	 _items = new ArrayList<Counter>();
    	 title = atitle;
    	 reset();
    }
    
    
    private Counter getCounter(String key)
    {
    	for (int i = 0; i < _items.size(); i++)
    	{
    		Counter c = _items.get(i);
    		if (c.key.equals(key)) return c;
    	}
    	return null;
    }
    
    public ArrayList<Counter> getItems()
    {
    	return _items;
    }
    
    
    public Counter step(String key)
    {
    	totalsteps++;
    	if (!key.equals("")) numevents++;
    	
    	Counter c = getCounter(key);
    	
    	if (c == null)
    	{
    		c = new Counter();
    		c.key = key;
    		c.min_duration = Integer.MAX_VALUE;
    		_items.add(c);
    	}
    	
    	
    	if (!key.equals(curkey))
    	{
    		// conclude prior data
    		if (current != null)
    		{
    			current.events += cursteps;
    			current.blocks++;
    			if (cursteps > current.max_duration)
    			{
    				current.max_duration = cursteps;
    			} 
    			if (cursteps < current.min_duration)
    			{
    				current.min_duration = cursteps;
    			}
    			
    			if (current.blocks > 0)
    			{
    				current.avg_duration = current.events / current.blocks;
    			}
 
    		}
    		current = c;
    		cursteps = 0;
    		curkey = key;
    	} 
 
   		cursteps++;
    	return c;
    }
    
 
    
    
    public void reset()
    {
    	_items.clear();
    	curkey = null;
    	cursteps = 0;
    	numevents = 0;
    	totalsteps = 0;
    }
    
    
    
    public void finish()
    {
    	totalsteps++;
		if (current == null) return;

		current.events += cursteps;
		current.blocks++;
		if (cursteps > current.max_duration)
		{
			current.max_duration = cursteps;
		}
		if (cursteps < current.min_duration)
		{
			current.min_duration = cursteps;
		}

		if (current.blocks > 0)
		{
			current.avg_duration = current.events / current.blocks;
		}
		

		for (int i = 0; i < _items.size(); i++)
		{
			Counter c = _items.get(i);
			if (c.key.equals("")) {
				_items.remove(i);
				break;
			}
		}
		
		
		for (int i = 0; i < _items.size(); i++)
		{
			Counter c = _items.get(i);
			if (numevents > 0)
			{
				c.perc_events_total = ((c.events * 1.0) / (numevents * 1.0)) * 100.0;
			}
			
			if (totalsteps > 0)
			{
				c.perc_events_duration = ((c.events * 1.0) / (totalsteps * 1.0)) * 100.0;
			}
			
		}
		
		
		
		
		
		Collections.sort(_items);
    
    }

    
    private String formatNumber(double a)
    {
    	return Long.toString( Math.round(a * 100.0)); 
    }
    
    public Element toXML(Document doc)
    {
		Element e = doc.createElement("counterlist");
		//doc.appendChild(rootElement);
 
		Element t = doc.createElement("title");
		t.appendChild( doc.createTextNode(title) );
		e.appendChild(t);
		
    	for (int i = 0; i < _items.size(); i++)
    	{
    		Counter c = _items.get(i);
    		Element item = doc.createElement("item");
    		
     		Element v;
    		v = doc.createElement("key"); v.appendChild( doc.createTextNode(c.key) ); item.appendChild(v); 
    		v = doc.createElement("events"); v.appendChild( doc.createTextNode( formatNumber(c.events) )); item.appendChild(v); 
    		v = doc.createElement("perc_events_total"); v.appendChild( doc.createTextNode( formatNumber(c.perc_events_total) )); item.appendChild(v); 
    		v = doc.createElement("perc_events_duration"); v.appendChild( doc.createTextNode( formatNumber(c.perc_events_duration) )); item.appendChild(v); 
    		
    		v = doc.createElement("blocks"); v.appendChild( doc.createTextNode( formatNumber(c.blocks)  )); item.appendChild(v); 
    		v = doc.createElement("min_duration"); v.appendChild( doc.createTextNode( formatNumber(c.min_duration) )); item.appendChild(v); 
    		v = doc.createElement("max_duration"); v.appendChild( doc.createTextNode( formatNumber(c.max_duration) )); item.appendChild(v); 
    		v = doc.createElement("avg_duration"); v.appendChild( doc.createTextNode( formatNumber(c.avg_duration) )); item.appendChild(v); 

    		e.appendChild(item);
    		
    	}
		
		
		return e;
    }
    
    public void fromXML(Node e)
    {
    	reset();
    	NodeList nl = e.getChildNodes();
    	for (int i = 0; i < nl.getLength(); i++)
    	{
    		Node x = nl.item(i);
    		String s = x.getNodeName();
    		if (s.equals("title"))
    		{
    			this.title = x.getTextContent();
    		} else
    		if (s.equals("item"))
    		{
    			NodeList ml = x.getChildNodes();
				Counter c = new Counter();
    			for (int j = 0; j < ml.getLength(); j++)
    			{
    				x = ml.item(j);
    				s = x.getNodeName();
    				if (s.equals("key")) 
    				{
    					c.key = x.getTextContent();
    				} else
    				if (s.equals("events"))
    				{
    					c.events = Integer.parseInt(x.getTextContent()) / 100.0;
					} else
					if (s.equals("perc_events_total"))
					{
    					c.perc_events_total = Integer.parseInt(x.getTextContent()) / 100.0;
					} else
					if (s.equals("perc_events_duration"))
					{
    					c.perc_events_duration = Integer.parseInt(x.getTextContent()) / 100.0;
					} else
					if (s.equals("blocks"))
					{
    					c.blocks = Integer.parseInt(x.getTextContent()) / 100.0;
					} else
					if (s.equals("min_duration"))
					{
    					c.min_duration = Integer.parseInt(x.getTextContent()) / 100.0;
					} else
					if (s.equals("max_duration"))
					{
    					c.max_duration = Integer.parseInt(x.getTextContent()) / 100.0;
					} else
					if (s.equals("avg_duration"))
					{
    					c.avg_duration = Integer.parseInt(x.getTextContent()) / 100.0;
					}
    			}
    			_items.add(c);
    			
    		}
    		
    	}
    }
    
    
	private Counter getByKey(String key)
	{
		String lct = key.toLowerCase();
		for (Counter c: _items)
		{
			String t = c.key.toLowerCase();
			if (t.equals(lct)) return c;
		}
		
		return null;
	}
    
    
    public void subtract(CounterList cl)
    {
    	for (Counter c: cl._items)
    	{
    		Counter d = getByKey(c.key);
    		if (d == null)
    		{
    			d = new Counter();
    			d.min_duration = 0;
    			d.key = c.key;
    			_items.add(d);
    		}
    		d.subtract(c);
    	}
    	
		Collections.sort(_items);
    }
    
    public void add(CounterList cl)
    {
    	for (Counter c: cl._items)
    	{
    		Counter d = getByKey(c.key);
    		if (d == null)
    		{
    			d = new Counter();
    			d.key = c.key;
    			_items.add(d);
    		}
    		d.add(c);
    	}
    	
		Collections.sort(_items);
    }
    
    public void divide(int d)
    {
    	for (Counter c: _items)
    	{
    		c.divide(d);
    	}
    	
		Collections.sort(_items);
    }

    public void dividePercentages(int d)
    {
    	for (Counter c: _items)
    	{
    		c.dividePercentages(d);
    	}
    	
		Collections.sort(_items);
    }

    public void absolute()
    {
    	for (Counter c: _items)
    	{
    		c.absolute();
    	}
    	
		Collections.sort(_items);
    }

    public void square()
    {
    	for (Counter c: _items)
    	{
    		c.square();
    	}
    	
		Collections.sort(_items);
    }
    
    public void squareroot()
    {
    	for (Counter c: _items)
    	{
    		c.squareroot();
    	}
    	
		Collections.sort(_items);
    }
    
    public void injectStdDev(CounterList cl)
    {
    	for (Counter c: cl._items)
    	{
    		Counter d = getByKey(c.key);
    		d.events_sd = c.events;
    		d.blocks_sd = c.blocks;
    		d.avg_duration_sd = c.avg_duration;
    		d.perc_events_duration_sd = c.perc_events_duration;
    		d.perc_events_total_sd = c.perc_events_total;
    	}
    	
		Collections.sort(_items);
    }
    
    
}