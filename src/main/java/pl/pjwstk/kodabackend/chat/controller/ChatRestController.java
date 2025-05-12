package pl.pjwstk.kodabackend.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pjwstk.kodabackend.chat.dto.ChatMessageDto;
import pl.pjwstk.kodabackend.chat.service.ChatService;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;

    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessageDto>> getChatMessages(
            @RequestParam UUID recipientId,
            Authentication authentication) {

        AppUser currentUser = (AppUser) authentication.getPrincipal();
        List<ChatMessageDto> messages = chatService.getChatMessages(currentUser.getId(), recipientId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/unread")
    public ResponseEntity<List<ChatMessageDto>> getUnreadMessages(Authentication authentication) {
        AppUser currentUser = (AppUser) authentication.getPrincipal();
        List<ChatMessageDto> unreadMessages = chatService.getUnreadMessages(currentUser.getId());
        return ResponseEntity.ok(unreadMessages);
    }

    @PutMapping("/messages/{messageId}/read")
    public ResponseEntity<Void> markMessageAsRead(
            @PathVariable UUID messageId,
            Authentication authentication) {

        chatService.markMessageAsRead(messageId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/messages/{messageId}/delivered")
    public ResponseEntity<Void> markMessageAsDelivered(
            @PathVariable UUID messageId,
            Authentication authentication) {

        chatService.markMessageAsDelivered(messageId);
        return ResponseEntity.ok().build();
    }
}