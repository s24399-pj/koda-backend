package pl.pjwstk.kodabackend.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        name = "Conversation",
        description = "Conversation summary containing participant details and latest message information"
)
public class ConversationDto {

    @Schema(
            description = "Unique identifier of the conversation participant (other user)",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID userId;

    @Schema(
            description = "Full name of the conversation participant",
            example = "Alice Johnson",
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String userName;

    @Schema(
            description = "URL to the participant's profile picture",
            example = "https://example.com/avatars/alice.jpg",
            maxLength = 255
    )
    private String profilePicture;

    @Schema(
            description = "Content of the most recent message in the conversation",
            example = "Thanks for the quick response!",
            maxLength = 100
    )
    private String lastMessage;

    @Schema(
            description = "Timestamp of the most recent message",
            example = "2024-12-07T14:30:45"
    )
    private LocalDateTime lastMessageDate;

    @Schema(
            description = "Number of unread messages in this conversation",
            example = "3",
            minimum = "0",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private int unreadCount;
}