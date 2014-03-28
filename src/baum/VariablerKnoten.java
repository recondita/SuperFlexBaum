package baum;

import java.util.Collection;
import java.util.TreeMap;

import org.apache.lucene.document.Document;


public abstract class VariablerKnoten<T, E extends Enum<E>&Ordner> extends Knoten<E>
{

	protected Ordner suchfeld;
	private TreeMap<T, BaumTeil> kinder;

	public VariablerKnoten(String name, BaumStruktur<E> struktur)
	{
		super(name, struktur);
		suchfeld = struktur.getSuchFeld();
		kinder = new TreeMap<T, BaumTeil>();
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
		T[] temp = gebeFeld(doc);
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
					if (struktur.kinder.length > 0)
					{
						weiter = machSubKnoten(temp[i]);
						kinder.put(temp[i], weiter);
					} else
					{
						kinder.put(temp[i], new Blatt(temp[i] + "", doc.get(((E) E.valueOf(null, "DATEINAME")).getFeld())));
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

	private Knoten<E> machSubKnoten(T name)
	{
		if (struktur.kinder[0].statisch)
			return new StatischerKnoten<E>(name + "", struktur.kinder[0]);
		else if (struktur.kinder[0].getSuchFeld().isInt())
			return new IntKnoten<E>(name + "", struktur.kinder[0]);
		else
			return new StringKnoten<E>(name + "", struktur.kinder[0]);

	}

	@Override
	protected void remove(Document doc)
	{
		String temp = doc.get(suchfeld.getFeld());
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
		String temp = doc.get(suchfeld.getFeld());
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

	protected abstract T[] gebeFeld(Document doc);
}
