package baum;


import org.apache.lucene.document.Document;


public abstract class Knoten<E extends Enum<E> & Ordner> extends BaumTeil
{
	
	protected BaumStruktur<E> struktur;
	
	public Knoten(String name, BaumStruktur<E> struktur)
	{
		super(name, false);
		this.struktur=struktur;
	}


	public abstract BaumTeil[] gebeKinder();

	protected abstract boolean add(Document doc);

	public boolean istAusgeklappt()
	{		
		return struktur.isExpanded();
	}

	protected abstract void remove(Document doc);
	
	public abstract boolean isEmpty();
	
	public abstract boolean contains(Document doc);

}
