package org.cce.backend.controller;

import lombok.RequiredArgsConstructor;
import org.cce.backend.dto.DocumentChangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class DocController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/change/{id}")
    public void greeting(@DestinationVariable String id, DocumentChangeDTO message){
        System.out.println(id);
        messagingTemplate.convertAndSend("/docs/broadcast/changes/"+id,message.getMsg());
    }

}
