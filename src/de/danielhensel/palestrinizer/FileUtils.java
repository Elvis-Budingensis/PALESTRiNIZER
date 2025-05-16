package de.danielhensel.palestrinizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FileUtils {

	public static String[] getFilesForPattern(String pattern, boolean recursive) throws IOException
	{
		ArrayList<String> resultset = new ArrayList<String>();
		
		String[] pathnames = pattern.split(File.pathSeparator);
		for (String path: pathnames)
		{
			scanFolder(path,resultset,recursive);
		}
		return resultset.toArray(new String[0]);
	}
	
	private static void scanFolder(String pattern,ArrayList<String> resultset,boolean recursive) throws IOException
	{
		if (pattern == null) return;

		File f;

		f = new File(pattern);
		if (f.exists() && f.isFile())
		{
			resultset.add(f.getCanonicalPath());
			return;
		}
		

		int p = pattern.lastIndexOf(File.separator);
		if (p == -1) return;
		
		String folder = pattern.substring(0,p+1);
		pattern = pattern.substring(p+1);
		
		f = new File(folder);
		File files[] = f.listFiles();
		
		if (files == null) return;
		
		String check;
		check = Pattern.quote(pattern).replace("*", "\\E.*\\Q").replace("?", "\\E.\\Q");
	
		for (File item: files)
		{
			if (item.isDirectory())
			{
				if (recursive)
				{
					scanFolder(folder + item.getName() + File.separator + pattern,resultset,true);
				}
			} else {
				
				
				if (item.getName().matches(check))
				{
					resultset.add(item.getCanonicalPath());
				}
			}
		}
		
	}

}
