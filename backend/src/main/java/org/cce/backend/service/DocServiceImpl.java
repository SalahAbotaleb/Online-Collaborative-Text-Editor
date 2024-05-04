package org.cce.backend.service;

import org.cce.backend.dto.UserDocDTO;
import org.cce.backend.entity.Doc;
import org.cce.backend.dto.DocumentDTO;
import org.cce.backend.entity.User;
import org.cce.backend.entity.UserDoc;
import org.cce.backend.repository.DocRepository;
import org.cce.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class DocServiceImpl implements DocService {
    @Autowired
    DocRepository docRepository;

    @Autowired
    UserRepository userRepository;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username).get(0);
        }

        return null; // or throw an exception
    }

    @Override
    public DocumentDTO createDoc(DocumentDTO documentDTO) {
        Doc doc = Doc.builder()
                .owner(getCurrentUser())
                .title(documentDTO.getTitle())
                .content(documentDTO.getContent())
                .build();

        Doc savedDoc = docRepository.save(doc);

        return DocumentDTO.builder()
                .id(savedDoc.getId())
                .owner(savedDoc.getOwner())
                .title(savedDoc.getTitle())
                .content(savedDoc.getContent())
                .build();
    }

    @Transactional
    @Override
    public String deleteDoc(String id) {
        docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
        docRepository.deleteById(id);
        return id;
    }

    @Transactional
    @Override
    public String updateDocTitle(String id, DocumentDTO documentDTO) {
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
        doc.setTitle(documentDTO.getTitle());
        docRepository.save(doc);
        return "Title updated successfully";
    }

    @Transactional
    @Override
    public UserDocDTO addUser(String id, UserDocDTO userDocDTO) {
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
        UserDoc sharedUser = UserDoc.builder()
                .user(userDocDTO.getUser())
                .permission(userDocDTO.getPermission())
                .build();

        if(doc.getSharedWith() == null) {
            doc.setSharedWith(new ArrayList<>());
        }

        doc.getSharedWith().add(sharedUser);
        docRepository.save(doc);
        return userDocDTO;
    }

    @Override
    public DocumentDTO getSharedUsers(String id) {
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
        if(doc.getSharedWith() == null) {
            return DocumentDTO.builder()
                    .id(doc.getId())
                    .owner(doc.getOwner())
                    .title(doc.getTitle())
                    .content(doc.getContent())
                    .sharedWith(new ArrayList<>())
                    .build();
        }
//        for(UserDoc userDoc : doc.getSharedWith()) {
//            sharedUsers.add(userDoc.getUser());
//        }
        return DocumentDTO.builder()
                .id(doc.getId())
                .owner(doc.getOwner())
                .title(doc.getTitle())
                .content(doc.getContent())
                .sharedWith(doc.getSharedWith())
                .build();
    }

    @Transactional
    @Override
    public String removeUser(String id, UserDocDTO userDocDTO) {
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

        if(doc.getSharedWith() == null) {
            return "No users to remove";
        }


        doc.getSharedWith().removeIf(userDoc -> {
            User user = userDoc.getUser();
            return user != null && user.getId().equals(userDocDTO.getUser().getId());
        });
        docRepository.save(doc);
        return "User removed successfully";
    }

    @Transactional
    @Override
    public String updatePermission(String id, UserDocDTO userDocDTO) {
        validatePermission(userDocDTO);
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

        if(doc.getSharedWith() == null) {
            return "No users to update";
        }

        for(UserDoc userDoc : doc.getSharedWith()) {
            User user = userDoc.getUser();
            if(user != null && user.getId().equals(userDocDTO.getUser().getId())) {
                userDoc.setPermission(userDocDTO.getPermission());
            }
        }

        docRepository.save(doc);
        return "User permission updated successfully";
    }

    @Override
    public Iterable<DocumentDTO> getAllDocs() {
        User user = getCurrentUser();
        ArrayList <DocumentDTO> docs = new ArrayList<>();

        if(user == null) {
            throw new RuntimeException("User not found");
        }
        return docRepository.findByOwner(user).stream()
                .map(doc -> DocumentDTO.builder()
                        .id(doc.getId())
                        .owner(doc.getOwner())
                        .title(doc.getTitle())
                        .content(doc.getContent())
                        .sharedWith(doc.getSharedWith())
                        .build())
                .collect(Collectors.toList());
    }

    private void validatePermission(UserDocDTO userDocDTO) {
        int permission = userDocDTO.getPermission().ordinal();
        if (permission < 0 || permission > 2) {
            throw new IllegalArgumentException("Invalid permission value. It should be 0, 1, or 2.");
        }
    }
}
