
/**
 * @author 
 * @version 
 */
public class Spieler
{
    // Attribute
    int farbe = -1;//0: rot; 1: blau; (-1: nicht gesetzt)
    String name = "";
    // Konstruktor
    public Spieler()
    {

    }

    // Dienste
    public void setzeFarbe(int i)
    {
        farbe = i;
    }
    
    public boolean farbeGesetzt()
    {
        return farbe != -1;
    }
    
    public int holeFarbe()
    {
        return farbe;
    }
    
    public void setzeName(String s)
    {
        name = s;
    }
    
    public String holeName()
    {
        return name;
    }

}
