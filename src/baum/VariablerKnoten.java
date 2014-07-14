package baum;

import java.util.Collection;
import java.util.TreeMap;

import org.apache.lucene.document.Document;

/**
 * 
 * @author Jan Hofmeier
 *
 * @param <T> String/Int
 * @param <E> Enum das die moeglichen Attribute enthaelt.
 */
public class VariablerKnoten<E extends Enum<E>&Ordner> extends Knoten<E>
{

	protected Ordner[] suchFelder;
	protected int hauptSuchFeld;
	private TreeMap<String, BaumTeil> kinder;

	protected VariablerKnoten(String name, BaumStruktur<E> struktur)
	{
		super(name, struktur);
		suchFelder = struktur.getSuchFelder();
		hauptSuchFeld=struktur.getHauptSuchFeld();
		kinder = new TreeMap<String, BaumTeil>();
	}

	@Override
	public BaumTeil[] gebeKinder()
	{
		Collection<BaumTeil> temp = kinder.values();
		return temp.toArray(new BaumTeil[temp.size()]);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean add(Document doc)
	{
		String[] temp = gebeFeld(doc);
		boolean ret = false;
		if (temp != null)
		{
			// String[] destlist = temp.split(", ");
			for (int i=0; i< temp.length;i++)
			{
				Knoten<E> weiter = null;
				if(temp[i]==null)
					continue;
				if (!kinder.containsKey(temp[i]))
				{
					String name=gebeName(doc,i);
					if (struktur.kinder.length > 0)
					{
						weiter = machSubKnoten(name);
						kinder.put(temp[i], weiter);
					} else
					{
						kinder.put(temp[i], new Blatt(name, doc.get(struktur.getValueFlield().getFeld())));
						ret = true;
					}
				} else
				{
					if (struktur.kinder.length > 0)
						weiter = (Knoten<E>) kinder.get(temp[i]);
					else
						ret = false;
				}
				if (struktur.kinder.length > 0)
					ret = weiter.add(doc) | ret;

			}

		} else
			return false;
		return ret;
	}

	private Knoten<E> machSubKnoten(String name)
	{
		if (struktur.kinder[0].statisch)
			return new StatischerKnoten<E>(name + "", struktur.kinder[0]);
		else 
			return new VariablerKnoten<E>(name + "", struktur.kinder[0]);
	}

	@Override
	protected void remove(Document doc)
	{
		String temp = doc.get(suchFelder[hauptSuchFeld].getFeld());
		if (temp != null && temp.length() > 0 && !temp.equals("-1"))
		{
			String[] destlist = temp.split(", ");
			for (String dest : destlist)
			{
				if (kinder.containsKey(dest))
				{
					if (struktur.kinder.length > 0)
					{
						@SuppressWarnings("unchecked")
						Knoten<E> next = ((Knoten<E>) kinder.get(dest));
						next.remove(doc);
						if (next.isEmpty())
							kinder.remove(dest);

					} else
					{
						kinder.remove(dest);
					}
				}

			}

		}
	}

	public boolean isEmpty()
	{
		return kinder.isEmpty();
	}

	@Override
	public boolean contains(Document doc)
	{
		String temp = doc.get(suchFelder[hauptSuchFeld].getFeld());
		if (temp != null && temp.length() > 0 && !temp.equals("-1"))
		{
			String[] destlist = temp.split(", ");
			for (String dest : destlist)
			{
				if (kinder.containsKey(dest))
				{
					if (struktur.kinder.length > 0)
					{
						@SuppressWarnings("unchecked")
						Knoten<E> next = ((Knoten<E>) kinder.get(dest));
						if (next.contains(doc))
							return true;

					} else
					{
						if (kinder.containsKey(dest))
							return true;
					}
				}

			}

		}
		return false;
	}

	protected String gebeName(Document doc, int nummer)
	{
		StringBuffer ret=new StringBuffer();
		for(int i=0; i<suchFelder.length; i++)
		{
			ret.append(doc.getValues(suchFelder[i].getFeld())[(nummer==hauptSuchFeld)?nummer:0]);
			ret.append(" ");
		}
		return ret.toString();
	}
	
	protected String[] gebeFeld(Document doc)
	{
		return doc.getValues(suchFelder[hauptSuchFeld].getFeld());
	}
}
