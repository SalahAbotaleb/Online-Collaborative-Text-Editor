package org.cce.backend.service;

import org.cce.backend.dto.DocTitleDTO;
import org.cce.backend.dto.UserDocDTO;
import org.cce.backend.entity.*;
import org.cce.backend.dto.DocumentDTO;
import org.cce.backend.enums.Permission;
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
//        UserDocId userDocId = UserDocId.builder().docId(savedDoc.getId()).username(user.getUsername()).build();
//        userDocRepository.save(
//                UserDoc.builder()
//                        .userDocId(userDocId)
//                        .doc(savedDoc)
//                        .user(user)
//                        .permission(Permission.OWNER)
//                        .build() );

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
//        UserDoc user = doc.getSharedWith().stream()
//                .filter(userDoc -> userDoc.getUser().getUsername().equals(userDocDTO.getUsername()))
//                .findFirst()
//                .orElse(null);
//        if (user != null) {
//            return userDocMapper.userDocToUserDocDTO(user);
//        }
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
//        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

//        if (doc.getSharedWith().isEmpty()) {
//            return "No users to remove";
//        }

//        String removedUser = null;
//        for (Iterator<UserDoc> iterator = doc.getSharedWith().iterator(); iterator.hasNext();) {
//            UserDoc userDoc = iterator.next();
//            User user = userDoc.getUser();
//            if (user != null && user.getUsername().equals(userDocDTO.getUsername()) && userDoc.getPermission().equals(userDocDTO.getPermission())) {
//                removedUser = user.getUsername();
//                iterator.remove();
//                userDocRepository.deleteById(userDoc.getUserDocId());
//                break;
//            }
//        }
        String username = SecurityUtil.getCurrentUsername();

        int isDeleted = userDocRepository.deleteUserDocBy(userDocDTO.getUsername(), id, username);

        //docRepository.save(doc);

        return isDeleted != 0 ? "User removed successfully" : "User not found";
    }

    @Transactional
    @Override
    public String updatePermission(Long id, UserDocDTO userDocDTO) {
        validatePermission(userDocDTO);
//        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
//
//
//        UserDoc userDocToUpdate = doc.getSharedWith().stream()
//                .filter(userDoc -> userDoc.getUser().getUsername().equals(userDocDTO.getUsername()))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("User not found in sharedWith list"));
//
//        userDocToUpdate.setPermission(userDocDTO.getPermission());
//        UserDoc userDoc = userDocRepository.findById(UserDocId.builder().docId(id).username(userDocDTO.getUsername()).build())
//                .orElseThrow(() -> new RuntimeException("User not found in sharedWith list"));
//        userDoc.setPermission(userDocDTO.getPermission());
//        userDocRepository.save(userDoc);
        String username = SecurityUtil.getCurrentUsername();
        int isUpdated = userDocRepository.updateUserDocBy(userDocDTO.getUsername(), id, username, userDocDTO.getPermission());

        return isUpdated != 0 ? "User updated successfully" : "Failed to update";
    }

    @Override
    public List<DocumentDTO> getAllDocs() {
//        User user = getCurrentUser();

        String username = SecurityUtil.getCurrentUsername();
        List<Doc> docs = docRepository.findByUsername(username);
//        docs.stream().forEach(
//                        doc->{
//                            List<UserDoc> sharedWith = doc.getSharedWith();
//                            sharedWith = sharedWith.stream().filter(
//                                    item->!item.getPermission().equals(Permission.OWNER)
//                            ).toList();
//                            doc.setSharedWith(sharedWith);
//                        }
//                );

        return docs.stream().map(doc -> documentMapper.toDto(doc))
                .collect(Collectors.toList());
    }

    private void validatePermission(UserDocDTO userDocDTO) {
        int permission = userDocDTO.getPermission().ordinal();
        if (permission < 0 || permission > 2) {
            throw new IllegalArgumentException("Invalid permission value. It should be 0, 1, or 2.");
        }
    }
}
