package pl.pjwstk.kodabackend.chat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import pl.pjwstk.kodabackend.chat.dto.ChatMessageDto;
import pl.pjwstk.kodabackend.chat.dto.ConversationDto;
import pl.pjwstk.kodabackend.chat.mapper.ChatMessageMapper;
import pl.pjwstk.kodabackend.chat.persistence.entity.ChatMessage;
import pl.pjwstk.kodabackend.chat.persistence.entity.MessageStatus;
import pl.pjwstk.kodabackend.chat.persistence.repository.ChatMessageRepository;
import pl.pjwstk.kodabackend.exception.EntityNotFoundException;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;
import pl.pjwstk.kodabackend.security.user.persistance.repository.AppUserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private ChatMessageMapper chatMessageMapper;

    @InjectMocks
    private ChatService chatService;

    private UUID senderId;
    private UUID recipientId;
    private AppUser sender;
    private AppUser recipient;
    private ChatMessage chatMessage;
    private ChatMessageDto chatMessageDto;

    @BeforeEach
    void setUp() {
        senderId = UUID.randomUUID();
        recipientId = UUID.randomUUID();

        sender = new AppUser();
        sender.setId(senderId);
        sender.setFirstName("John");
        sender.setLastName("Doe");

        recipient = new AppUser();
        recipient.setId(recipientId);
        recipient.setFirstName("Jane");
        recipient.setLastName("Smith");

        chatMessageDto = ChatMessageDto.builder()
                .senderId(senderId)
                .recipientId(recipientId)
                .content("Test message")
                .build();

        chatMessage = ChatMessage.builder()
                .id(UUID.randomUUID())
                .sender(sender)
                .recipient(recipient)
                .content("Test message")
                .createdAt(LocalDateTime.now())
                .status(MessageStatus.SENT)
                .build();
    }

    @Test
    void saveMessage_WhenValidData_SavesAndReturnsMessage() {
        // Arrange
        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(recipientId)).thenReturn(Optional.of(recipient));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);
        when(chatMessageMapper.toDto(chatMessage)).thenReturn(chatMessageDto);

        // Act
        ChatMessageDto result = chatService.saveMessage(chatMessageDto);

        // Assert
        assertNotNull(result);
        assertEquals(chatMessageDto, result);
        verify(userRepository).findById(senderId);
        verify(userRepository).findById(recipientId);
        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(chatMessageMapper).toDto(chatMessage);
    }

    @Test
    void saveMessage_WhenSenderNotFound_ThrowsRuntimeException() {
        // Arrange
        when(userRepository.findById(senderId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> chatService.saveMessage(chatMessageDto)
        );

        assertEquals("Sender does not exist", exception.getMessage());
        verify(userRepository).findById(senderId);
        verify(userRepository, never()).findById(recipientId);
        verify(chatMessageRepository, never()).save(any());
    }

    @Test
    void saveMessage_WhenRecipientNotFound_ThrowsRuntimeException() {
        // Arrange
        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(recipientId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> chatService.saveMessage(chatMessageDto)
        );

        assertEquals("Recipient does not exist", exception.getMessage());
        verify(userRepository).findById(senderId);
        verify(userRepository).findById(recipientId);
        verify(chatMessageRepository, never()).save(any());
    }

    @Test
    void getChatMessages_WhenMessagesExist_ReturnsMessageDtoList() {
        // Arrange
        List<ChatMessage> messages = Arrays.asList(chatMessage, chatMessage);
        when(chatMessageRepository.findChatMessages(senderId, recipientId)).thenReturn(messages);
        when(chatMessageMapper.toDto(any(ChatMessage.class))).thenReturn(chatMessageDto);

        // Act
        List<ChatMessageDto> result = chatService.getChatMessages(senderId, recipientId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(chatMessageRepository).findChatMessages(senderId, recipientId);
        verify(chatMessageMapper, times(2)).toDto(any(ChatMessage.class));
    }

    @Test
    void getChatMessages_WhenNoMessages_ReturnsEmptyList() {
        // Arrange
        when(chatMessageRepository.findChatMessages(senderId, recipientId))
                .thenReturn(Collections.emptyList());

        // Act
        List<ChatMessageDto> result = chatService.getChatMessages(senderId, recipientId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(chatMessageRepository).findChatMessages(senderId, recipientId);
        verify(chatMessageMapper, never()).toDto(any());
    }

    @Test
    void getUserConversations_WhenConversationsExist_ReturnsConversationDtoList() {
        // Arrange
        UUID partnerId = UUID.randomUUID();
        AppUser partner = new AppUser();
        partner.setId(partnerId);
        partner.setFirstName("Partner");
        partner.setLastName("User");
        partner.setProfilePicture("test".getBytes());

        List<UUID> partnerIds = Collections.singletonList(partnerId);
        List<ChatMessage> recentMessages = Collections.singletonList(chatMessage);

        when(chatMessageRepository.findConversationPartnerIds(senderId)).thenReturn(partnerIds);
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(partner));
        when(chatMessageRepository.findMessagesByConversation(
                eq(senderId), eq(partnerId), any(PageRequest.class)))
                .thenReturn(recentMessages);
        when(chatMessageRepository.countUnreadMessagesInConversation(senderId, partnerId))
                .thenReturn(3);

        // Act
        List<ConversationDto> result = chatService.getUserConversations(senderId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        ConversationDto conversation = result.get(0);
        assertEquals(partnerId, conversation.getUserId());
        assertEquals("Partner User", conversation.getUserName());
        assertNotNull(conversation.getProfilePicture());
        assertEquals(chatMessage.getContent(), conversation.getLastMessage());
        assertEquals(chatMessage.getCreatedAt(), conversation.getLastMessageDate());
        assertEquals(3, conversation.getUnreadCount());

        verify(chatMessageRepository).findConversationPartnerIds(senderId);
        verify(userRepository).findById(partnerId);
        verify(chatMessageRepository).findMessagesByConversation(
                eq(senderId), eq(partnerId), any(PageRequest.class));
        verify(chatMessageRepository).countUnreadMessagesInConversation(senderId, partnerId);
    }

    @Test
    void getUserConversations_WhenNoConversations_ReturnsEmptyList() {
        // Arrange
        when(chatMessageRepository.findConversationPartnerIds(senderId))
                .thenReturn(Collections.emptyList());

        // Act
        List<ConversationDto> result = chatService.getUserConversations(senderId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(chatMessageRepository).findConversationPartnerIds(senderId);
        verify(userRepository, never()).findById(any());
    }

    @Test
    void getUserConversations_WhenPartnerNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        UUID partnerId = UUID.randomUUID();
        List<UUID> partnerIds = Collections.singletonList(partnerId);

        when(chatMessageRepository.findConversationPartnerIds(senderId)).thenReturn(partnerIds);
        when(userRepository.findById(partnerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                EntityNotFoundException.class,
                () -> chatService.getUserConversations(senderId)
        );

        verify(chatMessageRepository).findConversationPartnerIds(senderId);
        verify(userRepository).findById(partnerId);
    }

    @Test
    void getUserConversations_WhenNoRecentMessages_ReturnsConversationWithNullLastMessage() {
        // Arrange
        UUID partnerId = UUID.randomUUID();
        AppUser partner = new AppUser();
        partner.setId(partnerId);
        partner.setFirstName("Partner");
        partner.setLastName("User");

        List<UUID> partnerIds = Collections.singletonList(partnerId);

        when(chatMessageRepository.findConversationPartnerIds(senderId)).thenReturn(partnerIds);
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(partner));
        when(chatMessageRepository.findMessagesByConversation(
                eq(senderId), eq(partnerId), any(PageRequest.class)))
                .thenReturn(Collections.emptyList());
        when(chatMessageRepository.countUnreadMessagesInConversation(senderId, partnerId))
                .thenReturn(0);

        // Act
        List<ConversationDto> result = chatService.getUserConversations(senderId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        ConversationDto conversation = result.get(0);
        assertNull(conversation.getLastMessage());
        assertNull(conversation.getLastMessageDate());
        assertEquals(0, conversation.getUnreadCount());
    }

    @Test
    void getUserConversations_WhenPartnerHasNoProfilePicture_ReturnsConversationWithNullProfilePicture() {
        // Arrange
        UUID partnerId = UUID.randomUUID();
        AppUser partner = new AppUser();
        partner.setId(partnerId);
        partner.setFirstName("Partner");
        partner.setLastName("User");
        partner.setProfilePicture(null); // No profile picture

        List<UUID> partnerIds = Collections.singletonList(partnerId);
        List<ChatMessage> recentMessages = Collections.singletonList(chatMessage);

        when(chatMessageRepository.findConversationPartnerIds(senderId)).thenReturn(partnerIds);
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(partner));
        when(chatMessageRepository.findMessagesByConversation(
                eq(senderId), eq(partnerId), any(PageRequest.class)))
                .thenReturn(recentMessages);
        when(chatMessageRepository.countUnreadMessagesInConversation(senderId, partnerId))
                .thenReturn(0);

        // Act
        List<ConversationDto> result = chatService.getUserConversations(senderId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getProfilePicture());
    }
}