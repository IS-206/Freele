/*
 *  Chat server
 */
package freeleserver;

/**
 *
 * @author Mirza
 */
public class FreeleServer {

    /**
     * @param args the command line arguments
     */
    ArrayList clientOutputStream;
    Arraylist<String> onlineUsers = new ArrayList();
    
    
    /*
        This class is gonna handle our clients, with socket and with connections from diffrent users.
    */
    public class userServer implements Runnable {
        
        BufferedReader reader;
        Socket sock;
        PrinterWriter client;
        
        /*
            Connection socket
        */
        public userServer(socker clientSocket, PrintWriter user) {
            
        }
        
        
        /*
            Message runner, here the program will handel the message resived.
        */
        public void run() {
            
        }
        
        
        
    }
    
    /*
       Main function to start the program
    */
    public static void main(String[] args) {
        new FreeleServer().start();
    }
    
    /*
       Server start function
    */
    public void start() {
        
    }
    
    /*
       This method adds the user to the server
    */
    public addUser(String data) {
        
    }
    
    /*
       This method removes the user from the server
    */
    public removeUser(String data) {
        
    }
    
    /*
       This is a method that handels all the messages that is written, or notifacation that is made.
    */
    public void messageAll(String message) {
        
    }
    
}
