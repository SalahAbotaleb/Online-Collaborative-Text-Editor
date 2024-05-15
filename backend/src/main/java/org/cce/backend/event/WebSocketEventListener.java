package org.cce.backend.event;

import org.cce.backend.dto.ActiveUsers;
import org.cce.backend.engine.Crdt;
import org.cce.backend.engine.CrdtManagerService;
import org.cce.backend.entity.WebSocketSession;
import org.cce.backend.repository.DocRepository;
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
    @Autowired
    private CrdtManagerService crdtManagerService;
    public WebSocketEventListener(){
        socketSession =new ConcurrentHashMap<>();
        docSessions=new ConcurrentHashMap<>();
    }
    @EventListener
    private void handleSessionConnected(SessionConnectedEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String sessionId = getSessionId(headers);
        String username = getUsername(headers);
        socketSession.put(sessionId,new WebSocketSession(username,""));
    }

    @EventListener
    private void handleSessionSubscribe(SessionSubscribeEvent event){
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String docId = getDocId(headers);
        if(docId == "") return;
        String sessionId = getSessionId(headers);
        socketSession.get(sessionId).setDocId(docId);
        if(docSessions.containsKey(docId) == false){
            crdtManagerService.createCrdt(Long.parseLong(docId));
        }
        List<String> docSessionParticipants = docSessions.getOrDefault(docId,new ArrayList<>());
        docSessionParticipants.add(sessionId);
        docSessions.put(docId,docSessionParticipants);
        notifyActiveUsers(docId);
    }
    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String sessionId= headers.getSessionId();
        WebSocketSession sessionData = socketSession.get(sessionId);
        socketSession.remove(sessionId);
        if(sessionData == null) return;
        String docId = sessionData.getDocId();
        List<String> docSessionParticipants = docSessions.get(docId);
        if(docSessionParticipants==null) return;
        docSessionParticipants.remove(sessionId);
        if(docSessionParticipants.size() == 0){
            docSessions.remove(docId);
            crdtManagerService.saveAndDeleteCrdt(Long.parseLong(docId));
        }
        notifyActiveUsers(docId);
    }

    private String extractDocIdFromPath(String path) {
        UriTemplate uriTemplate = new UriTemplate("/docs/broadcast/changes/{id}");
        Map<String, String> matchResult = uriTemplate.match(path);
        return matchResult.getOrDefault("id","");
    }

    private void notifyActiveUsers(String docId){
        List<String> docSessionParticipants = docSessions.get(docId);
        if(docSessionParticipants==null){
            return;
        }
        ActiveUsers activeUsers = new ActiveUsers();
        List<String> usernames = docSessionParticipants.stream().map((sessionKey)->
        {
            return socketSession.get(sessionKey).getUsername();
        }).toList();
        activeUsers.setUsernames(usernames);
        messagingTemplate.convertAndSend("/docs/broadcast/usernames/"+docId,activeUsers);
    }

    private String getSessionId(SimpMessageHeaderAccessor headers){
        return headers.getSessionId();
    }

    private String getUsername(SimpMessageHeaderAccessor headers){
        return headers.getUser().getName();
    }

    private String getDocId(SimpMessageHeaderAccessor headers){
        return extractDocIdFromPath(headers.getDestination());
    }
}
