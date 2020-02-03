import sum.netz.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.Graphics2D.*;
import sum.multimedia.*;
import sum.werkzeuge.*;
import java.math.*;
import javax.swing.*;

public class Billard extends Clientverbindung
{
    //Objekte
    Uhr dieUhr;
    String s1;
    String s2;
    JFrame dasFrame;
    Image doubleBuffer = null;
    Tisch derTisch;
    Queue derQueue;
    Maus maus;
    Ton plock;
    HintergrundMusik dieMusik;
    boolean foul = false;
    boolean ende = false;
    boolean weiterspielen = false;
    Kugel[] kugeln;
    Kugel schlagkugelfake;
    Vector2D theForce;
    HUD dasDisplay;
    int aktuellerSpieler = 0; //0: Spieler 1; 1: Spieler 2
    Spieler[] spieler;
    ChatListener chat;
    
    boolean sound = false;
    boolean musik = false; //Copyright!
    int speed = 60; //2TPS = 1FPS
    int delay = 1000 / speed;

    double kugel1x = 300;
    double kugel1y = 400;

    public Billard()
    {
        //initialisieren
        super(JOptionPane.showInputDialog(null, "Server","mchomeserver.spdns.de"),80,false);
        dieUhr = new Uhr();
        maus = new Maus();
        
        dasFrame = new JFrame("BILLARD ONLINE");
        dasFrame.addMouseListener(maus);
        dasFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        dasFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        dasFrame.setVisible(true);
        
        chat = new ChatListener();
        chat.kenntClient(this);
        
        derQueue = new Queue();
        derTisch = new Tisch(200, 200, 1000, 700);
        dasDisplay = new HUD();
        kugeln = new Kugel[16];
        spieler = new Spieler[2];
        schlagkugelfake = new Kugel(0, 0, 4, 3);
        theForce = new Vector2D();//-20.14,-1.68);// (Math.random() / 3) - 0.3 , (Math.random() * -5) - 10 );
        spieler[0] = new Spieler();
        spieler[1] = new Spieler();
        doubleBuffer =  new BufferedImage((int) dasFrame.getBounds().getWidth(), (int) dasFrame.getBounds().getHeight(), BufferedImage.TYPE_INT_ARGB);
        setup();
        settings();
        username();
    }
    
    public void bearbeiteNachricht(String pNachricht) {
        System.out.println();
        System.out.println();
        System.out.println(pNachricht.substring(0,4));
        System.out.println(pNachricht.substring(5));
        switch (pNachricht.substring(0,4)) {
            case "NAME" : name(pNachricht.substring(5)); break;
            case "MPOS" : updateQueue(pNachricht.substring(5)); break;
            case "DRAN" : spielzug(); break;
            case "SIMU" : simulation(pNachricht.substring(5)); break;
            case "DONE" : updateKugeln(pNachricht.substring(5)); break;
            case "FOUL" : foul(); break;
            case "ENDE" : ende(pNachricht.substring(5)); break;
            case "DISC" : disconnected(pNachricht.substring(5)); break;
            case "CHAT" : chatNachricht(pNachricht.substring(5)); break;
        }
    }
    
    private void chatNachricht(String pNachtisch){
        chat.nachricht(pNachtisch);
    }
    
    private void disconnected(String name){
        dasDisplay.disconnected(name);
        zeichneAlles(true);
        ende = true;
    }
    
    private void updateKugeln(String positions){
        String[] kugelpos = positions.split(":");
        for (int i = 0; i < 16; i++) {
            String[] pos = kugelpos[i].split(";");
            kugeln[i].pos.set(tryParse(pos[0]),tryParse(pos[1]));
        }
    }
    
    private void simulation(String positions){
        String[] pos = positions.split(":");
        double MausX1 = tryParse(pos[0]);
        double MausY1 = tryParse(pos[1]);
        double MausX2 = tryParse(pos[2]);
        double MausY2 = tryParse(pos[3]);
        
        simulation(MausX1, MausY1, MausX2, MausY2);
    }
    
