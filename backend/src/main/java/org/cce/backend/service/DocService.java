package org.cce.backend.service;

import org.cce.backend.dto.DocTitleDTO;
import org.cce.backend.dto.DocumentChangeDTO;
import org.cce.backend.dto.UserDocDTO;
import org.cce.backend.dto.DocumentDTO;

import java.util.List;


public interface DocService {
    DocumentDTO createDoc(DocTitleDTO title);
    Long deleteDoc(Long id);
    String updateDocTitle(Long id, DocTitleDTO documentDTO);
    UserDocDTO addUser(Long id, UserDocDTO userDoc);
    List<UserDocDTO> getSharedUsers(Long id);
    String removeUser(Long id, UserDocDTO userDoc);
    String updatePermission(Long id, UserDocDTO userDoc);
    List<DocumentDTO> getAllDocs();
    List<DocumentChangeDTO> getDocChanges(Long id);

    DocumentDTO getDoc(Long id);
}
