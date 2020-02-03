
import sum.werkzeuge.*;
import sum.multimedia.*;
import java.util.Random;
import java.io.*;
import javax.sound.sampled.*;

/**
 * @author 
 * @version 
 */
public class HintergrundMusik
{
    // Objekte
    
    int song = -1;
    int NextSongTime = -1;
    double CurrentSongLength = -1;
    
    Uhr dieUhr;
    Random rand;
    
    String[] files;
    double[] length;
    Ton[] songs;
    
    // Konstruktor
    public HintergrundMusik()
    {
        dieUhr = new Uhr();
        rand = new Random();
       
        createArrays();
    
    }

    // Dienste
    public void musikPruefen()
    {
        if (dieUhr.verstricheneZeit() > NextSongTime) {
            if (song != -1) {
                songs[song].gibFrei();
                songs[song] = null;
            }
            newSong();
        }
    }
    
    public void newSong()
    {
        if (files.length > 1) {
            song = rand.nextInt(files.length-1);
            songs[song] = new Ton();
            songs[song].ladeTon(files[song]);
            songs[song].spieleTon();
            CurrentSongLength = length[song];
            NextSongTime = (int) dieUhr.verstricheneZeit()+ (int) CurrentSongLength*1000;
        }
    }
    
    public void createArrays()
    {
        File folder = new File("./musik");
        File[] listOfFiles = folder.listFiles();
        int anzahl = 0;
        String ext;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && getExtension(listOfFiles[i].getName()) == "wav") {
                anzahl++;
            }
        }
        files = new String[anzahl];
        length = new double[anzahl];
        songs = new Ton[anzahl];
        int zaehler = 0;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && getExtension(listOfFiles[i].getName()) == "wav") {
                files[zaehler] = "musik/"+listOfFiles[i].getName();
                length[zaehler] = getDurationOfWavInSeconds(listOfFiles[i]);
                zaehler++;
            }
        }
    }
    
    public String getExtension(String filename) {
        int extensionPos = filename.lastIndexOf(".");
        return filename.substring(extensionPos + 1).intern();
    }
    
    public double getDurationOfWavInSeconds(File file)
    {   
        AudioInputStream stream = null;

        try 
        {
            stream = AudioSystem.getAudioInputStream(file);
            
            AudioFormat format = stream.getFormat();
        
            return file.length() / format.getSampleRate() / (format.getSampleSizeInBits() / 8.0) / format.getChannels();
        }
        catch (Exception e) 
        {
            // log an error
            return -1;
        }
        finally
        {
            try { stream.close(); } catch (Exception ex) { }
        }
    }

}









