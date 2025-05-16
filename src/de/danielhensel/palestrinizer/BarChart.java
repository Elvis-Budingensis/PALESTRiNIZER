package de.danielhensel.palestrinizer;

import java.awt.image.BufferedImage;
import java.awt.*;

import javax.imageio.ImageIO;
import java.io.*;

public class BarChart 
{
	private BufferedImage img;
	private int totalwidth,totalheight;
	private int chartwidth,chartheight;
	private int leftoffset,topoffset;

	private int[][] pixeldata;
	private int numextracts;
	private int totalduration = 0;

	private double[] values;
	private double minvalue = Double.MIN_VALUE;
	private double maxvalue = Double.MAX_VALUE;
	
	public boolean renderInfos = true;
	public String label = "";

	private int divider = 0;
	
	
	public BarChart(int width, int height, int numsamples)
	{
		values = new double[numsamples];

		img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		totalwidth = width;
		totalheight = height;
		
		if (renderInfos)
		{
			chartwidth = (int) (totalwidth*0.9);
			chartheight = (int) (totalheight*0.8);
			leftoffset = (int) ((totalwidth-chartwidth) * 0.5);
			topoffset = (totalheight-chartheight) / 2;
		} else {
			chartwidth = totalwidth;
			chartheight = totalheight;
			leftoffset = 0;
			topoffset = 0;
		}
		
		pixeldata = new int[numsamples][chartheight];
		numextracts = 0;
		
		clearValues();
	}
	
	public void clearValues()
	{
		numextracts = 0;
		totalduration = 0;
		for (int i = 0; i < values.length; i++)
		{
			values[i] = 0;
			for (int j = 0; j < chartheight; j++)
			{
				pixeldata[i][j] = 0;
			}
		}
	}
	
	
	
	private void updatePixeldata()
	{
		int center = chartheight / 2;

		for (int i = 0; i < values.length; i++)
		{
			int cy;
			cy = (int) (((values[i] - minvalue) / (maxvalue-minvalue)) * chartheight*0.5);
			
			for (int j = center-cy; j < center+cy; j++)
			{
				if (j < 0) continue;
				if (j >= chartheight) continue;
				pixeldata[i][j]++;
			}
		}
		
		numextracts++;
	}
	
