
import sum.kern.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;

public class Queue
{
    Buntstift meinStift;
    boolean drawOK = false;
    BufferedImage image;
    Image derPuffer;
    // Konstruktor
    public Queue()
    {
        image = ladeBild("queue.png");
        meinStift = new Buntstift();
    }
    
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
    
    public void zeichne(double x1,double y1,double x2,double y2,double d,int p,String s)
    {   
        Graphics g = derPuffer.getGraphics();
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        if (p == 0) {
            g.setColor(new Color(200,180,0));
        }
        else {
            g.setColor(new Color(0,180,200));
        }
    
        //Namen
        g.drawString(s,(int)x2+20,(int)y2-20);
    
        double winkel = Math.atan2(y2 - y1, x2 - x1);
        //Queue zeichnen, geht garantiert besser und schöner und schneller
        double rotationRequired = winkel-Math.PI/2;
        double locationX = image.getHeight();
        double locationY = image.getHeight();
        AffineTransform at = new AffineTransform();
        at.translate(locationX, locationY);
        at.rotate(rotationRequired);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        // (int)x1, (int)(y1-d)
        // Drawing the rotated image at the required drawing locations
        Graphics2D g2d = (Graphics2D) derPuffer.getGraphics();
        double x = x1-locationX-Math.cos(rotationRequired)*(image.getWidth()/2)+Math.cos(winkel)*d; //+0.5*image.getWidth()
        double y = y1-locationY-Math.sin(rotationRequired)*(image.getWidth()/2)+Math.sin(winkel)*d; //+0.5*image.getWidth()
        g2d.drawImage(op.filter(image,null), (int) x, (int) y, null);
    
    }
    
    public void kenntPuffer(Image pUffer)
    {
        derPuffer = pUffer;
    }
}
