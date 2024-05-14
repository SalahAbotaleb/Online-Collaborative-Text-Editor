package org.cce.backend.event;

import org.antlr.v4.runtime.misc.Pair;
import org.cce.backend.dto.ActiveUsers;
import org.cce.backend.entity.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.UriTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private ConcurrentHashMap<String, WebSocketSession> socketSession;
    private ConcurrentHashMap<String, List<String>> docSessions;
    public WebSocketEventListener(){
        socketSession =new ConcurrentHashMap<>();
        docSessions=new ConcurrentHashMap<>();
    }
    @EventListener
    private void handleSessionConnected(SessionConnectedEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String sessionId= headers.getSessionId();
        String username = headers.getUser().getName();
        System.out.println("connect");
        socketSession.put(sessionId,new WebSocketSession(username,""));
    }

    @EventListener
    private void handleSessionSubscribe(SessionSubscribeEvent event){
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String docId = extractDocIdFromPath(headers.getDestination());
        if(docId == "") return;
        String sessionId = headers.getSessionId();
        socketSession.get(sessionId).setDocId(docId);

        List<String> docSessionParticipants = docSessions.getOrDefault(docId,new ArrayList<>());
        docSessionParticipants.add(sessionId);
        docSessions.put(docId,docSessionParticipants);

        ActiveUsers activeUsers = new ActiveUsers();

        List<String> usernames = docSessionParticipants.stream().map((sessionKey)->
        {
            return socketSession.get(sessionId).getUsername();
        }).toList();
        activeUsers.setUsernames(usernames);
        System.out.println(activeUsers);
        System.out.println(socketSession);
        System.out.println(docSessions);
    }
    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String sessionId= headers.getSessionId(); System.out.println(headers.getSessionId());
        System.out.println("diconnect");
        WebSocketSession sessionData = socketSession.get(sessionId);
        socketSession.remove(sessionId);
        if(sessionData == null) return;
        String docId = sessionData.getDocId();
        List<String> sessions = docSessions.get(docId);
        if(sessions==null) return;
        sessions.remove(sessionId);
        if(sessions.size()==0){
            docSessions.remove(docId);
        }
        System.out.println(socketSession);
        System.out.println(docSessions);

    }

    private String extractDocIdFromPath(String path) {
        UriTemplate uriTemplate = new UriTemplate("/docs/broadcast/changes/{id}");
        Map<String, String> matchResult = uriTemplate.match(path);
        return matchResult.getOrDefault("id","");
    }
}
