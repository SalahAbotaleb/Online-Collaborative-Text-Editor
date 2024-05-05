package org.cce.backend.service;

import org.cce.backend.dto.DocTitleDTO;
import org.cce.backend.dto.UserDocDTO;
import org.cce.backend.dto.DocumentDTO;

import java.util.List;


public interface DocService {
    DocumentDTO createDoc(DocTitleDTO title);
    String deleteDoc(String id);
    String updateDocTitle(String id, DocTitleDTO documentDTO);
    UserDocDTO addUser(String id, UserDocDTO userDoc);
    List<UserDocDTO> getSharedUsers(String id);
    String removeUser(String id, UserDocDTO userDoc);
    String updatePermission(String id, UserDocDTO userDoc);
    List<DocumentDTO> getAllDocs();
}
