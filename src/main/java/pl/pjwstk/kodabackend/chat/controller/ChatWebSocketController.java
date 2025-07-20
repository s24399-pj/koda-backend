package pl.pjwstk.kodabackend.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import pl.pjwstk.kodabackend.chat.dto.ChatMessageDto;
import pl.pjwstk.kodabackend.chat.service.ChatService;
import pl.pjwstk.kodabackend.security.util.UserUtils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * WebSocket controller for handling real-time chat functionality.
 * Handles message sending between authenticated users.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    /**
     * Handles chat message sending between users.
     * Requires user authentication.
     *
     * @param chatMessageDto The message data from the client
     * @param principal      The authenticated user
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDto chatMessageDto, Principal principal) {

        if (principal == null) {
            log.error("No authenticated user found for chat message");
            throw new IllegalStateException("User must be authenticated to send messages");
        }

        try {
            UUID currentUserId = UserUtils.extractUserIdFromPrincipal(principal);
            String currentUserName = extractUserNameFromPrincipal(principal);

            chatMessageDto.setSenderId(currentUserId);
            chatMessageDto.setSenderName(currentUserName);
            chatMessageDto.setCreatedAt(LocalDateTime.now());

            ChatMessageDto savedMessage = chatService.saveMessage(chatMessageDto);

            // Send message to recipient only
            messagingTemplate.convertAndSendToUser(
                    savedMessage.getRecipientId().toString(),
                    "/queue/messages",
                    savedMessage
            );

            log.debug("Chat message sent from {} to {}", currentUserId, savedMessage.getRecipientId());

        } catch (Exception e) {
            log.error("Error processing chat message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process chat message", e);
        }
    }

    /**
     * Extracts user's full name from principal
     */
    private String extractUserNameFromPrincipal(Principal principal) {
        try {
            var appUser = UserUtils.extractUserFromPrincipal(principal);
            return appUser.getFirstName() + " " + appUser.getLastName();
        } catch (Exception e) {
            log.warn("Could not extract user name from principal: {}", e.getMessage());
            return "Unknown User";
        }
    }
}