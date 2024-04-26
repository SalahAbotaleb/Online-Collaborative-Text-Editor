package org.cce.backend.controller;

import jakarta.validation.Valid;
import org.cce.backend.dto.UserDocDTO;
import org.cce.backend.dto.DocumentDTO;
import org.cce.backend.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/docs")
public class DocController {
    @Autowired
    DocService docService;
    @PostMapping("/create")
    public DocumentDTO createDoc(@RequestBody DocumentDTO documentDTO) {
        return docService.createDoc(documentDTO);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteDoc(@PathVariable String id) {
        return docService.deleteDoc(id);
    }

    @PatchMapping("/rename/{id}")
    public String updateDocTitle(@PathVariable String id, @RequestBody DocumentDTO documentDTO) {
        return docService.updateDocTitle(id, documentDTO);
    }

    @PatchMapping("/users/add/{id}")
    public UserDocDTO addUser(@PathVariable String id,@Valid @RequestBody UserDocDTO userDoc) {
        return docService.addUser(id, userDoc);
    }

    @GetMapping("/users/all/{id}")
    public DocumentDTO getSharedUsers(@PathVariable String id) {
        return docService.getSharedUsers(id);
    }

    @DeleteMapping("/users/remove/{id}")
    public String removeUser(@PathVariable String id, @RequestBody UserDocDTO userDoc) {
        return docService.removeUser(id, userDoc);
    }

    @PatchMapping("users/permission/{id}")
    public String updatePermission(@PathVariable String id, @RequestBody UserDocDTO userDoc) {
        return docService.updatePermission(id, userDoc);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
