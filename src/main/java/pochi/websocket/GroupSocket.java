package pochi.websocket;

import java.io.IOException;
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
    public void onWebSocketConnect(Session session)
    {
    	List<Session> currentList;
    	String groupId = session.getRequestURI().getQuery();
    	currentList = groups.containsKey(groupId) ? groups.get(groupId) : new ArrayList<Session>();
    	currentList.add(session);
    	groups.put(groupId, currentList);
        
    	System.out.println("Socket Connected: " + session);
        System.out.println("Group is: " + groupId + " , and currnet group size is : " + currentList.size());
    }
    
    @OnMessage
    public void onWebSocketText(String message, Session session)
    {
    	String groupId = session.getRequestURI().getQuery();
    	List<Session> currentList = groups.get(groupId);
    	for(Session currentSession : currentList) {
    		try {
				currentSession.getBasicRemote().sendText(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    @OnClose
    public void onWebSocketClose(CloseReason reason, Session session)
    {
    	String groupId = session.getRequestURI().getQuery();
    	List<Session> currentList = groups.get(groupId);
    	currentList.remove(session);
    	
        System.out.println("Socket Closed: " + reason);
        System.out.println("Group is: " + groupId + " , and currnet group size is : " + currentList.size());

    }
    
    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }
}