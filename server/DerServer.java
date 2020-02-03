import sum.netz.*;

public class DerServer extends Server
{
    int spielZaehler = 1;
    
    Billard[] spiele = new Billard[1000];
    int spielIndex = 0;
    boolean spieler1gesetzt = false;
    
    String[] spieler1IP = new String[spiele.length];
    int[] spieler1Port = new int[spiele.length];
    String[] spieler1Name = new String[spiele.length];
    
    String[] spieler2IP = new String[spiele.length];
    int[] spieler2Port = new int[spiele.length];
    String[] spieler2Name = new String[spiele.length];
    
    boolean[] spieler1done = new boolean[spiele.length];
    boolean[] spieler2done = new boolean[spiele.length];
    
    public DerServer() {
        super(80,false);
        System.out.println("The server has started successfully!");
        asciiArt();
        for (int i = 0; i < spiele.length; i++) {
            spieler1IP[i] = "";
            spieler1Name[i] = "";
            spieler1Port[i] = -1;
            spieler2IP[i] = "";
            spieler2Name[i] = "";
            spieler2Port[i] = -1;
            spieler1done[i] = false;
            spieler2done[i] = false;
        }
    }
    
    public void asciiArt() {
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("                          ____  _ _ _               _  ____        _ _            ");
        System.out.println("                         |  _ \\(_) | |             | |/ __ \\      | (_)           ");
        System.out.println("                         | |_) |_| | | __ _ _ __ __| | |  | |_ __ | |_ _ __   ___ ");
        System.out.println("                         |  _ <| | | |/ _` | '__/ _` | |  | | '_ \\| | | '_ \\ / _ \\");
        System.out.println("                         | |_) | | | | (_| | | | (_| | |__| | | | | | | | | |  __/");
        System.out.println("                         |____/|_|_|_|\\__,_|_|  \\__,_|\\____/|_| |_|_|_|_| |_|\\___|");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("                _ _,---._ ");
        System.out.println("             ,-','       `-.___ ");
        System.out.println("            /-;'               `._ ");
        System.out.println("           /\\/          ._   _,'o \\ ");
        System.out.println("          ( /\\       _,--'\\,','\"`. ) ");
        System.out.println("           |\\      ,'o     \\'    //\\ ");
        System.out.println("           |      \\        /   ,--'\"\"`-. ");
        System.out.println("           :       \\_    _/ ,-'         `-._ ");
        System.out.println("            \\        `--'  /                ) ");
        System.out.println("             `.  \\`._    ,'     ________,',' ");
        System.out.println("               .--`     ,'  ,--` __\\___,;' ");
        System.out.println("                \\`.,-- ,' ,`_)--'  /`.,' ");
        System.out.println("                 \\( ;  | | )      (`-/ ");
        System.out.println("                   `--'| |)       |-/ ");
        System.out.println("                     | | |        | | ");
        System.out.println("                     | | |,.,-.   | |_ ");
        System.out.println("                     | `./ /   )---`  ) ");
        System.out.println("                    _|  /    ,',   ,-' ");
        System.out.println("                   ,'|_(    /-<._,' |--, ");
        System.out.println("                   |    `--'---.     \\/ \\ ");
        System.out.println("                   |          / \\    /\\  \\ ");
        System.out.println("                 ,-^---._     |  \\  /  \\  \\ ");
        System.out.println("              ,-'        \\----'   \\/    \\--`. ");
        System.out.println("             /            \\              \\   \\ ");
        System.out.println("");
        System.out.println("");
    }
    
