package de.danielhensel.palestrinizer;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import de.danielhensel.palestrinizer.io.IMetricsFormatter;
import de.danielhensel.palestrinizer.io.MidiFileReader;
import de.danielhensel.palestrinizer.io.PlaintextMetricsFormatter;
import de.danielhensel.palestrinizer.io.XMLMetricsFormatter;
import de.danielhensel.palestrinizer.io.XMLMetricsReader;

//sequences -mkeytoneprogression -g16 -w3 -l5 "D:\Dropbox\Palestrinizer 2.0\Analyse-Midi\Lassus\Motetten\gesamt\Fa-Gruppe\5.Modus\naturale\*.midi"
//compositesoundchart "D:\Dropbox\Palestrinizer 2.0\Analysen\Palestrina\Motetten\gesamt\einzeln\m4ntdiss-g_pal_gesamt_mot.png" "D:\Dropbox\Palestrinizer 2.0\Analysen\Palestrina\Motetten\gesamt\einzeln\m4ntmaj_pal_gesamt_mot.png" "D:\test.png"

public class Palestrinizer
{
	
	public static void printUsage(String level)
	{
		System.out.println("fehler");
	}
	
	
	
	public static void main(String[] args) throws Exception
	{
		if (args.length < 1)
		{
			printUsage("general");
			return;
		}
		
		for (int i = 0; i < args.length; i++)
		{
			System.out.print(args[i]+ " ");
		}
		System.out.println("");
		
		
		String subcommand = args[0];
		if (subcommand.equals("process"))
		{
			if (args.length < 2)
			{
				printUsage("process");
				return;
			}
			executeProcess(args);
			return;
		}

		if (subcommand.equals("batchprocess"))
		{
			executeBatchProcess(args);
			return;
		}
		
		if (subcommand.equals("difference"))
		{
			executeDifference(args);
			return;
		}

		if (subcommand.equals("aggregate"))
		{
			executeAggregate(args);
			return;
		}

		
		if (subcommand.equals("soundchart"))
		{
			executeBarchart(args);
			return;
		}

		if (subcommand.equals("linechart"))
		{
			executeLinechart(args);
			return;
		}
		
		if (subcommand.equals("sequences"))
		{
			executeSequences(args);
			return;
		}

		if (subcommand.equals("compositesoundchart"))
		{
			executeCompositeBarchart(args);
			return;
		}
		
		if (subcommand.equals("negativesoundchart"))
		{
			executeNegativeBarChart(args);
			return;
		}
		
		printUsage("general");
		return;
	}	


	
	
	
	//--------------------------------------------------------------------------------------
	// process a single file with output options
	//--------------------------------------------------------------------------------------
	private static void executeProcess(String[] args) throws Exception
	{
		//process [-o<filename>] -f<xml|text> -g<granularity> <infile>
		int granularity = 4;
		IMetricsFormatter formatter = new PlaintextMetricsFormatter();
		String inputfilename = null;
		String outputfilename = null;
		
		for (int i = 1; i < args.length; i++)
		{
			String opt = args[i].toLowerCase();

			// granularity
			if (opt.startsWith("-g"))
			{
				int g = Integer.parseInt( opt.substring(2) );
				if ( (g == 1) || (g == 2) ||
					 (g == 4) || (g == 8) ||
					 (g == 16) || (g == 32) )
				{
					granularity = g;
				}
				continue;
			}


			// output filename (prints to stdout if omitted)
			if (opt.startsWith("-o"))
			{
				outputfilename = opt.substring(2);
				continue;
			}
		
			// format (xml or text)
			if (opt.startsWith("-f"))
			{
				opt = opt.substring(2);
				if (opt.equals("xml")) formatter = new XMLMetricsFormatter();
				if (opt.equals("plaintext")) formatter = new PlaintextMetricsFormatter();
				continue;
			}
			
			// if its not an option, it has to be the input filename
			inputfilename = opt;
		}
		
		
		// no inputfile? no process!
		if (inputfilename == null)
		{
			printUsage("process");
			return;
		}
		
		OutputStream stream = System.out;
		
		if (outputfilename != null)
		{
			File f = new File(outputfilename);
			if (f.exists()) f.delete();
			stream = new FileOutputStream(f);
		}
		
		MidiFileReader mr = new MidiFileReader();
		MusicGrid grid = mr.readFromSelfDescribingFile(inputfilename,granularity);
		grid.write(formatter, stream);
		
		
		if (outputfilename != null)
		{
			stream.flush();
			stream.close();
		}
		
	}
	//--------------------------------------------------------------------------------------

	
	
	
	//--------------------------------------------------------------------------------------
	// process a batch of files based on filename patterns
	// files are saved in xml-format, output filename is inputfilename + .palestrinizer
	//--------------------------------------------------------------------------------------
	public static void executeBatchProcess(String[] args) throws Exception
	{
		//batchprocess -g<granularity> -r <infile> <infile> <infile> ...
		int granularity = 4;
		boolean recursive = false;
		String inputpatterns = "";
		IMetricsFormatter formatter = new XMLMetricsFormatter();
		boolean isxml = true;

		
		for (int i = 1; i < args.length; i++)
		{
			String opt = args[i].toLowerCase();

			// granularity
			if (opt.startsWith("-g"))
			{
				int g = Integer.parseInt( opt.substring(2) );
				if ( (g == 1) || (g == 2) ||
					 (g == 4) || (g == 8) ||
					 (g == 16) || (g == 32) )
				{
					granularity = g;
				}
				continue;
			}


			// recursive
			if (opt.startsWith("-r"))
			{
				recursive = true;
				continue;
			}

			// format (xml or text)
			if (opt.startsWith("-f"))
			{
				opt = opt.substring(2);
				if (opt.equals("xml")) 
				{
					formatter = new XMLMetricsFormatter();
					isxml = true;
				}
				if (opt.equals("plaintext"))
				{
					formatter = new PlaintextMetricsFormatter();
					isxml = false;
				}
				continue;
			}
			
			// if its not an option, it has to be the input filename
			if (inputpatterns.length() != 0) inputpatterns += File.pathSeparator;
			inputpatterns += args[i];
		}
		
		
		
		
		MidiFileReader mr = new MidiFileReader();
		
		String files[] = FileUtils.getFilesForPattern(inputpatterns, recursive);
		
		for (String filename: files)
		{
			System.out.print(filename);
			try
			{
				MusicGrid grid = mr.readFromSelfDescribingFile(filename,granularity);

				FileOutputStream fos;
				
				if (isxml)
				{
					File xmlfile = new File(filename + ".palestrinizer");
					if (xmlfile.exists()) xmlfile.delete();
					fos = new FileOutputStream(xmlfile);
				} else {
					File txtfile = new File(filename + ".txt");
					if (txtfile.exists()) txtfile.delete();
					fos = new FileOutputStream(txtfile);
					
				}
				
				
				grid.write(formatter, fos);
				fos.flush();
				fos.close();
				
				System.out.println(" : ok");
				
			} catch (Exception e) {
				System.out.println(" : failed (" + e.getMessage() + ")");
			}
		}
	}
	//--------------------------------------------------------------------------------------

	

	
	//--------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------
	public static void executeDifference(String[] args) throws Exception
	{
		//subtract [-o<filename>] -f<xml|text> <infile_a> <infile_b>
		IMetricsFormatter formatter = new PlaintextMetricsFormatter();
		String filea = null;
		String fileb = null;
		String outputfilename = null;
		boolean absolute = false;
		
		for (int i = 1; i < args.length; i++)
		{
			String opt = args[i].toLowerCase();

			// output filename (prints to stdout if omitted)
			if (opt.startsWith("-o"))
			{
				outputfilename = opt.substring(2);
				continue;
			}
		
			// format (xml or text)
			if (opt.startsWith("-f"))
			{
				opt = opt.substring(2);
				if (opt.equals("xml")) formatter = new XMLMetricsFormatter();
				if (opt.equals("plaintext")) formatter = new PlaintextMetricsFormatter();
				continue;
			}
			
			if (opt.startsWith("-abs"))
			{
				absolute = true;
				continue;
			}
			
			// if its not an option, it has to be the input filename
			if (filea == null) 
			{
				filea = opt;
				continue;
			}

			if (fileb == null) 
			{
				fileb = opt;
				continue;
			}
		}
		
		
		// no inputfile? no process!
		if ((filea == null) || (fileb == null))
		{
			printUsage("difference");
			return;
		}
		
		OutputStream stream = System.out;
		
		if (outputfilename != null)
		{
			File f = new File(outputfilename);
			if (f.exists()) f.delete();
			stream = new FileOutputStream(f);
		}
		
		XMLMetricsReader xmr = new XMLMetricsReader();
		MusicMetrics a = xmr.readFromFile( new File(filea) );
		MusicMetrics b = xmr.readFromFile( new File(fileb) );
		a.subtract(b);
		if (absolute) a.absolute();
		
		MusicGrid grid = new MusicGrid(10,4);
		grid.isaggregated = true;
		grid.metrics = a;
		grid.write(formatter, stream);
		
		
		if (outputfilename != null)
		{
			stream.flush();
			stream.close();
		}
	}
	//--------------------------------------------------------------------------------------

	
	
	
	
	
	
