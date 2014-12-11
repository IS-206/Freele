/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package freeleclient;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author Vetle, Mirza, Kjetil
 */
public class FreeleClient extends javax.swing.JFrame {

    String username;
    String serverIP;
    int port;
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    ArrayList<String> userList = new ArrayList();
    Boolean isConnected = false;
    DefaultListModel model = new DefaultListModel();
    JList onlineUsersList = new JList(model);
    PrivateChat chat;
    PrivateChat ownChat;
    HashMap<String, String> ongoingPrivChat = new HashMap<>();

    public FreeleClient() {
        initComponents();
        ongoingPrivChat.put("Init", "List");
    }

    public class IncomingReader implements Runnable {

        /**
         * Responds on signals from the server (Invoked by the threadListener())
         */
        @Override
        public void run() {
            String transfer;
            String[] data;
            String done = "Done";
            String connect = "Connect";
            String chat = "Chat";
            String privateChat = "Private";

            try {
                while ((transfer = bufferedReader.readLine()) != null) {
                    data = transfer.split("β"); // this symbol will split

                    if (data[2].equals(chat)) {
                        chatArea.append(data[0] + ": " + data[1] + "\n");
                        chatArea.setCaretPosition(chatArea.getDocument().getLength());
                    } else if (data[2].equals(connect)) {
                        chatArea.removeAll();
                        addUser(data[0]);
                    } else if (data[2].equals(done)) {
                        writeUsers();
                        userList.clear();
                    } else if (data[2].equals(privateChat)) {
                        privateMessage(data[3], printWriter, data[0]);
                    }

                }
            } catch (IOException e) {
                //
            }
        }
    }

//--------------------------------------------------------------------------------------------------------        
    /**
     * Starts a new Thread of the IncommingReader class
     */
    public void threadListener() {
        Thread IncommingReader = new Thread(new IncomingReader());
        IncommingReader.start();
    }

//--------------------------------------------------------------------------------------------------------         
    /**
     * Adds user to addUser list that shows online users on the client side
     *
     * @param data
     */
    public void addUser(String data) {
        userList.add(data);
    }

    public void writeUsers() {
        model.clear();
        for (String s : userList) {
            model.addElement(s);
        }
        jScrollPane4.setViewportView(onlineUsersList);
    }
//--------------------------------------------------------------------------------------------------------         

