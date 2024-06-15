package org.cce.backend.controller;

import lombok.RequiredArgsConstructor;
import org.cce.backend.dto.CursorDTO;
import org.cce.backend.dto.DocumentChangeDTO;
import org.cce.backend.engine.Crdt;
import org.cce.backend.engine.CrdtManagerService;
import org.cce.backend.engine.Item;
import org.cce.backend.mapper.DocumentChangeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DocWebSocketController {
    @Autowired
    CrdtManagerService crdtManagerService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    DocumentChangeMapper documentChangeMapper;

    @MessageMapping("/change/{id}")
    public void greeting(@DestinationVariable String id, DocumentChangeDTO message) {
        Crdt crdt = crdtManagerService.getCrdt(Long.parseLong(id));
        if (message.getOperation().equals("delete")) {
            crdt.delete(message.getId());
        } else if (message.getOperation().equals("insert")) {
            crdt.insert(message.getId(),
                    new Item(message.getId(), message.getContent(), crdt.getItem(message.getRight()),
                            crdt.getItem(message.getLeft()), message.getOperation(), message.getIsDeleted(),
                            message.getIsBold(), message.getIsItalic()));
        } else {
            crdt.format(message.getId(), message.getIsBold(), message.getIsItalic());
        }

        messagingTemplate.convertAndSend("/docs/broadcast/changes/" + id, message);
    }

    @MessageMapping("/cursor/{id}")
    public void cursor(@DestinationVariable String id, CursorDTO message) {
        messagingTemplate.convertAndSend("/docs/broadcast/cursors/" + id, message);
    }

    @MessageMapping("/username/{id}")
    public void usernames(@DestinationVariable String id, String message) {
        messagingTemplate.convertAndSend("/docs/broadcast/usernames/" + id, message);
    }

}
