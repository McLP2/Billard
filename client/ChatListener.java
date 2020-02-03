
import sum.netz.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class ChatListener extends JFrame implements ActionListener
{
    // Bezugsobjekte
    Clientverbindung client;
    // Attribute
    JFrame dasChatFrame;
    JButton senden;
    JTextField nachricht;
    JTextArea verlauf;
    
    String spieler;
    String gegner;

    // Konstruktor
    public ChatListener()
    {
        dasChatFrame = new JFrame("BILLARD ONLINE CHAT");
        senden = new JButton("Senden");
        nachricht = new JTextField();
        verlauf = new JTextArea();
        verlauf.setLineWrap(true);
        verlauf.setEditable(false);
        DefaultCaret caret = (DefaultCaret)verlauf.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        senden.addActionListener(this);
        nachricht.addActionListener(this);
        
        dasChatFrame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        dasChatFrame.setSize(300, 500);
        dasChatFrame.setResizable(false);
        dasChatFrame.setAlwaysOnTop(true);
        dasChatFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width-360, Toolkit.getDefaultToolkit().getScreenSize().height-550);
        dasChatFrame.setLayout(null);
        
        int FrameWidth = dasChatFrame.getSize().width;
        int FrameHeigth = dasChatFrame.getSize().height;
        
        senden.setBounds(FrameWidth-80,0,80,20);
        nachricht.setBounds(0,0,FrameWidth-80,20);
        //verlauf.setBounds(0,0,300,480);
        JPanel panel = new JPanel();
        panel.setBounds(0,20,FrameWidth,FrameHeigth-20);
        panel.setLayout(new BorderLayout()); 
        JScrollPane scroll = new JScrollPane(verlauf);
        //scroll.setViewportView(verlauf);
        panel.add(scroll, BorderLayout.CENTER);
        
        dasChatFrame.add(nachricht);
        dasChatFrame.add(senden);
        dasChatFrame.add(panel);
        //dasChatFrame.pack();
    }

    // Dienste
    public void namen (String pSpieler, String pGegner){
        spieler = pSpieler;
        gegner = pGegner;
        
        dasChatFrame.setVisible(true);
    }
    
    public void kenntClient (Clientverbindung pClient){
        client = pClient;
    }
    
    public void nachricht (String pNachricht){
        verlauf.append(gegner + ": " + pNachricht + "\n\n");
    }
    
    public void actionPerformed (ActionEvent ae){
        if( ae.getSource() == this.senden || ae.getSource() == this.nachricht && !nachricht.getText().isEmpty() ){
            client.sende("CHAT "+nachricht.getText());
            verlauf.append(spieler + ": " + nachricht.getText() + "\n\n");
            nachricht.setText("");
        }
    }
}