    private double tryParse(String pString){
        try {
            return Double.parseDouble(pString);
        } catch (NumberFormatException nfe) {
            System.out.println(pString);
            //falsche daten -> "mitte"
            return 500;
        }
    }
    
    private void updateQueue(String positions){
        aktuellerSpieler = 1;
        String[] pos = positions.split(":");
        double MausX1 = tryParse(pos[0]);
        double MausY1 = tryParse(pos[1]);
        double MausX2 = tryParse(pos[2]);
        double MausY2 = tryParse(pos[3]);
        zeichneAlles(false);
        derQueue.zeichne(MausX1, MausY1, MausX2, MausY2, -7, aktuellerSpieler, spieler[aktuellerSpieler].holeName());
            
        show();
            
    }

    private void setup()
    {
        //Kugeln aufbauen
        kugeln[0] = new Kugel(kugel1x, kugel1y, 2);
        kugeln[1] = new Kugel(kugel1x, kugel1y + 22, 3);
        kugeln[2] = new Kugel(kugel1x, kugel1y + 44, 2);
        kugeln[3] = new Kugel(kugel1x, kugel1y + 66, 3);
        kugeln[4] = new Kugel(kugel1x, kugel1y + 88, 3);
        kugeln[5] = new Kugel(kugel1x + 20, kugel1y + 11, 2);
        kugeln[6] = new Kugel(kugel1x + 20, kugel1y + 33, 3);
        kugeln[7] = new Kugel(kugel1x + 20, kugel1y + 55, 2);
        kugeln[8] = new Kugel(kugel1x + 20, kugel1y + 77, 3);
        kugeln[9] = new Kugel(kugel1x + 40, kugel1y + 22, 2);
        kugeln[10] = new Kugel(kugel1x + 40, kugel1y + 44, 1);
        kugeln[11] = new Kugel(kugel1x + 40, kugel1y + 66, 3);
        kugeln[12] = new Kugel(kugel1x + 60, kugel1y + 33, 3);
        kugeln[13] = new Kugel(kugel1x + 60, kugel1y + 55, 2);
        kugeln[14] = new Kugel(kugel1x + 80, kugel1y + 44, 2);

        kugeln[15] = new Kugel(kugel1x + 460, kugel1y + 33, 0);
        
        for (int i = 0; i < 16; i++) {
            kugeln[i].kenntPuffer(doubleBuffer);
        }
        derQueue.kenntPuffer(doubleBuffer);
        derTisch.kenntPuffer(doubleBuffer);
        dasDisplay.kenntPuffer(doubleBuffer);
    }

    private void username()
    {
        zeichneAlles(true);

        s1 = JOptionPane.showInputDialog(null, "Spieler:\nBitte gib deinen Namen ein!","");
        if(s1 == null) {
            s1 = "Spieler"+String.valueOf((int)(Math.random()*1000));
        }
        
        if(s1.isEmpty()) {
            s1 = "Spieler"+String.valueOf((int)(Math.random()*1000));
        }
        
        spieler[0].setzeName(s1);

        //sende ab und warte bis anderer spieler gefunden
        sende("INIT "+s1);
    }

