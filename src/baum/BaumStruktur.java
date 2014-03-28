package baum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class BaumStruktur<E extends Enum<E> & Ordner>
{

	protected BaumStruktur<E>[] kinder;
	final boolean statisch;
	private Enum<E> suchfeld;
	private boolean expanded;
	public final String name;
	private Enum<E> valueField;

	protected BaumStruktur(String name, BufferedReader struktur, boolean expanded, E value) throws IOException
	{
		this.name = name;
		this.expanded=expanded;
		this.statisch = build(struktur,value);
	}


	private boolean build(BufferedReader struktur, E value) throws IOException
	{
		boolean temp = true;
		ArrayList<BaumStruktur<E>> kinder = new ArrayList<BaumStruktur<E>>();
		String line = schneide(struktur.readLine());
		while (line != null && !line.contains("}"))
		{
			String nextline = schneide(struktur.readLine());
			if (line.length() > 0)
			{
				String name = null;
				boolean expanded=line.charAt(line.length()-1)=='+';
				if (temp && line.charAt(0) == '*')
				{					
					suchfeld = E.valueOf(value.getDeclaringClass(), expanded?line.substring(1,line.length()-1):line.substring(1));
					valueField=value;
					temp = false;
				} else
				{
					name = expanded?line.substring(0,line.length()-2):line;
				}
				if (nextline != null && nextline.contains("{"))
					kinder.add(new BaumStruktur<E>(name, struktur,expanded,value));
			}
			line = nextline;
		}
		//BaumStruktur[] a=BaumStruktur[kinder.size()];
		@SuppressWarnings("unchecked")
		BaumStruktur<E>[] a=(BaumStruktur[]) Array.newInstance((Class<BaumStruktur<E>>)this.getClass(), kinder.size());
		this.kinder =  kinder.toArray(a) ;
		return temp;
	}


	@SuppressWarnings("resource")
	public static <I extends Enum<I>& Ordner>BaumStruktur<I> ladeStruktur(File f, I value) throws FileNotFoundException, BaumConfigException
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(f));
			if (!"#Baummodel".equals(br.readLine()))
				throw new BaumConfigException();
			String line=null;
			do{
			line=schneide(br.readLine());
			if(line==null)
				throw new BaumConfigException();
			}while(line.length()==0);
			boolean expanded=line.charAt(line.length()-1)=='+';
			return new BaumStruktur<I>(expanded?line.substring(0,line.length()-1):line, br,expanded, value);

		} catch (Exception e)
		{
			e.printStackTrace();
			throw new BaumConfigException(e);
		}

	}

	public Ordner getSuchFeld()
	{
		return (Ordner) suchfeld;
	}

	private static String schneide(String str)
	{
		if (str != null)
			str = str.replaceAll(" ", "").replaceAll("\t", "").split("//")[0];
		return str;
	}

	public boolean isExpanded()
	{
		return expanded;
	}

	public Ordner getValueFlield()
	{
		return (Ordner) valueField;
	}

	public static class BaumConfigException extends Exception
	{
		private static final long serialVersionUID = 1L;
		protected BaumConfigException(Exception e){
			super(e);
		}
		
		protected BaumConfigException(){
		
		}
	}
}
