import java.rmi.*;
import java.util.*;

public interface ChatServer extends Remote {

	public void SendMessage(String message, String who, char mode) throws RemoteException;
	public void Connect(ChatClient c) throws RemoteException;
	public void Disconnect(ChatClient c) throws RemoteException;
}
