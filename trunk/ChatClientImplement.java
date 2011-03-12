import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.server.UnicastRemoteObject;
import java.lang.Thread;

import javax.swing.*;        
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;



public class ChatClientImplement extends UnicastRemoteObject implements ChatClient, Runnable
{
    private ChatServer cs;
    private String client_name;
    private JTextField text;
    private JTextArea text_area;
    
    public synchronized String GetName() throws RemoteException {
      return client_name;
    }


    public ChatClientImplement(ChatServer serv) throws RemoteException {
        super();
        cs = serv;
    }

    public synchronized void receive(String message, String who, char mode) throws RemoteException {
      switch(mode) {
        //if 'who' user has said 'message'
        case 's':
		  //Behrooz: To add "addHist(who + ": " + message)" function here
          text_area.append(who + ": " + message + "\n");
          break;
        //if 'who' user has entered the chat
        case 'e':
		  text_area.append(who + " has entered the chat\n\n");
          break;
        //if 'who' user has left the chat
        case 'l':
          text_area.append(who + " has left the chat\n");
          break;
        //if the user just came -- display 
        case 'h':
          text_area.append(message + "\n");
          break;
        }
    }
    
    public void run() {
      createAndShowGUI();
      
    }
    
    private void createAndShowGUI() {
        
        JFrame frame = new JFrame("Open Chat");
        GridBagLayout lout = new GridBagLayout();
        frame.setLayout(lout);
        
        GridBagConstraints c = new GridBagConstraints();

        text = new JTextField(10);
        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        frame.add(text, c);
        
        final JButton but1 = new JButton("Join");
        c.gridx = 1;
        c.gridy = 0;
        frame.add(but1, c);
        
        final JButton but2 = new JButton("Leave");
        c.gridx = 1;
        c.gridy = 1;
        frame.add(but2, c);
        
        text_area = new JTextArea(10, 20);
        final JScrollPane scrollPane = new JScrollPane(text_area);
        scrollPane.setMinimumSize(new Dimension(100,200));
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 5;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        frame.add(scrollPane, c);

        //before the user enters chat -- prompt to specify the name
        text_area.append("Enter your name, please: \n");
        
        text.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            String msg;
            try {
              
              //get next input from user
              if (text.getText() == "")
                 return;
              else
                msg = text.getText();
              
              //send the entered message to everyone
              cs.SendMessage(msg, ChatClientImplement.this.GetName(), 's');
              
              text.setText("");
            }  
            catch (Exception exc) {
              System.err.println("error: " + exc.getMessage());
              return;
            }
          }
        });
        
        but1.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            client_name = text.getText();
            text.setText("");
            text_area.setText("");
            text.requestFocusInWindow();
            
            //connect user to the chat
            try {
              cs.Connect(ChatClientImplement.this);
            }
            catch(Exception connectException) {
              System.out.println("Connection error");  
            }
          }
        });
        
        but2.addActionListener( new ActionListener() {
          
          public void actionPerformed(ActionEvent e) {
            try {
              cs.Disconnect(ChatClientImplement.this);
              System.exit(0);
            }
            catch (Exception exc) {
              System.err.println("Some problem while exiting");
              return;
            }
          }
        });

        //Display the window.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        frame.setVisible(true);
        
        text.requestFocusInWindow();
    }



    public static void main(String[] args) {
      String url = "rmi://localhost/ChatServer";

      try {
        ChatServer chatServ = (ChatServer)Naming.lookup(url);
        Thread thrd = new Thread(new ChatClientImplement(chatServ));
        thrd.start();
      }
      catch (Exception e) {
        System.err.println("Some problem while creating thread");  
      }

    }
}
