package baum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * 
 * @author Jan Hofmeier
 *
 * @param <E>
 *            Enum das die die moeglichen Attribute enthaelt.
 * 
 *            Liest die Struktur des Baumes aus einer Datei um sie dem Baum zur
 *            verf�gung zu stellen.
 */
public class BaumStruktur<E extends Enum<E> & Ordner> {

	protected BaumStruktur<E>[] kinder;
	final boolean statisch;
	private Ordner[] suchFelder;
	private int hauptSuchFeld;
	private final boolean expanded;
	public final String name;
	private Enum<E> valueField;

	protected BaumStruktur(String pName, BufferedReader struktur,
			boolean expanded, E value) throws IOException, BaumConfigException {
		this.name = pName;
		this.expanded = expanded;
		this.statisch = build(struktur, value);
	}

	private boolean build(BufferedReader struktur, E value) throws IOException,
			BaumConfigException {
		boolean temp = true;
		ArrayList<BaumStruktur<E>> kinder = new ArrayList<BaumStruktur<E>>();
		String line = schneide(struktur.readLine());
		while (line != null && !line.contains("}")) {
			String nextline = schneide(struktur.readLine());
			if (line.length() > 0) {
				String name = null;
				boolean nextexpanded = line.charAt(line.length() - 1) == '+';
				if (temp && line.charAt(0) == '*') {
					int start = 1;
					int end = nextexpanded ? line.length() - 1 : line.length();
					int count = 1;
					int count2 = 1;
					for (int i = start; i < end; i++) {
						if (line.charAt(i) == '[')
							count++;
						else if (line.charAt(i) == ']')
							count2++;
					}
					if (count != count2)
						throw new BaumConfigException();
					suchFelder = new Ordner[count];

					count = 0;
					hauptSuchFeld = -1;
					for (int i = start; i < end; i++, count++) {
						start = (line.charAt(i) == '[') ? ++i : i;
						while (i < end && line.charAt(i) != ']'
								&& line.charAt(i) != '[') {
							i++;
						}
						if ((i==end||line.charAt(i) != ']') && hauptSuchFeld < 0)
							hauptSuchFeld = count;
						suchFelder[count] = E.valueOf(
								value.getDeclaringClass(),
								line.substring(start, i));
					}

					// suchfeld = E.valueOf(value.getDeclaringClass(),
					// nextexpanded?line.substring(1,line.length()-1):line.substring(1));
					valueField = value;
					temp = false;
				} else {
					name = nextexpanded ? line.substring(0, line.length() - 2)
							: line;
				}
				if (nextline != null && nextline.contains("{"))
					kinder.add(new BaumStruktur<E>(name, struktur,
							nextexpanded, value));
			}
			line = nextline;
		}
		// BaumStruktur[] a=BaumStruktur[kinder.size()];
		@SuppressWarnings("unchecked")
		BaumStruktur<E>[] a = (BaumStruktur[]) Array.newInstance(
				(Class<BaumStruktur<E>>) this.getClass(), kinder.size());
		this.kinder = kinder.toArray(a);
		return temp;

	}

	@SuppressWarnings("resource")
	public static <I extends Enum<I> & Ordner> BaumStruktur<I> ladeStruktur(
			File f, I value) throws FileNotFoundException, BaumConfigException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			if (!"#Baummodel".equals(br.readLine()))
				throw new BaumConfigException();
			String line = null;
			do {
				line = schneide(br.readLine());
				if (line == null)
					throw new BaumConfigException();
			} while (line.length() == 0);
			boolean expanded = line.charAt(line.length() - 1) == '+';
			return new BaumStruktur<I>(expanded ? line.substring(0,
					line.length() - 1) : line, br, expanded, value);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BaumConfigException(e);
		}

	}

	protected Ordner[] getSuchFelder() {
		return suchFelder;
	}

	protected int getHauptSuchFeld()
	{
		return hauptSuchFeld;
	}
	
	
	private static String schneide(String str) {
		if (str != null)
			str = str.replaceAll(" ", "").replaceAll("\t", "").split("//")[0];
		return str;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public Ordner getValueFlield() {
		return (Ordner) valueField;
	}

	public static class BaumConfigException extends Exception {
		private static final long serialVersionUID = 1L;

		protected BaumConfigException(Exception e) {
			super(e);
		}

		protected BaumConfigException() {

		}
	}
}
