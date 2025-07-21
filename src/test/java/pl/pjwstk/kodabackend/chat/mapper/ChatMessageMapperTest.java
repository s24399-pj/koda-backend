package pl.pjwstk.kodabackend.chat.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pjwstk.kodabackend.chat.dto.ChatMessageDto;
import pl.pjwstk.kodabackend.chat.persistence.entity.ChatMessage;
import pl.pjwstk.kodabackend.chat.persistence.entity.MessageStatus;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageMapperTest {

    private ChatMessageMapper chatMessageMapper;

    private ChatMessage chatMessage;
    private AppUser sender;
    private AppUser recipient;
    private UUID messageId;
    private UUID senderId;
    private UUID recipientId;
    private LocalDateTime createdAt;

    @BeforeEach
    void setUp() {
        chatMessageMapper = new ChatMessageMapper();

        messageId = UUID.randomUUID();
        senderId = UUID.randomUUID();
        recipientId = UUID.randomUUID();
        createdAt = LocalDateTime.now();

        sender = new AppUser();
        sender.setId(senderId);
        sender.setFirstName("John");
        sender.setLastName("Doe");

        recipient = new AppUser();
        recipient.setId(recipientId);
        recipient.setFirstName("Jane");
        recipient.setLastName("Smith");

        chatMessage = ChatMessage.builder()
                .id(messageId)
                .sender(sender)
                .recipient(recipient)
                .content("Test message content")
                .createdAt(createdAt)
                .status(MessageStatus.SENT)
                .build();
    }

    @Test
    void toDto_WhenChatMessageIsNull_ReturnsNull() {
        // Act
        ChatMessageDto result = chatMessageMapper.toDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDto_WhenValidChatMessage_ReturnsChatMessageDto() {
        // Act
        ChatMessageDto result = chatMessageMapper.toDto(chatMessage);

        // Assert
        assertNotNull(result);
        assertEquals(messageId, result.getId());
        assertEquals(senderId, result.getSenderId());
        assertEquals("John Doe", result.getSenderName());
        assertEquals(recipientId, result.getRecipientId());
        assertEquals("Jane Smith", result.getRecipientName());
        assertEquals("Test message content", result.getContent());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(MessageStatus.SENT, result.getStatus());
    }

    @Test
    void toDto_WhenSenderHasNullFirstName_HandlesGracefully() {
        // Arrange
        sender.setFirstName(null);

        // Act
        ChatMessageDto result = chatMessageMapper.toDto(chatMessage);

        // Assert
        assertNotNull(result);
        assertEquals("null Doe", result.getSenderName());
    }

    @Test
    void toDto_WhenSenderHasNullLastName_HandlesGracefully() {
        // Arrange
        sender.setLastName(null);

        // Act
        ChatMessageDto result = chatMessageMapper.toDto(chatMessage);

        // Assert
        assertNotNull(result);
        assertEquals("John null", result.getSenderName());
    }

    @Test
    void toDto_WhenRecipientHasNullFirstName_HandlesGracefully() {
        // Arrange
        recipient.setFirstName(null);

        // Act
        ChatMessageDto result = chatMessageMapper.toDto(chatMessage);

        // Assert
        assertNotNull(result);
        assertEquals("null Smith", result.getRecipientName());
    }

    @Test
    void toDto_WhenRecipientHasNullLastName_HandlesGracefully() {
        // Arrange
        recipient.setLastName(null);

        // Act
        ChatMessageDto result = chatMessageMapper.toDto(chatMessage);

        // Assert
        assertNotNull(result);
        assertEquals("Jane null", result.getRecipientName());
    }

    @Test
    void toDto_WhenContentIsNull_MapsNullContent() {
        // Arrange
        chatMessage.setContent(null);

        // Act
        ChatMessageDto result = chatMessageMapper.toDto(chatMessage);

        // Assert
        assertNotNull(result);
        assertNull(result.getContent());
    }

    @Test
    void toDto_WhenContentIsEmpty_MapsEmptyContent() {
        // Arrange
        chatMessage.setContent("");

        // Act
        ChatMessageDto result = chatMessageMapper.toDto(chatMessage);

        // Assert
        assertNotNull(result);
        assertEquals("", result.getContent());
    }

    @Test
    void toDto_WhenStatusIsNull_MapsNullStatus() {
        // Arrange
        chatMessage.setStatus(null);

        // Act
        ChatMessageDto result = chatMessageMapper.toDto(chatMessage);

        // Assert
        assertNotNull(result);
        assertNull(result.getStatus());
    }

    @Test
    void toDto_WhenDifferentMessageStatus_MapsProperly() {
        // Arrange
        chatMessage.setStatus(MessageStatus.DELIVERED);

        // Act
        ChatMessageDto result = chatMessageMapper.toDto(chatMessage);

        // Assert
        assertNotNull(result);
        assertEquals(MessageStatus.DELIVERED, result.getStatus());
    }

    @Test
    void toDto_WhenBothNamesAreNull_HandlesGracefully() {
        // Arrange
        sender.setFirstName(null);
        sender.setLastName(null);
        recipient.setFirstName(null);
        recipient.setLastName(null);

        // Act
        ChatMessageDto result = chatMessageMapper.toDto(chatMessage);

        // Assert
        assertNotNull(result);
        assertEquals("null null", result.getSenderName());
        assertEquals("null null", result.getRecipientName());
    }
}