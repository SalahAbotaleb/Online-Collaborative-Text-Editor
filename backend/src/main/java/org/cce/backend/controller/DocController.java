package org.cce.backend.controller;

import org.cce.backend.dto.DocumentDTO;
import org.cce.backend.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/docs")
public class DocController {
    @Autowired
    DocService docService;
    @PostMapping("/create")
    public DocumentDTO createDoc(@RequestBody DocumentDTO documentDTO) {
        return docService.createDoc(documentDTO);
    }

}
