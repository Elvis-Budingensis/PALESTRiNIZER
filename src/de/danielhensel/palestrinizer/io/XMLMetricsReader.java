package de.danielhensel.palestrinizer.io;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.danielhensel.palestrinizer.CounterList;
import de.danielhensel.palestrinizer.MusicMetrics;

public class XMLMetricsReader 
{
	
	public MusicMetrics readFromString(String xmldata) throws Exception
	{
		MusicMetrics result;
		result = new MusicMetrics();

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// root elements
		Document doc = docBuilder.parse( xmldata );
		Element root = doc.getDocumentElement();
		
		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node x = nl.item(i);
			if (x.getNodeName().equals("counterlist"))
			{
				CounterList cl = new CounterList("");
				cl.fromXML(x);
				result.items.add(cl);
			}
		}
		
		return result;
			
	}

	public MusicMetrics readFromFile(File f) throws Exception
	{
		MusicMetrics result;
		result = new MusicMetrics();

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// root elements
		Document doc = docBuilder.parse( f );
		Element root = doc.getDocumentElement();
		
		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node x = nl.item(i);
			if (x.getNodeName().equals("counterlist"))
			{
				CounterList cl = new CounterList("");
				cl.fromXML(x);
				result.items.add(cl);
			}
		}
		
		return result;
	}
}
