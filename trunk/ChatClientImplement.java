import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.lang.Thread;

public class ChatClientImplement extends UnicastRemoteObject implements ChatClient, Runnable
{
    private ChatServer cs;
    private String client_name;
    
    public synchronized String GetName() throws RemoteException {
      return client_name;
    }

    public ChatClientImplement(ChatServer serv) throws RemoteException {
        super();
        cs = serv;
        System.out.println("Enter your name, please: ");
        Scanner forname = new Scanner(System.in);
        client_name = forname.nextLine();
        
        cs.Connect(this);
    }

    public synchronized void receive(String message, String who, char mode) throws RemoteException {
      switch(mode) {
        case 's':
          System.out.println (who + " says : " + message);
          break;
        case 'e':
          System.out.println (who + " entered the chat");
          break;
        case 'l':
          System.out.println (who + " left the chat");
          break;
        case 'h':
          System.out.println (message);
          break;
        }
    }
    
    public void run() {
      Scanner in = new Scanner(System.in);
      String msg;

      while(true) {
        try {
          msg = in.nextLine();
          if (msg.equals("exit")) {
            cs.Disconnect(this);
            break;
          }
          cs.SendMessage(msg, this.GetName(), 's');
        }  
        catch (Exception e) {
          System.err.println("Some problem while broadcasting");
        }
      }
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
