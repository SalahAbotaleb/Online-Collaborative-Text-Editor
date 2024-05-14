package org.cce.backend.service;

import org.cce.backend.dto.DocTitleDTO;
import org.cce.backend.dto.UserDocDTO;
import org.cce.backend.entity.*;
import org.cce.backend.dto.DocumentDTO;
import org.cce.backend.enums.Permission;
import org.cce.backend.exception.UnauthorizedUserException;
import org.cce.backend.exception.UserNotFoundException;
import org.cce.backend.mapper.DocumentMapper;
import org.cce.backend.mapper.UserDocMapper;
import org.cce.backend.mapper.UserMapper;
import org.cce.backend.repository.DocRepository;
import org.cce.backend.repository.UserDocRepository;
import org.cce.backend.repository.UserRepository;
import org.cce.backend.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DocServiceImpl implements DocService {
    @Autowired
    DocRepository docRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    DocumentMapper documentMapper;

    @Autowired
    UserDocMapper userDocMapper;

    @Autowired
    UserDocRepository userDocRepository;


    private User getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userRepository.findById(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public DocumentDTO createDoc(DocTitleDTO docTitleDTO) {
        System.out.println("test");
        String title = docTitleDTO.getTitle();
        User user = getCurrentUser();
        System.out.println("aftergetuser");
        Doc doc = Doc.builder()
                .owner(user)
                .title(title)
                .content("")
                .sharedWith(new ArrayList<>())
                .build();

        Doc savedDoc = docRepository.save(doc);
        return documentMapper.toDto(savedDoc);
    }

    @Transactional
    @Override
    public Long deleteDoc(Long id) {
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
        docRepository.deleteById(id);
        return id;
    }

    @Transactional
    @Override
    public String updateDocTitle(Long id, DocTitleDTO title) {
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
        doc.setTitle(title.getTitle());
        docRepository.save(doc);
        return "Title updated successfully";
    }

    @Transactional
    @Override
    public UserDocDTO addUser(Long id, UserDocDTO userDocDTO) {
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
        User user = userRepository.findById(userDocDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserDocId userDocId = UserDocId.builder().docId(doc.getId()).username(user.getUsername()).build();
        UserDoc userDoc = UserDoc.builder()
                .userDocId(userDocId)
                .user(user)
                .doc(doc).permission(userDocDTO.getPermission()).build();
        userDocRepository.save(userDoc);
        return userDocDTO;
    }

    @Override
    public List<UserDocDTO> getSharedUsers(Long id) {
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

        return doc.getSharedWith().stream()
                .map(userDoc -> userDocMapper.userDocToUserDocDTO(userDoc))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public String removeUser(Long id, UserDocDTO userDocDTO) {
        String username = SecurityUtil.getCurrentUsername();

        int isDeleted = userDocRepository.deleteUserDocBy(userDocDTO.getUsername(), id, username);

        return isDeleted != 0 ? "User removed successfully" : "User not found";
    }

    @Transactional
    @Override
    public String updatePermission(Long id, UserDocDTO userDocDTO) {
        validatePermission(userDocDTO);
        String username = SecurityUtil.getCurrentUsername();
        int isUpdated = userDocRepository.updateUserDocBy(userDocDTO.getUsername(), id, username, userDocDTO.getPermission());

        return isUpdated != 0 ? "User updated successfully" : "Failed to update";
    }

    @Transactional
    @Override
    public List<DocumentDTO> getAllDocs() {
        String username = SecurityUtil.getCurrentUsername();
        List<Doc> docs = docRepository.findByUsername(username);
        return docs.stream().map(doc -> documentMapper.toDto(doc))
                .collect(Collectors.toList());
    }

    @Override
    public DocumentDTO getDoc(Long id) {
        Doc doc = docRepository.getDocById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return documentMapper.toDto(doc);
    }

    private void validatePermission(UserDocDTO userDocDTO) {
        int permission = userDocDTO.getPermission().ordinal();
        if (permission < 0 || permission > 2) {
            throw new IllegalArgumentException("Invalid permission value. It should be 0, 1, or 2.");
        }
    }
}
