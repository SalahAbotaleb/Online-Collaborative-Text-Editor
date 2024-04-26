package org.cce.backend.service;

import org.cce.backend.dto.UserDocDTO;
import org.cce.backend.dto.DocumentDTO;


public interface DocService {
    DocumentDTO createDoc(DocumentDTO documentDTO);
    String deleteDoc(String id);
    String updateDocTitle(String id, DocumentDTO documentDTO);
    UserDocDTO addUser(String id, UserDocDTO userDoc);
    DocumentDTO getSharedUsers(String id);
    String removeUser(String id, UserDocDTO userDoc);
    String updatePermission(String id, UserDocDTO userDoc);
}
