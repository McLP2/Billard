
import java.awt.event.*;

public class Maus implements MouseListener
{
    // Bezugsobjekte
    
    // Attribute
    volatile private boolean mouseDownLeft;
    volatile private boolean mouseDownRight;
    // Konstruktor
    public Maus()
    {
        mouseDownLeft = false;
        mouseDownRight = false;
    }

    // Dienste
    public boolean gedruecktLinks()
    {
        return mouseDownLeft;
    }
    
    public boolean gedruecktRechts()
    {
        return mouseDownRight;
    }
    
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1: mouseDownLeft = true; break;
            case MouseEvent.BUTTON3: mouseDownRight = true; break;
        }
    }

    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1: mouseDownLeft = false; break;
            case MouseEvent.BUTTON3: mouseDownRight = false; break;
        }
    }

    public void mouseExited(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}


}
