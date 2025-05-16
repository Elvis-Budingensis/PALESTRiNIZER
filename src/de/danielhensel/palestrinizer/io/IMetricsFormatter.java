package de.danielhensel.palestrinizer.io;

import de.danielhensel.palestrinizer.MusicGrid;
import java.io.OutputStream;
import java.io.IOException;

public interface IMetricsFormatter 
{
	void write(MusicGrid musicgrid,OutputStream stream) throws IOException;
	
}
