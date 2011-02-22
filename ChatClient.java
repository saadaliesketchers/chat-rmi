import java.rmi.*;
import java.util.*;

public interface ChatClient extends Remote {
  //----------------Receive--------------------
  //  display information to the user
  //  type of information is specified by the 'mode' flag:
  //  's' -- someone said something
  //  'e' -- someone has entered the chat
  //  'l' -- someone has left the chat
  //  'h' -- display the history
	public void receive(String message, String who, char mode) throws RemoteException;
  
  //return the name of user
  public String GetName() throws RemoteException; 
}
