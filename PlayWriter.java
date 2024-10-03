/*
 * PlayWriter.java
 *
 * PLayWriter class.
 * Creates the lovers, and writes the two lover's story (to an output text file).
 */

import javafx.util.Pair;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class PlayWriter {
    private Romeo  myRomeo  = null;
    private InetAddress RomeoAddress = null;
    private int RomeoPort = 0;
    private Socket RomeoMailbox = null;
    private Juliet myJuliet = null;
    private InetAddress JulietAddress = null;
    private int JulietPort = 0;
    private Socket JulietMailbox = null;
    double[][] theNovel = null;
    int novelLength = 0;
    public PlayWriter()
    {
        novelLength = 500; //Number of verses
        theNovel = new double[novelLength][2];
        theNovel[0][0] = 0;
        theNovel[0][1] = 1;
    }
    //Create the lovers
    public void createCharacters()
    {
        //Create the lovers
        System.out.println("PlayWriter: Romeo enters the stage.");
        //TO BE COMPLETED
        myRomeo= new Romeo(0);
        myRomeo.start();

        System.out.println("PlayWriter: Juliet enters the stage.");
        //TO BE COMPLETED
        myJuliet= new Juliet(1);
        myJuliet.start();
    }
    //Meet the lovers and start letter communication
    public void charactersMakeAcquaintances()
    {
        //TO BE COMPLETED
        Pair<InetAddress,Integer> romeoInfo = myRomeo.getAcquaintance();
        RomeoAddress= romeoInfo.getKey();
        RomeoPort= romeoInfo.getValue();
        System.out.println("PlayWriter: I've made acquaintance with Romeo");

        //TO BE COMPLETED
        Pair<InetAddress,Integer> julietInfo = myJuliet.getAcquaintance();
        JulietAddress = julietInfo.getKey();
        JulietPort= julietInfo.getValue();
        System.out.println("PlayWriter: I've made acquaintance with Juliet");
    }
    //Request next verse: Send letters to lovers communicating the partner's love in previous verse
    public void requestVerseFromRomeo(int verse)
    {
        System.out.println("PlayWriter: Requesting verse " + verse + " from Romeo.-> (" + theNovel[verse-1][1] + ")");
        //TO BE COMPLETED
        try
        {
            try
            {
                RomeoMailbox = new Socket(RomeoAddress, RomeoPort);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            OutputStream outputStream = this.RomeoMailbox.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(Double.toString(theNovel[verse-1][1]) + 'J');
            outputStreamWriter.flush();
        } catch (IOException e) {
            System.out.println("Client: I/0 error. "+ e);
        }
    }
    //Request next verse: Send letters to lovers communicating the partner's love in previous verse
    public void requestVerseFromJuliet(int verse)
    {
        System.out.println("PlayWriter: Requesting verse " + verse + " from Juliet.-> (" + theNovel[verse-1][0] + ")");
        //TO BE COMPLETED
        try
        {
            try {
                JulietMailbox = new Socket(JulietAddress, JulietPort);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            OutputStream outputStream = this.JulietMailbox.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(Double.toString(theNovel[verse-1][0]) + 'R');
            outputStreamWriter.flush();
        } catch (IOException e) {
            System.out.println("Client: I/0 error. "+ e);
        }
    }
    //Receive letter from Romeo with renovated love for current verse
    public void receiveLetterFromRomeo(int verse)
    {
        //System.out.println("PlayWriter: Receiving letter from Romeo for verse " + verse + ".");
        //TO BE COMPLETED
        try
        {
            InputStream inputStream = RomeoMailbox.getInputStream();
            InputStreamReader inputStreamReader =new InputStreamReader(inputStream);
            StringBuffer stringBuffer = new StringBuffer();
            char b;
            while (true)
            {
                b= (char) inputStreamReader.read();
                if(b == 'R')
                    break;
                stringBuffer.append(b);
            }
            double newValue = Double.parseDouble(stringBuffer.toString());
            theNovel[verse][0] = newValue;
            RomeoMailbox.close();
//            System.out.println(newValue);
        } catch (IOException e) {
            System.out.println("Failed to receive letter from Romeo: " + e.getMessage());
        }
        System.out.println("PlayWriter: Romeo's verse " + verse + " -> " + theNovel[verse][0]);
    }
    //Receive letter from Juliet with renovated love fro current verse
    public void receiveLetterFromJuliet(int verse)
    {
        //TO BE COMPLETED
        try
        {
            InputStream inputStream = JulietMailbox.getInputStream();
            InputStreamReader inputStreamReader =new InputStreamReader(inputStream);
            StringBuffer stringBuffer = new StringBuffer();
            char b;
            while (true)
            {
                b= (char) inputStreamReader.read();
                if(b == 'J')
                    break;
                stringBuffer.append(b);
            }
            double newValue = Double.parseDouble(stringBuffer.toString());
            theNovel[verse][1] = newValue;
            JulietMailbox.close();
//            System.out.println(newValue);
        } catch (IOException e) {
            System.out.println("Client: I/0 error. "+ e);
        }
        System.out.println("PlayWriter: Juliet's verse " + verse + " -> " + theNovel[verse][1]);
    }
    //Let the story unfold
    public void storyClimax()
    {
        for (int verse = 1; verse < novelLength; verse++)
        {
            //Write verse
            System.out.println("PlayWriter: Writing verse " + verse + ".");
            //TO BE COMPLETED
            requestVerseFromRomeo(verse);
            receiveLetterFromRomeo(verse);
            requestVerseFromJuliet(verse);
            receiveLetterFromJuliet(verse);

            System.out.println("PlayWriter: Verse " + verse + " finished.");
        }
    }
    //Character's death
    public void charactersDeath()
    {
        //TO BE COMPLETED
        try
        {
            RomeoMailbox.close();
            JulietMailbox.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //A novel consists of introduction, conflict, climax and denouement
    public void writeNovel() {
        System.out.println("PlayWriter: The Most Excellent and Lamentable Tragedy of Romeo and Juliet.");
        System.out.println("PlayWriter: A play in IV acts.");
        //Introduction,
        System.out.println("PlayWriter: Act I. Introduction.");
        this.createCharacters();
        //Conflict
        System.out.println("PlayWriter: Act II. Conflict.");
        this.charactersMakeAcquaintances();
        //Climax
        System.out.println("PlayWriter: Act III. Climax.");
        this.storyClimax();
        //Denouement
        System.out.println("PlayWriter: Act IV. Denouement.");
        this.charactersDeath();
    }
    //Dump novel to file
    public void dumpNovel() {
        FileWriter Fw = null;
        try {
            Fw = new FileWriter("RomeoAndJuliet.csv");
        } catch (IOException e) {
            System.out.println("PlayWriter: Unable to open novel file. " + e);
        }
        System.out.println("PlayWriter: Dumping novel. ");
        StringBuilder sb = new StringBuilder();
        for (int act = 0; act < novelLength; act++) {
            String tmp = theNovel[act][0] + ", " + theNovel[act][1] + "\n";
            sb.append(tmp);
            //System.out.print("PlayWriter [" + act + "]: " + tmp);
        }
        try {
            BufferedWriter br = new BufferedWriter(Fw);
            br.write(sb.toString());
            br.close();
        } catch (Exception e) {
            System.out.println("PlayWriter: Unable to dump novel. " + e);
        }
    }
    public static void main (String[] args) {
        PlayWriter Shakespeare = new PlayWriter();
        Shakespeare.writeNovel();
        Shakespeare.dumpNovel();
        System.exit(0);
    }
}
