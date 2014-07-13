package baum;

import org.apache.lucene.document.Document;

public class IntKnoten<E extends Enum<E> &Ordner> extends VariablerKnoten<Integer,E>
{

	protected IntKnoten(String name, BaumStruktur<E> struktur)
	{
		super(name, struktur);
	}

	@Override
	protected Integer[] gebeFeld(Document doc)
	{

		String[] tempa = doc.getValues(suchFelder[hauptSuchFeld].getFeld());
		Integer[] i = new Integer[tempa.length];
		for (int j = 0; j < i.length; j++)
			try
			{
				i[j] = Integer.parseInt(tempa[j]);
			} catch (Exception e)
			{
			}
		return i;
	}

}
