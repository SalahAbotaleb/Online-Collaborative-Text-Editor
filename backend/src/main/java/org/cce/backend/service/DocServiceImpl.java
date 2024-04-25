package org.cce.backend.service;

import org.cce.backend.entity.Doc;
import org.cce.backend.dto.DocumentDTO;
import org.cce.backend.entity.User;
import org.cce.backend.repository.DocRepository;
import org.cce.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class DocServiceImpl implements DocService{
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
                .title(savedDoc.getTitle())
                .content(savedDoc.getContent())
                .build();
    }
}
