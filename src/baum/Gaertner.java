package baum;


import org.apache.lucene.document.Document;

public class Gaertner<E extends Enum<E> & Ordner>
{
	final Knoten<E> root;
		
	public Gaertner(BaumStruktur<E> struktur)
	{
		if(struktur.statisch)
			root=new StatischerKnoten<E>(struktur.name, struktur);
		else if (struktur.getSuchFeld().isInt())
			root=new IntKnoten<E>(struktur.name, struktur);
		else
			root=new StringKnoten<E>(struktur.name, struktur);
		
	}

	public boolean add(Document doc)
	{
		return root.add(doc);
	}
	
	public void remove(Document doc)
	{
		root.remove(doc);
	}

	public Knoten<E> gebeWurzel()
	{
		return root;
	}
	
	public boolean contains(Document doc)
	{
		return root.contains(doc);
	}


}