	public static void executeAggregate(String[] args) throws Exception
	{
		//batchprocess -g<granularity> -r <infile> <infile> <infile> ...
		boolean recursive = false;
		boolean average = false;
		int numfiles = 0;
		String inputpatterns = "";
		IMetricsFormatter formatter = new PlaintextMetricsFormatter();
		String outputfilename = null;

		for (int i = 1; i < args.length; i++)
		{
			String opt = args[i].toLowerCase();

			// recursive
			if (opt.startsWith("-r"))
			{
				recursive = true;
				continue;
			}
			
			if (opt.startsWith("-avg"))
			{
				average = true;
				continue;
			}

			
			// output filename (prints to stdout if omitted)
			if (opt.startsWith("-o"))
			{
				outputfilename = opt.substring(2);
				continue;
			}
		
			// format (xml or text)
			if (opt.startsWith("-f"))
			{
				opt = opt.substring(2);
				if (opt.equals("xml")) formatter = new XMLMetricsFormatter();
				if (opt.equals("plaintext")) formatter = new PlaintextMetricsFormatter();
				continue;
			}
			
			// if its not an option, it has to be the input filename
			if (inputpatterns.length() != 0) inputpatterns += File.pathSeparator;
			inputpatterns += args[i];
		}
		
		
		File outputfile = null;
		OutputStream stream = System.out;

		if (outputfilename != null)
		{
			outputfile = new File(outputfilename);
			if (outputfile.exists()) outputfile.delete();
			stream = new FileOutputStream(outputfile);
		}
		
		XMLMetricsReader xmr = new XMLMetricsReader();
		MusicMetrics result = new MusicMetrics();

		String files[] = FileUtils.getFilesForPattern(inputpatterns, recursive);
		
		for (String filename: files)
		{
			System.out.println("--------------");
			if (outputfile != null)
			{
				File testfile = new File(filename);
				if (testfile.getCanonicalPath().equals(outputfile.getCanonicalPath()) ) continue;
			}

			System.out.print(filename);
			try
			{
				
				MusicMetrics item = xmr.readFromFile( new File(filename) );
				result.add(item);
				numfiles++;
				System.out.println(" : ok");
				
			} catch (Exception e) {
				System.out.println(" : failed (" + e.getMessage() + ")");
			}
		}
		
		
		
		if (average)
		{
			result.divide(numfiles);

			// standardabweichung:
			MusicMetrics stddev = new MusicMetrics();
			numfiles = 0;
			
			for (String filename: files)
			{
				System.out.println("--------------");
				if (outputfile != null)
				{
					File testfile = new File(filename);
					if (testfile.getCanonicalPath().equals(outputfile.getCanonicalPath()) ) continue;
				}
	
				System.out.print(filename);
				try
				{
					
					MusicMetrics item = xmr.readFromFile( new File(filename) );
					item.subtract(result);
					item.square();
					
					stddev.add(item);
					numfiles++;
					System.out.println(" : ok");
					
				} catch (Exception e) {
					System.out.println(" : failed (" + e.getMessage() + ")");
				}
			}
			stddev.divide(numfiles);
			stddev.squareroot();
			
	    	for (CounterList l: stddev.items)
	    	{
	        	for (CounterList m: result.items)
	        	{
	        		if (!m.title.equals(l.title)) continue;
	        		m.injectStdDev(l);
	        	}
	    	}
	    	
	    	//------------------------------
		} else {
			result.dividePercentages(numfiles);
		}
		
		
		
		MusicGrid grid = new MusicGrid(10,4);
		grid.isaggregated = true;
		grid.metrics = result;
		grid.write(formatter, stream);
		 
		
		if (outputfilename != null)
		{
			stream.flush();
			stream.close();
		}
		
	}
	//--------------------------------------------------------------------------------------

	
	
	
	//--------------------------------------------------------------------------------------
	public static void executeBarchart(String[] args) throws Exception
	{
		//batchprocess -g<granularity> -r <infile> <infile> <infile> ...
		int granularity = 4;
		boolean recursive = false;
		String inputpatterns = "";
		String outputfilename = null;
		String mode = "pattern";
		
		int w = 0,h = 0,l = 0;
		String pattern = null;

		for (int i = 1; i < args.length; i++)
		{
			String opt = args[i].toLowerCase();

			// granularity
			if (opt.startsWith("-g"))
			{
				int g = Integer.parseInt( opt.substring(2) );
				if ( (g == 1) || (g == 2) ||
					 (g == 4) || (g == 8) ||
					 (g == 16) || (g == 32) )
				{
					granularity = g;
				}
				continue;
			}

			// recursive
			if (opt.startsWith("-r"))
			{
				recursive = true;
				continue;
			}
			
			// output filename (prints to stdout if omitted)
			if (opt.startsWith("-o"))
			{
				outputfilename = opt.substring(2);
				continue;
			}
			
			if (opt.startsWith("-w"))
			{
				w = Math.abs( Integer.valueOf(opt.substring(2)) );
			}
			if (opt.startsWith("-h"))
			{
				h = Math.abs( Integer.valueOf(opt.substring(2)) );
			}
		
			if (opt.startsWith("-l"))
			{
				l = Math.abs( Integer.valueOf(opt.substring(2)) );
			}

			if (opt.startsWith("-p"))
			{
				pattern = opt.substring(2);
				mode = "pattern";
			}
			
			if (opt.startsWith("-m"))
			{
				mode = opt.substring(2);
			}
		

			// if its not an option, it has to be the input filename
			if (inputpatterns.length() != 0) inputpatterns += File.pathSeparator;
			inputpatterns += args[i];
		}
		
		
		if (outputfilename == null) throw new Exception("No outputfile given");
		if ((w*h*l) <= 0) throw new Exception("Invalid parameters for width,height,length");
		
		if (mode.equals("pattern"))
		{
			if ((pattern == null) || (pattern.length() == 0)) throw new Exception("No pattern given");
		}
		
		String files[] = FileUtils.getFilesForPattern(inputpatterns, recursive);
		if (files.length <= 0) throw new Exception("No input files found");
		
		MidiFileReader mr = new MidiFileReader();
		BarChart br = new BarChart(w,h,l);
		
		for (String filename: files)
		{
			System.out.println("--------------");
			System.out.print(filename);
			
			MusicGrid grid = null;
			try
			{
				grid = mr.readFromSelfDescribingFile(filename,granularity);
			} catch (Exception e) {
				System.out.println(" : failed (" + e + ")");
			}

			if (grid == null) continue;
			
			if (mode.equals("pattern"))
			{
				br.label = "pattern: " + pattern;
				br.extractSound(grid, pattern);
			} else
			if (mode.equals("dissonancegrade"))
			{
				br.label = "dissonace-grade";
				br.extractDissonaceGrade(grid);
			} else
			if (mode.equals("crossings"))
			{
				br.label = "crossings";
				br.extractCrossings(grid);
			} else
			if (mode.equals("density"))
			{
				br.label = "density";
				br.extractDensity(grid);
			} else
			if (mode.equals("sentencedensity"))
			{
				br.label = "sentence-density";
				br.extractSentenceDensity(grid);
			}
			
				
		}
		
		br.renderChart( new Color(255,255,255), new Color(0,0,0) );
		br.finishRender(outputfilename);
		
	}
	//--------------------------------------------------------------------------------------

	
	
