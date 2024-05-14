package org.cce.backend.controller;

import lombok.RequiredArgsConstructor;
import org.cce.backend.dto.CursorDTO;
import org.cce.backend.dto.DocumentChangeDTO;
import org.cce.backend.engine.Crdt;
import org.cce.backend.engine.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DocWebSocketController {
    @Autowired
    Crdt crdt;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/change/{id}")
    public void greeting(@DestinationVariable String id, DocumentChangeDTO message) {
        System.out.println(id);
        System.out.println(message);

        if (message.getOperation().equals("delete")) {
            crdt.delete(message.getId());
        } else if (message.getOperation().equals("insert")) {
            crdt.insert(message.getId(),
                    new Item(message.getId(), message.getContent(), crdt.getItem(message.getRight()),
                            crdt.getItem(message.getLeft()), message.isDeleted(), message.isIsbold(),
                            message.isIsitalic()));
        } else {
            crdt.format(message.getId(), message.isIsbold(), message.isIsitalic());
        }
        // System.out.println(crdt.toString());
        System.out.println("hnaaaa");
        // System.out.println(crdt.getItems());
        messagingTemplate.convertAndSend("/docs/broadcast/changes/" + id, message);
    }

    @MessageMapping("/cursor/{id}")
    public void cursor(@DestinationVariable String id, CursorDTO message) {
        System.out.println(id);
        System.out.println(message);
        messagingTemplate.convertAndSend("/docs/broadcast/cursors/" + id, message);
    }

    @MessageMapping("/username/{id}")
    public void usernames(@DestinationVariable String id, String message) {
        messagingTemplate.convertAndSend("/docs/broadcast/usernames/" + id, message);
    }

}
