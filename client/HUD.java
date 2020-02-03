
import sum.werkzeuge.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;
/**
 * @author 
 * @version 
 */
public class HUD
{
    // Objekte
    Uhr dieUhr;
    int rot = 0;
    int blau = 0;
    int schwarz = 0;
    int frames = 0;
    double lastmillis_fps = 0;
    double lastmillis_msg = 0;
    double power = 0;
    boolean drawOK = false;
    boolean showPower = false;
    int msgState = 0;
    int playerColors = -1; //-1 not set; 0 spieler 1 rot, spieler 2 blau; 1 spieler 1 blau, spieler 2 rot
    String gewinnerName = "";
    String disconnectName = "";
    String playerName1 = "Spieler 1";
    String playerName2 = "Spieler 2";
    
    Image derPuffer;

    // Konstruktor
    public HUD()
    {
        dieUhr = new Uhr();
    }

    // Dienste
    public void zeichne()
    {
        // Aktionsteil
        Graphics g = derPuffer.getGraphics();
        
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 26));
        switch (msgState) {
            case 1: g.drawString("FOUL!",600,500); break;
            case 2: g.drawString(gewinnerName+" hat GEWONNEN!",600,500); break;
            case 3: g.drawString(disconnectName+" hat (die Verbindung) VERLOREN!",500,500); break;
        }
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    
        g.drawString("Rote: "+Integer.toString(rot),50,100);
    
        g.drawString("Blaue: "+Integer.toString(blau),50,120);
    
        g.drawString("Schwarze: "+Integer.toString(schwarz),50,140);
    
        if (playerColors == 0) {
            g.drawString(playerName1+": Rot",50,180);
            g.drawString(playerName2+": Blau",50,200);
        } else if (playerColors == 1){
            g.drawString(playerName1+": Blau",50,180);
            g.drawString(playerName2+": Rot",50,200);
        } else {
            g.drawString(playerName1+": -",50,180);
            g.drawString(playerName2+": -",50,200);
        }
    
        frame();
        int millis = (int) dieUhr.verstricheneZeit();
        double fps= frames/((millis-lastmillis_fps)/1000);
        g.drawString("FPS: "+Integer.toString((int) fps),50,240);
        if ((millis-lastmillis_fps) >= 500) {
            frames=0;
            lastmillis_fps=millis;
        }
        if ((millis-lastmillis_msg) >= 2000) {
            msgState=0;
            lastmillis_msg=millis;
        }
    
        if (showPower) {
            g.drawRect(1100,300,10,300);
            g.fillRect(1100,(int)(600-power*(300/15)),10,(int)(power*(300/15)));
        }
    }
    
    public void rot()
    {
        rot++;
    }
    
    public void blau()
    {
        blau++;
    }
    
    public void schwarz()
    {
        schwarz++;
    }
    
    public void frame()
    {
        frames++;
    }
    
    public void foul()
    {
        msgState=1;
    }
    
    public void gewonnen(String pSpieler)
    {
        msgState = 2;
        gewinnerName = pSpieler;
    }
    
    public void disconnected(String pSpieler)
    {
        msgState = 3;
        disconnectName = pSpieler;
    }
    
    public int rote()
    {
        return rot;
    }
    
    public int blaue()
    {
        return blau;
    }
    
    public int schwarze()
    {
        return schwarz;
    }
    
    public void setPlayerColors(int m)
    {
        playerColors = m;
    }
    
    public void setzeNamen(String s1, String s2)
    {
        playerName1 = s1;
        playerName2 = s2;
    }
    
    public void setzePower(double p)
    {
        aktivierePower();
        power = p;
    }
    
    public void deaktivierePower()
    {
        showPower = false;
    }
    
    public void aktivierePower()
    {
        showPower = true;
    }
    
    public void kenntPuffer(Image pUffer)
    {
        derPuffer = pUffer;
    }
}