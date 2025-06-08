package pl.pjwstk.kodabackend.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pjwstk.kodabackend.chat.dto.ChatMessageDto;
import pl.pjwstk.kodabackend.chat.dto.ConversationDto;

import java.util.List;
import java.util.UUID;

@Tag(name = "Messages", description = "API for managing chat messages and conversations")
public interface ChatController {

    @Operation(
            summary = "Get chat messages between users",
            description = "Retrieves all chat messages between the authenticated user and specified recipient. Messages are ordered chronologically."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved chat messages",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatMessageDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid recipient ID",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized, user not logged in",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recipient user not found",
                    content = @Content
            )
    })
    @GetMapping("/messages")
    List<ChatMessageDto> getChatMessages(
            @Parameter(description = "UUID of the message recipient", required = true)
            @RequestParam UUID recipientId,
            Authentication authentication
    );

    @Operation(
            summary = "Get user conversations",
            description = "Retrieves all conversations for the authenticated user with latest message details and unread counts."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user conversations",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConversationDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized, user not logged in",
                    content = @Content
            )
    })
    @GetMapping("/conversations")
    List<ConversationDto> getUserConversations(Authentication authentication);
}