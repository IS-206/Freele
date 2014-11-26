/*
 *  Chat server
 */
package freeleserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author Vetle, Mirza, Kjetil
 */
public class FreeleServer {

    /**
     * @param args the command line arguments
     */
    HashMap<String, PrintWriter> userOutputStream;
    HashMap<String, InetAddress> onlineUsers = new HashMap(); //trenger kanskje ikke denne listen, brukernavnene lagres også i userOutputStream
    //ArrayList<String> onlineUsers = new ArrayList();

    /*
     This class is gonna handle our clients, with socket and with connections from diffrent users.
     */
    public class UserServer implements Runnable {

        BufferedReader bufferedReader;
        Socket socket;
        PrintWriter client;
        InetAddress clientAddress;

        /*
         Connection socket
         */
        public UserServer(Socket clientSocket, PrintWriter user) {
            client = user;
            try {
                socket = clientSocket;
                clientAddress = socket.getInetAddress();
                InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(isReader);
            } catch (Exception ex) {
                System.out.println("Error beginning StreamReader. \n");
            }
        }
//--------------------------------------------------------------------------------------------------------        

        /*
         Message runner, here the program will handel the message resived.
         */
        @Override
        public void run() {
            String message;
            String connect = "Connect";
            String chat = "Chat";
            String disconnect = "Disconnect";
            String privateChat = "Private";
            String[] data;

            try {
                while ((message = bufferedReader.readLine()) != null) {
                    System.out.println("Message: " + message);
                    data = message.split("β");
                    for (String i : data) {
                        System.out.println(i + "\n");
                    }
                    if (data[2].equals(connect)) {
                        messageAll((data[0] + "β" + data[1] + "β" + chat));
                        userOutputStream.put(data[0], client);
                        addUser(data[0], clientAddress);
                    } else if (data[2].equals(disconnect)) {
                        messageAll((data[0] + "βhas disconnected." + "β" + chat));
                        removeUser(data[0]);
                    } else if (data[2].equals(chat)) {
                        messageAll(message);
                    } else if (data[2].equals(privateChat)) {
                        privateConversation(data[0], data[1], data[3]);
                        System.out.println(data[0] + "5kommer hit" + data[1] + data[2] + data[3]);
                    } else {
                        System.out.println("something gone wrong");
                    }
                }

            } catch (Exception e) {
                System.out.println("connection lost");
                userOutputStream.remove(client);
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
     This method establish connection to the clients and creats a new thread to the client.
     */

    public void start() {
        userOutputStream = new HashMap();

        try {
            ServerSocket serverSocket = new ServerSocket(4000);

            while (true) {
                Socket clientSock = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
                //userOutputStream.add(writer);

                Thread listener = new Thread(new UserServer(clientSock, writer));
                listener.start();
                System.out.println("connection complete");

            }
        } catch (Exception e) {
            System.out.println("connection failed");
        }
    }
//--------------------------------------------------------------------------------------------------------    

    /**
     * This method add users into the system and sends a signal to print out
     * users on the client side
     *
     * @param data
     * @param ip
     */
    public void addUser(String data, InetAddress ip) {
        String message;
        String add = "β βConnect";
        String done = "Serverβ βDone";
        onlineUsers.put(data, ip);
        //String[] l = new String[(onlineUsers.size())];
        //onlineUsers.toArray(l);

        for (Entry<String, InetAddress> s : onlineUsers.entrySet()) {
            String k = s.getKey();
            message = (k + add);
            messageAll(message);
        }
        messageAll(done);
    }
//--------------------------------------------------------------------------------------------------------    

    /**
     * This method removes users into the system and sends a signal to print out
     * users on the client side
     *
     * @param data
     */
    public void removeUser(String data) {
        String message;
        String add = "β βConnect";
        String done = "Serverβ βDone";
        onlineUsers.remove(data);
        //String[] l = new String[(onlineUsers.size())];
        //onlineUsers.toArray(l);

        for (Entry<String, InetAddress> s : onlineUsers.entrySet()) {
            String k = s.getKey();
            message = (k + add);
            messageAll(message);
        }
        messageAll(done);
    }

//--------------------------------------------------------------------------------------------------------
    /**
     * This method is used for sending messages one to one
     *
     * @param username
     * @param m
     * @param privName
     */
    public void privateConversation(String username, String m, String privName) {
        String message = "ββPrivate" + "β" + privName;
        for (Entry<String, PrintWriter> s : userOutputStream.entrySet()) {
            PrintWriter p = s.getValue();
            if (username.equals(s.getKey())) {
                p.println(m + message);
                p.flush();
            }
        }

    }
//--------------------------------------------------------------------------------------------------------    
//    /**
//     * This method is used to send messages to all clients connected to the server
//     * @param m 
//     */
//    public void messageAll(String m) {
//        Iterator i = userOutputStream.iterator();
//        
//        while(i.hasNext()){
//            try{
//                PrintWriter w = (PrintWriter) i.next();
//                w.println(m);
//                System.out.println("Send " + m);
//                w.flush();
//            }catch(Exception e){
//                System.out.println("message all failed");
//            }
//        }
//    }

    public void messageAll(String m) {
        for (Entry<String, PrintWriter> name : userOutputStream.entrySet()) {
            try {
                PrintWriter w = name.getValue();
                w.println(m);
                System.out.println("Send " + m);
                w.flush();
            } catch (Exception e) {
                System.out.println("message all failed");
            }
        }
    }
}
