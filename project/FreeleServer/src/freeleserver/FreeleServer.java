/*
 *  Chat server
 */
package freeleserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    public class UserServer implements Runnable {

        BufferedReader bufferedReader;
        Socket socket;
        PrintWriter client;

        /*
         Connection socket
         */
        
        public UserServer(Socket clientSocket, PrintWriter user) {
            client = user;
            try {
                socket = clientSocket;
                InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(isReader);
            }
            catch (Exception ex) {
                System.out.println("Error beginning StreamReader. \n");
            }           
        }
//--------------------------------------------------------------------------------------------------------        

        /*
         Message runner, here the program will handel the message resived.
         */
        public void run() {
            String message;
            String conncent = "Connect";
            String chat = "Chat";
            String[] data;

            try {
                while ((message = bufferedReader.readLine()) != null) {
                    System.out.println("Message: " + message);
                    data = message.split("β");
                    for (String i : data) {
                        System.out.println(i + "\n");
                    }
                    if (data[2].equals(conncent)) {
                        messageAll((data[0] + "β" + data[1] + "β" + chat));
                        addUser(data[0]);
                    } else if (data[2].equals(chat)) {
                        messageAll(message);
                    } else {
                        System.out.println("something gone wrong");
                    }
                }

            } catch (Exception e) {
                System.out.println("connection lost");
                clientOutputStream.remove(client);
            }

        }
    }
//--------------------------------------------------------------------------------------------------------        

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
