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
		String temp = doc.get(suchfeld.getFeld());
		if (temp == null)
			return null;
		return temp.split(", ");
	}

}
