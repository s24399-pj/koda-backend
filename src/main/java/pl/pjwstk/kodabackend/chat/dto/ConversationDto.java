package pl.pjwstk.kodabackend.chat.dto;

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
public class ConversationDto {
    private UUID userId;
    private String userName;
    private String profilePicture;
    private String lastMessage;
    private LocalDateTime lastMessageDate;
    private int unreadCount;
}