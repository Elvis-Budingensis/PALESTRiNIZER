package de.danielhensel.palestrinizer;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.*;

public class CompositeBarChart
{
	
	private File a;
	private File b;

	private BufferedImage img_a;
	private BufferedImage img_b;
	
	private BufferedImage img_out;
	
	public CompositeBarChart(String filename_a, String filename_b)
	{
		a = new File(filename_a);
		b = new File(filename_b);
		
	}
	
	public void combine(String outputfile) throws Exception
	{
		img_a = ImageIO.read(a);
		img_b = ImageIO.read(b);
		
		
		

		
		
		int x,y,w,h;
		w = img_a.getWidth();
		h = img_a.getHeight();
		
		if ((w != img_b.getWidth()) || (h != img_b.getHeight()))
		{
			throw new Exception("Image-Dimensions must be the same");
		}

		img_out = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		
		
		// move b-titleline 
		for (y = 0; y < (h / 12); y++)
		{
			for (x = 0; x < (w / 2); x++)
			{
				img_b.setRGB(x + (w/2), y, img_b.getRGB(x, y));
				img_b.setRGB(x,y,0xFFFFFF);
			}
		}
		
		
		for (y = 0; y < h; y++)
		{
			for (x = 0; x < w; x++)
			{
				int pa = img_a.getRGB(x, y);
				int pb = img_b.getRGB(x, y);
				
				if (pa != 0xFFFFFF)
				pa &= 0x0000FFFF;
				pa |= 0x00FF0000;

				pb &= 0x00FF00FF;
				pb |= 0x0000FF00;
				
				
				
				int po = ((pa & 0xfefefe) + (pb & 0xfefefe)) >> 1;
				img_out.setRGB(x, y, po);
			}
		}
		
		ImageIO.write(img_out, "png", new File(outputfile));		
	
	}
	

}
