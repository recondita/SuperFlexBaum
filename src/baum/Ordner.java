package baum;

/**
 * 
 * @author Jan Hofmeier
 * 
 * Behinhaltet die Methoden die ein Enum zur Verfuegung stellen muss, damit es vom Baum verwendet werden kann.
 *
 */
public interface Ordner
{

	public abstract String getFeld();

	public abstract boolean isInt();
	
	public abstract int maxStellen();
	
	public abstract boolean absteigend();


}
