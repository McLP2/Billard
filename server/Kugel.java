
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
    
    boolean eingelocht = false;
    int fakecounter = 0;
    
    int typ = 0; //0: Spielball; 1: Schwarze; 2: Rot; 3:Blau;
    public double rad = 10;
    
    public Vector2D pos;
    public Vector2D vel;
    public Vector2D acc;
    
    
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
        pos = new Vector2D(x,y);
        vel = new Vector2D(0,0);
        acc = new Vector2D(0,0);
    }
    
    public Kugel(double x, double y, int t, double r)
    {
        typ=t;
        rad=r;
        pos = new Vector2D(x,y);
        vel = new Vector2D(0,0);
        acc = new Vector2D(0,0);
    }

    // Dienste
    
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