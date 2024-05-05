package org.cce.backend.service;

public interface DocAuthorizationService {
    boolean canAccess(String docId);
    boolean canEdit(String docId);
    boolean fullAccess(String docId);
}
