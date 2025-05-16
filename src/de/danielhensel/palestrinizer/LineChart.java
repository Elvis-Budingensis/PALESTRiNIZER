package de.danielhensel.palestrinizer;

import java.awt.image.BufferedImage;
import java.awt.*;

import javax.imageio.ImageIO;
import java.io.*;



public class LineChart 
{
	private BufferedImage img;
	private int imgwidth,imgheight;

	double[] values;
	double minvalue = Double.MIN_VALUE;
	double maxvalue = Double.MAX_VALUE;
	
	public LineChart(int width, int height, int numsamples)
	{
		values = new double[numsamples];

		img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		imgwidth = width;
		imgheight = height;
	}
	
	public void clearValues()
	{
		for (int i = 0; i < values.length; i++)
		{
			values[i] = 0;
		}
	}

	public void clearImage(Color color)
	{
		Graphics g = img.getGraphics();
		g.setColor(color );
		g.fillRect(0, 0, imgwidth, imgheight);
	}
	
	
	public void extractDensities(MusicGrid musicgrid)
	{
		double slice_size = musicgrid.duration / values.length;

		minvalue = Double.MAX_VALUE;
		maxvalue = Double.MIN_VALUE;
		
		for (int i = 0; i < values.length; i++)
		{
			int slice_start = (int) (i*slice_size);
			int slice_end = (int) ((i+1)*slice_size);

			double sum_density = 0;
			int num_densities = 0;
			for (int j = slice_start; j < slice_end; j++)
			{
				if (musicgrid.densities[j] >= 0)
				{
					sum_density += musicgrid.densities[j];
					num_densities++;
				}
			}
			
			if (num_densities > 0)
				values[i] = sum_density / num_densities;
			else
				values[i] = 0;
			
			if (values[i] < minvalue) minvalue = values[i];
			if (values[i] > maxvalue) maxvalue = values[i];
			
		}
		minvalue = 0;
		maxvalue = 60;
		
	}
	

	
	public void extractPitch(MusicGrid musicgrid, int voiceno)
	{
		double slice_size = musicgrid.duration / values.length;

		minvalue = -1;
		maxvalue = 80;
		
		
		for (int i = 0; i < values.length; i++)
		{
			int slice_start = (int) (i*slice_size);
			int slice_end = (int) ((i+1)*slice_size);

			double sum_pitch = 0;
			int num_pitch = 0;
			for (int j = slice_start; j < slice_end; j++)
			{
				if (musicgrid.notes[j][voiceno] > 0)
				{
					sum_pitch += musicgrid.notes[j][voiceno];
					num_pitch++;
				}
			}
			
			if (num_pitch > 0)
				values[i] = sum_pitch / num_pitch;
			else
				values[i] = 0;
			
			if (values[i] < minvalue) minvalue = values[i];
			if (values[i] > maxvalue) maxvalue = values[i];
			
			
		}

		minvalue = 40;
		maxvalue = 75;
		
		
	}
	
	
	
	public void renderValues(Color color)
	{
		Graphics g = img.getGraphics();
		
		g.setColor( color );
		
		double stride = (imgwidth/values.length);
		double center = stride/2;
//		double cellheight = imgheight / (maxvalue-minvalue);
		
		int lx = 0;
		int ly = (int) (((values[0] - minvalue) / (maxvalue-minvalue)) * imgheight);
		ly = imgheight - ly;
		for (int i = 0; i < values.length; i++)
		{
			int cx = (int) (center + i*stride);
			//int cy = (int) (((values[i] + minvalue) / (maxvalue-minvalue)) * imgheight);
			int cy = 0;
			
			if (values[i] > 0)
			{
				cy = (int) (((values[i] - minvalue) / (maxvalue-minvalue)) * imgheight);
				cy = imgheight - cy;
				
				//g.fillRect(cx, cy, (int) stride, (int) cellheight);
				g.drawLine(lx, ly, cx, cy);
			}
			
			lx = cx;
			ly = cy;
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
	
	
}