	public void renderChart(Color bgcolor, Color fgcolor)
	{
		Graphics g = img.getGraphics();
		g.setColor( bgcolor );
		g.fillRect(0, 0, totalwidth, totalheight);
		
		
		int bg_r = bgcolor.getRed();
		int bg_g = bgcolor.getGreen();
		int bg_b = bgcolor.getBlue();

		int dg_r = fgcolor.getRed();
		int dg_g = fgcolor.getGreen();
		int dg_b = fgcolor.getBlue();
		
		int or,og,ob;
	
		if (numextracts == 0) return;
		
		double stride = ( chartwidth / (double) values.length);
		
		for (int i = 0; i < values.length; i++)
		{
			int cx = (int) (i*stride);
			int cw = (int) (stride);
			
   			for (int y = 0; y < chartheight; y++)
			{
				int cv = (255*pixeldata[i][y]) / numextracts;
				int icv = 255-cv;
			
				or = (cv*dg_r) + (icv*bg_r) >> 8;
				og = (cv*dg_g) + (icv*bg_g) >> 8;
				ob = (cv*dg_b) + (icv*bg_b) >> 8;
   			
				int rgb = ob | (og << 8) | (or << 16);
				for (int x = cx; x <= (cx+cw); x++)
				{

					img.setRGB(leftoffset + x, topoffset + y, rgb);
				}

			}
		}

		if (renderInfos)
		{
			g.setColor(fgcolor);
			g.drawLine(leftoffset, topoffset, leftoffset, chartheight + topoffset);

			
			
			
			g.setFont( new Font( "Verdana", Font.PLAIN, (int) (leftoffset * 0.2) ) );
			FontMetrics fm = g.getFontMetrics();

			int tw = fm.stringWidth("100%");
			g.drawString("100%", leftoffset-tw-14, topoffset + fm.getAscent()/2 -1 );

			g.drawLine(leftoffset, topoffset, leftoffset-12, topoffset);
			g.drawString("100%", leftoffset-tw-14, topoffset + chartheight + fm.getAscent()/2 -1 );

			//g.drawLine(leftoffset-24, chartheight + topoffset, leftoffset+chartwidth+20, chartheight + topoffset);
			g.drawLine(leftoffset-12, chartheight + topoffset, leftoffset+chartwidth+20, chartheight + topoffset);

			
			tw = fm.stringWidth("0%");
			g.setXORMode(bgcolor);
			g.drawLine(leftoffset+chartwidth+20, chartheight / 2 + topoffset, leftoffset-12, chartheight / 2 + topoffset);
			g.setPaintMode();
			g.drawString("0%", leftoffset-tw-14, topoffset + (chartheight / 2) + fm.getAscent()/2 -1  );

			
			tw = fm.stringWidth("50%");
			g.drawLine(leftoffset, chartheight / 4 + topoffset, leftoffset-12, chartheight / 4 + topoffset);
			g.drawString("50%", leftoffset-tw-14, topoffset + (chartheight / 4) + fm.getAscent()/2 -1  );
			
			g.drawLine(leftoffset, (chartheight / 4)*3 + topoffset, leftoffset-12, (chartheight / 4)*3 + topoffset);
			g.drawString("50%", leftoffset-tw-14, topoffset + (chartheight / 4)*3 + fm.getAscent()/2 -1  );
			
			//tw = fm.stringWidth("t");
			//g.drawString("t", leftoffset + chartwidth + 16, topoffset + (chartheight / 2) - fm.getDescent() -4 );
			
			
			
			
			int x;
			int avgduration = (int) ((float) totalduration/ (float) numextracts);
			String t;
			
			x = 0;
			t = "0";
			g.drawLine(leftoffset + x,chartheight + topoffset,leftoffset + x,chartheight+6 + topoffset);
			g.drawString(t,leftoffset + x - fm.stringWidth(t)/2, chartheight + topoffset + 18 );
			
			x =  (int) (chartwidth / 4.0);
			t = Integer.toString( (int) ((avgduration / 4.0)*1) );
			g.drawLine(leftoffset + x,chartheight + topoffset,leftoffset + x,chartheight+6 + topoffset);
			g.drawString(t,leftoffset + x - fm.stringWidth(t)/2, chartheight + topoffset + 18 );

			x =  (int) ((chartwidth / 4.0)*2);
			t = Integer.toString( (int) ((avgduration / 4.0)*2) );
			g.drawLine(leftoffset + x,chartheight + topoffset,leftoffset + x,chartheight+6 + topoffset);
			g.drawString(t,leftoffset + x - fm.stringWidth(t)/2, chartheight + topoffset + 18 );

			x =  (int) ((chartwidth / 4.0) *3);
			t = Integer.toString( (int) ((avgduration / 4.0)*3) );
			g.drawLine(leftoffset + x,chartheight + topoffset,leftoffset + x,chartheight+6 + topoffset);
			g.drawString(t,leftoffset + x - fm.stringWidth(t)/2, chartheight + topoffset + 18 );
			
			x =  (int) ((chartwidth / 4.0) *4);
			t = Integer.toString( (int) ((avgduration / 4.0)*4) );
			g.drawLine(leftoffset + x,chartheight + topoffset,leftoffset + x,chartheight+6 + topoffset);
			g.drawString(t,leftoffset + x - fm.stringWidth(t)/2, chartheight + topoffset + 18 );


			int xa = (int) (chartwidth / 8.0);
			x = xa;
			g.drawLine(leftoffset + x,chartheight + topoffset,leftoffset + x,chartheight+6 + topoffset);
			
			x = xa + (int) (chartwidth / 4.0);
			g.drawLine(leftoffset + x,chartheight + topoffset,leftoffset + x,chartheight+6 + topoffset);

			x = xa + (int) ((chartwidth / 4.0)*2);
			g.drawLine(leftoffset + x,chartheight + topoffset,leftoffset + x,chartheight+6 + topoffset);

			x = xa + (int) ((chartwidth / 4.0) *3);
			g.drawLine(leftoffset + x,chartheight + topoffset,leftoffset + x,chartheight+6 + topoffset);
			
			x = xa + (int) ((chartwidth / 4.0) *4);
			g.drawLine(leftoffset + x,chartheight + topoffset,leftoffset + x,chartheight+6 + topoffset);
			
			//-----------------
		
		
		
			g.setFont( new Font( "Verdana", Font.BOLD, (int) (topoffset * 0.35) ) );
			fm = g.getFontMetrics();

			avgduration = (int) (totalduration / (float) (numextracts*values.length));
			String textout = label + " | items: " + numextracts + " | granularity: " + divider + " | avg. dur: " + avgduration;
			g.drawString(textout, 14,fm.getHeight()+2 );
			
		}
		
	}
	
	
	public void finishRender(String filename)
	{
		try {
			File outputfile = new File(filename);
			ImageIO.write(img, "png", outputfile);		
		} catch (Exception e) {
			
		}
	}
	
	
	

	
	public void extractSound(MusicGrid musicgrid, String pattern)
	{
		divider = musicgrid.divider; 

		pattern = pattern.toLowerCase();
		double slice_size = musicgrid.duration / (double) values.length;
		totalduration += musicgrid.duration;

		minvalue = Double.MAX_VALUE;
		maxvalue = Double.MIN_VALUE;

		
		for (int i = 0; i < values.length; i++)
		{
			int slice_start = (int) (i*slice_size);
			int slice_end = (int) ((i+1)*slice_size);

			double num_sounds = 0;
			for (int j = slice_start; j < slice_end; j++)
			{
				Sound s = musicgrid.sounds[j];
				String sname = s.toString().toLowerCase();
				
				if (sname.indexOf(pattern) >= 0)
				{
					num_sounds += 1.0;
				}
				
			}
			
			values[i] = num_sounds / slice_size;
			
		}
		
		minvalue = 0;
		maxvalue = 1.0;

		updatePixeldata();
	}


