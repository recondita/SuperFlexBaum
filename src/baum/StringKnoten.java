package baum;

import org.apache.lucene.document.Document;

public class StringKnoten<E extends Enum<E>&Ordner> extends VariablerKnoten<String,E>
{

	public StringKnoten(String name, BaumStruktur<E> struktur)
	{
		super(name, struktur);
	}

	@Override
	protected String[] gebeFeld(Document doc)
	{
		return doc.getValues(suchFelder[hauptSuchFeld].getFeld());
	}

}
