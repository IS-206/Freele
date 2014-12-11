/*
 *  Chat server
 */
package freeleserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

// CIPHER / GENERATORS
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;

// KEY SPECIFICATIONS
import java.security.spec.KeySpec;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;

// EXCEPTIONS
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

/**
 *
 * @author Vetle, Mirza, Kjetil
 */
public class FreeleServer {

    /**
     * @param args the command line arguments
     */
    HashMap<String, PrintWriter> userOutputStream;
    //HashMap<String, InetAddress> onlineUsers = new HashMap(); //trenger kanskje ikke denne listen, brukernavnene lagres også i userOutputStream
    //ArrayList<String> onlineUsers = new ArrayList();

    /*
     This class is gonna handle our clients, with socket and with connections from diffrent users.
     */
        // Encryption related members
    Cipher ecipher;
    Cipher dcipher;
    
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
            } catch (IOException ex) {
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
                    System.out.println("Encrypted Message" + message);
                    message = decrypt(message);
                    System.out.println("Message: " + message);
                    data = message.split("β");
                    for (String i : data) {
                        System.out.println(i + "\n");
                    }
                    if (data[2].equals(connect)) {
                        messageAll((data[0] + "β" + data[1] + "β" + chat));
                        addUser(data[0], client);
                        //userOutputStream.put(data[0], client);
                        //addUser(data[0], clientAddress);
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

            } catch (IOException e) {
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
        // initialize encryption/descryption
            // encryption initialization
            String passPhrase = "My Pass Phrase"; // key
            // 8-bytes Salt
            byte[] salt = {
                (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
                (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
            };

            // Iteration count
            int iterationCount = 19;

            try {

                KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
                SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

                ecipher = Cipher.getInstance(key.getAlgorithm());
                dcipher = Cipher.getInstance(key.getAlgorithm());

                // Prepare the parameters to the cipthers
                AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

                ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
                dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

                } catch (InvalidAlgorithmParameterException e) {
                System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
                } catch (InvalidKeySpecException e) {
                System.out.println("EXCEPTION: InvalidKeySpecException");
                } catch (NoSuchPaddingException e) {
                System.out.println("EXCEPTION: NoSuchPaddingException");
                } catch (NoSuchAlgorithmException e) {
                System.out.println("EXCEPTION: NoSuchAlgorithmException");
                } catch (InvalidKeyException e) {
                System.out.println("EXCEPTION: InvalidKeyException");
                }
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
        } catch (IOException e) {
            System.out.println("connection failed");
        }
        
    }
    /**
     * Takes a single String as an argument and returns an Encrypted version
     * of that String.
     * @param str String to be encrypted
     * @return <code>String</code> Encrypted version of the provided String
     */
    public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);

        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return null;
    }
       /**
     * Takes a encrypted String as an argument, decrypts and returns the 
     * decrypted String.
     * @param str Encrypted String to be decrypted
     * @return <code>String</code> Decrypted version of the provided String
     */
     public String decrypt(String str) {

        try {

            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");

        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return null;
    }
//--------------------------------------------------------------------------------------------------------    

    /**
     * This method add users into the system and sends a signal to print out
     * users on the client side
     *
     * @param data
     * @param p
     */
    public void addUser(String data, PrintWriter p) {
        String message;
        String add = "β βConnect";
        String done = "Serverβ βDone";
  
        userOutputStream.put(data, p);
        //String[] l = new String[(onlineUsers.size())];
        //onlineUsers.toArray(l);
        for (Entry<String, PrintWriter> s : userOutputStream.entrySet()) {
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
        userOutputStream.remove(data);
        //String[] l = new String[(onlineUsers.size())];
        //onlineUsers.toArray(l);
        for (Entry<String, PrintWriter> s : userOutputStream.entrySet()) {
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
                String CompleteMessage = m + message;
                p.println(encrypt(CompleteMessage));
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
                w.println(encrypt(m));
                System.out.println("Send " + m);
                w.flush();
            } catch (Exception e) {
                System.out.println("message all failed");
            }
        }
    }
}
