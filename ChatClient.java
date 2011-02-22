import java.rmi.*;
import java.util.*;

public interface ChatClient extends Remote {
	public void receive(String message, String who, char mode) throws RemoteException;
  public String GetName() throws RemoteException; 
}