    public void bearbeiteVerbindungsende(String pClientIP, int pPartnerPort) {
        if (pClientIP.contentEquals(spieler1IP[spielIndex]) && (pPartnerPort == spieler1Port[spielIndex]) ) {
            spieler1gesetzt = false;
            spieler1IP[spielIndex] = "";
            spieler1Name[spielIndex] = "";
            spieler1Port[spielIndex] = -1;
        }
        for (int i = 0; i < spiele.length; i++) {
            if (spieler1IP[i].contentEquals(pClientIP) && (pPartnerPort == spieler1Port[i]) ) {
                sendeAnEinen(spieler1IP[i], spieler1Port[i], "DISC "+spieler1Name[i]);
                sendeAnEinen(spieler2IP[i], spieler2Port[i], "DISC "+spieler1Name[i]);
                resetGame(i);
            } else if (spieler2IP[i].contentEquals(pClientIP) && (pPartnerPort == spieler2Port[i]) ) {
                sendeAnEinen(spieler1IP[i], spieler1Port[i], "DISC "+spieler2Name[i]);
                sendeAnEinen(spieler2IP[i], spieler2Port[i], "DISC "+spieler2Name[i]);
                resetGame(i);
            }
        }
    }
    
    public void resetGame(int i) {
        spieler1IP[i] = "";
        spieler1Port[i] = -1;
        spieler1Name[i] = "";
        spieler2IP[i] = "";
        spieler2Port[i] = -1;
        spieler2Name[i] = "";
        spieler1done[i] = false;
        spieler2done[i] = false;
        spiele[i] = null;
    }
    
    public void bearbeiteVerbindungsverlust(String pClientIP, int pPartnerPort) {
        bearbeiteVerbindungsende(pClientIP, pPartnerPort);
    }
       
    public void bearbeiteNachricht(String pClientIP, int pPartnerPort, String pNachricht)
    {
        System.out.println();
        System.out.println();
        System.out.println(pClientIP + ":" + pPartnerPort);
        if (pNachricht.length() > 5) {
            System.out.println(pNachricht.substring(0,4));
            System.out.println(pNachricht.substring(5));
        } else {
            System.out.println(pNachricht);
        }
        if (pNachricht.length() > 4) {
            switch (pNachricht.substring(0,4)) {
                case "INIT" : init(pClientIP, pPartnerPort, pNachricht); break;
                case "MPOS" : leiteWeiter(pClientIP, pPartnerPort, pNachricht); break;
                case "CHAT" : leiteWeiter(pClientIP, pPartnerPort, pNachricht); break;
                case "CLCK" : simulationInit(pClientIP, pPartnerPort, pNachricht); break;
                case "DONE" : done(pClientIP, pPartnerPort, pNachricht); break;
                default : System.out.println("Ungültiger Typ ^^"); break;
            }
        } else {
            System.out.println("Ungültige Nachricht ^^");
        }
   }
    
    public void simulationInit(String pClientIP, int pPartnerPort, String pNachricht)
    {
        for (int i = 0; i < spiele.length; i++) {
            if ((spieler1IP[i].contentEquals(pClientIP) && (pPartnerPort == spieler1Port[i])) || (spieler2IP[i].contentEquals(pClientIP) && (pPartnerPort == spieler2Port[i]))) {
                //simulieren
                String[] pos = pNachricht.substring(5).split(":");
                double MausX1 = tryParse(pos[0]);
                double MausY1 = tryParse(pos[1]);
                double MausX2 = tryParse(pos[2]);
                double MausY2 = tryParse(pos[3]);
                spiele[i].simulation(MausX1, MausY1, MausX2, MausY2); 
                sendeAnEinen(spieler1IP[i], spieler1Port[i], "SIMU "+pNachricht.substring(5));
                sendeAnEinen(spieler2IP[i], spieler2Port[i], "SIMU "+pNachricht.substring(5));
                i=spiele.length;
            }
        }
    }
    
    private double tryParse(String pString){
        try {
            return Double.parseDouble(pString);
        } catch (NumberFormatException nfe) {
            System.out.println(pString);
            //falsche daten -> 0
            return 0;
        }
    }
    
