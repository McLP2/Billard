import java.math.*;

public class Vector2D
{
    double x;
    double y;

    public Vector2D()
    {
        x=0;
        y=0;
    }

    public Vector2D(double initx, double inity)
    {
        x=initx;
        y=inity;
    }
    
    public Vector2D add(Vector2D v)
    {
        x=x+v.x();  
        y=y+v.y();   
        return this; 
    }
    
    public Vector2D add(double addx, double addy)
    {
        x+=addx;
        y+=addy;
        return this; 
    }
    
    public Vector2D mult(double d)
    {
        x=x*d; 
        y=y*d;   
        return this;
    }
    
    public Vector2D set(double setx, double sety)
    {
        x=setx;
        y=sety;
        return this;
    }
    
    public Vector2D set(Vector2D v)
    {
        x=v.x();
        y=v.y();
        return this;
    }
    
    public double x()
    {
        return x;       
    }
    
    public double winkel()
    {
        return Math.atan2(x,y);       
    }
    
    public double laenge()
    {
        return Math.sqrt(y*y+x*x);       
    }
    
    public Vector2D setzeLaenge(double len)
    {
        double rot = winkel();
        x=Math.cos(rot)*len;
        y=Math.sin(rot)*len;
        return this;
    }
    
    public double y()
    {
        return y;       
    }
}
