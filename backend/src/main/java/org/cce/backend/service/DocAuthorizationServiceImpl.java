package org.cce.backend.service;

import org.cce.backend.entity.User;
import org.cce.backend.enums.Permission;
import org.cce.backend.exception.UserNotFoundException;
import org.cce.backend.repository.UserRepository;
import org.cce.backend.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocAuthorizationServiceImpl implements DocAuthorizationService{

    @Autowired
    UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .stream().findFirst().orElseThrow(()->new UserNotFoundException());
        return user;
    }

    @Override
    public boolean canAccess(String docId) {
        User user = getCurrentUser();
        return user.getAccessDoc().stream().anyMatch(doc -> doc.getDoc().getId().equals(docId));
    }

    @Override
    public boolean canEdit(String docId) {
        User user = getCurrentUser();
        return user.getAccessDoc().stream().anyMatch(doc -> doc.getDoc().getId().equals(docId) &&
                (doc.getPermission().equals(Permission.EDIT) || doc.getPermission().equals(Permission.OWNER)));
    }

    public boolean fullAccess(String docId) {
        User user = getCurrentUser();
        return user.getAccessDoc().stream().anyMatch(doc -> doc.getDoc().getId().equals(docId) &&
                doc.getPermission().equals(Permission.OWNER));
    }
}
