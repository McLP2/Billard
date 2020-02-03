
import sum.kern.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;

public class Tisch
{
    //Objekte
    Buntstift meinStift;
    
    int links=0;
    int oben=0;
    int rechts=0;
    int unten=0;
    
    Image derPuffer;
    
    Vector2D[] loch = new Vector2D[6];
    
    boolean drawOK = false;
    
    // Konstruktor
    public Tisch(int x1,int y1,int x2,int y2)
    {
        meinStift = new Buntstift();
        meinStift.setzeFuellmuster(1);
        meinStift.setzeFarbe(Farbe.rgb(0,100,0));
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
    
    public void zeichne()
    {
        Graphics g = derPuffer.getGraphics();
        
        //zeichne hintergrund
        g.setColor(new Color(110,160,255));
        g.fillRect(0,0,2000,2000);
        //zeichne rahmen
        g.setColor(new Color(150,10,0));
        g.fillRect(links-20,oben-20,rechts-links+40,unten-oben+40);
        //zeichne feld
        g.setColor(new Color(0,100,0));
        g.fillRect(links,oben,rechts-links,unten-oben);
        //zeichne löcher
        g.setColor(new Color(0,10,0));
        for (int i = 0; i<loch.length; i++) {
            int r = 18;
            g.fillOval((int)loch[i].x()-r,(int)loch[i].y()-r,2*r,2*r);
        }
    
    }
    
    public void kenntPuffer(Image pUffer)
    {
        derPuffer = pUffer;
    }
    
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
