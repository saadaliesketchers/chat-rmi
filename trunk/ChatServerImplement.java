import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ChatServerImplement extends UnicastRemoteObject implements ChatServer
{
  //list of users connected to Chat server
  private LinkedList <ChatClient> clients;
  //list of message history
  private ArrayList <String> history;

  public ChatServerImplement() throws RemoteException {
    super();
    clients = new LinkedList<ChatClient>(); 
    history = new ArrayList<String>();//create a history array
  }

  public synchronized void SendMessage(String message, String who, char mode) throws RemoteException {
    
    //if type=='s' -- don't forget to add the message to history
    if (mode == 's') {
      history.add(who + " have said " + message);
    }
    
    //send all the information to each user
    for (int i = 0; i < clients.size(); i++)
      clients.get(i).receive(message, who, mode);
  }


  public synchronized void Connect(ChatClient c) throws RemoteException {
    //add a new user to the list
    clients.add(c); 
	//Behrooz: To add "initHist()" function here	
    if (!history.isEmpty()) {
      //send to user all the messages from history
      for (int i = 0; i < history.size(); i++) {
        c.receive(history.get(i), "", 'h');  
      }  
    }

    //notify everybody that a new user has come
    SendMessage(" has entered chat", c.GetName(), 'e');
  }


  public void Disconnect(ChatClient c) throws RemoteException {
    
    //remove user from the list of users
    clients.remove(c); 
	//Behrooz: To add "endHist()" function here
    
    //notify everybody that user has left the chat
    SendMessage(" has left chat", c.GetName(), 'l');
  }

  public static void main (String[] args) {
    try {
      Naming.rebind("ChatServer", new ChatServerImplement() );
    }  
    catch(Exception e) {
      System.err.println("Some problem while rebinding");
    }
    
  }
  
}
