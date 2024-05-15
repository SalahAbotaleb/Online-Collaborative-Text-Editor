package org.cce.backend.service;

import org.cce.backend.entity.Doc;

public interface DocAuthorizationService {
    boolean canAccess(String docId);
    boolean canEdit(String username, Doc doc);
    boolean fullAccess(String username, Doc doc);
}