    /**
     * Prints the userList in the onlineUsers area
     */
//    public void writeUsers() {
//        String[] list = new String[userList.size()];
//        userList.toArray(list);
//        for (String s : list) {
//            onlineUsers.append(s + "\n");
//        }
//        System.out.println("1 userList har " + userList.size() + " items");
//        final String[] strings = new String[userList.size()];
//        userList.toArray(strings);
//        jList1.setModel(new javax.swing.AbstractListModel() {
//            
//            
//            @Override
//            public int getSize() { return strings.length; }
//            @Override
//            public String getElementAt(int i) { return strings[i]; }
//        });
//        jList1 = new JList(userList.toArray(new String[userList.size()]));
//        System.out.println("2 strings har " + strings.length + " items.");
//    }
//--------------------------------------------------------------------------------------------------------     
    /**
     * Prints the userList in the onlineUsers area
     *
     * @param privName //the name of the client that creates the connection
     * @param p
     * @param m
     */
    public void privateMessage(String privName, PrintWriter p, String m) {
        if (ownChat == null) {
            ownChat = new PrivateChat(privName, p, usernameField.getText());
            ownChat.setVisible(true);
        }
        if(!m.equals("")){
            ownChat.print(privName + ": " + m + "\n");
        }
    }

//--------------------------------------------------------------------------------------------------------         
    /**
     * Sends a disconnect-signal to the server and flushes the buffer
     */
    public void signalingDisconnect() {
        String off = (username + "β βDisconnect");
        try {
            printWriter.println(off);
            printWriter.flush();
        } catch (Exception e) {
            chatArea.append("Could not send the disconnect message. \n");
        }
    }

//--------------------------------------------------------------------------------------------------------         
    /**
     * Closes the socket to the server and cleans the client-window
     */
    public void disconnect() {
        try {
            chatArea.append("Disconnected \n");
            socket.close();
            model.clear();
        } catch (IOException e) {
            chatArea.append("Failed to disconnect");
        }
        isConnected = false;
        usernameField.setEditable(true);
        getIP.setEditable(true);
        getPORT.setEditable(true);
    }

//--------------------------------------------------------------------------------------------------------         
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        usernameField2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        connectButton = new javax.swing.JButton();
        disconnectButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        inputField = new javax.swing.JTextArea();
        getIP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        getPORT = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        privateConversationButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        usernameField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameField2ActionPerformed(evt);
            }
        });
        usernameField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                usernameField2KeyPressed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Username:");

        usernameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameFieldActionPerformed(evt);
            }
        });
        usernameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                usernameFieldKeyPressed(evt);
            }
        });

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        disconnectButton.setText("Disconnect");
        disconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Online users");

        chatArea.setEditable(false);
        chatArea.setColumns(20);
        chatArea.setLineWrap(true);
        chatArea.setRows(5);
        jScrollPane2.setViewportView(chatArea);

        inputField.setColumns(20);
        inputField.setLineWrap(true);
        inputField.setRows(1);
        inputField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputFieldKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(inputField);

        getIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getIPActionPerformed(evt);
            }
        });
        getIP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                getIPKeyPressed(evt);
            }
        });

        jLabel3.setText("IP :");

        getPORT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getPORTActionPerformed(evt);
            }
        });
        getPORT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                getPORTKeyPressed(evt);
            }
        });

        jLabel4.setText("Port:");

        privateConversationButton.setText("Private conversation");
        privateConversationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                privateConversationButtonActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(privateConversationButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(getIP, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18)
                                        .addComponent(getPORT))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(connectButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(disconnectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(49, 49, 49)
                                .addComponent(jLabel2)))
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(getIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(getPORT, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(connectButton)
                    .addComponent(disconnectButton)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(privateConversationButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void usernameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameFieldActionPerformed

    /**
     * Sends the server a disconnect signal before the socket closes
     *
     * @param evt
     */
    private void disconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectButtonActionPerformed
        signalingDisconnect();
        disconnect();
    }//GEN-LAST:event_disconnectButtonActionPerformed

    /**
     * Creates a socket and establish a connection to the server process.
     *
     * @param evt
     */
    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        if (isConnected == false) {
            username = usernameField.getText();
            serverIP = getIP.getText();
            port = Integer.parseInt(getPORT.getText());
            usernameField.setEditable(false);
            getIP.setEditable(false);
            getPORT.setEditable(false);

            try {
                socket = new Socket(serverIP, port);
                InputStreamReader reader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(reader);
                printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println(username + "βhas connected.βConnect");
                printWriter.flush();
                isConnected = true;
            } catch (IOException e) {
                chatArea.append("Cannot connect, please try again. \n");
                usernameField.setEditable(true);
            }
            threadListener();
        } else if (isConnected == true) {
            chatArea.append("You are already connected \n");
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    // Added a keyvenet in the inputfield, where when the user press the ENTER on the keyboard that sends the message to the server.
    private void inputFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            String n = "";
            if ((inputField.getText()).equals(n)) {
                inputField.setText("");
                inputField.requestFocus();
            } else {
                try {
                    printWriter.println(username + "β" + inputField.getText() + "β" + "Chat");
                    printWriter.flush();
                } catch (Exception e) {
                    chatArea.append("Error in sending message. \n");
                }
                inputField.setText("");
                inputField.requestFocus();
            }

            inputField.setText(null);
            inputField.requestFocus();
        }
    }//GEN-LAST:event_inputFieldKeyPressed

    private void usernameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernameFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (isConnected == false) {
                username = usernameField.getText();
                usernameField.setEditable(false);
                getIP.setEditable(false);
                getPORT.setEditable(false);

                try {
                    socket = new Socket(serverIP, port);
                    InputStreamReader reader = new InputStreamReader(socket.getInputStream());
                    bufferedReader = new BufferedReader(reader);
                    printWriter = new PrintWriter(socket.getOutputStream());
                    printWriter.println(username + "βhas connected.βConnect");
                    printWriter.flush();
                    isConnected = true;
                } catch (IOException e) {
                    chatArea.append("Cannot connect, please try again. \n");
                    usernameField.setEditable(true);
                }
                threadListener();
            } else if (isConnected == true) {
                chatArea.append("You are already connected \n");
            }
        }
    }//GEN-LAST:event_usernameFieldKeyPressed

// Fixes the new line that the inputFieldKeyPressed() makes after sending a message, this will set the text field to nothing and afther you release the enter button.
    private void inputFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputFieldKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            inputField.setText(null);
        }
    }//GEN-LAST:event_inputFieldKeyReleased

    private void getIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getIPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_getIPActionPerformed

    private void getIPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_getIPKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_getIPKeyPressed

    private void usernameField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameField2ActionPerformed

    private void usernameField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernameField2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameField2KeyPressed

    private void getPORTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getPORTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_getPORTActionPerformed

    private void getPORTKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_getPORTKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_getPORTKeyPressed

    private void privateConversationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_privateConversationButtonActionPerformed
        String targetName = onlineUsersList.getSelectedValue().toString();
        String ownName = usernameField.getText();
        boolean isListed = false;
        for (Map.Entry<String, String> s : ongoingPrivChat.entrySet()) {
            System.out.println("leser 1");
            if (s.getValue().equals(targetName) && s.getKey().equals(ownName) || s.getValue().equals(ownName) && s.getKey().equals(targetName)) {
                isListed = true;

                System.out.println("leser 2");
            }
        }
        if (isListed == false) {
            ongoingPrivChat.put(ownName, targetName);
            ownChat = new PrivateChat(targetName, printWriter, ownName);
            ownChat.setVisible(true);
        }
        System.out.println("leser 3");
        String u = usernameField.getText();
        printWriter.println(targetName + "ββPrivate" + "β" + u);
        printWriter.flush();
        System.out.println(ongoingPrivChat.size());
    }//GEN-LAST:event_privateConversationButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FreeleClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FreeleClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FreeleClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FreeleClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FreeleClient().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatArea;
    private javax.swing.JButton connectButton;
    private javax.swing.JButton disconnectButton;
    private javax.swing.JTextField getIP;
    private javax.swing.JTextField getPORT;
    private javax.swing.JTextArea inputField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton privateConversationButton;
    private javax.swing.JTextField usernameField;
    private javax.swing.JTextField usernameField2;
    // End of variables declaration//GEN-END:variables
}
