package org.cce.backend.service;

import org.cce.backend.dto.DocTitleDTO;
import org.cce.backend.dto.UserDocDTO;
import org.cce.backend.entity.AccessDoc;
import org.cce.backend.entity.Doc;
import org.cce.backend.dto.DocumentDTO;
import org.cce.backend.entity.User;
import org.cce.backend.entity.UserDoc;
import org.cce.backend.enums.Permission;
import org.cce.backend.exception.UserNotFoundException;
import org.cce.backend.mapper.DocumentMapper;
import org.cce.backend.mapper.UserDocMapper;
import org.cce.backend.mapper.UserMapper;
import org.cce.backend.repository.DocRepository;
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

    private User getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .stream().findFirst().orElseThrow(()->new UserNotFoundException());
        return user;
    }

    @Override
    public DocumentDTO createDoc(DocTitleDTO docTitleDTO) {
        return null;
//        String title = docTitleDTO.getTitle();
//        userMapper.toDTO(getCurrentUser());
//        Doc doc = Doc.builder()
//                .owner(getCurrentUser())
//                .title(title)
//                .content("")
//                .sharedWith(new ArrayList<>())
//                .build();
//
//        Doc savedDoc = docRepository.save(doc);
//        User user = getCurrentUser();
//        List<AccessDoc> accessDoc = user.getAccessDoc();
//        accessDoc.add(AccessDoc.builder()
//                .doc(savedDoc)
//                .permission(Permission.OWNER)
//                .build());
//        userRepository.save(user);
//        return documentMapper.toDto(savedDoc);
    }

    @Transactional
    @Override
    public Long deleteDoc(Long id) {
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
//        for (User user : userRepository.findAll()) {
//            List<AccessDoc> accessDoc = user.getAccessDoc();
//            accessDoc.removeIf(accessDoc1 -> accessDoc1.getDoc().getId().equals(id));
//            userRepository.save(user);
//        }
//        for (UserDoc userDoc : doc.getSharedWith()){
//            userRepository.findByUsername(userDoc.getUser().getUsername()).stream()
//                    .findFirst()
//                    .ifPresent(user -> {
//                        List<AccessDoc> accessDoc = user.getAccessDoc();
//                        accessDoc.removeIf(accessDoc1 -> accessDoc1.getDoc().getId().equals(id));
//                        userRepository.save(user);
//                    });
//        }
//        docRepository.deleteById(id);
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
        UserDoc user = doc.getSharedWith().stream()
                .filter(userDoc -> userDoc.getUser().getUsername().equals(userDocDTO.getUsername()))
                .findFirst()
                .orElse(null);

        if (user != null) {
            return userDocMapper.toDto(user);
        }

        List<User> users = userRepository.findByUsername(userDocDTO.getUsername());
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User userFound = users.get(0);

        UserDoc sharedUser = UserDoc.builder()
                .user(userFound)
                .permission(userDocDTO.getPermission())
                .build();
        doc.getSharedWith().add(sharedUser);
        docRepository.save(doc);
        return userDocDTO;
    }

    @Override
    public List<UserDocDTO> getSharedUsers(Long id) {
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

        return doc.getSharedWith().stream()
                .map(userDoc -> userDocMapper.toDto(userDoc))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public String removeUser(Long id, UserDocDTO userDocDTO) {
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

        if (doc.getSharedWith().isEmpty()) {
            return "No users to remove";
        }

        Long removedUserId = null;
        for (Iterator<UserDoc> iterator = doc.getSharedWith().iterator(); iterator.hasNext();) {
            UserDoc userDoc = iterator.next();
            User user = userDoc.getUser();
            if (user != null && user.getUsername().equals(userDocDTO.getUsername()) && userDoc.getPermission().equals(userDocDTO.getPermission())) {
                removedUserId = user.getId();
                iterator.remove();
                break;
            }
        }

        docRepository.save(doc);
        return removedUserId != null ? "User with ID " + removedUserId + " removed successfully" : "User not found";
    }

    @Transactional
    @Override
    public String updatePermission(Long id, UserDocDTO userDocDTO) {
        validatePermission(userDocDTO);
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));


        UserDoc userDocToUpdate = doc.getSharedWith().stream()
                .filter(userDoc -> userDoc.getUser().getUsername().equals(userDocDTO.getUsername()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found in sharedWith list"));

        userDocToUpdate.setPermission(userDocDTO.getPermission());

        docRepository.save(doc);

        return "User permission updated successfully";
    }

    @Override
    public List<DocumentDTO> getAllDocs() {
//        String userId = SecurityUtil.getCurrentUserId();
//        ArrayList<DocumentDTO> docs = new ArrayList<>();
//
//        if (userId == null) {
//            throw new RuntimeException("User not found");
//        }
//        long prev = System.currentTimeMillis();
//        var res = docRepository.findByOwnerId(userId).stream()
//                .map(doc -> documentMapper.toDto(doc))
//                .collect(Collectors.toList());
//       long now = System.currentTimeMillis();
//        System.out.println((now-prev));
        return null;
    }

    private void validatePermission(UserDocDTO userDocDTO) {
        int permission = userDocDTO.getPermission().ordinal();
        if (permission < 0 || permission > 2) {
            throw new IllegalArgumentException("Invalid permission value. It should be 0, 1, or 2.");
        }
    }
}
