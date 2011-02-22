import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.util.LinkedList;
import java.util.Scanner;

public class ChatServerImplement extends UnicastRemoteObject implements ChatServer
{
  private LinkedList <ChatClient> clients;
  private ArrayList <String> history;

  public ChatServerImplement() throws RemoteException {
    super();
    clients = new LinkedList<ChatClient>(); 
    history = new ArrayList<String>();//create a history array
  }

  public synchronized void SendMessage(String message, String who, char mode) throws RemoteException {
    
    if (mode == 's') {
      history.add(who + " said " + message);
    }
    
    for (int i = 0; i < clients.size(); i++)
      clients.get(i).receive(message, who, mode);
  }

  public synchronized void Connect(ChatClient c) throws RemoteException {
    clients.add(c); 
    
    if (!history.isEmpty()) {
      for (int i = 0; i < history.size(); i++) {
        c.receive(history.get(i), "", 'h');  
      }  
    }

    SendMessage(" has entered chat", c.GetName(), 'e');
  }

  public void Disconnect(ChatClient c) throws RemoteException {
    clients.remove(c); 
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
