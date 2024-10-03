/*
 * Romeo.java
 *
 * Romeo class.  Implements the Romeo subsystem of the Romeo and Juliet ODE system.
 */

import javafx.util.Pair;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Romeo extends Thread {
    private ServerSocket ownServerSocket = null; //Romeo's (server) socket
    private Socket serviceMailbox = null; //Romeo's (service) socket
    private double currentLove = 0;
    private double a = 0;
    //The ODE constant
    //Class Constructor
    public Romeo(double initialLove) {
        currentLove = initialLove;
        a = 0.02;
        try {
            //TO BE COMPLETED
            int romeoPort = 7778;
            ownServerSocket = new ServerSocket(romeoPort, 10, InetAddress.getByName("127.0.0.1"));
            // Now, you can listen for incoming client connections and handle them as needed.
            // See the Java documentation for ServerSocket and Socket for more information on this.
            System.out.println("Romeo: What lady is that, which doth enrich the hand\n" + "       Of yonder knight?");
        } catch(Exception e) {
            System.out.println("Romeo: Failed to create own socket " + e);
        }
    }
    //Get acquaintance with lover;
    public Pair<InetAddress,Integer> getAcquaintance()
    {
        System.out.println("Romeo: Did my heart love till now? forswear it, sight! For I ne'er saw true beauty till this night.");
        //TO BE COMPLETED
        Pair<InetAddress,Integer> pair = null;
        try
        {
            pair = new Pair<>(InetAddress.getByName("127.0.0.1"), 7778);
        } catch (UnknownHostException e) {
            System.out.println(e);
        }
        return pair;
    }
    //Retrieves the lover's love
    public double receiveLoveLetter() {
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
                if (x=='J')
                    break;
                stringBuffer.append(x);
            }
            tmp = Double.parseDouble(stringBuffer.toString());
        } catch (IOException e) {
            System.out.println("Romeo: Failed to receive love letter from Juliet: " + e.getMessage());
        }
        System.out.println("Romeo: O sweet Juliet... (<-" + tmp + ")");
        return tmp;
    }
    //Love (The ODE system)
    //Given the lover's love at time t, estimate the next love value for Romeo
    public double renovateLove(double partnerLove)
    {
        System.out.println("Romeo: But soft, what light through yonder window breaks?\n" + "       It is the east, and Juliet is the sun.");
        currentLove = currentLove+(a*partnerLove);
        return currentLove;
    }
    //Communicate love back to playwriter
    public void declareLove()
    {
        //TO BE COMPLETED

        try
        {
            OutputStream os= serviceMailbox.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            System.out.println("Romeo: I would I were thy bird. (->"+currentLove+"R)");
            osw.write(currentLove + "R");
            osw.flush();
        } catch (IOException e) {
            System.out.println("Romeo: Failed to declare love to playwriter: " + e.getMessage());
        }
    }
    //Execution
    public void run ()
    {
        try {
            while (!this.isInterrupted()) {
                //Retrieve lover's current love
                double JulietLove = this.receiveLoveLetter();
                //Estimate new love value
                this.renovateLove(JulietLove);
                //Communicate love back to playwriter
                this.declareLove();
            }
        }catch (Exception e){
            System.out.println("Romeo: " + e);
        }
        if (this.isInterrupted()) {
            System.out.println("Romeo: Here's to my love. O true apothecary,\n" +
                    "Thy drugs are quick. Thus with a kiss I die." );
        }
    }
}