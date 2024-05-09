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
        User user = userRepository.findById(username)
                .orElseThrow(()->new UserNotFoundException());
        return user;
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
        userDocRepository.save(
                UserDoc.builder().doc(savedDoc).user(user).permission(Permission.OWNER).build() );
        userRepository.save(user);
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
        UserDoc user = doc.getSharedWith().stream()
                .filter(userDoc -> userDoc.getUser().getUsername().equals(userDocDTO.getUsername()))
                .findFirst()
                .orElse(null);

        if (user != null) {
            return userDocMapper.userDocToUserDocDTO(user);
        }

        User users = userRepository.findById(userDocDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDoc userDoc = UserDoc.builder().user(users)
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
        Doc doc = docRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));

        if (doc.getSharedWith().isEmpty()) {
            return "No users to remove";
        }

        String removedUser = null;
        for (Iterator<UserDoc> iterator = doc.getSharedWith().iterator(); iterator.hasNext();) {
            UserDoc userDoc = iterator.next();
            User user = userDoc.getUser();
            if (user != null && user.getUsername().equals(userDocDTO.getUsername()) && userDoc.getPermission().equals(userDocDTO.getPermission())) {
                removedUser = user.getUsername();
                iterator.remove();
                break;
            }
        }

        docRepository.save(doc);
        return removedUser != null ? "User with ID " + removedUser + " removed successfully" : "User not found";
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
        User user = getCurrentUser();
        System.out.println("New query");
        List<Doc>docs = userDocRepository.getDocsByUser_Username(user.getUsername());
        docs.stream().forEach(
                        doc->{
                            List<UserDoc> sharedWith = doc.getSharedWith();
                            sharedWith = sharedWith.stream().filter(
                                    item->!item.getPermission().equals(Permission.OWNER)
                            ).toList();
                            doc.setSharedWith(sharedWith);
                        }
                );

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
