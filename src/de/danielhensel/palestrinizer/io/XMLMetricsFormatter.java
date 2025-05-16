package de.danielhensel.palestrinizer.io;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.danielhensel.palestrinizer.CounterList;
import de.danielhensel.palestrinizer.MusicGrid;

public class XMLMetricsFormatter implements IMetricsFormatter 
{

	public void write(MusicGrid musicgrid,OutputStream stream) throws IOException,RuntimeException 
	{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (Exception e) {
			throw new RuntimeException("bla");
		}
		
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("musicgrid");
		doc.appendChild(rootElement);

		// grundinformationen
 		Element v;
		Element item = doc.createElement("info");
		v = doc.createElement("modus"); v.appendChild( doc.createTextNode(musicgrid.modus) ); item.appendChild(v); 
		v = doc.createElement("transpose"); v.appendChild( doc.createTextNode( Integer.toString(musicgrid.transpose) ) ); item.appendChild(v); 
		v = doc.createElement("duration"); v.appendChild( doc.createTextNode( Integer.toString(musicgrid.duration) ) ); item.appendChild(v); 
		v = doc.createElement("divider"); v.appendChild( doc.createTextNode( Integer.toString(musicgrid.divider) ) ); item.appendChild(v); 
		rootElement.appendChild(item);
		
		item = doc.createElement("staff");
		for (int i = 0; i < musicgrid.staff.length; i++)
		{
    		v = doc.createElement("item"); v.appendChild( doc.createTextNode(musicgrid.staff[i].toString()) ); item.appendChild(v); 
		}
		rootElement.appendChild(item);
		
		
		// alle counterlisten
		for (int i = 0; i < musicgrid.metrics.items.size(); i++)
		{
			CounterList cl = musicgrid.metrics.items.get(i);
			Element e = cl.toXML(doc);
			rootElement.appendChild(e);
		}
		
		
		String output = "";				
		try 
		{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			StringWriter writer = new StringWriter(); 
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			output = writer.getBuffer().toString();
		} catch (Exception e) {
			throw new RuntimeException("XMLExporter failed");
		}
		
		OutputStreamWriter osw = new OutputStreamWriter(stream,"UTF-8");
		osw.write( output );
		osw.flush();
	}
	

}
