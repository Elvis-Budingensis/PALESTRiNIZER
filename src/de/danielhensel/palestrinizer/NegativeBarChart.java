package de.danielhensel.palestrinizer;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.*;

public class NegativeBarChart
{
	
	private File a;
	private File b;

	private File c;
	private File d;

	private File e;
	private File f;

	private File g;
	private File k;



	private BufferedImage img_a;
	private BufferedImage img_b;

	private BufferedImage img_c;
	private BufferedImage img_d;

	private BufferedImage img_e;
	private BufferedImage img_f;

	private BufferedImage img_g;
	private BufferedImage img_k;
	
	private BufferedImage img_out;
	
	public NegativeBarChart(String filename_a, String filename_b, String filename_c, String filename_d, String filename_e, String filename_f, String filename_g, String filename_k)
	{
		a = new File(filename_a);
		b = new File(filename_b);
		c = new File(filename_c);
		d = new File(filename_d);
		e = new File(filename_e);
		f = new File(filename_f);
		g = new File(filename_g);
		k = new File(filename_k);
		
	}
	
	public void combine(String outputfile) throws Exception
	{
		img_a = ImageIO.read(a);
		img_b = ImageIO.read(b);
		img_c = ImageIO.read(c);
		img_d = ImageIO.read(d);
		img_e = ImageIO.read(e);
		img_f = ImageIO.read(f);
		img_g = ImageIO.read(g);
		img_k = ImageIO.read(k);
		
		
		

		
		
		int x,y,w,h;
		w = img_a.getWidth();
		h = img_a.getHeight();
		
		if ((w != img_b.getWidth()) || (h != img_b.getHeight()))
		{
			throw new Exception("Image-Dimensions must be the same");
		}

		img_out = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		
		
		// titelzeile von b verschieben
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
				int pc = img_c.getRGB(x, y);
				int pd = img_d.getRGB(x, y);
				int pe = img_e.getRGB(x, y);
				int pf = img_f.getRGB(x, y);
				int pg = img_g.getRGB(x, y);
				int pk = img_k.getRGB(x, y);
				
				
				
				if (pa != 0xFFFFFF)
				pa &= 0x0000FFFF;
				pa |= 0x00FF0000;

				pb &= 0x00FF00FF;
				pb |= 0x0000FF00;
				
				pc &= 0x0FF00FF0;
				pc |= 0x000FF000;
				
				pd &= 0xFF00FF00;
				pd |= 0x00FF0000;
				
				pe &= 0xF00FF0F;
				pe |= 0x0FF000F;
				
				pf &= 0x00FF00FF;
				pf |= 0xFF0000FF;
				
				pg &= 0x0FF00FF0;
				pg |= 0xF0000FF0;
				
				pk &= 0xFF000FF0;
				pk |= 0x00000FF0;
				int po = ((pa & 0xfefefe) + (pb & 0xfefefe)) >> 1;
				img_out.setRGB(x, y, po);
			}
		}
		
		ImageIO.write(img_out, "png", new File(outputfile));		
	
	}
	

}
