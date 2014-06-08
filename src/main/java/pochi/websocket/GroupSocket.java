package pochi.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.EncodeException;


@ClientEndpoint
@ServerEndpoint(value="/events/")
public class GroupSocket
{
		public static ConcurrentHashMap<String, CopyOnWriteArrayList<Session>> groups =
			new ConcurrentHashMap<String, CopyOnWriteArrayList<Session>>();

    @OnOpen
    public void onWebSocketConnect(Session session)
    {
    	CopyOnWriteArrayList<Session> currentList;
    	String groupId = session.getRequestURI().getQuery();
    	currentList = groups.containsKey(groupId) ? groups.get(groupId) : new CopyOnWriteArrayList<Session>();
    	currentList.add(session);
    	groups.put(groupId, currentList);

    	System.out.println("Socket Connected: " + session);
    	System.out.println("Group is: " + groupId + " , and currnet group size is : " + currentList.size());
    }

    @OnMessage
    public void onWebSocketText(String message, Session session) throws IOException, EncodeException
    {
    	String groupId = session.getRequestURI().getQuery();
    	List<Session> currentList = groups.get(groupId);
    	for(Session currentSession : currentList) {
    		try {
					currentSession.getBasicRemote().sendObject(message);
				} catch (IOException e) {
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
    public void onWebSocketError(Session session, Throwable cause)
    {
      cause.printStackTrace(System.err);
    }
}
