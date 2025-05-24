package pl.pjwstk.kodabackend.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pjwstk.kodabackend.chat.dto.ChatMessageDto;
import pl.pjwstk.kodabackend.chat.dto.ConversationDto;
import pl.pjwstk.kodabackend.chat.service.ChatService;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;

    @GetMapping("/messages")
    public List<ChatMessageDto> getChatMessages(@RequestParam UUID recipientId, Authentication authentication) {
        AppUser currentUser = (AppUser) authentication.getPrincipal();
        return chatService.getChatMessages(currentUser.getId(), recipientId);
    }

    @GetMapping("/conversations")
    public List<ConversationDto> getUserConversations(Authentication authentication) {
        AppUser currentUser = (AppUser) authentication.getPrincipal();
        return chatService.getUserConversations(currentUser.getId());
    }
}