
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
/**
 * @author 
 * @version 
 */
public class Kugel
{
    // Objekte
    Image derPuffer;
    
    boolean eingelocht = false;
    int fakecounter = 0;
    
    Color color = new Color(0,0,0);
    int typ = 0; //0: Spielball; 1: Schwarze; 2: Rot; 3:Blau;
    public double rad = 10;
    
    public Vector2D pos;
    public Vector2D vel;
    public Vector2D acc;
    
    BufferedImage bild;
    
    boolean drawOK = false;
    // Konstruktor
    public Kugel()
    {
        pos = new Vector2D(0,0);
        vel = new Vector2D(0,0);
        acc = new Vector2D(0,0);
    }
    
    public Kugel(double x, double y)
    {
        pos = new Vector2D(x,y);
        vel = new Vector2D(0,0);
        acc = new Vector2D(0,0);
    }
    
    public Kugel(double x, double y, int t)
    {
        typ=t;
        switch (typ) {
            case 0: color = new Color(255,255,200); break;
            case 1: color = new Color(0,0,0); break;
            case 2: color = new Color(255,0,0); break;
            case 3: color = new Color(0,0,255); break;
            case 4: color = new Color(0,255,0); break;
        }
        switch (typ) {
            case 0: bild = ladeBild("weisseKugel.png"); break;
            case 1: bild = ladeBild("schwarzeKugel.png"); break;
            case 2: bild = ladeBild("roteKugel.png"); break;
            case 3: bild = ladeBild("blaueKugel.png"); break;
            case 4: bild = ladeBild("weisseKugel.png"); break;
        }
        pos = new Vector2D(x,y);
        vel = new Vector2D(0,0);
        acc = new Vector2D(0,0);
    }
    
    public Kugel(double x, double y, int t, double r)
    {
        typ=t;
        switch (typ) {
            case 0: color = new Color(255,255,200); break;
            case 1: color = new Color(0,0,0); break;
            case 2: color = new Color(255,0,0); break;
            case 3: color = new Color(0,0,255); break;
            case 4: color = new Color(0,255,0); break;
        }
        switch (typ) {
            case 0: bild = ladeBild("weisseKugel.png"); break;
            case 1: bild = ladeBild("schwarzeKugel.png"); break;
            case 2: bild = ladeBild("roteKugel.png"); break;
            case 3: bild = ladeBild("blaueKugel.png"); break;
            case 4: bild = ladeBild("weisseKugel.png"); break;
        }
        rad=r;
        pos = new Vector2D(x,y);
        vel = new Vector2D(0,0);
        acc = new Vector2D(0,0);
    }

    // Dienste
    
    public BufferedImage ladeBild(String path_and_name)
    {
        //erstell ein leeres image      
        BufferedImage image = null;
        //versuch, das Bild zu laden
       try {
            image = ImageIO.read(new File(path_and_name));
       } catch(IOException ioe) {
            System.out.println("Bild konnte nicht geladen werden!");  
       }
       if (image == null) {
            System.out.println("Bild ist null!");  
       }
       return image;
    }
    
    public void setpos(double x, double y)
    {
        pos.set(x,y);
    }
    
    public void update()
    {
        //Physiksimulation (geschwindigkeit & beschleunigung -> position)
        pos.add(vel);
        vel.add(acc);
        acc.set(0,0);
        //Reibung faken
        vel.mult(0.995);
        if ( Math.abs(vel.x())+Math.abs(vel.y()) < 0.05 ) {
            vel.mult(0.95); 
        }
        if ( Math.abs(vel.x())+Math.abs(vel.y()) < 0.015 ) { 
            vel.mult(0); 
        }
        //Kugel zeichnen
        zeichne();
        //schlag simulations kugel nach x frames deaktivieren
        if (typ==4 && !eingelocht) {fakecounter++;}
        if (fakecounter>14) {einlochen();}
    }
    
    public void kraft(Vector2D force)
    {
        acc.add(force); //kraft als beschleunigung (masse = 1)
    }
    
    public void kraft(double fX, double fY)
    {
        acc.add(fX,fY); //kraft als beschleunigung (masse = 1)
    }
    
    private void zeichne()
    {
        if (!eingelocht && typ != 4) {
            derPuffer.getGraphics().drawImage(bild,(int) (pos.x() - rad),(int) (pos.y() - rad),null);
        }
    }
    
    public void kenntPuffer(Image pUffer)
    {
        derPuffer = pUffer;
    }
    
    public void einlochen()
    {
        eingelocht = true;
        pos.set(0,0);
        vel.set(0,0);
        acc.set(0,0);
        fakecounter = 0;
    }
    
    public void auslochen()
    {
        eingelocht = false;
        pos.set(0,0);
        vel.set(0,0);
        acc.set(0,0);
        fakecounter= 0;
    }
    
    public void auslochen(double x, double y)
    {
        eingelocht = false;
        pos.set(x,y);
        vel.set(0,0);
        acc.set(0,0);
        fakecounter= 0;
    }
    
    public boolean eingelocht()
    {
        return eingelocht;
    }
    
    public int typ()
    {
        return typ;
    }
}