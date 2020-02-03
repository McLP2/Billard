
import sum.netz.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.Graphics2D.*;
import sum.multimedia.*;
import java.math.*;
import javax.swing.*;
public class Billard extends Thread
{
    //Objekte
    Image doubleBuffer = null;
    Tisch derTisch;
    public boolean foul = false;
    public boolean ende = false;
    public boolean weiterspielen = false;
    public Kugel[] kugeln;
    Kugel schlagkugelfake;
    int aktuellerSpieler = 0; //0: Spieler 1; 1: Spieler 2
    Spieler[] spieler;
    Server derServer;
    String gewinner;
    int spielIndex;
    int rote = 0;
    int blaue = 0;
    
    double kugel1x = 300;
    double kugel1y = 400;

    public Billard(Server pServer, String pIP1, int pPort1, String pName1, String pIP2, int pPort2, String pName2, int pIndex)
    {
        //initialisieren
        spielIndex = pIndex;
        derTisch = new Tisch(200, 200, 1000, 700);
        kugeln = new Kugel[16];
        spieler = new Spieler[2];
        schlagkugelfake = new Kugel(0, 0, 4, 3);
        spieler[0] = new Spieler();
        spieler[1] = new Spieler();
        derServer = pServer;
        setup();
        usernames(pName1, pIP1, pPort1, pName2, pPort2, pIP2);
    }

    public void setup()
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
    }

    public void usernames(String name1, String ip1, int port1, String name2, int port2, String ip2)
    {
        spieler[0].setzeName(name1);
        spieler[0].setzeIP(ip1);
        spieler[0].setzePort(port1);
        spieler[1].setzeName(name2);
        spieler[1].setzeIP(ip2);
        spieler[1].setzePort(port2);
    }

    private double dist(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt(Math.pow(Math.abs(x1-x2),2)+Math.pow(Math.abs(y1-y2),2));
    }

    public void simulation(double MausX1, double MausY1, double MausX2, double MausY2)
    {
                                    //Offline Simulation
        weiterspielen = false;
                                    
        double winkel = 0;
        double power = 0;
        double forceX = 0;
        double forceY = 0;
        
        //schlagrichtung und stärke berechenen
        power = Math.pow(dist(MausX1,MausY1,MausX2,MausY2)/50,2);
        power = Math.min(power,15); // 0 bis 15
        
        winkel = Math.atan2(MausY2 - MausY1, MausX2 - MausX1);
        forceX = -Math.cos(winkel) * power/2;
        forceY = -Math.sin(winkel) * power/2;

        //Schlag mithilfe kleiner Kugel simulieren
        schlagkugelfake.auslochen(MausX1-Math.cos(winkel) * 5, MausY1-Math.sin(winkel) * 5);
        schlagkugelfake.kraft(forceX, forceY);
        
        double addall = 0; //beendigungs ?berpr?fungs variable
        foul = false;
        do {
            for (int i = 0; i < 16; i++) { //jede kugel
                kugeln[i].update();
            }
            
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
        } while(addall > 0); //wiederholen bis alle kugeln unbeweglich
        
        if (!weiterspielen) {
            aktuellerSpieler = (aktuellerSpieler + 1) % 2;
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
                        ende();
                    }
                    if (dieKugel.typ() == 2) {
                        rote++;
                        if(!spieler[aktuellerSpieler].farbeGesetzt()) {
                            spieler[aktuellerSpieler].setzeFarbe(0);
                            spieler[(aktuellerSpieler + 1) % 2].setzeFarbe(1);
                        }
                        if (spieler[aktuellerSpieler].holeFarbe() == 0) {
                            weiterspielen = true;
                        }
                    }
                    if (dieKugel.typ() == 3) {
                        blaue++; 
                        if (!spieler[aktuellerSpieler].farbeGesetzt()) {
                            spieler[aktuellerSpieler].setzeFarbe(1);
                            spieler[(aktuellerSpieler + 1) % 2].setzeFarbe(0);
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
        derServer.sendeAnEinen(spieler[0].holeIP(), spieler[0].holePort(), "FOUL ");
        derServer.sendeAnEinen(spieler[1].holeIP(), spieler[1].holePort(), "FOUL ");
        foul = true;
    }

    private void ende()
    {
        if (spieler[aktuellerSpieler].holeFarbe() == 0 && rote == 7) {
            gewinner = spieler[aktuellerSpieler].holeName();
        } else if (spieler[aktuellerSpieler].holeFarbe() == 1 && blaue == 7) {
            gewinner = spieler[aktuellerSpieler].holeName();
        } else {
            gewinner = spieler[(aktuellerSpieler + 1) % 2].holeName();
        }
        
        ende = true;
    }

    private void abprallen(Kugel dieKugel)
    {
        if (!dieKugel.eingelocht()) {
            //Überprüfung ob kugel einen rand berührt, falls ja, entsprechende richtung umkehren inkl. verlust
            if (dieKugel.pos.x() - dieKugel.rad <= derTisch.linkeKante()) {
                dieKugel.vel.set(dieKugel.vel.x() * -0.9, dieKugel.vel.y());
                dieKugel.pos.add(dieKugel.vel);
            }
            if (dieKugel.pos.y() - dieKugel.rad <= derTisch.obereKante()) {
                dieKugel.vel.set(dieKugel.vel.x(), dieKugel.vel.y() * -0.9);
                dieKugel.pos.add(dieKugel.vel);
            }
            if (dieKugel.pos.x() + dieKugel.rad >= derTisch.rechteKante()) {
                dieKugel.vel.set(dieKugel.vel.x() * -0.9, dieKugel.vel.y());
                dieKugel.pos.add(dieKugel.vel);
            }
            if (dieKugel.pos.y() + dieKugel.rad >= derTisch.untereKante()) {
                dieKugel.vel.set(dieKugel.vel.x(), dieKugel.vel.y() * -0.9);
                dieKugel.pos.add(dieKugel.vel);
            }
        }
    }
}
