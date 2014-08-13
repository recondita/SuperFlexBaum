package baum;

import java.text.Collator;
import java.util.Collection;
import java.util.Locale;
import java.util.TreeMap;

import org.apache.lucene.document.Document;

/**
 * 
 * @author Jan Hofmeier
 *
 * @param <T>
 *            String/Int
 * @param <E>
 *            Enum das die moeglichen Attribute enthaelt.
 */
public class VariablerKnoten<E extends Enum<E> & Ordner> extends Knoten<E> {

	protected Ordner[] suchFelder;
	protected int hauptSuchFeld;
	private TreeMap<String, BaumTeil> kinder;

	protected VariablerKnoten(String name, BaumStruktur<E> struktur) {
		super(name, struktur);
		suchFelder = struktur.getSuchFelder();
		hauptSuchFeld = struktur.getHauptSuchFeld();
		Collator collator = Collator.getInstance(Locale.GERMAN);
		collator.setStrength(Collator.SECONDARY);
		kinder = new TreeMap<String, BaumTeil>(collator);
	}

	@Override
	public BaumTeil[] gebeKinder() {
		Collection<BaumTeil> temp = kinder.values();
		return temp.toArray(new BaumTeil[temp.size()]);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean add(Document doc) {
		String[] temp = gebeFeld(doc);
		boolean ret = false;
		if (temp != null) {
			// String[] destlist = temp.split(", ");
			for (int i = 0; i < temp.length; i++) {
				Knoten<E> weiter = null;
				if (temp[i] == null)
					continue;
				if (!kinder.containsKey(temp[i])) {
					//String name = gebeName(doc, i);
					if (struktur.kinder.length > 0) {
						weiter = machSubKnoten(temp[i]);
						kinder.put(temp[i], weiter);
					} else {
						kinder.put(
								temp[i],
								new Blatt(temp[i], doc.get(struktur
										.getValueFlield().getFeld())));
						ret = true;
					}
				} else {
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

	private Knoten<E> machSubKnoten(String name) {
		if (struktur.kinder[0].statisch)
			return new StatischerKnoten<E>(name + "", struktur.kinder[0]);
		else
			return new VariablerKnoten<E>(name + "", struktur.kinder[0]);
	}

	@Override
	protected void remove(Document doc) {
		String temp = doc.get(suchFelder[hauptSuchFeld].getFeld());
		if (temp != null && temp.length() > 0 && !temp.equals("-1")) {
			String[] destlist = temp.split(", ");
			for (String dest : destlist) {
				if (kinder.containsKey(dest)) {
					if (struktur.kinder.length > 0) {
						@SuppressWarnings("unchecked")
						Knoten<E> next = ((Knoten<E>) kinder.get(dest));
						next.remove(doc);
						if (next.isEmpty())
							kinder.remove(dest);

					} else {
						kinder.remove(dest);
					}
				}

			}

		}
	}

	public boolean isEmpty() {
		return kinder.isEmpty();
	}

	@Override
	public boolean contains(Document doc) {
		/*
		 * String temp = doc.get(suchFelder[hauptSuchFeld].getFeld()); if (temp
		 * != null && temp.length() > 0 && !temp.equals("-1")) {
		 */
		String[] destlist = gebeFeld(doc); // temp.split(", ");
		for (String dest : destlist) {
			if (kinder.containsKey(dest)) {
				if (struktur.kinder.length > 0) {
					@SuppressWarnings("unchecked")
					Knoten<E> next = ((Knoten<E>) kinder.get(dest));
					if (next.contains(doc))
						return true;

				} else {
					if (kinder.containsKey(dest))
						return true;
				}
			}

		}

		return false;
	}

	/*
	 * protected String gebeName(Document doc, int suchfeld) { StringBuffer
	 * ret=new StringBuffer(); for(int i=0; i<suchFelder.length; i++) {
	 * ret.append
	 * (doc.getValues(suchFelder[i].getFeld())[(suchfeld==hauptSuchFeld
	 * )?suchfeld:0]); ret.append(" "); } return ret.toString(); }
	 */

	protected String[] gebeFeld(Document doc) {
		String[] ret = doc.getValues(suchFelder[hauptSuchFeld].getFeld());
		if (suchFelder.length == 1)
		{
			if(suchFelder[hauptSuchFeld].isInt())
			{
				for(int i=0; i<ret.length;i++)
					ret[i]=vorLeer(ret[i],suchFelder[hauptSuchFeld].maxStellen());
			}
			return ret;
		}

		String pre;
		if (hauptSuchFeld > 0) {
			StringBuffer preSB = new StringBuffer();
			for (int i = 0; i < hauptSuchFeld; i++) {
				String[] temp = doc.getValues(suchFelder[i].getFeld());
				for (String val : temp) {
					preSB.append(suchFelder[i].isInt()?vorLeer(val,suchFelder[i].maxStellen()):val);
					preSB.append(" ");
				}
			}
			pre = preSB.toString();
		} else {
			if(suchFelder[hauptSuchFeld].isInt())
			{
				for(int i=0; i<ret.length;i++)
					ret[i]=vorLeer(ret[i],suchFelder[hauptSuchFeld].maxStellen());
			}
			pre = "";
		}

		String suff;
		if (suchFelder.length > hauptSuchFeld + 1) {
			StringBuffer suffSB = new StringBuffer();
			for (int i = hauptSuchFeld + 1; i < suchFelder.length; i++) {
				String[] temp = doc.getValues(suchFelder[i].getFeld());
				for (String val : temp) {
					suffSB.append(val);
					suffSB.append(" ");
				}

			}
			suff = suffSB.toString();
		} else {
			suff = "";
		}

		for (int i = 0; i < ret.length; i++) {
			StringBuffer temp = new StringBuffer();
			temp.append(pre);
			temp.append(ret[i]);
			if (!suff.equals("")) {
				temp.append(" ");
				temp.append(suff);
			}
			ret[i]=temp.toString();
		}
		return ret;
	}
	
	
	protected String vorLeer(String str, int length)
	{
		if(str.length()==length)
			return str;
		for(int i=0; i<str.length(); i++)
			if(str.charAt(i)<0x30||str.charAt(i)>0x39)
				return str;
		
		StringBuffer ret=new StringBuffer();
		for(int i=str.length(); i<length;i++)
		{
			ret.append(" ");
		}
		ret.append(str);
		return ret.toString();
	}
}
