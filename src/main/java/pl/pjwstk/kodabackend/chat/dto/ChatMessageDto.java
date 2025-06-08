package pl.pjwstk.kodabackend.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pjwstk.kodabackend.chat.persistence.entity.MessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        name = "ChatMessage",
        description = "Chat message data transfer object containing all message details"
)
public class ChatMessageDto {

    @Schema(
            description = "Unique identifier of the chat message",
            example = "550e8400-e29b-41d4-a716-446655440000",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private UUID id;

    @Schema(
            description = "Unique identifier of the message sender",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID senderId;

    @Schema(
            description = "Full name of the message sender",
            example = "John Doe",
            maxLength = 100
    )
    private String senderName;

    @Schema(
            description = "Unique identifier of the message recipient",
            example = "789e0123-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID recipientId;

    @Schema(
            description = "Full name of the message recipient",
            example = "Jane Smith",
            maxLength = 100
    )
    private String recipientName;

    @Schema(
            description = "Content/text of the chat message",
            example = "Hello! I'm interested in your car offer.",
            maxLength = 1000,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String content;

    @Schema(
            description = "Timestamp when the message was created",
            example = "2024-12-07T10:15:30",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Current status of the message",
            example = "DELIVERED",
            allowableValues = {"SENT", "DELIVERED", "READ"}
    )
    private MessageStatus status;
}