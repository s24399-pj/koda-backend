package pl.pjwstk.kodabackend.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import pl.pjwstk.kodabackend.chat.dto.ChatMessageDto;
import pl.pjwstk.kodabackend.chat.service.ChatService;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDto chatMessageDto, Authentication authentication) {
        AppUser currentUser = (AppUser) authentication.getPrincipal();

        chatMessageDto.setSenderId(currentUser.getId());
        chatMessageDto.setSenderName(currentUser.getFirstName() + " " + currentUser.getLastName());
        chatMessageDto.setCreatedAt(LocalDateTime.now());

        ChatMessageDto savedMessage = chatService.saveMessage(chatMessageDto);

        messagingTemplate.convertAndSendToUser(
                savedMessage.getRecipientId().toString(),
                "/queue/messages",
                savedMessage
        );

        messagingTemplate.convertAndSendToUser(
                savedMessage.getSenderId().toString(),
                "/queue/messages",
                savedMessage
        );
    }
}