	//--------------------------------------------------------------------------------------
	public static void executeLinechart(String[] args) throws Exception
	{
		//batchprocess -g<granularity> -r <infile> <infile> <infile> ...
		int granularity = 4;
		boolean recursive = false;
		String inputpatterns = "";
		String outputfilename = null;
		
		int w = 0,h = 0,l = 0;
		String pattern = null;

		for (int i = 1; i < args.length; i++)
		{
			String opt = args[i].toLowerCase();

			// granularity
			if (opt.startsWith("-g"))
			{
				int g = Integer.parseInt( opt.substring(2) );
				if ( (g == 1) || (g == 2) ||
					 (g == 4) || (g == 8) ||
					 (g == 16) || (g == 32) )
				{
					granularity = g;
				}
				continue;
			}

			// recursive
			if (opt.startsWith("-r"))
			{
				recursive = true;
				continue;
			}
			
			// output filename (prints to stdout if omitted)
			if (opt.startsWith("-o"))
			{
				outputfilename = opt.substring(2);
				continue;
			}
			
			if (opt.startsWith("-w"))
			{
				w = Math.abs( Integer.valueOf(opt.substring(2)) );
			}
			if (opt.startsWith("-h"))
			{
				h = Math.abs( Integer.valueOf(opt.substring(2)) );
			}
		
			if (opt.startsWith("-l"))
			{
				l = Math.abs( Integer.valueOf(opt.substring(2)) );
			}

			if (opt.startsWith("-p"))
			{
				pattern = opt.substring(2);
			}
		

			// if its not an option, it has to be the input filename
			if (inputpatterns.length() != 0) inputpatterns += File.pathSeparator;
			inputpatterns += args[i];
		}
		
		
		if (outputfilename == null) throw new Exception("No outputfile given");
		if ((w*h*l) <= 0) throw new Exception("Invalid parameters for width,height,length");
		if ((pattern == null) || (pattern.length() == 0)) throw new Exception("No pattern given");
		
		String files[] = FileUtils.getFilesForPattern(inputpatterns, recursive);
		if (files.length <= 0) throw new Exception("No input files found");
		
		MidiFileReader mr = new MidiFileReader();
		LineChart lr = new LineChart(w,h,l);
		lr.clearImage( new Color(0,0,0,255) );
		
		int opacity = (int) (255 / files.length);
		
		for (String filename: files)
		{
			System.out.println("--------------");
			System.out.print(filename);
			try
			{
				MusicGrid grid = mr.readFromSelfDescribingFile(filename,granularity);
				//lr.extractDensities(grid);
				lr.extractPitch(grid, 0);
				lr.renderValues( new Color(255,255,255,opacity));
				
			} catch (Exception e) {
				System.out.println(" : failed (" + e.getMessage() + ")");
			}
		}
		
		lr.finishRender(outputfilename);
		
	}
	//--------------------------------------------------------------------------------------

	
	//--------------------------------------------------------------------------------------
		public static void executeNegativeBarChart(String[] args) throws Exception
		{
			//batchprocess -g<granularity> -r <infile> <infile> <infile> ...
			int granularity = 4;
			boolean recursive = false;
			String inputpatterns = "";
			String outputfilename = null;
			String mode = "pattern";
			
			int w = 0,h = 0,l = 0;
			String pattern = null;

			for (int i = 1; i < args.length; i++)
			{
				String opt = args[i].toLowerCase();

				// granularity
				if (opt.startsWith("-g"))
				{
					int g = Integer.parseInt( opt.substring(2) );
					if ( (g == 1) || (g == 2) ||
						 (g == 4) || (g == 8) ||
						 (g == 16) || (g == 32) )
					{
						granularity = g;
					}
					continue;
				}

				// recursive
				if (opt.startsWith("-r"))
				{
					recursive = true;
					continue;
				}
				
				// output filename (prints to stdout if omitted)
				if (opt.startsWith("-o"))
				{
					outputfilename = opt.substring(2);
					continue;
				}
				
				if (opt.startsWith("-w"))
				{
					w = Math.abs( Integer.valueOf(opt.substring(2)) );
				}
				if (opt.startsWith("-h"))
				{
					h = Math.abs( Integer.valueOf(opt.substring(2)) );
				}
			
				if (opt.startsWith("-l"))
				{
					l = Math.abs( Integer.valueOf(opt.substring(2)) );
				}

				if (opt.startsWith("-p"))
				{
					pattern = opt.substring(2);
					mode = "pattern";
				}
				
				if (opt.startsWith("-m"))
				{
					mode = opt.substring(2);
				}
			

				// if its not an option, it has to be the input filename
				if (inputpatterns.length() != 0) inputpatterns += File.pathSeparator;
				inputpatterns += args[i];
			}
			
			
			if (outputfilename == null) throw new Exception("No outputfile given");
			if ((w*h*l) <= 0) throw new Exception("Invalid parameters for width,height,length");
			
			if (mode.equals("pattern"))
			{
				if ((pattern == null) || (pattern.length() == 0)) throw new Exception("No pattern given");
			}
			
			String files[] = FileUtils.getFilesForPattern(inputpatterns, recursive);
			if (files.length <= 0) throw new Exception("No input files found");
			
			MidiFileReader mr = new MidiFileReader();
			BarChart br = new BarChart(w,h,l);
			
			for (String filename: files)
			{
				System.out.println("--------------");
				System.out.print(filename);
				
				MusicGrid grid = null;
				try
				{
					grid = mr.readFromSelfDescribingFile(filename,granularity);
				} catch (Exception e) {
					System.out.println(" : failed (" + e + ")");
				}

				if (grid == null) continue;
				
				if (mode.equals("pattern"))
				{
					br.label = "pattern: " + pattern;
					br.extractSound(grid, pattern);
				} else
				if (mode.equals("dissonancegrade"))
				{
					br.label = "dissonace-grade";
					br.extractDissonaceGrade(grid);
				} else
				if (mode.equals("crossings"))
				{
					br.label = "crossings";
					br.extractCrossings(grid);
				} else
				if (mode.equals("density"))
				{
					br.label = "density";
					br.extractDensity(grid);
				} else
				if (mode.equals("sentencedensity"))
				{
					br.label = "sentence-density";
					br.extractSentenceDensity(grid);
				}
				
					
			}
			
			br.renderChart( new Color(255,255,255), new Color(0,0,0) );
			br.finishRender(outputfilename);
			
		}
		//--------------------------------------------------------------------------------------
	
	
	public static void executeSequences(String[] args) throws Exception
	{
		//batchprocess -g<granularity> -r <infile> <infile> <infile> ...
		int granularity = 4;
		boolean recursive = false;
		int window = 2;
		int limit = 10;
		String inputpatterns = "";
		String outputfilename = null;
		int mode = 0;
		
		for (int i = 1; i < args.length; i++)
		{
			String opt = args[i].toLowerCase();

			// granularity
			if (opt.startsWith("-g"))
			{
				int g = Integer.parseInt( opt.substring(2) );
				if ( (g == 1) || (g == 2) ||
					 (g == 4) || (g == 8) ||
					 (g == 16) || (g == 32) )
				{
					granularity = g;
				}
				continue;
			}

			// recursive
			if (opt.startsWith("-r"))
			{
				recursive = true;
				continue;
			}
			
			// output filename (prints to stdout if omitted)
			if (opt.startsWith("-o"))
			{
				outputfilename = opt.substring(2);
				continue;
			}
			
			if (opt.startsWith("-w"))
			{
				window = Math.abs( Integer.valueOf(opt.substring(2)) );
			}

			if (opt.startsWith("-l"))
			{
				limit = Math.abs( Integer.valueOf(opt.substring(2)) );
			}
			
			if (opt.startsWith("-m"))
			{
				if (opt.equals("-mkeytoneprogression"))
				{
					mode = 1;
				} else {
					mode = 0;
				}
			}
			
			// if its not an option, it has to be the input filename
			if (inputpatterns.length() != 0) inputpatterns += File.pathSeparator;
			inputpatterns += args[i];
		}
		
		
		//if (outputfilename == null) throw new Exception("No outputfile given");
		
		System.out.println(inputpatterns);
		
		String files[] = FileUtils.getFilesForPattern(inputpatterns, recursive);
		if (files.length <= 0) return; //throw new Exception("No input files found");
		
		MidiFileReader mr = new MidiFileReader();
		SequenceAnalyser sa = new SequenceAnalyser(window,mode);
		
		for (String filename: files)
		{
			System.out.println("--------------");
			System.out.print(filename);
			
			MusicGrid grid = null;
			try
			{
				grid = mr.readFromSelfDescribingFile(filename,granularity);
			} catch (Exception e) {
				System.out.println(" : failed (" + e + ")");
			}

			if (grid == null) continue;
			
			sa.scan(grid);
		}
		

		File outputfile = null;
		OutputStream stream = System.out;

		if (outputfilename != null)
		{
			outputfile = new File(outputfilename);
			if (outputfile.exists()) outputfile.delete();
			stream = new FileOutputStream(outputfile);
		}
		
		sa.sortAndTruncate(limit,stream);

		if (outputfilename != null)
		{
			stream.flush();
			stream.close();
		}
		
		
	}
	//------------------
	//--------------------------------------------------------------------------------------
	public static void executeCompositeBarchart(String[] args) throws Exception
	{
		CompositeBarChart cbc = new CompositeBarChart(args[1],args[2]);
		cbc.combine(args[3]);
	}
	//--------------------------------------------------------------------------------------

	public static void executeNegativeBarchart(String[] args) throws Exception
	
	{
		//NegativeBarChart cbc = new NegativeBarChart(args[1],args[2]);
		//cbc.combine(args[3]);
	}
	//--------------------------------------------------------------------------------------
	
}