    public void done(String pClientIP, int pPartnerPort, String pNachricht)
    {
        for (int i = 0; i < spiele.length; i++) {
            if ((spieler1IP[i].contentEquals(pClientIP) && pPartnerPort == spieler1Port[i]) || (spieler2IP[i].contentEquals(pClientIP) && pPartnerPort == spieler2Port[i])) {
                if (spieler1IP[i].contentEquals(pClientIP) && (pPartnerPort == spieler1Port[i])) {
                    spieler1done[i] = true;
                } else {
                    spieler2done[i] = true;
                }
                if (spieler1done[i] && spieler2done[i]) {
                    //endpos
                    String nachricht = "DONE ";
                    for (int j = 0; j < 16; j++) {
                        nachricht += spiele[i].kugeln[j].pos.x()+";"+spiele[i].kugeln[j].pos.y()+":";
                    }
                    sendeAnEinen(spieler1IP[i], nachricht);
                    sendeAnEinen(spieler2IP[i], nachricht);
                    if (spiele[i].ende){
                        //evtl. ende
                        sendeAnEinen(spieler1IP[i], spieler1Port[i], "ENDE "+spiele[i].gewinner);
                        sendeAnEinen(spieler2IP[i], spieler2Port[i], "ENDE "+spiele[i].gewinner);
                        resetGame(i);
                    } else {
                        //spieler senden
                        if (spiele[i].aktuellerSpieler == 0){
                             sendeAnEinen(spieler1IP[i], spieler1Port[i], "DRAN ");
                        } else {
                             sendeAnEinen(spieler2IP[i], spieler2Port[i], "DRAN ");
                        }
                    }
                    spieler1done[i] = false;
                    spieler2done[i] = false;
                }
            }
        }
    }
    
    public void leiteWeiter(String pClientIP, int pPartnerPort, String pNachricht)
    {
        if (pNachricht.length() > 5) {
            for (int i = 0; i < spiele.length; i++) {
                if (spieler1IP[i].contentEquals(pClientIP) && pPartnerPort == spieler1Port[i]) {
                    sendeAnEinen(spieler2IP[i], spieler2Port[i], pNachricht); i=spiele.length;
                } else if (spieler2IP[i].contentEquals(pClientIP) && pPartnerPort == spieler2Port[i]) {
                    sendeAnEinen(spieler1IP[i], spieler1Port[i], pNachricht); i=spiele.length;
                }
            }
        }
    }
    
    public void init(String pClientIP, int pPartnerPort, String pNachricht)
    {
        if (!spieler1gesetzt) {
            spieler1IP[spielIndex] = pClientIP;
            spieler1Port[spielIndex] = pPartnerPort;
            spieler1Name[spielIndex] = pNachricht.substring(5);
            spieler1gesetzt = true;
            printStatus(spielIndex);
        } else {
            spieler2IP[spielIndex] = pClientIP;
            spieler2Port[spielIndex] = pPartnerPort;
            spieler2Name[spielIndex] = pNachricht.substring(5);
            spieler1gesetzt = false;
            printStatus(spielIndex);
            starte();
        }
    }
    
    public void printStatus(int pIndex) {
        System.out.println();
        System.out.println("IP1: " + spieler1IP[pIndex] + ":" + spieler1Port[pIndex]);
        System.out.println("Name1: " + spieler1Name[pIndex]);
        System.out.println("IP2: " + spieler2IP[pIndex] + ":" + spieler2Port[pIndex]);
        System.out.println("Name2: " + spieler2Name[pIndex]);
    }
    
    public void starte()
    {
        sendeAnEinen(spieler1IP[spielIndex], spieler1Port[spielIndex], "NAME "+spieler2Name[spielIndex]);
        sendeAnEinen(spieler2IP[spielIndex], spieler2Port[spielIndex], "NAME "+spieler1Name[spielIndex]);
        spiele[spielIndex] = new Billard(this,  spieler1IP[spielIndex],
                                                spieler1Port[spielIndex],
                                                spieler1Name[spielIndex],
                                                spieler2IP[spielIndex],
                                                spieler2Port[spielIndex],
                                                spieler2Name[spielIndex],
                                                spielIndex);
        sendeAnEinen(spieler1IP[spielIndex], spieler1Port[spielIndex], "DRAN ");
        do {
            spielIndex++;
            if (spielIndex >= spiele.length) {
                spielIndex = 0;
            }
        } while (spiele[spielIndex] != null);
        System.out.println();
        System.out.println("Spiel Nr."+String.valueOf(spielZaehler)+" wurde gestartet" );
        spielZaehler++;
    }
}
