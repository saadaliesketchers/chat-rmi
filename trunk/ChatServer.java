import java.rmi.*;
import java.util.*;

public interface ChatServer extends Remote {
  
  //send message to all the clients
	public void SendMessage(String message, String who, char mode) throws RemoteException;

  //connect the new user to chat
	public void Connect(ChatClient c) throws RemoteException;
  
  //disconnect the user from chat
	public void Disconnect(ChatClient c) throws RemoteException;
}
