package pl.pjwstk.kodabackend.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@Validated
class ChatControllerImpl implements ChatController {

    private final ChatService chatService;

    @GetMapping("/messages")
    @Override
    public List<ChatMessageDto> getChatMessages(@RequestParam UUID recipientId, Authentication authentication) {
        log.info("Fetching chat messages between authenticated user and recipient: {}", recipientId);
        AppUser currentUser = (AppUser) authentication.getPrincipal();
        return chatService.getChatMessages(currentUser.getId(), recipientId);
    }

    @GetMapping("/conversations")
    @Override
    public List<ConversationDto> getUserConversations(Authentication authentication) {
        AppUser currentUser = (AppUser) authentication.getPrincipal();
        log.info("Fetching conversations for user: {}", currentUser.getId());
        return chatService.getUserConversations(currentUser.getId());
    }
}