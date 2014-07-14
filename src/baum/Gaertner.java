package baum;


import org.apache.lucene.document.Document;

/**
 * 
 * @author Jan Hofmeier
 *
 * @param <E> Enum das moegliche Attribute enthaelt
 * 
 * Wrapper-Klasse des Baums, erm�glicht das veraendern und anlegen aus einem anderen Package heraus
 */
public class Gaertner<E extends Enum<E> & Ordner>
{
	final Knoten<E> root;
		
	public Gaertner(BaumStruktur<E> struktur)
	{
		if(struktur.statisch)
			root=new StatischerKnoten<E>(struktur.name, struktur);
		else 
			root=new VariablerKnoten<E>(struktur.name, struktur);
		
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
