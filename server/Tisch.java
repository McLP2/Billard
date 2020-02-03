

public class Tisch
{
    //Objekte
    
    int links=0;
    int oben=0;
    int rechts=0;
    int unten=0;
    
    Vector2D[] loch = new Vector2D[6];
    
    
    // Konstruktor
    public Tisch(int x1,int y1,int x2,int y2)
    {
        //Setze Rand
        links=x1;
        oben=y1;
        rechts=x2;
        unten=y2;
        //Platziere löcher
        loch[0]=new Vector2D(links,oben);
        loch[1]=new Vector2D((rechts-links)/2+links,oben);
        loch[2]=new Vector2D(rechts,oben);
        loch[3]=new Vector2D(rechts,unten);
        loch[4]=new Vector2D((rechts-links)/2+links,unten);
        loch[5]=new Vector2D(links,unten);
    }

    // Dienste
    
    public double linkeKante()
    {
        return(links);
    }
    
    public Vector2D loch(int i)
    {
        return(loch[i]);
    }
    
    public double obereKante()
    {
        return(oben);
    }
    
    public double rechteKante()
    {
        return(rechts);
    }
    
    public double untereKante()
    {
        return(unten);
    }

}
