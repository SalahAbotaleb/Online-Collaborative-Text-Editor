package org.cce.backend.service;

public interface UserWSSessionService {
    void addUserSession(String sessionId,String userId);
    void checkSessionPermissionForDoc(String sessionId,String documentId);
}
