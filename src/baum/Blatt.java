package baum;


public class Blatt extends BaumTeil
{
	public final String fileName;
	public Blatt(String name,String fileName)
	{
		super(name,true);
		this.fileName=fileName;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	


}
