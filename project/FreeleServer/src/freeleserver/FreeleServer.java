/*
 *  Chat server
 */
package freeleserver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Vetle, Mirza, Kjetil
 */
public class FreeleServer {

    /**
     * @param args the command line arguments
     */
    ArrayList clientOutputStream;
    ArrayList<String> onlineUsers = new ArrayList();
    
    
    /*
        This class is gonna handle our clients, with socket and with connections from diffrent users.
    */
    public class userServer implements Runnable {
        
        BufferedReader reader;
        Socket sock;
        PrintWriter client;
        
        /*
            Connection socket
        */
        public userServer(Socket clientSocket, PrintWriter user) {
            
        }
//--------------------------------------------------------------------------------------------------------        
        
        /*
            Message runner, here the program will handel the message resived.
        */
        public void run() {
            
        }
//--------------------------------------------------------------------------------------------------------        
        
        
    }
    
    /*
       Main function to start the program
    */
    public static void main(String[] args) {
        new FreeleServer().start();
    }
//--------------------------------------------------------------------------------------------------------    
    /*
       Server start function
    */
    public void start() {
        
    }
//--------------------------------------------------------------------------------------------------------    
    /**
     * 
     * @param data 
     */
    public addUser(String data) {
        
    }
//--------------------------------------------------------------------------------------------------------    
    /*
       This method removes the user from the server
    */
    public removeUser(String data) {
        
    }
//--------------------------------------------------------------------------------------------------------    
    /*
       This is a method that handels all the messages that is written, or notifacation that is made.
    */
    public void messageAll(String message) {
        
    }
    
}
