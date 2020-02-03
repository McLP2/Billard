
/**
 * @author 
 * @version 
 */
public class Spieler
{
    // Attribute
    private int farbe = -1;//0: rot; 1: blau; (-1: nicht gesetzt)
    private String name = "";
    private String ip = "";
    private int port = -1;
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
    
    public void setzeIP(String s)
    {
        ip = s;
    }
    
    public String holeIP()
    {
        return ip;
    }
    
    public void setzePort(int p)
    {
        port = p;
    }
    
    public int holePort()
    {
        return port;
    }

}