	public void extractDensity(MusicGrid musicgrid)
	{
		divider = musicgrid.divider; 

		double slice_size = musicgrid.duration / (double) values.length;
		totalduration += musicgrid.duration;

		minvalue = Double.MAX_VALUE;
		maxvalue = Double.MIN_VALUE;

		
		for (int i = 0; i < values.length; i++)
		{
			int slice_start = (int) (i*slice_size);
			int slice_end = (int) ((i+1)*slice_size);

			double sum_density = 0;
			int c = 0;
			for (int j = slice_start; j < slice_end; j++)
			{
				double d = musicgrid.densities[j];
				if (d >= 0)
				{
					sum_density += d;
					c++;
				}
			}
			
			if (c > 0)
			{
				values[i] = sum_density / c;
				if (values[i] < minvalue) minvalue = values[i];
				if (values[i] > maxvalue) maxvalue = values[i];
			}
			
		}
		
		
		maxvalue = maxvalue * 1.1;
		updatePixeldata();
		
		
	}

	

	public void extractDissonaceGrade(MusicGrid musicgrid)
	{
		divider = musicgrid.divider; 

		double slice_size = musicgrid.duration / (double) values.length;
		totalduration += musicgrid.duration;
		if (slice_size < 1.0) slice_size = 1.0;

		minvalue = Double.MAX_VALUE;
		maxvalue = Double.MIN_VALUE;

		
		for (int i = 0; i < values.length; i++)
		{
			int slice_start = (int) (i*slice_size);
			int slice_end = (int) ((i+1)*slice_size);
			if (slice_start == slice_end) slice_end++;
			if (slice_end >= musicgrid.duration) slice_end = musicgrid.duration-1;

			double sum_dg = 0;
			for (int j = slice_start; j < slice_end; j++)
			{
				Sound s = musicgrid.sounds[j];
				if (s.relation == SoundRelation.DISSONANT)
				{
					sum_dg += s.sgrade;
				}
			}
			
			values[i] = sum_dg / slice_size;
			if (values[i] < minvalue) minvalue = values[i];
			if (values[i] > maxvalue) maxvalue = values[i];
			
		}
		updatePixeldata();
	}

	
	public void extractCrossings(MusicGrid musicgrid)
	{
		divider = musicgrid.divider; 

		double slice_size = musicgrid.duration / (double) values.length;
		totalduration += musicgrid.duration;
		//if (slice_size < 1.0) slice_size = 1.0;

		minvalue = Double.MAX_VALUE;
		maxvalue = Double.MIN_VALUE;

		
		for (int i = 0; i < values.length; i++)
		{
			int slice_start = (int) (i*slice_size);
			int slice_end = (int) ((i+1)*slice_size);
			if (slice_start == slice_end) slice_end++;
			if (slice_end >= musicgrid.duration) slice_end = musicgrid.duration-1;

			double sum_xing = 0;
			for (int j = slice_start; j < slice_end; j++)
			{
				for (int k = 0; k < musicgrid.numvoices; k++)
				{
					if (musicgrid.crossings[j][k] == true)
					{
						sum_xing += 1.0;
						break;
					}
					
				}
			}
			
			values[i] = sum_xing / slice_size;
			if (values[i] < minvalue) minvalue = values[i];
			if (values[i] > maxvalue) maxvalue = values[i];
			
		}
		updatePixeldata();
	}
	
	
	
	public void extractSentenceDensity(MusicGrid musicgrid)
	{
		divider = musicgrid.divider; 

		double slice_size = musicgrid.duration / (double) values.length;
		totalduration += musicgrid.duration;

		minvalue = 0;
		maxvalue = 1;
		
		
		for (int i = 0; i < values.length; i++)
		{
			int slice_start = (int) (i*slice_size);
			int slice_end = (int) ((i+1)*slice_size);
			
			int numgaps = 0;
			int maxgaps = (slice_end - slice_start) * musicgrid.numvoices;

			for (int j = slice_start; j < slice_end; j++)
			{
				for (int k = 0; k < musicgrid.numvoices; k++)
				{
					if (musicgrid.notes[j][k] == -1)
					{
						numgaps++;
					}
				}
			}
			
			if (maxgaps != 0)
			{
				values[i] = (numgaps*1.0) / (maxgaps*1.0);
			} else {
				values[i] = 0;
			}
		}
		updatePixeldata();
	}
	

	
}
