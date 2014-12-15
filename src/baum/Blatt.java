package baum;

import java.util.HashSet;


public class Blatt extends BaumTeil
{
	public final String fileName;
	public Blatt(String name,String fileName)
	{
		super(name,true);
		this.fileName=fileName;
	}
	public void getFileStrings(HashSet<String> collector)
	{
		collector.add(getFileName());
	}
	
	public String getFileName()
	{
		return fileName;
	}
	


}
