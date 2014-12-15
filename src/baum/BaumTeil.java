package baum;

import java.util.HashSet;


public abstract class BaumTeil
{
	public final boolean isLeaf;
	public final String name;
	
	public BaumTeil(String name,boolean isLeaf)
	{
		this.isLeaf=isLeaf;
		this.name=name;
	}
	
	public abstract void getFileStrings(HashSet<String> collector);
	
	public BaumTeil[] gebeKinder()
	{
		return null;		
	}
	
	public String toString()
	{
		return name;
	}
	
	public boolean isLeaf()
	{
		return isLeaf;
	}
	
	public String getFileName()
	{
		return null;
	}
	


}
