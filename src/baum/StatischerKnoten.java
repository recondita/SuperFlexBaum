package baum;

import org.apache.lucene.document.Document;

public class StatischerKnoten<E extends Enum<E> & Ordner> extends Knoten<E>
{

	private Knoten<E>[] kinder;

	public StatischerKnoten(String name, BaumStruktur<E> struktur)
	{
		super(name, struktur);
		@SuppressWarnings("unchecked")
		Knoten<E>[] kinder=(Knoten<E>[])new Knoten[struktur.kinder.length];
		this.kinder =kinder; 
		for (int i = 0; i < kinder.length; i++)
		{
			if (struktur.kinder[i].statisch)
				kinder[i] = new StatischerKnoten<E>(struktur.kinder[i].name, struktur.kinder[i]);
			else if (struktur.kinder[i].getSuchFelder()[0].isInt())
				kinder[i] = new IntKnoten<E>(struktur.kinder[i].name, struktur.kinder[i]);
			else
				kinder[i] = new StringKnoten<E>(struktur.kinder[i].name, struktur.kinder[i]);

		}
	}



	@Override
	public BaumTeil[] gebeKinder()
	{
		return kinder;
	}



	@Override
	protected boolean add(Document doc)
	{
		boolean ret=false;
		for(Knoten<E> kind: kinder)
			ret=kind.add(doc)||ret;					
		return ret;
	}



	@Override
	protected void remove(Document doc)
	{
		for(Knoten<E> kind: kinder)
		{
			kind.remove(doc);
		}		
	}



	@Override
	public boolean isEmpty()
	{
		for(Knoten<E> kind: kinder)
			if(!kind.isEmpty())
				return false;
		return true;
				
	}



	@Override
	public boolean contains(Document doc)
	{
		for(Knoten<E> kind: kinder)
		{
			if(kind.contains(doc));
		}
		return false;
	}

}