    private void settings()
    {
        zeichneAlles(true);
        
        if(JOptionPane.showConfirmDialog(null, "Soll der Kollisionssound aktiviert werden?","Ton",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            plock = new Ton();
            plock.ladeTon("plock.wav");
            sound = true;
        }
        
        if(JOptionPane.showConfirmDialog(null, "Soll Musik laufen?","Musik",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            dieMusik = new HintergrundMusik();
            musik = true; //Copyright!
        }
    }

    private void name(String opponentName)
    {
        s2 = opponentName;
        spieler[1].setzeName(s2);
        dasDisplay.setzeNamen(s1,s2);
        chat.namen(s1,s2);

        zeichneAlles(true);
    }

    private void spielzug()
    {        
        aktuellerSpieler = 0;
        
        boolean abbruch = false;
        
        double MausX1 = 0;
        double MausY1 = 0;
        double MausX2 = 0;
        double MausY2 = 0;
        
        double winkel = 0;
        double power = 0;
        double forceX = 0;
        double forceY = 0;
        do {
            //Punkt 1 festlegen
            MausX1 = MouseInfo.getPointerInfo().getLocation().x-dasFrame.getLocationOnScreen().x;
            MausY1 = MouseInfo.getPointerInfo().getLocation().y-dasFrame.getLocationOnScreen().y;
            MausX2 = MausX1;
            MausY2 = MausY1 + 10;
            sende("MPOS "+MausX1+":"+MausY1+":"+MausX2+":"+MausY2);
            zeichneAlles(false);
            //Queue 7 pixel über Mausposition
            derQueue.zeichne(MausX1, MausY1, MausX2, MausY2, -7, aktuellerSpieler, spieler[aktuellerSpieler].holeName());
           
            show();
            
            dieUhr.warte(delay);
        } while (!maus.gedruecktLinks());
        //warten bis wieder losgelassen wird
        do { /* nothing*/ } while (maus.gedruecktLinks());
        do {
            //Punkt 2 festlegen
            MausX2 = MouseInfo.getPointerInfo().getLocation().x-dasFrame.getLocationOnScreen().x;
            MausY2 = MouseInfo.getPointerInfo().getLocation().y-dasFrame.getLocationOnScreen().y;
            sende("MPOS "+MausX1+":"+MausY1+":"+MausX2+":"+MausY2);
            zeichneAlles(false);
            //Queue 7 pixel über punkt 1, Ende zeigt auf Maus.
            derQueue.zeichne(MausX1, MausY1, MausX2, MausY2, -7, aktuellerSpieler, spieler[aktuellerSpieler].holeName());
            power = Math.pow(dist(MausX1,MausY1,MausX2,MausY2)/50,2);
            power = Math.min(power,15); // 0 bis 15
            dasDisplay.setzePower(power);
            
            show();
            
            dieUhr.warte(delay);
            
            //Abbruch
            if (maus.gedruecktRechts()) {
                dasDisplay.deaktivierePower();
                spielzug();
                abbruch = true;
                break;
            }
            
        } while(!maus.gedruecktLinks());
        if (!abbruch) {
            sende("CLCK "+MausX1+":"+MausY1+":"+MausX2+":"+MausY2);
        }
        //warten bis maus nicht gedrückt gegen probleme, wenn die maus nicht losgelassen wurde
        do { /* nothing*/ } while (maus.gedruecktLinks());
    }

    private double dist(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt(Math.pow(Math.abs(x1-x2),2)+Math.pow(Math.abs(y1-y2),2));
    }
    
    private void show()
    {
        dasFrame.getGraphics().drawImage(doubleBuffer,0,0,dasFrame);            
        Graphics2D graphics = (Graphics2D) doubleBuffer.getGraphics();
        graphics.setBackground(new Color(255, 255, 255, 0));
        graphics.clearRect(0,0, (int) dasFrame.getBounds().getWidth(), (int) dasFrame.getBounds().getHeight());
    }

    private void simulation(double MausX1, double MausY1, double MausX2, double MausY2)
    {
                                    //Offline Simulation
        
        double winkel = 0;
        double power = 0;
        double forceX = 0;
        double forceY = 0;
        
        //schlagrichtung und stärke berechenen
        power = Math.pow(dist(MausX1,MausY1,MausX2,MausY2)/50,2);
        power = Math.min(power,15); // 0 bis 15
        
        dasDisplay.deaktivierePower();
        winkel = Math.atan2(MausY2 - MausY1, MausX2 - MausX1);
        forceX = -Math.cos(winkel) * power/2;
        forceY = -Math.sin(winkel) * power/2;

        //Schlag mithilfe kleiner Kugel simulieren
        schlagkugelfake.auslochen(MausX1-Math.cos(winkel) * 5, MausY1-Math.sin(winkel) * 5);
        schlagkugelfake.kraft(forceX, forceY);
        
        double addall = 0; //beendigungs ?berpr?fungs variable
        foul = false;
        
        do {
            derTisch.zeichne();
            dasDisplay.zeichne();
            
            for (int i = 0; i < 16; i++) { //jede kugel
                kugeln[i].update();
            }
            show();
            
            dieUhr.warte(delay);
            schlagkugelfake.update();
            for (int i = 0; i < 16; i++) { //jede kugel
                if (!foul) {
                    kollision(schlagkugelfake, kugeln[i]);
                }
                for (int n = i + 1; n < 16; n++) { //mit jeder anderen kugel
                    kollision(kugeln[i], kugeln[n]);
                }

                loch(kugeln[i]);
                abprallen(kugeln[i]);
            }
            //alle geschwindigkeitsbetr?ge addieren
            addall = Math.abs(schlagkugelfake.vel.x()) + Math.abs(schlagkugelfake.vel.y());
            for (int i = 0; i < 16; i++) {
                addall += Math.abs(kugeln[i].vel.x()) + Math.abs(kugeln[i].vel.y());
            }
            if(musik) {
                dieMusik.musikPruefen();
            }
        } while(addall > 0); //wiederholen bis alle kugeln unbeweglich
        
        sende("DONE ");
    }
    
    private void zeichneAlles(boolean show)//vereinfachte simulation, ohne jegliche pr?fung, nur au?erhalb der echten simulation gut nutzbar
    {
        derTisch.zeichne();
        dasDisplay.zeichne();
        if (musik) {
            dieMusik.musikPruefen();
        }
        for (int i = 0; i < 16; i++) {
            kugeln[i].update();
        }
        if (show) {
            show();
        }
    }

    private void kollision(Kugel kugel1, Kugel kugel2)
    {
        if (!(kugel1.eingelocht() && kugel2.eingelocht())) {
            //kollisionssimulation
            double dx2 = kugel1.pos.x() - kugel2.pos.x();
            double dy2 = kugel1.pos.y() - kugel2.pos.y();

            double radiusSum = kugel1.rad + kugel2.rad;
            if (Math.abs(dx2) * Math.abs(dx2) + Math.abs(dy2) * Math.abs(dy2) <= radiusSum * radiusSum) { //wenn kugeln sich berühren
                if (kugel1.typ() == 4 && kugel2.typ() != 0) { //wenn schlagkugel nicht weiße trifft (und kein cheatmode)
                    foul();
                } else {
                    if (sound) {
                        plock.spieleTon();
                    }

                    double Abstand2 = Math.sqrt(dx2 * dx2 + dy2 * dy2);
                    double dx = dx2 / Abstand2;
                    double dy = dy2 / Abstand2;

                    double skalarproduktdifferenz =(kugel1.vel.x() * dx + kugel1.vel.y() * dy) -(kugel2.vel.x() * dx + kugel2.vel.y() * dy);

                    double Kraftx = dx * skalarproduktdifferenz;
                    double Krafty = dy * skalarproduktdifferenz;

                    //berechnete kr?fte ?bernehmen
                    kugel1.vel.set(kugel1.vel.x() - Kraftx, kugel1.vel.y() - Krafty);
                    kugel2.vel.set(kugel2.vel.x() + Kraftx, kugel2.vel.y() + Krafty);
                    
                    double winkelZwischenKugel = (Math.atan2(dy2 / 2, dx2 / 2));
                    double abstand = (radiusSum - Abstand2) / 2;
                    double kugelXmove = Math.cos(winkelZwischenKugel) * abstand;
                    double kugelYmove = Math.sin(winkelZwischenKugel) * abstand;
                    kugel1.pos.add(kugelXmove, kugelYmove);
                    kugel2.pos.add(-kugelXmove, -kugelYmove);
                }
                if(kugel1.typ() == 4) { //wenn schlagkugel kollision: mach sie weg
                    kugel1.einlochen();
                }
            }
        }
    }

    private void loch(Kugel dieKugel)
    {
        double radiusSum = 18; //loch radius, damit der mittelpunkt der kugel z?hlt

        for(int i = 0; i < 6; i++) {
            double dx2 = dieKugel.pos.x() - derTisch.loch(i).x();
            double dy2 = dieKugel.pos.y() - derTisch.loch(i).y();

            if (Math.abs(dx2) * Math.abs(dx2) + Math.abs(dy2) * Math.abs(dy2) <= radiusSum * radiusSum) {
                //wenn kugel im loch, der kugel bescheid sagen und je nach farbe anzeigen
                if (dieKugel.typ() != 0) {
                    dieKugel.einlochen();
                    if (dieKugel.typ() == 1) {
                        dasDisplay.schwarz();
                    }
                    if (dieKugel.typ() == 2) {
                        dasDisplay.rot();
                        if(!spieler[aktuellerSpieler].farbeGesetzt()) {
                            spieler[aktuellerSpieler].setzeFarbe(0);
                            spieler[(aktuellerSpieler + 1) % 2].setzeFarbe(1);
                            dasDisplay.setPlayerColors(aktuellerSpieler);
                        }
                        if (spieler[aktuellerSpieler].holeFarbe() == 0) {
                            weiterspielen = true;
                        }
                    }
                    if (dieKugel.typ() == 3) {
                        dasDisplay.blau();
                        if (!spieler[aktuellerSpieler].farbeGesetzt()) {
                            spieler[aktuellerSpieler].setzeFarbe(1);
                            spieler[(aktuellerSpieler + 1) % 2].setzeFarbe(0);
                            dasDisplay.setPlayerColors((aktuellerSpieler + 1) % 2);
                        }
                        if (spieler[aktuellerSpieler].holeFarbe() == 1) {
                            weiterspielen = true;
                        }
                    }
                } else {
                    dieKugel.pos.add(-3 * dieKugel.vel.x(), -3 * dieKugel.vel.y());
                    dieKugel.vel.set(0, 0);
                }
            }
        }
    }

    private void foul()
    {
        dasDisplay.foul();
        foul = true;
    }

    private void ende(String pSpieler)
    {
        dasDisplay.gewonnen(pSpieler);
        zeichneAlles(true);
        ende = true;
    }

    private void abprallen(Kugel dieKugel)
    {
        if (!dieKugel.eingelocht()) {
            //Überprüfung ob kugel einen rand berührt, falls ja, entsprechende richtung umkehren inkl. verlust
            if (dieKugel.pos.x() - dieKugel.rad <= derTisch.linkeKante()) {
                if (sound) {
                    plock.spieleTon();
                }
                dieKugel.vel.set(dieKugel.vel.x() * -0.9, dieKugel.vel.y());
                dieKugel.pos.add(dieKugel.vel);
            }
            if (dieKugel.pos.y() - dieKugel.rad <= derTisch.obereKante()) {
                if (sound) {
                    plock.spieleTon();
                }
                dieKugel.vel.set(dieKugel.vel.x(), dieKugel.vel.y() * -0.9);
                dieKugel.pos.add(dieKugel.vel);
            }
            if (dieKugel.pos.x() + dieKugel.rad >= derTisch.rechteKante()) {
                if (sound) {
                    plock.spieleTon();
                }
                dieKugel.vel.set(dieKugel.vel.x() * -0.9, dieKugel.vel.y());
                dieKugel.pos.add(dieKugel.vel);
            }
            if (dieKugel.pos.y() + dieKugel.rad >= derTisch.untereKante()) {
                if (sound) {
                    plock.spieleTon();
                }
                dieKugel.vel.set(dieKugel.vel.x(), dieKugel.vel.y() * -0.9);
                dieKugel.pos.add(dieKugel.vel);
            }
        }
    }
}
