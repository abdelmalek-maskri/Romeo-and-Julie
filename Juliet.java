/*
 * Juliet.java
 *
 * Juliet class.  Implements the Juliet subsystem of the Romeo and Juliet ODE
system.
 */

import javafx.util.Pair;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Juliet extends Thread {
    private ServerSocket ownServerSocket = null; //Juliet's (server) socket
    private Socket serviceMailbox = null; //Juliet's (service) socket
    private double currentLove = 0;
    private double b = 0;
    //Class Constructor
    public Juliet(double initialLove)
    {
        currentLove = initialLove;
        b = 0.01;
        //TO BE COMPLETED
        try {
            int julietPort = 7779;
            String julietIPAddress="127.0.0.1";
            ownServerSocket = new ServerSocket(julietPort, 10, InetAddress.getByName(julietIPAddress));
            System.out.println("Juliet: Good pilgrim, you do wrong your hand too much, ...");
        } catch(Exception e) {
            System.out.println("Juliet: Failed to create own socket " + e);
        }
    }
    //Get acquaintance with lover;
    // Receives lover's socket information and share's own socket
    public Pair<InetAddress,Integer> getAcquaintance()
    {
        System.out.println("Juliet: My bounty is as boundless as the sea,\n" +
                "       My love as deep; the more I give to thee,\n" +
                "       The more I have, for both are infinite.");
        //TO BE COMPLETED
        return new Pair<>(ownServerSocket.getInetAddress(), ownServerSocket.getLocalPort());
    }
    //Retrieves the lover's love
    public double receiveLoveLetter()
    {
        //TO BE COMPLETED
        double tmp = 0;
        try {
            serviceMailbox = ownServerSocket.accept();
            InputStream outcomeStream = serviceMailbox.getInputStream();
            InputStreamReader outcomeStreamReader = new InputStreamReader(outcomeStream);
            StringBuffer stringBuffer = new StringBuffer();
            char x;
            while(true)
            {
                x=(char) outcomeStreamReader.read();
                if (x=='R')
                    break;
                stringBuffer.append(x);
            }
            tmp = Double.parseDouble(stringBuffer.toString());
        } catch (IOException e) {
            System.out.println("Romeo: Failed to receive love letter from Juliet: " + e.getMessage());
        }
        System.out.println("Juliet: Romeo, Romeo! Wherefore art thou Romeo? (<-" + tmp + ")");
        return tmp;
    }
    //Love (The ODE system)
    //Given the lover's love at time t, estimate the next love value for Romeo
    public double renovateLove(double partnerLove){
        System.out.println("Juliet: Come, gentle night, come, loving black-browed night,\n" +
                "       Give me my Romeo, and when I shall die,\n" +
                "       Take him and cut him out in little stars.");
        currentLove = currentLove-(b*partnerLove);
        return currentLove;
    }
    //Communicate love back to playWriter
    public void declareLove()
    {

        try{
            OutputStream os= serviceMailbox.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            System.out.println("Juliet: Good night, good night! Parting is such sweet sorrow,\n That I shall say good night till it be mor- row. (->"+currentLove+"J)");
            osw.write(currentLove + "J");
            osw.flush();
        } catch (IOException e) {
            System.out.println("Juliet: Failed to declare love to playwriter: " + e.getMessage());
        }
    }
    //Execution
    public void run () {
        try {
            while (!this.isInterrupted()) {
                //Retrieve lover's current love
                double RomeoLove = this.receiveLoveLetter();
                //Estimate new love value
                this.renovateLove(RomeoLove);
                //Communicate back to lover, Romeo's love
                this.declareLove();
            }
        }catch (Exception e){
            System.out.println("Juliet: " + e);
        }
        if (this.isInterrupted()) {
            System.out.println("Juliet: I will kiss thy lips.\n" +
                    "Haply some poison yet doth hang on them\n" +
                    "To make me die with a restorative.");
        }
    }
}