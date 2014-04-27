package pochi.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value="/events/")
public class GroupSocket
{
	public static HashMap<String, List<Session>> groups = new HashMap<String, List<Session>>();
	
    @OnOpen
    public void onWebSocketConnect(Session sess)
    {
    	List<Session> currentList;
    	String groupId = sess.getRequestURI().getQuery();
    	currentList = groups.containsKey(groupId) ? groups.get(groupId) : new ArrayList<Session>();
    	currentList.add(sess);
    	groups.put(groupId, currentList);
        
    	System.out.println("Socket Connected: " + sess);
        System.out.println("Group is: " + groupId + " , and currnet group size is : " + currentList.size());
    }
    
    @OnMessage
    public void onWebSocketText(String message)
    {
        System.out.println("Received TEXT message: " + message);
    }
    
    @OnClose
    public void onWebSocketClose(Session sess, CloseReason reason)
    {
    	String groupId = sess.getRequestURI().getQuery();
    	List<Session> currentList = groups.get(groupId);
    	currentList.remove(sess);
    	
        System.out.println("Socket Closed: " + reason);
        System.out.println("Group is: " + groupId + " , and currnet group size is : " + currentList.size());

    }
    
    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }
}